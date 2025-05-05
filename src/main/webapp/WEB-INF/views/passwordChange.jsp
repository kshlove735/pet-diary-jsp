<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>비밀번호 변경</title>
    <link rel="stylesheet" href="/resources/styles.css">
</head>
<body class="popup">
    <h2>비밀번호 변경</h2>
    <form id="passwordChangeForm">
        <div class="form-group">
            <label for="current-password">현재 비밀번호</label>
            <input type="password" id="current-password" name="currentPassword" required>
        </div>
        <div class="form-group">
            <label for="new-password">변경할 비밀번호</label>
            <input type="password" id="new-password" name="newPassword" required>
        </div>
        <div class="form-group">
            <label for="confirm-password">변경할 비밀번호 확인</label>
            <input type="password" id="confirm-password" name="confirmPassword" required>
            <span id="password-error" class="error"></span>
        </div>
        <div class="form-group">
            <button type="button" class="btn" onclick="submitPasswordChange()">변경</button>
        </div>
    </form>

    <script>
        async function submitPasswordChange() {
            const form = document.getElementById('passwordChangeForm');
            const currentPassword = form.querySelector('#current-password').value;
            const newPassword = form.querySelector('#new-password').value;
            const confirmPassword = form.querySelector('#confirm-password').value;
            const errorSpan = document.getElementById('password-error');

            if (newPassword !== confirmPassword) {
                errorSpan.textContent = '변경할 비밀번호가 일치하지 않습니다.';
                return;
            }

            const data = {
                currentPassword: currentPassword,
                newPassword: newPassword
            };

            try {
                const response = await fetch('/api/v1/user/password', {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(data),
                    credentials: 'include' //쿠키 포함
                });

                const result = await response.json();
                if (result.success) {
                    alert('비밀번호가 성공적으로 변경되었습니다!');
                    window.close();
                } else {
                    alert('비밀번호 변경에 실패했습니다: ' + result.message);
                }
            } catch (error) {
                console.error('Error:', error);
                alert('비밀번호 변경 중 오류가 발생했습니다.');
            }
        }
    </script>
</body>
</html>