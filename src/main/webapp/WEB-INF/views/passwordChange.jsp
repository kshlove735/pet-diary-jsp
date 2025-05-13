<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>비밀번호 변경</title>
    <link rel="stylesheet" href="/resources/css/styles.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body class="popup">
    <div class="popup-content">
        <h2>비밀번호 변경</h2>
        <form id="passwordChangeForm">
            <div class="form-group">
                <label for="currentPassword">현재 비밀번호</label>
                <input type="password" id="currentPassword" name="currentPassword" 
                    placeholder="비밀번호(영문 대 소문자, 숫자, 특수문자 포함 8~16자)를 입력하세요!" required>
                <div id="currentPasswordError" class="error"></div>
            </div>
            <div class="form-group">
                <label for="changedPassword">변경할 비밀번호</label>
                <input type="password" id="changedPassword" name="changedPassword" 
                    placeholder="변경할 비밀번호(영문 대 소문자, 숫자, 특수문자 포함 8~16자)를 입력하세요!" required>
                <div id="changedPasswordError" class="error"></div>
            </div>
            <div class="form-group">
                <label for="changedPasswordCheck">변경할 비밀번호 확인</label>
                <input type="password" id="changedPasswordCheck" name="changedPasswordCheck" 
                    placeholder="비밀번호를 재입력하세요!" required>
                <div id="changedPasswordCheckError" class="error"></div>
            </div>
            <div class="form-group">
                <button type="button" id="submitBtn" class="btn full-width" onclick="submitPasswordChange()">변경</button>
            </div>
        </form>
    </div>

    

    <script>
        $(document).ready(function() {
            $('#changedPassword, #changedPasswordCheck').on('input', function() {
                const changedPassword = $('#changedPassword').val();
                const changedPasswordCheck = $('#changedPasswordCheck').val();
                if (changedPassword && changedPasswordCheck && changedPassword !== changedPasswordCheck) {
                    $('#changedPasswordCheckError').text('비밀번호가 일치하지 않습니다.').addClass('error');
                } else {
                    $('#changedPasswordCheckError').text('').removeClass('error');
                }
            });
        });


        function submitPasswordChange() {
            const form = document.getElementById('passwordChangeForm');
            const data = {
                currentPassword: form.querySelector('#currentPassword').value,
                changedPassword: form.querySelector('#changedPassword').value,
                changedPasswordCheck: form.querySelector('#changedPasswordCheck').value
            };

            console.log('PUT 요청 전송: /api/v1/user/password, 데이터:', data);

            // 이전 오류 메시지 지우기
            $('#currentPasswordError, #changedPasswordError, #changedPasswordCheckError')
                .text('').removeClass('error');

            fetch('/api/v1/user/password', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: JSON.stringify(data),
                credentials: 'include'
            })
            .then(response => response.json())
            .then(result => {
                console.log('응답:', result);
                if (result.success) {
                    alert('비밀번호가 성공적으로 변경되었습니다!');
                    window.close();
                } else {
                    if (result.data) {
                        if (result.data.currentPassword) {
                            $('#currentPasswordError').text(result.data.currentPassword).addClass('error');
                        }
                        if (result.data.changedPassword) {
                            $('#changedPasswordError').text(result.data.changedPassword).addClass('error');
                        }
                        if (result.data.changedPasswordCheck) {
                            $('#changedPasswordCheckError').text(result.data.changedPasswordCheck).addClass('error');
                        }
                    } else {
                        alert('비밀번호 변경에 실패했습니다: ' + result.message);
                    }
                }
            })
            .catch(error => {
                console.error('PUT /api/v1/user/password 오류:', error);
                alert('비밀번호 변경 중 오류가 발생했습니다.');
            });
        }
    </script>
</body>
</html>