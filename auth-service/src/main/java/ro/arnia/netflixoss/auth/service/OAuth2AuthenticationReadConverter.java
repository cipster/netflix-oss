package ro.arnia.netflixoss.auth.service;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import ro.arnia.netflixoss.auth.domain.model.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ReadingConverter
public class OAuth2AuthenticationReadConverter implements Converter<Document, OAuth2Authentication> {

    @Override
    @SuppressWarnings("unchecked")
    public OAuth2Authentication convert(Document source) {
        Document storedRequest = (Document) source.get("storedRequest");
        OAuth2Request oAuth2Request = new OAuth2Request((Map<String, String>) storedRequest.get("requestParameters"),
                (String) storedRequest.get("clientId"), null, true, new HashSet((List) storedRequest.get("scope")),
                null, null, null, null);
        Document userAuthorization = (Document) source.get("userAuthentication");
        Object principal = getPrincipalObject(userAuthorization.get("principal"));
        Authentication userAuthentication = new UsernamePasswordAuthenticationToken(
                principal,
                userAuthorization.get("credentials"),
                getAuthorities((List) userAuthorization.get("authorities")));

        return new OAuth2Authentication(oAuth2Request, userAuthentication);
    }

    private Object getPrincipalObject(Object principal) {
        if (principal instanceof Document) {
            Document principalDBObject = (Document) principal;
            return new User(principalDBObject);
        } else {
            return principal;
        }
    }

    private Collection<GrantedAuthority> getAuthorities(List<Map<String, String>> authorities) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>(authorities.size());
        for (Map<String, String> authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.get("role")));
        }
        return grantedAuthorities;
    }

}
