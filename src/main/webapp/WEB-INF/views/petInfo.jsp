<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PetCare - 반려견 수정 🐾</title>
    <link rel="stylesheet" href="/resources/css/styles.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>

<body class="popup">
    <div class="popup-content">
        <!-- 닫기 버튼 -->
        <button type="button" class="btn close-btn" onclick="window.close()">✖</button>
        <!-- 팝업 제목 -->
        <h2 class="popup-title">멍멍이 정보 수정 🐶</h2>

        <form id="petUpdateForm">
            <!-- 이름 -->
            <div class="form-group">
                <label for="name">이름</label>
                <input type="text" id="name" name="name" value="${petInfo.name}" placeholder="반려견 이름 (한글, 영문, 공백 1~50자)"
                    pattern="^[a-zA-Z가-힣\\s]+$" maxlength="50" required>
                <div id="nameError" class="error"></div>
            </div>

            <!-- 품종 -->
            <div class="form-group">
                <label for="breed">품종</label>
                <input type="text" id="breed" name="breed" value="${petInfo.breed}" placeholder="품종 (1~50자)"
                    maxlength="50" required>
                <div id="breedError" class="error"></div>
            </div>

            <!-- 생년월일 -->
            <div class="form-group">
                <label for="birthDate">생년월일</label>
                <input type="date" id="birthDate" name="birthDate" value="${petInfo.birthDate}"
                    max="<%= java.time.LocalDate.now() %>" required>
                <div id="birthDateError" class="error"></div>
            </div>

            <!-- 성별 -->
            <div class="form-group">
                <label for="gender">성별</label>
                <select id="gender" name="gender" required>
                    <option value="" disabled ${petInfo.gender==null ? "selected" : "" }>성별을 선택하세요</option>
                    <option value="MALE" ${petInfo.gender=='MALE' ? 'selected' : '' }>남자</option>
                    <option value="FEMALE" ${petInfo.gender=='FEMALE' ? 'selected' : '' }>여자</option>
                </select>
                <div id="genderError" class="error"></div>
            </div>

            <!-- 몸무게 -->
            <div class="form-group">
                <label for="weight">몸무게 (kg)</label>
                <input type="number" id="weight" name="weight" value="${petInfo.weight}"
                    placeholder="몸무게 (0.01~999.99kg)" step="0.01" min="0.01" max="999.99" required>
                <div id="weightError" class="error"></div>
            </div>

            <!-- 설명 -->
            <div class="form-group">
                <label for="description">설명</label>
                <textarea id="description" name="description" placeholder="반려견에 대한 설명 (최대 500자)" maxlength="500"
                    rows="4"><c:out value="${petInfo.description}" default=""/></textarea>
                <div id="descriptionError" class="error"></div>
            </div>

            <!-- 버튼 그룹 -->
            <div class="form-actions center">
                <button type="button" class="btn btn-primary full-width" onclick="submitPetUpdate('${petInfo.id}')">
                    수정하기
                </button>
                <button type="button" class="btn btn-danger full-width" onclick="confirmPetDelete('${petInfo.id}')">
                    삭제하기
                </button>
                <button type="button" class="btn btn-secondary full-width" onclick="window.close()">
                    취소
                </button>
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

        function submitPetUpdate(petId) {
            const data = {
                name: $('#name').val(),
                breed: $('#breed').val(),
                birthDate: $('#birthDate').val(),
                gender: $('#gender').val(),
                weight: parseFloat($('#weight').val()),
                description: $('#description').val()
            };

            // 에러 초기화
            $('#nameError, #breedError, #birthDateError, #genderError, #weightError, #descriptionError')
                .text('');

            fetch('/api/v1/pet/' + petId, {
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
                        alert('반려견 정보가 성공적으로 수정되었습니다!');
                        window.opener?.location.reload();
                        window.close();
                    } else if (result.data) {
                        Object.entries(result.data).forEach(([field, msg]) => {
                            $('#' + field + 'Error').text(msg);
                        });
                    } else {
                        alert('수정에 실패했습니다: ' + result.message);
                    }
                })
                .catch(err => {
                    console.error(err);
                    alert('수정 중 오류가 발생했습니다.');
                });
        }

        function confirmPetDelete(petId) {
            if (confirm('정말로 반려견 정보를 삭제하시겠습니까? 모든 관련 데이터가 삭제됩니다.')) {
                fetch('/api/v1/pet/' + petId, {
                    method: 'DELETE',
                    headers: { 'X-Requested-With': 'XMLHttpRequest' },
                    credentials: 'include'
                })
                    .then(res => res.json())
                    .then(result => {
                        if (result.success) {
                            alert('정상적으로 삭제되었습니다!');
                            window.opener?.location.reload();
                            window.close();
                        } else {
                            alert('삭제에 실패했습니다: ' + result.message);
                        }
                    })
                    .catch(err => {
                        console.error(err);
                        alert('삭제 중 오류가 발생했습니다.');
                    });
            }
        }
    </script>
</body>

</html>