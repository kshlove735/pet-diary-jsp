<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
    <div class="header">
        <h1>PetCare 🐶🐾</h1>
    </div>
    <div class="nav">
        <a href="signup.jsp">회원가입</a>
        <a href="login.jsp">로그인</a>
        <a href="userInfo.jsp">유저저 정보</a>
    </div>
    <div class="container">
        <div class="form-container">
            <h2>로그인해서 멍멍이 만나러 가요! 🐾</h2>
            <form action="login.jsp" method="post">
                <div class="form-group">
                    <label for="login-username">이메일</label>
                    <input type="text" id="login-username" name="email" placeholder="이메일을 입력하세요!" required>
                </div>
                <div class="form-group">
                    <label for="login-password">비밀번호</label>
                    <input type="password" id="login-password" name="password" placeholder="비밀번호를 입력하세요!" required>
                </div>
                <div class="form-group">
                    <input type="submit" value="로그인!">
                </div>
            </form>
        </div>
    </div>
</body>
</html>