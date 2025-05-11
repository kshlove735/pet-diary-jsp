<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PetCare - 멍멍 정보 🐾</title>
    <link rel="stylesheet" href="/resources/css/styles.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <div class="header">
        <h1>PetCare 🐶🐾</h1>
    </div>
    <div class="nav">
        <a href="/api/v1/auth/signup">회원가입</a>
        <a href="/api/v1/auth/login">로그인</a>
        <a href="/api/v1/user">유저 정보</a>
    </div>
    <div class="container">
        <div class="info-container">
            <h2>나와 멍멍이의 정보! 🐶</h2>
            <c:if test="${not empty message}">
                <p style="color: green;">${message}</p>
            </c:if>
            <form id="userInfoForm">
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
                <div class="form-group">
                    <button type="button" id="editButton" class="btn" onclick="enableEdit()">정보 수정</button>
                    <button type="button" id="changeButton" class="btn" onclick="submitChanges()">변경</button>
                    <button type="button" class="btn" onclick="openPasswordChangePopup()">비밀번호 수정</button>
                    <a href="/api/v1/user/delete" class="btn" onclick="return confirm('정말로 떠나실 건가요? 😢')">회원탈퇴</a>
                </div>
            </form>

            <div class="pet-section">
                <h3>내 멍멍이들! 🐕</h3>
                <table>
                    <thead>
                        <tr>
                            <th>이름</th>
                            <th>종</th>
                            <th>생일</th>
                            <th>성별</th>
                            <th>무게(kg)</th>
                            <th>설명</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="petInfo" items="${userInfo.petInfos}">
                            <tr>
                                <td>${petInfo.name}</td>
                                <td>${petInfo.breed}</td>
                                <td>${petInfo.birthDate}</td>
                                <c:if test="${petInfo.gender == 'MALE'}">
                                    <td>남자</td>
                                </c:if>
                                <c:if test="${petInfo.gender == 'FEMALE'}">
                                    <td>여자</td>
                                </c:if>
                                <td>${petInfo.weight}</td>
                                <td>${petInfo.description}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <a href="/api/v1/pet/register" class="btn pet-register-btn">새 멍멍이 등록! 🐾</a>
            </div>
        </div>
    </div>

    <script>
        function enableEdit() {
            document.querySelectorAll('#userInfoForm input:not(#info-email)').forEach(input => {
                input.disabled = false;
            });
            document.getElementById('editButton').style.display = 'none';
            document.getElementById('changeButton').style.display = 'inline-block';
            // 이전 오류 메시지 지우기
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
                  // 유효성 검사 오류 표시
                  if(result.data){
                     // 이전 오류 메시지 지우기
                    $('#nameError, #phoneError').text('').removeClass('error');

                    if(result.data.name){
                        $('#nameError').text(result.data.name).addClass('error');
                    }
                    if(result.data.phone){
                        $('#phoneError').text(result.data.phone).addClass('error');
                    }
                  }else {
                    alert('정보 수정에 실패했습니다 : ' + result.message);
                  }
                }
            } catch (error) {
                console.error('PUT /api/v1/user 오류:', error);
                alert('정보 수정 중 오류가 발생했습니다.');
            }
        }

        function openPasswordChangePopup() {
            window.open('/api/v1/user/password/change', 'passwordChange', 'width=400,height=500,scrollbars=no,resizable=no');
        }
    </script>
</body>
</html>