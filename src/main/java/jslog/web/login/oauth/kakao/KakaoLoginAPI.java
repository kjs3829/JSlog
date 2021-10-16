package jslog.web.login.oauth.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoLoginAPI {
    private final String REST_API_KEY="9b73f9c1ef9ccf7d60ad9de541112929";
    private final String REDIRECT_URI ="http://localhost:8080/auth/kakao/callback";

    public KakaoAuthToken getAuthToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id", REST_API_KEY);
        params.add("redirect_uri",REDIRECT_URI);
        params.add("code", code);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        //Post방식으로 Key-value 데이터를 요청(카카오쪽으로)
        RestTemplate rt = new RestTemplate();   //http 요청을 간단하게 해줄 수 있는 클래스

        //실제로 요청하기
        //Http 요청하기 - POST 방식으로 - 그리고 response 변수의 응답을 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoAuthToken kakaoAuthToken = null;
        //Model과 다르게 되있으면 그리고 getter setter가 없으면 오류가 날 것이다.
        try {
            kakaoAuthToken = objectMapper.readValue(response.getBody(), KakaoAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return kakaoAuthToken;
    }

    public KakaoProfile getProfile(KakaoAuthToken kakaoAuthToken) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+ kakaoAuthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
            System.out.println(kakaoProfile.getProperties().toString());
            System.out.println(response.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return kakaoProfile;
    }

    public void updateProperty(KakaoAuthToken kakaoAuthToken, KakaoProfile.Properties properties) throws JsonProcessingException {
        RestTemplate rt = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+ kakaoAuthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("properties", objectMapper.writeValueAsString(properties));

        HttpEntity<MultiValueMap<String,String>> kakaoProfileUpdateRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/update_profile",
                HttpMethod.POST,
                kakaoProfileUpdateRequest,
                String.class
        );

        /*
        String updateKakaoId = null;
        try {
            System.out.println(response.getBody());
            updateKakaoId = objectMapper.readValue(response.getBody(), String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return updateKakaoId;

         */
    }

    public void unLink(KakaoAuthToken kakaoAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+ kakaoAuthToken.getAccess_token());

        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest = new HttpEntity<>(headers);

        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/unlink",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        /*
        ObjectMapper objectMapper = new ObjectMapper();
        String unLinkedId = null;
        try {
            System.out.println("response = " + response.getBody());
            System.out.println(response.getBody().getClass());
            unLinkedId = objectMapper.readValue(response.getBody(), String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(unLinkedId + "연결 끊김");
         */
    }
}
