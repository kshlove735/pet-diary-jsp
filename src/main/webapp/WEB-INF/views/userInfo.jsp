<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PetCare - 멍멍 정보 🐾</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div class="header">
        <h1>PetCare 🐶🐾</h1>
    </div>
    <div class="nav">
        <a href="register.html">회원가입</a>
        <a href="login.html">로그인</a>
        <a href="info.html">멍멍 정보</a>
    </div>
    <div class="container">
        <div class="info-container">
            <h2>나와 멍멍이의 정보! 🐶</h2>
            <form action="update.jsp" method="post">
                <div class="form-group">
                    <label for="info-username">아이디</label>
                    <input type="text" id="info-username" name="username" readonly value="<%-- JSP로 데이터 삽입 --%>">
                </div>
                <div class="form-group">
                    <label for="info-name">이름</label>
                    <input type="text" id="info-name" name="name" value="<%-- JSP로 데이터 삽입 --%>">
                </div>
                <div class="form-group">
                    <label for="info-email">이메일</label>
                    <input type="email" id="info-email" name="email" value="<%-- JSP로 데이터 삽입 --%>">
                </div>
                <div class="form-group">
                    <label for="info-phone">전화번호</label>
                    <input type="text" id="info-phone" name="phone" value="<%-- JSP로 데이터 삽입 --%>">
                </div>
                <div class="form-group">
                    <input type="submit" value="정보 수정!">
                    <a href="delete.jsp" class="btn" onclick="return confirm('정말로 떠나실 건가요? 😢')">회원탈퇴</a>
                </div>
            </form>

            <div class="pet-section">
                <h3>내 멍멍이들! 🐕</h3>
                <table>
                    <thead>
                        <tr>
                            <th>이름</th>
                            <th>종</th>
                            <th>나이</th>
                            <th>성별</th>
                        </tr>
                    </thead>
                    <tbody>
                        <!-- JSP로 동적 데이터 삽입 -->
                        <tr>
                            <td><%-- 반려견 이름 --%></td>
                            <td><%-- 반려견 종 --%></td>
                            <td><%-- 반려견 나이 --%></td>
                            <td><%-- 반려견 성별 --%></td>
                        </tr>
                    </tbody>
                </table>
                <a href="pet_register.jsp" class="btn pet-register-btn">새 멍멍이 등록! 🐾</a>
            </div>
        </div>
    </div>
</body>
</html>