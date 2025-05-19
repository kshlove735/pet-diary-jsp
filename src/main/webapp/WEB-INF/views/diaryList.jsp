<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PetCare - 멍멍 일기 🐾</title>
    <link rel="stylesheet" href="/resources/css/styles.css">
    <!-- 최적화: 최신 jQuery 버전으로 업그레이드 및 보안 속성 추가 -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" 
            integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" 
            crossorigin="anonymous"></script>
</head>
<body>
    <header class="header">
        <h1>PetCare 🐶🐾</h1>
    </header>
    <nav class="nav">
        <a href="/auth/signup">회원가입</a>
        <a href="/auth/login">로그인</a>
        <a href="/user">유저 정보</a>
        <a href="/diary">멍멍 일기</a>
    </nav>
    <main class="container">
        <section class="info-container">
            <h2>멍멍 일기 📖</h2>
            <c:if test="${not empty message}">
                <p style="color: green;">${message}</p>
            </c:if>

            <!-- 필터 영역 -->
            <div class="filter-group">
                <div class="form-group">
                    <label for="petIds">반려견</label>
                    <select id="petIds" name="petIds" multiple>
                        <option value="all" selected>전체</option>
                        <!-- petId 동적 추가 -->
                    </select>
                </div>
                <div class="form-group">
                    <label for="dtypes">일기 유형</label>
                    <select id="dtypes" name="dtypes" multiple>
                        <option value="all" selected>전체</option>
                        <option value="activity">활동</option>
                        <option value="behavior">행동</option>
                        <option value="grooming">미용</option>
                        <option value="health">건강</option>
                        <option value="meal">식사</option>
                    </select>
                </div>
                <button type="button" class="btn" onclick="fetchDiaries(0)">검색</button>
            </div>

            <!-- 일기 목록 -->
            <section class="diary-section">
                <div class="diary-header">
                    <h3>일기 목록 🐕</h3>
                    <button type="button" class="btn pet-register-btn" onclick="openCreateDiaryPopup()">새 일기 작성! ✍️</button>
                </div>
                <table id= "diaryTable">
                    <thead>
                        <tr>
                            <th>반려견</th>
                            <th>날짜</th>
                            <th>유형</th>
                            <th>세부 정보</th>
                            <th>설명</th>
                            <th>수정</th>
                            <th>삭제</th>
                        </tr>
                    </thead>
                    <tbody id="diaryBody">
                        <!-- 동적 렌더링 -->
                    </tbody>
                </table>
                <div id="noData" style="display: none; text-align: center; margin-top: 20px;">
                    <p>일기가 없습니다.</p>
                </div>
            </section>

            <!-- 페이징 -->
            <div class="pagination" id="pagination">
                <!-- 동적 렌더링 -->
            </div>
        </section>
    </main>

    <script>
        let currentPage = 0;
        const pageSize = 5;

        $(document).ready(function () {
            // petId 조회
            fetchPetIds();
            // 초기 일기 조회
            fetchDiaries(0);
        });

        // 최적화: 인증 확인 로직을 재사용 가능한 함수로 통합
        async function verifyAuth() {
            try {
                const response = await fetch('/api/v1/user/verify-auth', {
                    method: 'GET',
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    credentials: 'include'
                });
                const result = await response.json();
                if (!result.success) {
                    alert('세션이 만료되었습니다. 다시 로그인해주세요.');
                    window.location.href = '/auth/login';
                    return false;
                }
                return true;
            } catch (error) {
                console.error('인증 확인 오류:', error);
                alert('세션 확인 중 오류가 발생했습니다.');
                return false;
            }
        }

        // 최적화: 팝업 창 크기 및 위치 계산을 별도 함수로 분리
        function openPopup(url, name, width = 500, height = 600) {
            const windowWidth = window.outerWidth || 1920;
            const windowHeight = window.outerHeight || 1080;
            const windowLeft = window.screenX || window.screenLeft || 0;
            const windowTop = window.screenY || window.screenTop || 0;
            const left = windowLeft + (windowWidth - width) / 2;
            const top = windowTop + (windowHeight - height) / 2;

            window.open(url, name,
                `width=\${width},height=\${height},left=\${left},top=\${top},scrollbars=no,resizable=no`);
        }

        // petId 조회
        async function fetchPetIds() {
            try {
                const response = await fetch('/api/v1/pet', {
                    method: 'GET',
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    credentials: 'include'
                });
                const result = await response.json();
                if (result.success) {
                    const petInfos = result.data;

                    const petIdSelect = $('#petIds');
                    petInfos.forEach(petInfo => {
                        petIdSelect.append(`<option value="\${petInfo.id}">\${petInfo.name}</option>`);
                    });
                    // 세션 스토리지에 저장
                    sessionStorage.setItem('petInfos', JSON.stringify(petInfos));
                } else {
                    alert('반려견 목록 조회 실패: ' + result.message);
                }
            } catch (error) {
                console.error('petIds 조회 오류:', error);
                alert('반려견 목록을 불러오지 못했습니다.');
            }
        }

        // 일기 조회
        async function fetchDiaries(page) {
            currentPage = page;
            const petIds = $('#petIds').val().includes('all') ? [] : $('#petIds').val();
            const dtypes = $('#dtypes').val().includes('all') ? [] : $('#dtypes').val();

            const params = new URLSearchParams({
                page: page,
                size: pageSize
            });
            petIds.forEach(id => params.append('petId', id));
            dtypes.forEach(type => params.append('dtype', type));

            console.log("path : " + '/api/v1/diary?' + params.toString());
            try {
                $('#diaryBody').html('<tr><td colspan="7">로딩 중...</td></tr>');

                const response = await fetch('/api/v1/diary?' + params.toString(), {
                    method: 'GET',
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    credentials: 'include'
                });
                const result = await response.json();

                console.log('result :', result);

                if (result.success) {
                    renderDiaries(result.data);
                    renderPagination(result.data);
                } else {
                    $('#diaryBody').empty();
                    $('#noData').show();
                    $('#pagination').empty();
                    alert('일기 조회 실패: ' + result.message);
                }
            } catch (error) {
                console.error('일기 조회 오류:', error);
                $('#diaryBody').empty();
                $('#noData').show();
                $('#pagination').empty();
                alert('일기 조회 중 오류가 발생했습니다.');
            }
        }

        // 최적화: 일기 렌더링 로직 간소화 및 가독성 개선
        function renderDiaries(pageData) {
            const diaries = pageData.content;
            const tbody = $('#diaryBody');
            tbody.empty();
            $('#noData').hide();

            if (diaries.length === 0) {
                $('#noData').show();
                return;
            }

            const typeMap = {
                activity: {
                    label: '활동',
                    details: diary => `활동 유형: \${{
                        'WALK': '산책',
                        'PLAY': '놀이',
                        'TRAINING': '훈련',
                        'SWIM': '수영'
                    }[diary.activityType] || ''}, 장소: \${diary.location || ''}, 시간: \${diary.duration || ''}분, 거리: \${diary.distance != null ? diary.distance : ''}km`
                },
                behavior: {
                    label: '행동',
                    details: diary => `행동 유형: \${diary.behaviorType || ''}, 강도: \${{
                        'LOW': '낮음',
                        'MEDIUM': '보통',
                        'HIGH': '높음'
                    }[diary.behaviorIntensity] || ''}`
                },
                grooming: {
                    label: '미용',
                    details: diary => `미용 유형: \${{
                        'BATH': '목욕',
                        'HAIRCUT': '이발',
                        'NAIL_TRIM': '발톱 손질',
                        'EAR_CLEANING': '귀 청소',
                        'TEETH_CLEANING': '치아 관리'
                    }[diary.groomingType] || ''}`
                },
                health: {
                    label: '건강',
                    details: diary => `건강 유형: \${{
                        'VACCINATION': '예방접종',
                        'CHECKUP': '건강검진',
                        'SURGERY': '수술',
                        'MEDICATION': '투약'
                    }[diary.healthType] || ''}, 병원: \${diary.clinic || ''}, 다음 방문: \${diary.nextDueDate || ''}`
                },
                meal: {
                    label: '식사',
                    details: diary => `식사 유형: \${{
                        'BREAKFAST': '아침',
                        'LUNCH': '점심',
                        'DINNER': '저녁',
                        'SNACK': '간식'
                    }[diary.mealType] || ''}, 브랜드: \${diary.foodBrand || ''}, 양: \${diary.foodAmount || ''}g`
                }
            };

            diaries.forEach(diary => {
                const config = typeMap[diary.dtype] || { label: '', details: () => '' };
                const description = diary.description && diary.description.length > 30
                    ? diary.description.substring(0, 30) + '...'
                    : diary.description || '';

                const row = `
                    <tr>
                        <td>\${diary.petName || diary.petId}</td>
                        <td>\${diary.date}</td>
                        <td>\${config.label}</td>
                        <td>\${config.details(diary)}</td>
                        <td>\${description}</td>
                        <td><button type="button" class="btn edit" onclick="openDiaryEditPopup(\${diary.diaryId})">수정</button></td>
                        <td><button type="button" class="btn delete" onclick="deleteDiary(\${diary.diaryId})">삭제</button></td>
                    </tr>`;
                tbody.append(row);
            });
        }

        // 페이징 렌더링
        function renderPagination(pageData) {
            const totalPages = pageData.totalPages;
            const currentPage = pageData.number;
            const pagination = $('#pagination');
            pagination.empty();

            if (totalPages <= 1) return;

            // 이전 버튼
            if (!pageData.first) {
                pagination.append(`<button class="btn" onclick="fetchDiaries(\${currentPage - 1})">이전</button>`);
            }

            // 페이지 번호
            for (let i = 0; i < totalPages; i++) {
                if (i === currentPage) {
                    pagination.append(`<span class="page-number active">\${i + 1}</span>`);
                } else {
                    pagination.append(`<button class="btn page-number" onclick="fetchDiaries(\${i})">\${i + 1}</button>`);
                }
            }

            // 다음 버튼
            if (!pageData.last) {
                pagination.append(`<button class="btn" onclick="fetchDiaries(\${currentPage + 1})">다음</button>`);
            }
        }

        // 새 일기 등록 팝업
        async function openCreateDiaryPopup() {
            if (await verifyAuth()) {
                openPopup('/diary/new', 'createDiary');
            }
        }

        // 일기 수정 팝업
        async function openDiaryEditPopup(diaryId) {
            if (await verifyAuth()) {
                openPopup(`/diary/\${diaryId}`, 'editDiary');
            }
        }

        // 일기 삭제
        async function deleteDiary(diaryId) {
            if (!confirm('이 일기를 삭제하시겠습니까?')) return;

            try {
                const response = await fetch(`/api/v1/diary/\${diaryId}`, {
                    method: 'DELETE',
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    credentials: 'include'
                });
                const result = await response.json();
                if (result.success) {
                    alert('일기가 삭제되었습니다.');
                    fetchDiaries(currentPage);
                } else {
                    alert('일기 삭제 실패: ' + result.message);
                }
            } catch (error) {
                console.error('일기 삭제 오류:', error);
                alert('일기 삭제 중 오류가 발생했습니다.');
            }
        }
    </script>
</body>
</html>