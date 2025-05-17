// 반려견 목록 조회
async function fetchPetIds(selectElement) {
    try {
        const response = await fetch('/api/v1/pet', {
            method: 'GET',
            headers: { 'X-Requested-With': 'XMLHttpRequest' },
            credentials: 'include'
        });
        const result = await response.json();
        console.log('result :', result);

        if (result.success) {
            selectElement.empty().append('<option value="">반려견을 선택하세요</option>');
            result.data.forEach(petInfo => {
                selectElement.append(`<option value="${petInfo.id}">${petInfo.name}</option>`);
            });
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('petIds 조회 오류:', error);
        alert('반려견 목록을 불러오지 못했습니다.');
    }
}