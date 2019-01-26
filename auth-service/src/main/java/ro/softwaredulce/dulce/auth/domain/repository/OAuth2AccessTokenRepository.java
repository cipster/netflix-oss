package ro.softwaredulce.dulce.auth.domain.repository;

import org.springframework.stereotype.Repository;
import ro.softwaredulce.dulce.auth.domain.model.OAuth2AuthenticationAccessToken;

import java.util.List;

@Repository
public interface OAuth2AccessTokenRepository extends BaseRepository<OAuth2AuthenticationAccessToken> {

    OAuth2AuthenticationAccessToken findByTokenId(String tokenId);

    OAuth2AuthenticationAccessToken findByRefreshToken(String refreshToken);

    OAuth2AuthenticationAccessToken findByAuthenticationId(String authenticationId);

    List<OAuth2AuthenticationAccessToken> findByClientIdAndUserName(String clientId, String username);

    List<OAuth2AuthenticationAccessToken> findByClientId(String clientId);
}