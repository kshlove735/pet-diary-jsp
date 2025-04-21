<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PetCare - 회원가입 🐾</title>
    <link rel="stylesheet" href="/resources/css/styles.css">
    <link rel="icon" href="/favicon.ico" type="image/x-icon">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function () {
            let isEmailChecked = false; // 이메일 중복 검사 여부 플래그

            // 이메일 중복 검사 버튼 클릭 시
            $("#checkEmailBtn").click(function () {
                const email = $("#email").val();
                if (!email) {
                    $("#emailError").text("이메일을 입력해주세요!").removeClass("success").addClass("error");
                    return;
                }
                $.ajax({
                    url: "/api/v1/auth/check-email",
                    type: "POST",
                    data: { email: email },
                    success: function (response) {
                        $("#emailError").text(response.message).removeClass(response.available ? "error" : "success").addClass(response.available ? "success" : "error");
                        isEmailChecked = response.available;
                        $("#emailChecked").val(isEmailChecked); // hidden 필드 업데이트
                        $("#submitBtn").prop("disabled", !isEmailChecked); // 버튼 활성화/비활성화
                    },
                    error: function () {
                        $("#emailError").text("이메일 검사 중 오류가 발생했습니다.").removeClass("success").addClass("error");
                        isEmailChecked = false;
                        $("#emailChecked").val(false); // hidden 필드 업데이트
                        $("#submitBtn").prop("disabled", true);
                    }
                });
            });

            // 비밀번호 일치 확인
            $("#password, #passwordCheck").on("input", function () {
                const password = $("#password").val();
                const passwordCheck = $("#passwordCheck").val();
                if (password && passwordCheck && password !== passwordCheck) {
                    $("#passwordCheckError").text("비밀번호가 일치하지 않습니다.").removeClass("success").addClass("error");
                } else {
                    $("#passwordCheckError").text("").removeClass("error");
                }
            });

            // 전화번호 형식 검사
            $("#phone").on("input", function () {
                const phone = $(this).val().replace(/\s/g, ""); // 공백 제거
                $(this).val(phone);
                const phonePattern = /^01[0-1,6-9]-?\d{3,4}-?\d{4}$/;
                if (phone && !phonePattern.test(phone)) {
                    $("#phoneError").text("유효한 전화번호 형식을 입력해주세요. (예: 010-1234-5678)").addClass("error");
                } else {
                    $("#phoneError").text("").removeClass("error");
                }
            });

            // 가입하기 버튼 클릭 시 이메일 중복 검사 여부 확인
            $("#signupForm").submit(function (event) {
                if (!isEmailChecked) {
                    event.preventDefault();
                    $("#emailError").text("이메일 중복 검사를 진행해주세요.").removeClass("success").addClass("error");
                }
            });
        });
    </script>

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
        <div class="form-container">
            <h2>가입하고 멍멍이와 함께해요! 🐕</h2>
            <c:if test="${not empty message}">
                <p class="success">${message}</p>
            </c:if>
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <form:form modelAttribute="signupReqDto" action="/api/v1/auth/signup" method="post" id="signupForm">
                <form:hidden path="emailChecked" value="${isEmailChecked ? 'true' : 'false'}" />
                <div class="form-group">
                    <label for="email">이메일</label>
                    <form:input type="email" id="email" path="email" placeholder="이메일을 입력하세요!" required="true" />
                    <button type="button" id="checkEmailBtn" class="btn">이메일 중복 검사</button>
                    <div id="emailError" class="error"></div>
                    <form:errors path="email" cssClass="error" />
                </div>
                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <form:input type="password" id="password" path="password"
                        placeholder="비밀번호(영문 대 소문자, 숫자, 특수문자 포함 8~16자)를 입력하세요!" required="true" />
                    <form:errors path="password" cssClass="error" />
                </div>
                <div class="form-group">
                    <label for="passwordCheck">비밀번호 확인</label>
                    <form:input type="password" id="passwordCheck" path="passwordCheck" placeholder="비밀번호를 재입력하세요!" required="true" />
                    <form:errors path="passwordCheck" cssClass="error" />
                </div>
                <div class="form-group">
                    <label for="name">이름</label>
                    <form:input type="text" id="name" path="name" placeholder="이름(특수문자를 제외한 2~10자)을 입력하세요!" required="true" />
                    <form:errors path="name" cssClass="error" />
                </div>
                <div class="form-group">
                    <label for="phone">전화번호</label>
                    <form:input type="text" id="phone" path="phone" placeholder="전화번호(예: 010-1234-5678)" required="true" />
                    <div id="phoneError" class="error"></div>
                    <form:errors path="phone" cssClass="error" />
                </div>
                <div class="form-group">
                    <input type="submit" id="submitBtn" value="가입하기!" disabled="true">
                </div>
            </form:form>
        </div>
    </div>
</body>

</html>