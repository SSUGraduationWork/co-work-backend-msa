package com.example.userservice.service;

import com.example.userservice.dto.GoogleOAuthToken;
import com.example.userservice.dto.GoogleUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GoogleOauth implements SocialOauth {
    private final Environment env;
    @Value("${google.uri}")
    private String GOOGLE_LOGIN_URL;

    @Value("${google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${google.redirect-uri}")
    private String GOOGLE_CALLBACK_URI;
    @Value("${google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${google.scope}")
    private String GOOGLE_DATA_ACCESS_SCOPE;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_CALLBACK_URI);
        params.put("response_type", "code");
        params.put("scope", GOOGLE_DATA_ACCESS_SCOPE);

        String parameterString = params.entrySet().stream()
                .map(x->x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        String redirectUri = GOOGLE_LOGIN_URL + "?" + parameterString;

        return redirectUri;

        /**
         * https://accounts.google.com/o/oauth2/auth?client_id=GOOGLE_CLIENT_ID
         * &redirect_uri=GOOGLE_CALLBACK_URI&response_type=code
         * &scope=GOOGLE_DATA_ACCESS_SCOPE
         * 로 Redirect URL을 생성하는 로직 구성
         */
    }
    public GoogleOAuthToken getAccessToken(String code) throws JsonProcessingException {
        String GOOGLE_TOKEN_REQUEST_URL = env.getProperty("google.token-uri");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", GOOGLE_CLIENT_ID);
        params.add("client_secret", GOOGLE_CLIENT_SECRET);
        params.add("redirect_uri", GOOGLE_CALLBACK_URI);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL, entity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK){
            //응답 객체가 JSON형식으로 되어 있으므로, 이를 역직렬화(deserialization)해서 자바 객체에 담을 것이다.
            GoogleOAuthToken googleOAuthToken = objectMapper.readValue(responseEntity.getBody(), GoogleOAuthToken.class);
            return googleOAuthToken;
        }
        return null;
    }

    public ResponseEntity<String> requestUserInfo(GoogleOAuthToken oAuthToken){
        String GOOGLE_USERINFO_REQUEST_URL = env.getProperty("google.resource-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer" + oAuthToken.getAccess_token());
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, entity, String.class);
    }

    public GoogleUser getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException{
        GoogleUser googleUser = objectMapper.readValue(userInfoRes.getBody(), GoogleUser.class);
        return googleUser;
    }

}
