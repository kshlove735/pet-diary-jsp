<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PetCare - 유저 정보 🐾</title>
    <link rel="stylesheet" href="/resources/css/styles.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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
        <!-- 사용자 정보 카드 -->
        <section class="info-container">
            <div class="section-header">
                <h2>내 정보</h2>
                <button type="button" class="btn edit-btn" onclick="enableEdit()">✏️ 수정</button>
            </div>
            <form id="userInfoForm">
                <div class="form-grid">
                    <div class="form-group">
                        <label for="info-email">이메일</label>
                        <input type="email" id="info-email" name="email" disabled value="${userInfo.email}">
                    </div>
                    <div class="form-group">
                        <label for="info-name">이름</label>
                        <input type="text" id="info-name" name="name" disabled value="${userInfo.name}">
                        <div id="nameError" class="error"></div>
                    </div>
                    <div class="form-group">
                        <label for="info-phone">전화번호</label>
                        <input type="text" id="info-phone" name="phone" disabled value="${userInfo.phone}">
                        <div id="phoneError" class="error"></div>
                    </div>
                </div>
                <div class="form-actions">
                    <button type="button" id="changeButton" class="btn btn-primary" style="display:none" onclick="submitChanges()">저장</button>
                    <button type="button" class="btn btn-secondary" onclick="openPasswordChangePopup()">비밀번호 변경</button>
                    <button type="button" class="btn btn-danger" onclick="confirmDelete()">회원 탈퇴</button>
                </div>
            </form>
        </section>

        <!-- 반려견 카드 그리드 -->
        <section class="info-container pet-section">
            <div class="section-header">
                <h2>내 멍멍이</h2>
                <button type="button" class="btn btn-primary" onclick="openCreatePetPopup()">새 멍멍이 등록</button>
            </div>
            <div class="pet-grid">
                <c:forEach var="petInfo" items="${userInfo.petInfos}" varStatus="status">
                    <div class="pet-card">
                        <h4>${petInfo.name}</h4>
                        <p><strong>품종:</strong> ${petInfo.breed}</p>
                        <p><strong>생일:</strong> ${petInfo.birthDate}</p>
                        <p><strong>성별:</strong> <c:choose><c:when test="${petInfo.gender == 'MALE'}">남자</c:when><c:otherwise>여자</c:otherwise></c:choose></p>
                        <p><strong>무게:</strong> ${petInfo.weight}kg</p>
                        <p class="desc">
                            <c:choose>
                                <c:when test="${fn:length(petInfo.description) > 15}">
                                    ${fn:substring(petInfo.description, 0, 15)}...
                                </c:when>
                                <c:otherwise>
                                    ${petInfo.description}
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <button type="button" class="btn btn-secondary edit-pet-btn" onclick="openPetInfoPopup('${petInfo.id}')">수정</button>
                    </div>
                </c:forEach>
            </div>
        </section>
    </main>

    <script>
        function enableEdit() {
            document.querySelectorAll('#userInfoForm input:not(#info-email)').forEach(input => input.disabled = false);
            document.getElementById('changeButton').style.display = 'inline-block';
            document.querySelector('.edit-btn').style.display = 'none';
            $('#nameError, #phoneError').text('').removeClass('error');
        }

        async function submitChanges() {
            const form = document.getElementById('userInfoForm');
            const data = {
                name: form.querySelector('#info-name').value,
                phone: form.querySelector('#info-phone').value
            };

            console.log('PUT 요청 전송: /api/v1/user, 데이터:', data);

            try {
                const response = await fetch('/api/v1/user', {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-Requested-With': 'XMLHttpRequest' // AJAX 요청 표시
                    },
                    body: JSON.stringify(data),
                    credentials: 'include' //쿠키 포함
                });

                const result = await response.json();

                console.log('응답 : ', result);

                if (result.success) {
                    alert('정보가 성공적으로 수정되었습니다!');
                    window.location.reload();
                } else {
                    // 이전 오류 메시지 지우기
                    $('#nameError, #phoneError').text('').removeClass('error');

                    // 유효성 검사 오류 표시
                    if (result.data) {
                        if (result.data.name) {
                            $('#nameError').text(result.data.name).addClass('error');
                        }
                        if (result.data.phone) {
                            $('#phoneError').text(result.data.phone).addClass('error');
                        }
                    } else {
                        alert('정보 수정에 실패했습니다 : ' + result.message);
                    }
                }
            } catch (error) {
                console.error('PUT /user 오류:', error);
                alert('정보 수정 중 오류가 발생했습니다.');
            }
        }

        async function openPasswordChangePopup() {
            // 인증 상태 확인
            try {
                const response = await fetch('/api/v1/user/verify-auth', {
                    method: 'GET',
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    credentials: 'include'
                });

                const result = await response.json();
                console.log('인증 확인 응답:', result);

                if (result.success) {
                    // 팝업 창 크기
                    const width = 405;
                    const height = 400;

                    // 현재 창의 위치와 크기를 기준으로 팝업 창 중앙 정렬
                    const windowWidth = window.outerWidth || 1920; // 현재 창 너비
                    const windowHeight = window.outerHeight || 1080; // 현재 창 높이
                    const windowLeft = window.screenX || window.screenLeft || 0; // 현재 창의 X 좌표
                    const windowTop = window.screenY || window.screenTop || 0; // 현재 창의 Y 좌표
                    const left = windowLeft + (windowWidth - width) / 2;
                    const top = windowTop + (windowHeight - height) / 2;

                    window.open('/user/password', 'passwordChange',
                        'width=' + width + ',height=' + height + ',left=' + left + ',top=' + top + ',scrollbars=no,resizable=no');

                } else {
                    alert('세션이 만료되었습니다. 다시 로그인해주세요.');
                    window.location.href = '/auth/login';
                }
            } catch (error) {
                console.error('인증 확인 오류:', error);
                alert('세션 확인 중 오류가 발생했습니다. 다시 로그인해주세요.');
                window.location.href = '/auth/login';
            }
        }

        function confirmDelete() {
            if (window.confirm('정말로 회원 탈퇴하시겠습니까? 모든 데이터가 삭제됩니다.')) {
                submitDelete();
            }
        }

        async function submitDelete() {
            try {
                const response = await fetch('/api/v1/user', {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    credentials: 'include' //쿠키 포함
                });

                const result = await response.json();
                console.log('응답:', result);
                if (result.success) {
                    alert('회원 탈퇴가 완료되었습니다.');
                    window.location.href = '/auth/login';
                } else {
                    alert('회원 탈퇴에 실패하였습니다. : ' + result.message);
                }
            } catch (error) {
                console.error('회원 탈퇴 오류:', error);
                alert('회원 탈퇴 중 오류가 발생했습니다.');
            }

        }

        // 반려견 등록 팝업 열기
        async function openCreatePetPopup() {
            // 인증 상태 확인
            try {
                const response = await fetch('/api/v1/user/verify-auth', {
                    method: 'GET',
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    credentials: 'include'
                });

                const result = await response.json();
                console.log('인증 확인 응답:', result);

                if (result.success) {
                    // 팝업 창 크기
                    const width = 400;
                    const height = 805;

                    // 현재 창의 위치와 크기를 기준으로 팝업 창 중앙 정렬
                    const windowWidth = window.outerWidth || 1920; // 현재 창 너비
                    const windowHeight = window.outerHeight || 1080; // 현재 창 높이
                    const windowLeft = window.screenX || window.screenLeft || 0; // 현재 창의 X 좌표
                    const windowTop = window.screenY || window.screenTop || 0; // 현재 창의 Y 좌표
                    const left = windowLeft + (windowWidth - width) / 2;
                    const top = windowTop + (windowHeight - height) / 2;

                    window.open('/pet', 'createPet',
                        'width=' + width + ',height=' + height + ',left=' + left + ',top=' + top + ',scrollbars=no,resizable=no');

                } else {
                    alert('세션이 만료되었습니다. 다시 로그인해주세요.');
                    window.location.href = '/auth/login';
                }
            } catch (error) {
                console.error('인증 확인 오류:', error);
                alert('세션 확인 중 오류가 발생했습니다. 다시 로그인해주세요.');
                window.location.href = '/auth/login';
            }
        }

        // 반려견 수정 팝업 열기
        async function openPetInfoPopup(petId) {
            // 인증 상태 확인
            try {
                const response = await fetch('/api/v1/user/verify-auth', {
                    method: 'GET',
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    credentials: 'include'
                });

                const result = await response.json();
                console.log('인증 확인 응답:', result);

                if (result.success) {
                    // 팝업 창 크기
                    const width = 400;
                    const height = 805;

                    // 현재 창의 위치와 크기를 기준으로 팝업 창 중앙 정렬
                    const windowWidth = window.outerWidth || 1920; // 현재 창 너비
                    const windowHeight = window.outerHeight || 1080; // 현재 창 높이
                    const windowLeft = window.screenX || window.screenLeft || 0; // 현재 창의 X 좌표
                    const windowTop = window.screenY || window.screenTop || 0; // 현재 창의 Y 좌표
                    const left = windowLeft + (windowWidth - width) / 2;
                    const top = windowTop + (windowHeight - height) / 2;

                    window.open('/pet/'+petId, 'petInfo',
                        'width=' + width + ',height=' + height + ',left=' + left + ',top=' + top + ',scrollbars=no,resizable=no');

                } else {
                    alert('세션이 만료되었습니다. 다시 로그인해주세요.');
                    window.location.href = '/auth/login';
                }
            } catch (error) {
                console.error('인증 확인 오류:', error);
                alert('세션 확인 중 오류가 발생했습니다. 다시 로그인해주세요.');
                window.location.href = '/auth/login';
            }
            
        }
    </script>
</body>
</html>