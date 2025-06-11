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
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
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

            <!-- 헤더 & 새 일기 버튼 -->
            <div class="section-header">
                <h2>멍멍이 일기 📖</h2>
                <button type="button" class="btn btn-primary" onclick="openCreateDiaryPopup()">
                    새 일기 작성 ✍️
                </button>
            </div>

            <!-- 필터 바 -->
            <div class="filter-group">
                <!-- 반려견 체크박스 -->
                <div class="filter-item">
                    <label>반려견</label>
                    <div class="checkbox-group" id="petCheckboxes">
                        <label class="checkbox-pill">
                            <input type="checkbox" value="all" checked> 전체
                        </label>
                        <!-- JS로 petId 목록을 체크박스로 추가 -->
                    </div>
                </div>

                <!-- 일기 유형 체크박스 -->
                <div class="filter-item">
                    <label>일기 유형</label>
                    <div class="checkbox-group" id="dtypeCheckboxes">
                        <label class="checkbox-pill">
                            <input type="checkbox" value="all" checked> 전체
                        </label>
                        <label class="checkbox-pill">
                            <input type="checkbox" value="activity"> 활동
                        </label>
                        <label class="checkbox-pill">
                            <input type="checkbox" value="behavior"> 행동
                        </label>
                        <label class="checkbox-pill">
                            <input type="checkbox" value="grooming"> 미용
                        </label>
                        <label class="checkbox-pill">
                            <input type="checkbox" value="health"> 건강
                        </label>
                        <label class="checkbox-pill">
                            <input type="checkbox" value="meal"> 식사
                        </label>
                    </div>
                </div>

                <!-- 검색 버튼 -->
                <button type="button" class="btn btn-secondary search-btn" onclick="fetchDiaries(0)">
                    검색
                </button>
            </div>

            <!-- 일기 없음 메시지 -->
            <div id="noData" style="display:none;">
                일기가 없습니다.
            </div>

            <!-- 일기 카드 그리드 -->
            <div id="diaryGrid" class="pet-grid">
                <!-- JS에서 .pet-card 형식으로 동적 렌더링 -->
            </div>

            <!-- 페이징 -->
            <div id="pagination" class="pagination"></div>
        </section>
    </main>

    <script>
        let currentPage = 0;
        const pageSize = 6;

        $(document).ready(function () {
            // 1) 반려견 목록 로드 & 체크박스 생성
            fetchPetIds();    

            // 2) “전체” 토글 + 개별 체크박스 동기화
            $('#petCheckboxes')
            // 전체 체크박스 클릭 시
            .on('change', 'input[value="all"]', function() {
                const checked = this.checked;
                // 전체 체크박스 상태에 따라 모든 체크박스 동기화
                $('#petCheckboxes input').prop('checked', checked);
            })
            // 개별 체크박스 클릭 시
            .on('change', 'input:not([value="all"])', function() {
                // 전체가 아니면서 체크된 개수
                const allCount     = $('#petCheckboxes input:not([value="all"])').length;
                const checkedCount = $('#petCheckboxes input:not([value="all"]):checked').length;
                // 모든 개별이 체크되어야만 전체가 체크됨
                $('#petCheckboxes input[value="all"]').prop('checked', checkedCount === allCount);
            });

            $('#dtypeCheckboxes')
            .on('change', 'input[value="all"]', function() {
                const checked = this.checked;
                $('#dtypeCheckboxes input').prop('checked', checked);
            })
            .on('change', 'input:not([value="all"])', function() {
                const allCount     = $('#dtypeCheckboxes input:not([value="all"])').length;
                const checkedCount = $('#dtypeCheckboxes input:not([value="all"]):checked').length;
                $('#dtypeCheckboxes input[value="all"]').prop('checked', checkedCount === allCount);
            });

            // 3) 초기 일기 조회
            fetchDiaries(0);  // 초기 일기 로드
        });

        // 인증 확인
        async function verifyAuth() {
            try {
                const res = await fetch('/api/v1/user/verify-auth', {
                    method: 'GET',
                    headers: { 'X-Requested-With': 'XMLHttpRequest' },
                    credentials: 'include'
                });
                const result = await res.json();
                if (!result.success) {
                    alert('세션이 만료되었습니다. 다시 로그인해주세요.');
                    window.location.href = '/auth/login';
                    return false;
                }
                return true;
            } catch (err) {
                console.error(err);
                alert('인증 확인 중 오류가 발생했습니다.');
                return false;
            }
        }

        // 범용 팝업 열기(일기 작성, 수정)
        function openPopup(url, name, width = 420, height = 600) {
            const windowWidth = window.outerWidth || 1920;
            const windowHeight = window.outerHeight || 1080;
            const windowLeft = window.screenX || window.screenLeft || 0;
            const windowTop = window.screenY || window.screenTop || 0;
            const left = windowLeft + (windowWidth - width) / 2;
            const top = windowTop + (windowHeight - height) / 2;

            window.open(url, name,
                `width=\${width},height=\${height},left=\${left},top=\${top},scrollbars=no,resizable=no`);
        }

        // 반려견 목록 조회
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

                    const container = $('#petCheckboxes');
                    petInfos.forEach(petInfo => {
                        container.append(`
                            <label class="checkbox-pill">
                                <input type="checkbox" value="\${petInfo.id}"> \${petInfo.name}
                            </label>`);
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
            // const petIds = $('#petIds').val().includes('all') ? [] : $('#petIds').val();
            // const dtypes = $('#dtypes').val().includes('all') ? [] : $('#dtypes').val();

            // 체크된 값만 수집, 'all'은 제외
            const petIds = $('#petCheckboxes input:checked')
                .map((_,el)=>el.value).get()
                .filter(v=>v!=='all');
            const dtypes = $('#dtypeCheckboxes input:checked')
                .map((_,el)=>el.value).get()
                .filter(v=>v!=='all');


            const params = new URLSearchParams({ page, size: pageSize });
            petIds.forEach(id => params.append('petId', id));
            dtypes.forEach(type => params.append('dtype', type));

            $('#diaryGrid').empty();
            $('#noData').hide();
            $('#pagination').empty();

            try {
                const res = await fetch('/api/v1/diary?' + params.toString(), {
                    method: 'GET',
                    headers: { 'X-Requested-With': 'XMLHttpRequest' },
                    credentials: 'include'
                });
                const result = await res.json();
                if (result.success) {
                    renderDiaries(result.data);
                    renderPagination(result.data);
                } else {
                    $('#noData').show();
                    alert('일기 조회 실패: ' + result.message);
                }
            } catch (err) {
                console.error(err);
                $('#noData').show();
                alert('일기 조회 중 오류가 발생했습니다.');
            }
        }

        // 일기 렌더링 (카드 그리드)
        function renderDiaries(pageData) {
            const { content: diaries } = pageData;
            const grid = $('#diaryGrid');
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
                const description = diary.description && diary.description.length > 50
                    ? diary.description.substring(0, 50) + '...'
                    : diary.description || '';

                const card = `
                    <div class="pet-card">
                        <h4 style="display:flex; justify-content:space-between; margin-bottom:8px;">
                            <span>\${diary.petName || diary.petId}</span>
                            <small style="color:#999;">\${diary.date}</small>
                        </h4>
                        <p><strong>\${config.label}</strong></p>
                        <p style="color:#555; margin:6px 0;">\${config.details(diary)}</p>
                        <p style="color:#333; margin-bottom:12px;">\${description}</p>
                        <div style="display:flex; gap:8px;">
                            <button class="btn btn-secondary" onclick="openDiaryEditPopup(\${diary.diaryId})">수정</button>
                            <button class="btn btn-danger"   onclick="deleteDiary(\${diary.diaryId})">삭제</button>
                        </div>
                    </div>`;
                grid.append(card);
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
                    pagination.append(`<button class="btn page-number active">\${i + 1}</button>`);
                } else {
                    pagination.append(`<button class="btn page-number" onclick="fetchDiaries(\${i})">\${i + 1}</button>`);
                }
            }

            // 다음 버튼
            if (!pageData.last) {
                pagination.append(`<button class="btn" onclick="fetchDiaries(\${currentPage + 1})">다음</button>`);
            }
        }

        // 새 일기 작성 팝업
        async function openCreateDiaryPopup() {
            if (await verifyAuth()) openPopup('/diary/new', 'createDiary');
        }
        // 일기 수정 팝업
        async function openDiaryEditPopup(diaryId) {
            if (await verifyAuth()) openPopup(`/diary/\${diaryId}`, 'editDiary');
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