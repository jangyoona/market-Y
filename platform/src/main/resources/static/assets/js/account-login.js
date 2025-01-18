$(function() {

    $('#saveId').on('change', function(e) {
        const isChecked = $(this).is(':checked');
        $('#saveId').val(isChecked);
    });

    $('#remember-me').on('change', function(e) {
        const isChecked = $(this).is(':checked');
        $('#remember-me').val(isChecked);
    });


    // submit
    $('#submit-btn').on('click', (e) => {

        e.preventDefault();
        e.stopPropagation();

        const userName = $('#userName').val().trim();
        const password = $('#password').val().trim();

        if(!validateUserName(userName)) {
            alert('아이디 형식을 확인해 주세요.');
            return;
        }

        if(userName == '') {
            alert('아이디를 입력해 주세요.');
            $('#userName').focus();
            return;
        } else if(password == ''){
            alert('아이디를 입력해 주세요.');
            $('#password').focus();
            return;
        }

        $.ajax({
            url: "/api/account/login",
            method: "POST",
            data: { userName, password, saveId : $('#saveId').val() },
            success: function(resp) {
                location.href = "/";
            },
            error: function(xhr) {
                alert("아이디, 비밀번호를 확인해 주세요.");
            }
        });

//        $('form').submit();
    });

    function validateUserName(userName) {
        const regex = /^[a-zA-Z0-9]+$/;  // 영문, 숫자만
        return regex.test(userName);
    }


});