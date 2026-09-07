package ru.n1fex.kinto.config;

import com.maxmind.geoip2.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GeoIpConfiguration {

    @Bean
    public DatabaseReader geoIpDatabaseReader() throws IOException {
        try (InputStream stream = new ClassPathResource("geoip/dbip-country-lite.mmdb").getInputStream()) {
            return new DatabaseReader.Builder(stream).build();
        }
    }

}
