# springboot-oauth-sample
SpringBootでのGitHubのOAuth2のクライアント実装サンプル

主な依存関係
```
implementation 'org.springframework.boot:spring-boot-starter-oauth2-client' // OAuth2 Client機能
implementation 'org.springframework.boot:spring-boot-starter-security' // Spring Security
implementation 'org.springframework.boot:spring-boot-starter-web' // Spring Web
implementation 'org.springframework.security:spring-security-oauth2-jose'// OpenID Connect JWT用
```

有名どころ(Google、GitHub、Facebookなど)ならclientidとclient-secretを設定するだけど良しなにやってくれるらしい
https://spring.pleiades.io/spring-security/reference/servlet/oauth2/login/core.html#oauth2login-common-oauth2-provider
<br>
<br>

application.propertiesに以下を追加
```
spring.security.oauth2.client.registration.github.client-id=
spring.security.oauth2.client.registration.github.client-secret=
```

Form認証(UsernamePasswordAuthenticationFileter)との併用もできるみたい。
ただし、SecurityContextHolderに格納されるのはAuthenticationのサブクラスのOAuth2AuthenticationTokenクラスのため、それぞれの扱いに気を付ける。

# 検証
複数の認証手段がある場合に、どういう動きをするのか。
例：Form認証→ログアウトせずにOAuthでログインするなど

検証結果
複数のSecurityContextが保持されるわけではなく、最新の認証状態に上書きされるらしい。
