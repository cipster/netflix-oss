package nl.globalorange.compliancewise.auth.domain.repository;

import nl.globalorange.compliancewise.auth.domain.model.OAuth2AuthenticationRefreshToken;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2RefreshTokenRepository extends BaseRepository<OAuth2AuthenticationRefreshToken> {

    OAuth2AuthenticationRefreshToken findByTokenId(String tokenId);
}