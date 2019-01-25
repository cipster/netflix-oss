package nl.globalorange.compliancewise.auth.service.security;

import lombok.RequiredArgsConstructor;
import nl.globalorange.compliancewise.auth.domain.repository.ClientRepository;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MongoClientDetailsService implements ClientDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clientRepository.findByName(clientId)
                .orElseThrow(() -> new NoSuchClientException(String.format("Client %s does not exist", clientId)));
    }
}
