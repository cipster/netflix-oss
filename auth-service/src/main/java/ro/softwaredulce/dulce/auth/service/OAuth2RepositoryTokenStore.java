package ro.softwaredulce.dulce.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import ro.softwaredulce.dulce.auth.domain.model.OAuth2AuthenticationAccessToken;
import ro.softwaredulce.dulce.auth.domain.model.OAuth2AuthenticationRefreshToken;
import ro.softwaredulce.dulce.auth.domain.repository.OAuth2AccessTokenRepository;
import ro.softwaredulce.dulce.auth.domain.repository.OAuth2RefreshTokenRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OAuth2RepositoryTokenStore implements TokenStore {

    private final OAuth2AccessTokenRepository oAuth2AccessTokenRepository;
    private final OAuth2RefreshTokenRepository oAuth2RefreshTokenRepository;


    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String tokenId) {
        return oAuth2AccessTokenRepository.findByTokenId(tokenId)
                .map(OAuth2AuthenticationAccessToken::getAuthentication)
                .orElse(null);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        OAuth2AuthenticationAccessToken oAuth2AuthenticationAccessToken = new OAuth2AuthenticationAccessToken(token,
                authentication, authenticationKeyGenerator.extractKey(authentication));

        oAuth2AccessTokenRepository
                .findByTokenId(oAuth2AuthenticationAccessToken.getOAuth2AccessToken().getValue())
                .orElseGet(() -> oAuth2AccessTokenRepository.save(oAuth2AuthenticationAccessToken));
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        return oAuth2AccessTokenRepository.findByTokenId(tokenValue)
                .map(OAuth2AuthenticationAccessToken::getOAuth2AccessToken)
                .orElse(null);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        oAuth2AccessTokenRepository.findByTokenId(token.getValue())
                .ifPresent(oAuth2AccessTokenRepository::delete);
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        oAuth2RefreshTokenRepository.save(new OAuth2AuthenticationRefreshToken(refreshToken, authentication));
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        return oAuth2RefreshTokenRepository.findByTokenId(tokenValue)
                .map(OAuth2AuthenticationRefreshToken::getOAuth2RefreshToken)
                .orElse(null);
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return oAuth2RefreshTokenRepository.findByTokenId(token.getValue())
                .map(OAuth2AuthenticationRefreshToken::getAuthentication)
                .orElse(null);
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        oAuth2RefreshTokenRepository.findByTokenId(token.getValue())
                .ifPresent(oAuth2RefreshTokenRepository::delete);
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        oAuth2AccessTokenRepository.findByRefreshToken(refreshToken.getValue())
                .ifPresent(oAuth2AccessTokenRepository::delete);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        return oAuth2AccessTokenRepository.findByAuthenticationId(authenticationKeyGenerator.extractKey(authentication))
                .map(OAuth2AuthenticationAccessToken::getOAuth2AccessToken)
                .orElse(null);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        List<OAuth2AuthenticationAccessToken> tokens = oAuth2AccessTokenRepository.findByClientIdAndUserName(clientId, userName);
        return extractAccessTokens(tokens);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List<OAuth2AuthenticationAccessToken> tokens = oAuth2AccessTokenRepository.findByClientId(clientId);
        return extractAccessTokens(tokens);
    }

    private Collection<OAuth2AccessToken> extractAccessTokens(List<OAuth2AuthenticationAccessToken> tokens) {
        return tokens
                .stream()
                .map(OAuth2AuthenticationAccessToken::getOAuth2AccessToken)
                .collect(Collectors.toList());
    }

}