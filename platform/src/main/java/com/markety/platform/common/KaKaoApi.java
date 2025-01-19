package com.markety.platform.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class KaKaoApi {

    // map
    @Value("${kakao.rest-key}")
    private String kakaoMapRestKey;


//    public Map<String, Object> getKakaoSearch(String addr) {
    public Map<String, Object> getKakaoSearch(double lat, double lng ) {

        // 주소 변환해야함 (~동) 빼는 작업
//        String address = addr.replaceAll("\\s*\\(.*?\\)", "");

        final String restAPIKey = "KakaoAK " + kakaoMapRestKey;

        //요청 URL과 검색어를 담음
//        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + address;
        String url = String.format("https://dapi.kakao.com/v2/local/search/keyword.json?query=restaurant&x=%f&y=%f", lng, lat);

        //RestTemplate를 이용해
        RestTemplate restTemplate = new RestTemplate();

        //HTTPHeader를 설정해줘야 하기때문에 생성함
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", restAPIKey);
        headers.set("Accept", "application/json");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        //ResTemplate를 이용해 요청을 보내고 KakaoSearchDto로 받아 response에 담음

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        // 응답 본문을 문자열로 가져옴
        String jsonResponse = response.getBody();

        // JSON 문자열을 JsonObject로 파싱
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        // documents 배열을 가져옴
        JsonArray documents = jsonObject.getAsJsonArray("documents");

        // 0번째 인덱스의 객체를 가져옴
        Map<String, Object> xy = new HashMap<>();
        String address = null;

        if (documents != null && documents.size() > 0) {
            JsonObject firstDocument = documents.get(0).getAsJsonObject();
            System.out.println(firstDocument.toString());

            // 경도(x), 위도(y) 추출
            xy.put("x", firstDocument.get("x").getAsString());
            xy.put("y", firstDocument.get("y").getAsString());
            xy.put("address_origin", firstDocument.get("address_name").getAsString());
            xy.put("address", addressSplit(firstDocument.get("address_name").getAsString()));
        } else {
            // x, y 없는 주소 만이빌딩으로 대체
            xy.put("x", "127.02675502058682");
            xy.put("y", "37.50077973287715");
        }
        return xy;

    }

    public String addressSplit(String addr) {
        // 화면 출력용 주소 자르기
        String[] addrCut = addr.split("\\s+");
//        String addrSplit = String.format("%s %s", addrCut[0], addrCut[1]);
        if (!addrCut[0].contains("서울")) {
            return String.format("%s %s", addrCut[1], addrCut[2]);
        }
        return String.format("%s %s", addrCut[0], addrCut[1]);
    }


}
