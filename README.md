# springboot-oauth-sample
SpringBootでのGitHubのOAuth2のクライアント実装サンプル

OAuthで取得したアクセストークンなどがHttpSessionやSecurityContextHolderに格納される。

主な依存関係
```
implementation 'org.springframework.boot:spring-boot-starter-oauth2-client' // OAuth2 Client機能
implementation 'org.springframework.boot:spring-boot-starter-security' // Spring Security
implementation 'org.springframework.boot:spring-boot-starter-web' // Spring Web
```

有名どころ(Google、GitHub、Facebookなど)ならclientidとclient-secretを設定するだけど良しなにやってくれるらしい<br>
https://spring.pleiades.io/spring-security/reference/servlet/oauth2/login/core.html#oauth2login-common-oauth2-provider
<br>
<br>

application.propertiesに以下を追加
```
spring.security.oauth2.client.registration.github.client-id=
spring.security.oauth2.client.registration.github.client-secret=
```
<br>
<br>
Form認証(UsernamePasswordAuthenticationFileter)との併用もできるみたい。 

ただし、SecurityContextHolderに格納されるのはそれぞれ、AuthenticationのサブクラスのOAuth2AuthenticationToken、UsernamePasswordAuthenticationTokenになるので扱いには注意。

# 併用時のプチ検証
複数の認証方法がある場合に、認証状態を保持したまま別の種類の認証を行うとどういう動作をするのか
例：Form認証→ログアウトせずにOAuthでログインするなど

検証結果 

とりあえず、最新の認証状態が引っ張られる模様。
最新ではない認証情報がそのまま保持されているかは不明。
