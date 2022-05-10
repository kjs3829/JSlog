package jslog.member.auth.infrastructure.kakao;

import jslog.member.auth.application.Client;
import jslog.member.auth.application.MemberDetails;
import jslog.member.auth.infrastructure.ClientResponseConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
public class KakaoClient implements Client {

    private static final String CONTENT_TYPE = "Content-type";
    private static final String AUTHORIZATION = "Authorization";
    private static final String DEFAULT_CHARSET = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String BEARER_FORM = "Bearer %s";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String ID = "id";

    private final RestTemplate restTemplate;
    private final ClientResponseConverter clientResponseConverter;
    private final KakaoOauthInfo kakaoOauthInfo;

    @Override
    public MemberDetails getDetails(String socialCode) {
        final String accessToken = getAccessToken(socialCode);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, String.format(BEARER_FORM, accessToken));
        try {
            final String body = restTemplate.exchange(
                    kakaoOauthInfo.getKakaoUserUrl(),
                    HttpMethod.GET,
                    new HttpEntity<>(httpHeaders),
                    String.class
            ).getBody();
            final String id = clientResponseConverter.extractDataAsString(body, ID);
            return new KakaoMemberDetails(id);
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void unLink(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, String.format(BEARER_FORM, accessToken));
        restTemplate.exchange(kakaoOauthInfo.getKakaoUnlinkUrl(), HttpMethod.POST,
                new HttpEntity<>(headers), Void.class);
    }


    private String getAccessToken(String socialCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", GRANT_TYPE);
        params.add("client_id", kakaoOauthInfo.getClientId());
        params.add("redirect_uri",kakaoOauthInfo.getKakaoRedirectUrl());
        params.add("code", socialCode);
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, DEFAULT_CHARSET);

        //실제로 요청하기
        //Http 요청하기 - POST 방식으로 - 그리고 response 변수의 응답을 받음.
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    kakaoOauthInfo.getKakaoTokenUrl(),
                    HttpMethod.POST,
                    new HttpEntity<>(params, headers),
                    String.class
            );
            return clientResponseConverter.extractDataAsString(response.getBody(), ACCESS_TOKEN);
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
