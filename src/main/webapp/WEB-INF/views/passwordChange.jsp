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
        <button type="button" class="btn close-btn" onclick="window.close()">✖</button>
        <h2 class="popup-title">비밀번호 변경</h2>
        <form id="passwordChangeForm">
            <div class="form-group">
                <label for="currentPassword">현재 비밀번호</label>
                <input type="password" id="currentPassword" name="currentPassword" placeholder="현재 비밀번호를 입력하세요" required>
                <div id="currentPasswordError" class="error"></div>
            </div>
            <div class="form-group">
                <label for="changedPassword">새 비밀번호</label>
                <input type="password" id="changedPassword" name="changedPassword" placeholder="새 비밀번호 (8~16자, 영문/숫자/특수문자)" required>
                <div id="changedPasswordError" class="error"></div>
            </div>
            <div class="form-group">
                <label for="changedPasswordCheck">비밀번호 확인</label>
                <input type="password" id="changedPasswordCheck" name="changedPasswordCheck" placeholder="비밀번호를 다시 입력하세요" required>
                <div id="changedPasswordCheckError" class="error"></div>
            </div>
            <div class="form-actions center">
                <button type="button" id="submitBtn" class="btn btn-primary full-width"
                    onclick="submitPasswordChange()">변경하기</button>
            </div>
        </form>
    </div>

    <script>
        $(function () {
            $('#changedPassword, #changedPasswordCheck').on('input', function () {
                const pwd = $('#changedPassword').val();
                const confirm = $('#changedPasswordCheck').val();
                if (pwd && confirm && pwd !== confirm) {
                    $('#changedPasswordCheckError').text('비밀번호가 일치하지 않습니다.').addClass('error');
                } else {
                    $('#changedPasswordCheckError').text('').removeClass('error');
                }
            });
        });

        function submitPasswordChange() {
            const form = $('#passwordChangeForm')[0];
            const data = {
                currentPassword: form.currentPassword.value,
                changedPassword: form.changedPassword.value,
                changedPasswordCheck: form.changedPasswordCheck.value
            };

            console.log('PUT 요청 전송: /api/v1/user/password, 데이터:', data);

            // 오류 초기화
            $('.error').text('').removeClass('error');

            fetch('/api/v1/user/password', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: JSON.stringify(data),
                credentials: 'include'
            })
            .then(res => res.json())
            .then(result => {
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
            .catch(err => {
                console.error(err);
                alert('비밀번호 변경 중 오류가 발생했습니다.');
            });
        }
    </script>
</body>
</html>