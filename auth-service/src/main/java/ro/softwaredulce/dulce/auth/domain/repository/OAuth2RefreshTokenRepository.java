package ro.softwaredulce.dulce.auth.domain.repository;

import org.springframework.stereotype.Repository;
import ro.softwaredulce.dulce.auth.domain.model.OAuth2AuthenticationRefreshToken;

import java.util.Optional;

@Repository
public interface OAuth2RefreshTokenRepository extends BaseRepository<OAuth2AuthenticationRefreshToken> {

    Optional<OAuth2AuthenticationRefreshToken> findByTokenId(String tokenId);
}