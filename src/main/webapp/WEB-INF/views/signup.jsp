<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PetCare - 회원가입 🐾</title>
    <link rel="stylesheet" href="/resources/css/styles.css">
</head>
<body>
    <div class="header">
        <h1>PetCare 🐶🐾</h1>
    </div>
    <div class="nav">
        <a href="signup.jsp">회원가입</a>
        <a href="login.jsp">로그인</a>
        <a href="dogInfo.jsp">멍멍 정보</a>
    </div>
    <div class="container">
        <div class="form-container">
            <h2>가입하고 멍멍이와 함께해요! 🐕</h2>
            <form action="/api/v1/auth/signup" method="post">
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="text" id="email" name="email" placeholder="이메일을 입력하세요!" required>
                </div>
                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <input type="password" id="password" name="password" placeholder="비밀번호(영문 대 소문자, 숫자, 특수문자 포함 8~16자)를 입력하세요!" required>
                </div>
                <div class="form-group">
                    <label for="passwordCheck">비밀번호 확인</label>
                    <input type="password" id="passwordCheck" name="passwordCheck" placeholder="비밀번호를 재입력하세요!" required>
                </div>
                <div class="form-group">
                    <label for="name">이름</label>
                    <input type="text" id="name" name="name" placeholder="이름(특수문자를 제외한 2~10자)을 입력하세요!" required>
                </div>
                <div class="form-group">
                    <label for="phone">전화번호</label>
                    <input type="text" id="phone" name="phone" placeholder="전화번호를 입력하세요!">
                </div>
                <div class="form-group">
                    <input type="submit" value="가입하기!">
                </div>
            </form>
        </div>
    </div>
</body>
</html>