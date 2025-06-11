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
            <h2>멍멍이의 새로운 친구가 되어주세요! 🐕</h2>
            <form:form modelAttribute="signupReqDto" action="/auth/signup" method="post" id="signupForm">
                <form:hidden path="emailChecked" value="false" />
                <div class="form-group">
                    <label for="email">이메일</label>
                    <form:input type="email" id="email" path="email" placeholder="이메일을 입력하세요!" required="true" />
                    <form:errors path="email" cssClass="error" />
                    <div id="emailError" class="error"></div>
                    <button type="button" id="checkEmailBtn" class="btn toggle-btn" onclick="checkEmailAvailability()">이메일 중복 검사</button>
                </div>
                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <form:input type="password" id="password" path="password" placeholder="비밀번호(영문 대 소문자, 숫자, 특수문자 포함 8~16자)를 입력하세요!" required="true" />
                    <form:errors path="password" cssClass="error" />
                </div>
                <div class="form-group">
                    <label for="passwordCheck">비밀번호 확인</label>
                    <form:input type="password" id="passwordCheck" path="passwordCheck" placeholder="비밀번호를 재입력하세요!" required="true" />
                    <form:errors path="passwordCheck" cssClass="error" />
                    <div id="passwordCheckError" class="error"></div>
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
                    <input type="submit" id="submitBtn" class="btn full-width" value="가입하기" disabled="true">
                </div>
            </form:form>
        </section>
    </main>

    <script>
        let isEmailChecked = false; // 이메일 중복 검사 여부 플래그

        $(document).ready(function () {

            // 이메일 중복 검사 버튼 클릭 시
            $("#checkEmailBtn").click(function () {
                checkEmailAvailability();
            });


            // 이메일 중복 검사
            async function checkEmailAvailability() {
                // 이전 오류 메시지 지우기
                $('#emailError').text('').addClass('success').removeClass('error');

                // 유효성 검사(빈칸인지)
                const email = $('#email').val();
                if (!email) {
                    $('#emailError').text('이메일을 입력해주세요!').removeClass('success').addClass('error');
                    return;
                }

                try {
                    const response = await fetch('/api/v1/auth/check-email?email=' + encodeURIComponent(email), {
                        method: 'GET',
                        headers: {
                            'X-Requested-With': 'XMLHttpRequest'
                        },
                        credentials: 'include'
                    });

                    const result = await response.json();

                    $("#emailError").text(result.message).removeClass(result.success ? "error" : "success").addClass(result.success ? "success" : "error");

                    isEmailChecked = result.success;

                    $("#emailChecked").val(result.success); // hidden 필드 업데이트
                    $("#submitBtn").prop("disabled", !result.success); // 버튼 활성화/비활성화

                    if (result.data) {
                        if (result.data.email) {
                            $('#emailError').text(result.data.email).addClass('error');
                        }
                    }


                } catch (error) {
                    console.error('GET /api/v1/auth/check-email 오류:', error);
                    alert('이메일 중복 검사 중 오류가 발생했습니다.');
                    isEmailChecked = false;
                }
            }

            // 비밀번호 일치 확인
            $("#passwordCheck").on("input", function () {
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
</body>

</html>