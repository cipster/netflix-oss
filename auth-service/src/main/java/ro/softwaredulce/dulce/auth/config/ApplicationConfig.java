package ro.softwaredulce.dulce.auth.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.softwaredulce.dulce.auth.service.OAuth2AuthenticationReadConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ApplicationConfig extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.authentication-database}")
    private String authenticationDatabaseName;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private char[] password;


    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(4);
// TODO: replace with BCrypt when user creation is in place
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Primary
    @Bean
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<>();
        OAuth2AuthenticationReadConverter converter = new OAuth2AuthenticationReadConverter();
        converterList.add(converter);
        return new MongoCustomConversions(converterList);
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(
                new ServerAddress(host, port),
                MongoCredential.createScramSha1Credential(username, authenticationDatabaseName, password),
                MongoClientOptions.builder().build()
        );
    }
}
