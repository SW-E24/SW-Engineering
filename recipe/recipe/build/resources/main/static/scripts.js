
    $(document).ready(function() {
    $('#checkIdBtn').on('click', function() {
        const userID = $('#userID').val();
        if (userID) {   //필드에 뭐라도 적은 상태면 아래 로직으로 중복 체크
            $.get('/check-duplicate-id', {userID: userID}, function(response) {
                if (response) {
                    $('#idCheckMessage').text('사용 가능한 아이디입니다.');
                } else {
                    $('#idCheckMessage').text('이미 사용 중인 아이디입니다.').css('color','red');
                }
            });
        } else {    //빈 칸인 상태로 누를 경우 팝업창 띄우기
            alert('아이디를 입력해 주세요.');
        }
    });

    $('#confirmuserPW').on('input', function() {
    const userPW = $('#userPW').val();
    const confirmPW = $(this).val();

    if (userPW !== confirmPW) {
    $('#pwErrorMessage').text('비밀번호가 일치하지 않습니다.');
} else {
    $('#pwErrorMessage').text('');  //일치하면 메세지 안 띄우게 공백
}
});
});