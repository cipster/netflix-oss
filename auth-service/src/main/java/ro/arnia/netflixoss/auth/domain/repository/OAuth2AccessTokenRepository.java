package ro.arnia.netflixoss.auth.domain.repository;

import org.springframework.stereotype.Repository;
import ro.arnia.netflixoss.auth.domain.model.OAuth2AuthenticationAccessToken;

import java.util.List;
import java.util.Optional;

@Repository
public interface OAuth2AccessTokenRepository extends BaseRepository<OAuth2AuthenticationAccessToken> {

    Optional<OAuth2AuthenticationAccessToken> findByTokenId(String tokenId);

    Optional<OAuth2AuthenticationAccessToken> findByRefreshToken(String refreshToken);

    Optional<OAuth2AuthenticationAccessToken> findByAuthenticationId(String authenticationId);

    List<OAuth2AuthenticationAccessToken> findByClientIdAndUserName(String clientId, String username);

    List<OAuth2AuthenticationAccessToken> findByClientId(String clientId);
}
