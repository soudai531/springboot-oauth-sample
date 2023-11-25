package com.example.springbootoauthsample;


import org.springframework.ui.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class OauthController {
    private final RestOperations restOperations = new RestTemplate();

    private final OAuth2AuthorizedClientService authorizedClientService;

    // 認可済みのクライアント情報(クライアント情報、認可したリソースオーナ名、アクセストークンなど)は
    // OAuth2AuthorizedClientService経由で取得できる
    public OauthController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/")
    public String index(OAuth2AuthenticationToken authentication, Model model) {
        // 画面に表示するために、OAuth2AuthorizedClientService経由で認可済みのクライアント情報を取得しModelに格納
        model.addAttribute("authorizedClient", this.getAuthorizedClient(authentication));
        System.out.println("-----------USERNAME---------\n" + authentication.getPrincipal().getAttribute("login"));

        // OAuth認証状況がある場合

        
        // Form認証状況がある場合



        return "index";
    }

    @GetMapping("/login/oauth2")
    public String login() {
        return "login";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("hello2")
    public String hello2() {
        return "hello2";
    }

    @GetMapping("/attributes")
    public String userAttributeAtLogin(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        // 認証が制した時点のユーザ属性であれば、プロバイダに問い合わせなくても認証情報から取得できる。
        // 注：あくまで認証時点での情報なので、情報は古くなっている可能性がある
        model.addAttribute("attributes", oauth2User.getAttributes());
        return "userinfo";
    }

    @GetMapping("/attributes/latest")
    public String userLatestAttribute(OAuth2AuthenticationToken authentication, Model model) {
        // 最新のユーザ属性が必要な場合は、認証時に取得したアクセストークンを付与してプロバイダから再取得する
        // ここでは、Spring Framework提供のRestTemplateを使用してユーザ属性の取得を行っている。
        // ちなみに・・・Spring Securityのデフォルト実装では、「Nimbus OAuth 2.0 SDK」を使用してアクセストークン及びユーザ属性の取得を行っている。
        OAuth2AuthorizedClient authorizedClient = this.getAuthorizedClient(authentication);
        String userInfoUri = authorizedClient.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
        RequestEntity<Void> requestEntity = RequestEntity.get(URI.create(userInfoUri))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authorizedClient.getAccessToken().getTokenValue())
                .build();
        model.addAttribute("attributes", restOperations.exchange(requestEntity, Map.class).getBody());
        return "userinfo";
    }

    @GetMapping("/repositories")
    public String getRepositories(OAuth2AuthenticationToken authentication, Model model) {
        // 最新のユーザ属性が必要な場合は、認証時に取得したアクセストークンを付与してプロバイダから再取得する
        // ここでは、Spring Framework提供のRestTemplateを使用してユーザ属性の取得を行っている。
        // ちなみに・・・Spring Securityのデフォルト実装では、「Nimbus OAuth 2.0 SDK」を使用してアクセストークン及びユーザ属性の取得を行っている。
        OAuth2AuthorizedClient authorizedClient = this.getAuthorizedClient(authentication);
        String repositoriesUri = "https://api.github.com/users/" + authentication.getPrincipal().getAttribute("login") + "/repos";
        RequestEntity<Void> requestEntity = RequestEntity.get(URI.create(repositoriesUri))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authorizedClient.getAccessToken().getTokenValue())
                .build();
        System.out.println("-----------AccessToken---------\n" +  authorizedClient.getAccessToken().getTokenValue());
        var response = restOperations.exchange(requestEntity, List.class).getBody();
        return response.toString();
    }

    private OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken authentication) {
        return this.authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(), authentication.getName());
    }
}
