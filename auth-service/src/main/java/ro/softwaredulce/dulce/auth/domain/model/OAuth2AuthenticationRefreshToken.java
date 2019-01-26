package ro.softwaredulce.dulce.auth.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "refresh_tokens")
public class OAuth2AuthenticationRefreshToken extends BaseEntity {

    private String tokenId;
    private OAuth2RefreshToken oAuth2RefreshToken;
    private OAuth2Authentication authentication;

    public OAuth2AuthenticationRefreshToken(OAuth2RefreshToken oAuth2RefreshToken, OAuth2Authentication authentication) {
        this.oAuth2RefreshToken = oAuth2RefreshToken;
        this.authentication = authentication;
        this.tokenId = oAuth2RefreshToken.getValue();
    }

}