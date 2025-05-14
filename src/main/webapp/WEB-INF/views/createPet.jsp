<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PetCare - 반려견 등록 🐾</title>
    <link rel="stylesheet" href="/resources/css/styles.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>

<body class="popup">
    <div class="popup-content">
        <h2>새 멍멍이 등록 🐶</h2>
        <form id="petRegisterForm">
            <div class="form-group">
                <label for="name">이름</label>
                <input type="text" id="name" name="name" placeholder="반려견 이름 (한글, 영문, 공백 1~50자)"
                    pattern="^[a-zA-Z가-힣\s]+$" maxlength="50" required>
                <div id="nameError" class="error"></div>
            </div>
            <div class="form-group">
                <label for="breed">품종</label>
                <input type="text" id="breed" name="breed" placeholder="품종 (1~50자)" maxlength="50" required>
                <div id="breedError" class="error"></div>
            </div>
            <div class="form-group">
                <label for="birthDate">생년월일</label>
                <input type="date" id="birthDate" name="birthDate" max="<%= java.time.LocalDate.now() %>" required>
                <div id="birthDateError" class="error"></div>
            </div>
            <div class="form-group">
                <label for="gender">성별</label>
                <select id="gender" name="gender" required>
                    <option value="" disabled selected>성별을 선택하세요</option>
                    <option value="MALE">남자</option>
                    <option value="FEMALE">여자</option>
                </select>
                <div id="genderError" class="error"></div>
            </div>
            <div class="form-group">
                <label for="weight">몸무게 (kg)</label>
                <input type="number" id="weight" name="weight" placeholder="몸무게 (0.01~999.99kg)" step="0.01" min="0.01"
                    max="999.99" required>
                <div id="weightError" class="error"></div>
            </div>
            <div class="form-group">
                <label for="description">설명</label>
                <textarea id="description" name="description" placeholder="반려견에 대한 설명 (최대 500자)"
                    maxlength="500"></textarea>
                <div id="descriptionError" class="error"></div>
            </div>
            <div class="form-group">
                <button type="button" id="submitBtn" class="btn full-width"
                    onclick="submitPetRegistration()">등록</button>
            </div>
        </form>
    </div>

    <script>
        $(document).ready(function () {
            // 클라이언트 측 유효성 검사
            $('#weight').on('input', function () {
                const weight = $(this).val();
                if (weight && (weight < 0.01 || weight > 999.99)) {
                    $('#weightError').text('몸무게는 0.01~999.99kg 사이여야 합니다.').addClass('error');
                } else {
                    $('#weightError').text('').removeClass('error');
                }
            });

            $('#name').on('input', function () {
                const name = $(this).val();
                const pattern = /^[a-zA-Z가-힣\s]+$/;
                if (name && !pattern.test(name)) {
                    $('#nameError').text('이름은 한글, 영문, 공백만 허용됩니다.').addClass('error');
                } else {
                    $('#nameError').text('').removeClass('error');
                }
            });
        });

        function submitPetRegistration() {
            const form = document.getElementById('petRegisterForm');
            const data = {
                name: form.querySelector('#name').value,
                breed: form.querySelector('#breed').value,
                birthDate: form.querySelector('#birthDate').value,
                gender: form.querySelector('#gender').value,
                weight: parseFloat(form.querySelector('#weight').value),
                description: form.querySelector('#description').value
            };

            console.log('POST 요청 전송: /api/v1/pet, 데이터:', data);

            // 에러 메시지 초기화
            $('#nameError, #breedError, #birthDateError, #genderError, #weightError, #descriptionError')
                .text('').removeClass('error');

            fetch('/api/v1/pet', {
                method: 'POST',
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
                        alert('반려견이 성공적으로 등록되었습니다!');

                        // 부모 창 새로고침 및 팝업 창 닫기
                        if(window.opener && !window.opener.closed) {
                            window.opener.location.reload();
                        }else{
                            console.warn('부모 창이 존재하지 않거나 닫혔습니다.')
                        }
                        window.close();
                    } else {
                        if (result.data) {
                            if (result.data.name) {
                                $('#nameError').text(result.data.name).addClass('error');
                            }
                            if (result.data.breed) {
                                $('#breedError').text(result.data.breed).addClass('error');
                            }
                            if (result.data.birthDate) {
                                $('#birthDateError').text(result.data.birthDate).addClass('error');
                            }
                            if (result.data.gender) {
                                $('#genderError').text(result.data.gender).addClass('error');
                            }
                            if (result.data.weight) {
                                $('#weightError').text(result.data.weight).addClass('error');
                            }
                            if (result.data.description) {
                                $('#descriptionError').text(result.data.description).addClass('error');
                            }
                        } else {
                            alert('반려견 등록에 실패했습니다: ' + result.message);
                        }
                    }
                })
                .catch(error => {
                    console.error('POST /api/v1/pet 오류:', error);
                    alert('반려견 등록 중 오류가 발생했습니다.');
                });
        }
    </script>
</body>

</html>