$(function() {

    function commonAjax(url, method, data, onSuccess, onError) {
        $.ajax({
            url: url,
            method: method,
            data: data,
            success: function(resp) {
                onSuccess(resp);
            },
            error: function(xhr) {
                onError(xhr);
            }
        });
    }

    $('#id-dup-btn').on('click', function(e) {
        e.preventDefault();
        e.stopPropagation();

        if(validateUserName($('#userName').val())) {
            commonAjax("/api/account/check/" + $('#userName').val(), "GET", null,
                function(resp) {
                    if(!resp) {
                        alert('사용 가능한 아이디 입니다.');
                        $('#userName')
                            .attr('data-id-check', true)
                            .prop('disabled', true)
                            .css('border', '1px solid var(--accent-color)');
                    } else {
                        alert('중복된 아이디 입니다.');
                        $('#userName').focus();
                    }
                },
                function(err) {
                    console.log(err);
                }
            )
        } else {
            alert('아이디 형식이 올바르지 않습니다. 영문+숫자만을 조합해 주세요.');
            $('#userName').focus();
        }
    });

    function validateUserName(userName) {
        const regex = /^[a-zA-Z0-9]+$/;  // 영문, 숫자만
        return regex.test(userName);  // 정규식 검사 (true/false 반환)
    }



});