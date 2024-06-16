package com.ghostchu.btn.btnserver.oauth;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghostchu.btn.btnserver.user.UserEntity;
import com.ghostchu.btn.btnserver.user.UserService;
import com.ghostchu.btn.btnserver.util.ServletUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.UnirestInstance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth/oauth2/github")
@Slf4j
public class OAuthController {
    private static final Cache<String, String> IP_STATE_MAPPING = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
    @Value("${oauth2.github.clientId}")
    private String clientId;
    @Value("${oauth2.github.clientSecret}")
    private String clientSecret;
    @Value("${oauth2.github.scope}")
    private String scope;
    @Autowired
    private UnirestInstance unirest;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Value("${server.root-url}")
    private String serverRootUrl;
    @Autowired
    private HttpServletRequest req;
    @Autowired
    private HttpServletResponse resp;

    @GetMapping("/login")
    public void loginToGithub() throws IOException {
        String state = UUID.randomUUID().toString();
        String jumpBack = UriComponentsBuilder.fromHttpUrl(serverRootUrl)
                .pathSegment("auth", "oauth2", "github", "callback")
                .toUriString();
        String userUri = UriComponentsBuilder.fromHttpUrl("https://github.com/login/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", jumpBack)
                .queryParam("scope", scope)
                .queryParam("state", state).toUriString();
        IP_STATE_MAPPING.put(ServletUtil.getIP(req), state);
        resp.sendRedirect(userUri);
    }

    @GetMapping("/callback")
    public void callback() throws IOException {
        String ip = ServletUtil.getIP(req);
//        String state = IP_STATE_MAPPING.getIfPresent(ip);
//        if (state == null) {
//            throw new IllegalStateException("Seems you didn't login to this BTN instance via login entrypoint but received login callback. Cross-site attack? Try again!");
//        }
        IP_STATE_MAPPING.invalidate(ip);
        String code = req.getParameter("code");
        if (code == null) {
            throw new IllegalStateException("The login callback didn't contains code field.");
        }
        HttpResponse<String> re = unirest.post("https://github.com/login/oauth/access_token")
                .field("client_id", clientId)
                .field("client_secret", clientSecret)
                .field("code", code)
                .accept("application/json")
                .asString();
        if (!re.isSuccess()) {
            throw new IllegalStateException("The login callback returns incorrect response: " + re.getStatus() + " - " + re.getStatusText() + " : " + re.getBody());
        }
        GithubAccessTokenCallback callback = objectMapper.readValue(re.getBody(), GithubAccessTokenCallback.class);
        HttpResponse<String> authResp = unirest.get("https://api.github.com/user")
                .header("Authorization", "Bearer " + callback.getAccessToken())
                .asString();
        if (!authResp.isSuccess()) {
            throw new IllegalStateException("An error occurred when requesting user Github profile via access token: " + re.getStatus() + " - " + re.getStatusText() + " : " + re.getBody());
        }
        GithubUserProfile userProfile = objectMapper.readValue(authResp.getBody(), GithubUserProfile.class);
        HttpResponse<String> emailResp = unirest.get("https://api.github.com/user/emails")
                .header("Authorization", "Bearer " + callback.getAccessToken())
                .asString();
        List<GithubUserEmail> userEmailList = objectMapper.readValue(emailResp.getBody(), new TypeReference<>() {
        });
        String emailSelected = userEmailList.stream().filter(GithubUserEmail::getPrimary).findFirst().orElse(new GithubUserEmail(userProfile.getLogin() + "@github-users.com", true, true)).getEmail();
        userLogin(userProfile, emailSelected);
        resp.sendRedirect("/");
    }

    private void userLogin(GithubUserProfile userProfile, String emailSelected) {
        UserEntity entity = userService.getUserByGithubIdentifier(userProfile.getLogin());
        if (entity == null) {
            entity = new UserEntity();
            entity.setGithubIdentifier(userProfile.getLogin());
            entity.setRegisterAt(new Timestamp(System.currentTimeMillis()));
        }
        entity.setAvatar(userProfile.getAvatarUrl());
        entity.setNickname(userProfile.getName() == null ? userProfile.getLogin() : userProfile.getName());
        entity.setEmail(emailSelected);
        entity = userService.save(entity);
        if (entity.getId() <= 0) {
            throw new IllegalStateException("User register failed, please contact BTN instance administrator.");
        }
        StpUtil.login(entity.getId());
        log.info("IPAddress {} now logged in as user {} (Login={}) (UID={})", ServletUtil.getIP(req), entity.getNickname(), userProfile.getLogin(), entity.getId());
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class GithubUserEmail {

        @JsonProperty("email")
        private String email;
        @JsonProperty("primary")
        private Boolean primary;
        @JsonProperty("verified")
        private Boolean verified;
    }

    @NoArgsConstructor
    @Data
    public static class GithubAccessTokenCallback {

        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("scope")
        private String scope;
        @JsonProperty("token_type")
        private String tokenType;
    }

    @NoArgsConstructor
    @Data
    public static class GithubUserProfile {

        @JsonProperty("login")
        private String login;
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("node_id")
        private String nodeId;
        @JsonProperty("avatar_url")
        private String avatarUrl;
        @JsonProperty("gravatar_id")
        private String gravatarId;
        @JsonProperty("url")
        private String url;
        @JsonProperty("html_url")
        private String htmlUrl;
        @JsonProperty("followers_url")
        private String followersUrl;
        @JsonProperty("following_url")
        private String followingUrl;
        @JsonProperty("gists_url")
        private String gistsUrl;
        @JsonProperty("starred_url")
        private String starredUrl;
        @JsonProperty("subscriptions_url")
        private String subscriptionsUrl;
        @JsonProperty("organizations_url")
        private String organizationsUrl;
        @JsonProperty("repos_url")
        private String reposUrl;
        @JsonProperty("events_url")
        private String eventsUrl;
        @JsonProperty("received_events_url")
        private String receivedEventsUrl;
        @JsonProperty("type")
        private String type;
        @JsonProperty("site_admin")
        private Boolean siteAdmin;
        @JsonProperty("name")
        private String name;
        @JsonProperty("company")
        private String company;
        @JsonProperty("blog")
        private String blog;
        @JsonProperty("location")
        private String location;
        @JsonProperty("email")
        private Object email;
        @JsonProperty("hireable")
        private Object hireable;
        @JsonProperty("bio")
        private String bio;
        @JsonProperty("twitter_username")
        private String twitterUsername;
        @JsonProperty("public_repos")
        private Integer publicRepos;
        @JsonProperty("public_gists")
        private Integer publicGists;
        @JsonProperty("followers")
        private Integer followers;
        @JsonProperty("following")
        private Integer following;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("updated_at")
        private String updatedAt;
    }
}
