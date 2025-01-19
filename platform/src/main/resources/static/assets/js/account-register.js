$(function() {

    let latitude;
    let longitude;
    let accuracy; // 정확도

    if ("geolocation" in navigator) {
        /* 위치정보 사용 가능 */
        console.log("위치 on");
//        const watchID = navigator.geolocation.watchPosition( // 꾸준히 정확도 높음
        const watchID = navigator.geolocation.getCurrentPosition( // 1번이지만 빠르게 정확도 낮음
            (position) => {
                latitude = position.coords.latitude;
                longitude = position.coords.longitude;
                accuracy = position.coords.accuracy; // 정확도 확인
                console.log("위도: " + position.coords.latitude);
                console.log("경도: " + position.coords.longitude);
                console.log("정확도(m): " + position.coords.accuracy);

                commonAjax("/api/account/user-location", "POST", { latitude: latitude, longitude: longitude },
                    function(resp) {
                        $('#current-location').text(resp);
                        $('#address').val(resp);
                    },
                    function(err) {
                        console.log(err);
                        console.log("실패");
                    }
                );
            },
            (error) => {
                $('#current-location').text("위치 정보를 가져오는데 실패했습니다.");
            },
            {
                enableHighAccuracy: true,   // 더 높은 정확도 요청
                timeout: 5000,              // 위치를 받아오는 최대 시간 (5초)
                maximumAge: 0               // 캐시된 위치 사용하지 않음
            }
        );
    } else {
        /* 위치정보 사용 불가능 */
        console.log("위치 off");
    }

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

        const userName = $('#userName').val().trim();

        if(userName == '') {
            alert('아이디를 입력해 주세요.');
            $('#userName').focus();
            return;
        }

        if(validateUserName(userName)) {
            commonAjax("/api/account/check/" + userName, "GET", null,
                function(resp) {
                    if(!resp) {
                        alert('사용 가능한 아이디 입니다.');
                        $('#userName')
                            .attr('data-id-check', true)
                            .prop('disabled', true)
                            .css('border', '1px solid var(--accent-color)');
                        $('#id-dup-btn').prop('disabled', true);
                        $('#password').focus();
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

    const passwordWarning = '비밀번호는 영문자와 숫자 특수문자(@$!%*?&) 중 3가지 이상으로\n조합하여 8자~20자 이내로 입력해 주세요';
    const passwordNotFits = '두 비밀번호가 일치하지 않습니다. 다시 입력해 주세요.';

    // 비밀번호 검증
    $('#password').on('propertychange change paste input', function() {
        if(!validatePassword($(this))) {
            $('#password-message').text(passwordWarning);
        } else {
            $('#password-message').text('');
            $('#password').css('border', '1px solid var(--accent-color)');

        }

        if($('#password2').val() == '' && $('#password').val().trim() !== $('#password2').val().trim()) {
            $('#password-message').text(passwordNotFits);
        }
    });

    // 비밀번호2 검증
    $('#password2').on('propertychange change paste input', function() {

        if ($('#password').val().trim() != $('#password2').val().trim()) {
            $('#password-message').text(passwordNotFits);
        } else {
            $('#password-message').text('');
            $('#password2').css('border', '1px solid var(--accent-color)');
        }

    });

    // 휴대폰 번호 인증
    $('#send-sns-btn').on('click', function(e) {
        e.preventDefault();
        const phone = $('#phone').val();

        if(phone.length < 11) {
            alert('휴대폰 번호 11자리를 입력해 주세요.');
            $('#phone').focus();
            return;
        }

        commonAjax("/api/account/check/phone", "POST", {phone},
            function(resp) {
                alert('인증번호 전송이 완료되었습니다. 확인 후 입력해 주세요.');
                $('#sms-auth-container').show();
                $('#smsCode').focus();
            },
            function(err) {
                alert('인증번호 전송 중 오류가 발생했습니다. 휴대폰 번호를 다시 확인해 주세요');
            }
        );
    });

    // 인증번호 검증
    $('#sms-check-btn').on('click', function(e) {
        e.preventDefault();

        const code = $('#smsCode').val().trim();
        if(code.length < 4) {
            alert('인증번호 4자리를 입력해 주세요.');
            $('#smsCode').focus();
            return;
        }

        commonAjax("/api/account/check/code", "POST", {code},
            function(resp) {
                alert('휴대폰 인증이 완료되었습니다.');

                $('#sms-auth-container').hide();
                $('#phone')
                    .attr('data-phone-check', true)
                    .prop('disabled', true)
                    .css('border', '1px solid var(--accent-color)');
                $('#send-sns-btn').prop('disabled', true);
                $('#nickName').focus();
            },
            function(err) {
                alert('인증번호가 올바르지 않습니다. 다시 입력해 주세요.');
                $('#smsCode').focus();
            }
        );
    });

    // 닉네임 검증
    $('#nickName').on('propertychange change paste input', function(e) {

        const nickName = $('#nickName').val().trim();
        console.log($(this));

        if(!validateNickName(nickName)) {
            $('#nickName-message').text('한/영, 숫자로 3~10자 이내로 입력해 주세요.');
            $('#nickName').css('border', '');
        } else {
            $('#nickName-message').text('');
            $('#nickName').css('border', '1px solid var(--accent-color)');
        }
    });

    // 이용약관 검증
    $('#privacy-policy').on('change', function(e) {
        const isChecked = $(this).is(':checked');
        $('#privacy-policy').val(isChecked);
    });

    $('#submit-btn').on('click', function(e) {
        e.preventDefault();

        const password = $('#password').val().trim();
        const password2 = $('#password2').val().trim();

        if($('#userName').data('id-check') == false) {
            alert('아이디 중복 체크를 진행해 주세요.');
            targetScroll($('#userName'));
            return;

        }

        if(password === '' || password2 === '') {
            alert(passwordWarning);
            targetScroll($('#password'));
            return;
        }

        if(password !== password2) {
            alert(passwordNotFits);
            targetScroll($('#password'));
            return;
        }

        if(!validatePassword(password) || !validatePassword(password2)) {
            alert(passwordWarning);
            targetScroll($('#password'));
            return;
        }

        if($('#phone').val() === '' || $('#phone').data('id-check') == false) {
            alert('휴대폰 인증을 진행해 주세요.');
            targetScroll($('#phone'));
            return;
        }

        if($('#nickName').val().trim() == '') {
            alert('닉네임을 입력해 주세요.');
            targetScroll($('#nickName'));
            return;
        }

        if($('#privacy-policy').val() == false) {
            alert('개인정보 수집 및 이용동의 약관을 확인해 주세요.');
            return;
        }

        // Submit
        const data = {
            userName: $('#userName').val(),
            password: password,
            nickName: $('#nickName').val(),
            phone: $('#phone').val(),
            address: $('#address').val(),
            latitude: latitude,
            longitude: longitude
        }

        commonAjax("/api/user", "POST", data, function(resp){
            alert('회원가입이 완료되었습니다!');
            location.href = '/account/login';
        },
        function(err) {
            console.log(err.status);
        });
    });

    function validateUserName(userName) {
        const regex = /^[a-zA-Z0-9]+$/;  // 영문, 숫자만
        return regex.test(userName);
    }

    function validatePassword(password) {
        const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/; // 영문, 숫자, 특수문자
        return regex.test(password);
    }

    function validateNickName(nickName) {
        const regex = /^[a-zA-Z0-9가-힣]{3,10}$/;
        return regex.test(nickName);
    }

    // 유효성 검사 실패시 해당 위치로 scroll
    function targetScroll(id) {
        const result = id.attr('id');
        const element = document.querySelector('#' + result);
        const elementRect = element.getBoundingClientRect();
        const elementMidpoint = elementRect.top + window.scrollY + elementRect.height / 2;
        // 해당 위치로 스크롤
        window.scrollTo({
            top: elementMidpoint - (window.innerHeight / 2),
            behavior: 'smooth'
        });
    }



});