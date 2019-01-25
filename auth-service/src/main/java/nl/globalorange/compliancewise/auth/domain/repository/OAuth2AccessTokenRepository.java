package nl.globalorange.compliancewise.auth.domain.repository;

import nl.globalorange.compliancewise.auth.domain.model.OAuth2AuthenticationAccessToken;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OAuth2AccessTokenRepository extends BaseRepository<OAuth2AuthenticationAccessToken> {

    OAuth2AuthenticationAccessToken findByTokenId(String tokenId);

    OAuth2AuthenticationAccessToken findByRefreshToken(String refreshToken);

    OAuth2AuthenticationAccessToken findByAuthenticationId(String authenticationId);

    List<OAuth2AuthenticationAccessToken> findByClientIdAndUserName(String clientId, String username);

    List<OAuth2AuthenticationAccessToken> findByClientId(String clientId);
}