<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PetCare - 로그인 🐾</title>
    <link rel="stylesheet" href="/resources/css/styles.css">
    <link rel="icon" href="/favicon.ico" type="image/x-icon">
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
        <section class="form-container">
            <h2>로그인해서 멍멍이 만나러 가요! 🐾</h2>
            <form:form modelAttribute="loginReqDto" action="/auth/login" method="post" id="loginForm">
                <div class="form-group">
                    <form:label path="email" for="login-username">이메일</form:label>
                    <form:input path="email" id="login-username" placeholder="이메일을 입력하세요!" required="true" />
                    <form:errors path="email" cssClass="error" />
                </div>
                <div class="form-group">
                    <form:label path="password" for="login-password">비밀번호</form:label>
                    <form:input path="password" type="password" id="login-password" placeholder="비밀번호를 입력하세요!" required="true" />
                    <form:errors path="password" cssClass="error" element="div" />
                    <!-- 비밀번호 표시/숨기기 토글 -->
                    <button type="button" class="btn toggle-btn" onclick="togglePasswordVisibility()">비밀번호 표시</button>
                </div>
                <div class="form-group">
                    <input type="submit" value="로그인" id="loginButton" class="btn full-width" aria-label="로그인 버튼">
                </div>
            </form:form>
        </section>
    </main>

    <script>
        // 추가: 비밀번호 표시/숨기기 토글 기능
        function togglePasswordVisibility() {
            const passwordInput = document.getElementById('login-password');
            const toggleButton = document.querySelector('.toggle-btn');
            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                toggleButton.textContent = '비밀번호 숨기기';
            } else {
                passwordInput.type = 'password';
                toggleButton.textContent = '비밀번호 표시';
            }
        }

        // 추가: 폼 제출 시 로딩 상태 표시
        document.getElementById('loginForm').addEventListener('submit', function () {
            const loginButton = document.getElementById('loginButton');
            loginButton.disabled = true;
            loginButton.value = '로그인 중...';
        });
    </script>
</body>

</html>
