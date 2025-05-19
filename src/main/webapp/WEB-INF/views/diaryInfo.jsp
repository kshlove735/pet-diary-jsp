<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PetCare - 일기 수정 🐾</title>
    <link rel="stylesheet" href="/resources/css/styles.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"
            integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
            crossorigin="anonymous"></script>
    <script src="/resources/js/utils.js"></script>

</head>
<body>
    <div class="popup">
        <div class="popup-content">
            <h2>일기 수정 📝</h2>
            <form id="diaryForm">
                <input type="hidden" id="diaryId" name="diaryId" value="${diaryId}">
                <div class="form-group">
                    <label for="petId">반려견 <span class="required">*</span></label>
                    <select id="petId" name="petId" required disabled>
                        <option value="">반려견을 선택하세요</option>
                        <!-- 동적으로 petId 추가 -->
                    </select>
                </div>
                <div class="form-group">
                    <label for="date">날짜 <span class="required">*</span></label>
                    <input type="date" id="date" name="date" required max="<%= java.time.LocalDate.now() %>">
                </div>
                <div class="form-group">
                    <label for="dtype">일기 유형 <span class="required">*</span></label>
                    <select id="dtype" name="dtype" required>
                        <option value="">유형을 선택하세요</option>
                        <option value="activity">활동</option>
                        <option value="behavior">행동</option>
                        <option value="grooming">미용</option>
                        <option value="health">건강</option>
                        <option value="meal">식사</option>
                    </select>
                </div>
                <!-- 동적 필드 렌더링 영역 -->
                <div id="dynamicFields"></div>
                <div class="form-group">
                    <label for="description">설명</label>
                    <textarea id="description" name="description" placeholder="일기에 대한 설명을 입력하세요"></textarea>
                </div>
                <div class="form-group buttons">
                    <button type="submit" class="btn full-width">수정</button>
                    <button type="button" class="btn cancel full-width" onclick="window.close()">취소</button>
                </div>
            </form>
            <div id="errorMessage" class="error" style="display: none;"></div>
            <div id="successMessage" class="success" style="display: none;"></div>
        </div>
    </div>

    <script>
        $(document).ready(async function () {
            setupForm();
            await initializeData();
        });

        // 데이터 초기화 함수 (비동기 처리를 순차적으로 실행)
        async function initializeData() {
            try {
                // 먼저 반려견 목록을 가져옴
                await fetchPetIds();
                // 반려견 목록을 가져온 후 일기 데이터를 가져옴
                await fetchDiaryData();
            } catch (error) {
                console.error('데이터 초기화 오류:', error);
                showError('데이터를 불러오는 중 오류가 발생했습니다.');
            }
        }

        // 반려견 목록 조회
        async function fetchPetIds() {
            const petIdSelect = $('#petId');
            try {
                const response = await fetch('/api/v1/pet', {
                    method: 'GET',
                    headers: { 'X-Requested-With': 'XMLHttpRequest' },
                    credentials: 'include'
                });
                
                const result = await response.json();

                if (result.success) {
                    petIdSelect.empty().append('<option value="">반려견을 선택하세요</option>');
                    result.data.forEach(petInfo => {
                        petIdSelect.append(`<option value="\${petInfo.id}">\${petInfo.name}</option>`);
                    });
                    return true;
                } else {
                    throw new Error(result.message || '반려견 목록을 불러오지 못했습니다.');
                }
            } catch (error) {
                console.error('반려견 목록 조회 오류:', error);
                showError('반려견 목록을 불러오지 못했습니다: ' + error.message);
                return false;
            }
        }

        // 일기 데이터 조회
        async function fetchDiaryData() {
            const diaryId = $('#diaryId').val();
            try {
                const response = await fetch(`/api/v1/diary/\${diaryId}`, {
                    method: 'GET',
                    headers: { 'X-Requested-With': 'XMLHttpRequest' },
                    credentials: 'include'
                });
                
                const result = await response.json();
                
                if (result.success) {
                    populateForm(result.data);
                    return true;
                } else {
                    throw new Error(result.message || '일기 조회에 실패했습니다.');
                }
            } catch (error) {
                console.error('일기 조회 오류:', error);
                showError('일기 데이터를 불러오지 못했습니다: ' + error.message);
                return false;
            }
        }

        // 폼 데이터 채우기
        function populateForm(diary) {
            $('#petId').val(diary.petId);
            $('#date').val(diary.date);
            $('#dtype').val(diary.dtype).trigger('change');
            $('#description').val(diary.description || '');

            // 유형별 필드 채우기
            setTimeout(() => {
                switch (diary.dtype) {
                    case 'activity':
                        $('#activityType').val(diary.activityType || '');
                        $('#duration').val(diary.duration || '');
                        $('#distance').val(diary.distance || '');
                        $('#location').val(diary.location || '');
                        break;
                    case 'behavior':
                        $('#behaviorType').val(diary.behaviorType || '');
                        $('#behaviorIntensity').val(diary.behaviorIntensity || '');
                        break;
                    case 'grooming':
                        $('#groomingType').val(diary.groomingType || '');
                        break;
                    case 'health':
                        $('#healthType').val(diary.healthType || '');
                        $('#nextDueDate').val(diary.nextDueDate || '');
                        $('#clinic').val(diary.clinic || '');
                        break;
                    case 'meal':
                        $('#mealType').val(diary.mealType || '');
                        $('#foodBrand').val(diary.foodBrand || '');
                        $('#foodAmount').val(diary.foodAmount || '');
                        break;
                }
            }, 100); // 동적 필드가 렌더링된 후 데이터 채우기를 위한 짧은 지연
        }

        // 폼 설정 및 동적 필드 렌더링
        function setupForm() {
            const dtypeSelect = $('#dtype');
            const dynamicFields = $('#dynamicFields');

            // 일기 유형 변경 시 동적 필드 렌더링
            dtypeSelect.on('change', function () {
                const dtype = $(this).val();
                dynamicFields.empty();

                const fields = getDynamicFields(dtype);
                dynamicFields.html(fields);

                // 입력 필드 유효성 검사 이벤트 추가
                dynamicFields.find('input, select').on('input change', validateField);
            });

            // 폼 제출 처리
            $('#diaryForm').on('submit', async function (e) {
                e.preventDefault();
                if (await validateForm()) {
                    submitDiary();
                }
            });

            // 초기 유효성 검사 이벤트
            $('input, select, textarea').on('input change', validateField);
        }

        // 유형별 동적 필드 생성
        function getDynamicFields(dtype) {
            switch (dtype) {
                case 'activity':
                    return `
                        <div class="form-group">
                            <label for="activityType">활동 유형 <span class="required">*</span></label>
                            <select id="activityType" name="activityType" required>
                                <option value="">선택하세요</option>
                                <option value="WALK">산책</option>
                                <option value="PLAY">놀이</option>
                                <option value="TRAINING">훈련</option>
                                <option value="SWIM">수영</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="duration">시간(분)</label>
                            <input type="number" id="duration" name="duration" min="0" step="1" placeholder="예: 30">
                        </div>
                        <div class="form-group">
                            <label for="distance">거리(km)</label>
                            <input type="number" id="distance" name="distance" min="0" step="0.01" placeholder="예: 1.5">
                        </div>
                        <div class="form-group">
                            <label for="location">장소</label>
                            <input type="text" id="location" name="location" placeholder="예: 공원">
                        </div>`;
                case 'behavior':
                    return `
                        <div class="form-group">
                            <label for="behaviorType">행동 유형 <span class="required">*</span></label>
                            <input type="text" id="behaviorType" name="behaviorType" required placeholder="예: 짖음">
                        </div>
                        <div class="form-group">
                            <label for="behaviorIntensity">강도 <span class="required">*</span></label>
                            <select id="behaviorIntensity" name="behaviorIntensity" required>
                                <option value="">선택하세요</option>
                                <option value="LOW">낮음</option>
                                <option value="MEDIUM">보통</option>
                                <option value="HIGH">높음</option>
                            </select>
                        </div>`;
                case 'grooming':
                    return `
                        <div class="form-group">
                            <label for="groomingType">미용 유형 <span class="required">*</span></label>
                            <select id="groomingType" name="groomingType" required>
                                <option value="">선택하세요</option>
                                <option value="BATH">목욕</option>
                                <option value="HAIRCUT">이발</option>
                                <option value="NAIL_TRIM">발톱 손질</option>
                                <option value="EAR_CLEANING">귀 청소</option>
                                <option value="TEETH_CLEANING">치아 관리</option>
                            </select>
                        </div>`;
                case 'health':
                    return `
                        <div class="form-group">
                            <label for="healthType">건강 유형 <span class="required">*</span></label>
                            <select id="healthType" name="healthType" required>
                                <option value="">선택하세요</option>
                                <option value="VACCINATION">예방접종</option>
                                <option value="CHECKUP">건강검진</option>
                                <option value="SURGERY">수술</option>
                                <option value="MEDICATION">투약</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="nextDueDate">다음 예정일</label>
                            <input type="date" id="nextDueDate" name="nextDueDate">
                        </div>
                        <div class="form-group">
                            <label for="clinic">병원</label>
                            <input type="text" id="clinic" name="clinic" placeholder="예: 행복 동물병원">
                        </div>`;
                case 'meal':
                    return `
                        <div class="form-group">
                            <label for="mealType">식사 유형 <span class="required">*</span></label>
                            <select id="mealType" name="mealType" required>
                                <option value="">선택하세요</option>
                                <option value="BREAKFAST">아침</option>
                                <option value="LUNCH">점심</option>
                                <option value="DINNER">저녁</option>
                                <option value="SNACK">간식</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="foodBrand">사료 브랜드</label>
                            <input type="text" id="foodBrand" name="foodBrand" placeholder="예: 로얄캐닌">
                        </div>
                        <div class="form-group">
                            <label for="foodAmount">급여량(g)</label>
                            <input type="number" id="foodAmount" name="foodAmount" min="0" step="1" placeholder="예: 100">
                        </div>`;
                default:
                    return '';
            }
        }

        // 입력 필드 유효성 검사
        function validateField() {
            const $input = $(this);
            const value = $input.val();
            const isRequired = $input.prop('required');
            const isValid = !isRequired || (value && value.trim() !== '');

            $input.toggleClass('invalid', !isValid);
            $input.toggleClass('valid', isValid);
            return isValid;
        }

        // 전체 폼 유효성 검사
        async function validateForm() {
            let isValid = true;
            const dtype = $('#dtype').val();
            const requiredFields = {
                activity: ['activityType'],
                behavior: ['behaviorType', 'behaviorIntensity'],
                grooming: ['groomingType'],
                health: ['healthType'],
                meal: ['mealType']
            };

            $('#diaryForm').find('input, select, textarea').each(function () {
                const $input = $(this);
                const name = $input.attr('name');
                const isRequired = $input.prop('required') || (requiredFields[dtype] && requiredFields[dtype].includes(name));
                const isValidField = !isRequired || ($input.val() && $input.val().trim() !== '');
                $input.toggleClass('invalid', !isValidField);
                $input.toggleClass('valid', isValidField);
                if (!isValidField) isValid = false;
            });

            if (!isValid) {
                showError('필수 필드를 모두 입력해주세요.');
            }
            return isValid;
        }

        // 에러 메시지 표시
        function showError(message) {
            $('#errorMessage').text(message).show();
            $('#successMessage').hide();
        }

        // 성공 메시지 표시
        function showSuccess(message) {
            $('#successMessage').text(message).show();
            $('#errorMessage').hide();
        }

        // 일기 제출
        async function submitDiary() {
            const formData = $('#diaryForm').serializeArray();
            const data = {};
            formData.forEach(item => {
                data[item.name] = item.value;
            });

            // 숫자 필드 변환
            ['duration', 'foodAmount'].forEach(key => {
                if (data[key]) data[key] = parseInt(data[key]) || null;
            });
            if (data.distance) data.distance = parseFloat(data.distance) || null;

            // 빈 문자열을 null로 변환
            for (const key in data) {
                if (data[key] === '') data[key] = null;
            }

            try {
                const response = await fetch(`/api/v1/diary/\${data.diaryId}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    credentials: 'include',
                    body: JSON.stringify(data)
                });

                const result = await response.json();
                if (result.success) {
                    showSuccess('일기가 수정되었습니다.');
                    setTimeout(() => {
                        window.opener.fetchDiaries(window.opener.currentPage);
                        window.close();
                    }, 1000);
                } else {
                    throw new Error(result.message || '일기 수정에 실패했습니다.');
                }
            } catch (error) {
                console.error('일기 수정 오류:', error);
                showError('일기 수정 중 오류가 발생했습니다: ' + error.message);
            }
        }
    </script>

    <style>
        .required {
            color: red;
            font-size: 0.9em;
        }
        .form-group input.invalid,
        .form-group select.invalid,
        .form-group textarea.invalid {
            border-color: red;
        }
        .form-group input.valid,
        .form-group select.valid,
        .form-group textarea.valid {
            border-color: green;
        }
    </style>
</body>
</html>