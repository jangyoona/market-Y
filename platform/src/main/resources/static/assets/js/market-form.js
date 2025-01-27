$(function() {

    // 공용 Ajax
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

    let latitude;
    let longitude;
    let accuracy; // 정확도

    if ("geolocation" in navigator) {
        /* 위치정보 사용 가능 */
        console.log("위치 on");
        const watchID = navigator.geolocation.watchPosition( // 꾸준히 정확도 높음
//        const watchID = navigator.geolocation.getCurrentPosition( // 1번이지만 빠르게 정확도 낮음
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
                        $('#latitude').val(latitude);
                        $('#longitude').val(longitude);
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

    $('#attach').on('change', function(event) {
        const selectedFile = event.target.files[0];
        if (selectedFile) {
          const fileReader = new FileReader();
          fileReader.onload = function(e) {
            $('#selected-image').attr('src', e.target.result);
            // $('#selected-image')
          };
          fileReader.readAsDataURL(selectedFile);
        }
    });

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
    $('#submit').on('click', function(e) {
        e.preventDefault();
        e.stopPropagation();

        const title = $('#title').val().trim();
        const price = $('#price').val().trim();
        const content = $('#content').val().trim();

        if($('#attach')[0].files.length === 0) {
            alert('상품 이미지를 입력해 주세요.');
            $('#attach').focus();
            return;
        }

        if(title.length == 0 || title.length < 2) {
            alert('제목은 2글자 이상으로 입력해 주세요.');
            $('#title').focus();
            return;
        }

        if(price.length == 0) {
            alert('가격을 입력해 주세요.');
            $('#price').focus();
            return;
        }

        if(content.length == 0) {
            alert('상품 설명을 입력해 주세요.');
            $('#content').focus();
            return;
        }

//        $('#form').submit();

        const form = $('#form');
        const formData = new FormData(form[0]);

        $.ajax({
            url : form.attr('action'),
            method : form.attr('method'),
            data : formData,
            processData: false,
            contentType: false,
            dataType : "text",
            success: (resp) => {
                alert('상품이 등록되었습니다.');
                location.href = '/';
            },
            error: (xhr) => {
                console.log(xhr.status);
                alert('상품 등록에 실패했습니다.');
            }
        });

    });

});


