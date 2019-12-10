package ro.arnia.netflixoss.administration.service;

import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("ALL")
public class CustomUserInfoTokenServices implements ResourceServerTokenServices {
    private static final List<String> PRINCIPAL_KEYS = List.of("user", "username", "userid", "user_id", "login", "id", "name");

    private final String userInfoUri;
    private final String clientId;


    public CustomUserInfoTokenServices(String userInfoUri, String clientId) {
        this.userInfoUri = userInfoUri;
        this.clientId = clientId;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        OAuth2RestOperations restTemplate;
        BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
        resource.setClientId(this.clientId);
        restTemplate = new OAuth2RestTemplate(resource);

        OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext().getAccessToken();

        if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
            DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(
                    accessToken);
            token.setTokenType(DefaultOAuth2AccessToken.BEARER_TYPE);
            restTemplate.getOAuth2ClientContext().setAccessToken(token);
        }
        Map map = restTemplate.getForEntity(this.userInfoUri, Map.class).getBody();

        Object principal = getPrincipal(map);
        OAuth2Request request = getRequest(map);
        List<GrantedAuthority> authorities = new FixedAuthoritiesExtractor().extractAuthorities(map);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                principal, "N/A", authorities);
        token.setDetails(map);

        return new OAuth2Authentication(request, token);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return null;
    }

    private Object getPrincipal(Map<String, Object> map) {
        for (String key : PRINCIPAL_KEYS) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return "unknown";
    }

    private OAuth2Request getRequest(Map<String, Object> map) {
        Map<String, Object> request = (Map<String, Object>) map.get("oauth2Request");

        String clientId = (String) request.get("clientId");
        Set<String> scope = new LinkedHashSet<>(request.containsKey("scope") ?
                (Collection<String>) request.get("scope") : Collections.emptySet());

        return new OAuth2Request(null, clientId, null, true, new HashSet<>(scope),
                null, null, null, null);
    }
}
