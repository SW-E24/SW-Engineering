/*
* 회원가입 시 사용자가 입력한 값의 중복 확인을 위한 로직
* 클라이언트 -> 서버 로 전송해서 확인하기 위한 과정
* */
$(document).ready(function () {

    // 아이디 중복 체크
    $('#checkIdBtn').on('click', function () {
        const id = $('#userID').val();
        if (id) {   // 필드에 입력된 상태에만 중복 체크
            $.get('/api/members/check-duplicate-id', {userID: id}, function (response) {
                if (response) {
                    $('#idCheckMessage').text('사용 가능한 아이디입니다.').css('color', 'green');
                } else {
                    $('#idCheckMessage').text('이미 사용 중인 아이디입니다.').css('color', 'red');
                }
            });
        } else {    //빈 칸인 상태로 누를 경우 상단 팝업창 띄우기
            alert('아이디를 입력해 주세요.');
        }
    });

    // 이메일 중복 체크
    $('#checkEmailBtn').on('click', function () {
        const email = $('#userEmail').val();
        if (email) {
            $.get('api/members/check-duplicate-email', {userEmail: email}, function (response) {
                if (response) {
                    $('#emailCheckMessage').text('이미 사용 중인 이메일입니다.').css('color', 'red');
                } else {
                    $('#emailCheckMessage').text('사용 가능한 이메일입니다.').css('color', 'green');
                }
            });
        } else {
            alert("이메일을 입력해 주세요.");
        }
    });

    // 전화번호 중복 체크
    $('#checkPhoneBtn').on('click', function () {
        const userPhone = $('#userPhone').val();
        if (userPhone) {  // 전화번호가 입력된 경우에만 중복 체크
            $.get('/api/members/check-duplicate-phone', {userPhone: userPhone}, function (response) {
                if (response) {
                    $('#phoneCheckMessage').text('이미 사용 중인 전화번호입니다.').css('color', 'red');
                } else {
                    $('#phoneCheckMessage').text('사용 가능한 전화번호입니다.').css('color', 'green');
                }
            });
        } else {  // 전화번호 입력이 없는 경우
            alert('전화번호를 입력해 주세요.');
        }
    });

    // 비밀번호 입력 체크
    $('#confirmuserPW').on('input', function () {
        const password = $('#userPW').val();
        const confirmPW = $(this).val();

        if (password !== confirmPW) {
            $('#pwErrorMessage').text('비밀번호가 일치하지 않습니다.');
        } else {
            $('#pwErrorMessage').text('');  //일치하면 메세지 안 띄우게 공백
        }
    });

    //회원가입 폼 제출 처리
    $('#registerForm').on('submit', function (event) {
        event.preventDefault();
        const userID = $('#id').val();
        const userEmail = $('#email').val();
        const userPhone = $('#phone').val();
        const userPW = $('#password').val();
        const confirmPW = $('#confirmuserPW').val();

        // 데이터 전송
        $.post('api/members/register', {userID, userEmail, userPhone, userPW}, function (response) {
            if (response.success) {
                alert('회원가입 성공!');
                window.location.href = '/login';
            } else {
                alert('회원가입 실패: ' + response.message);
            }
        });
    });

});