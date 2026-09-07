package ru.n1fex.kinto.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import ru.n1fex.kinto.dto.SessionMetadata;

import java.net.InetAddress;


@Service
@RequiredArgsConstructor
public class SessionMetadataService {

    private final UserAgentAnalyzer userAgentAnalyzer;
    private final DatabaseReader geoIpDatabaseReader;

    public SessionMetadata extract(HttpServletRequest request) {
        String userAgentHeader = request.getHeader(HttpHeaders.USER_AGENT);
        UserAgent agent = userAgentAnalyzer.parse(userAgentHeader);

        String browser = agent.getValue(UserAgent.AGENT_NAME_VERSION_MAJOR); // "Chrome 128"
        String os = agent.getValue(UserAgent.OPERATING_SYSTEM_NAME_VERSION); // "Windows 11"
        String deviceClass = agent.getValue(UserAgent.DEVICE_CLASS);         // "Desktop" / "Phone" / "Tablet"

        String ip = extractClientIp(request);
        String country = resolveCountry(ip);

        return new SessionMetadata(deviceClass, os, browser, ip, country);
    }

    private String extractClientIp(HttpServletRequest request) {
        // Если бэкенд за прокси/балансировщиком (nginx, ingress) — реальный IP в X-Forwarded-For
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String resolveCountry(String ip) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            CountryResponse response = geoIpDatabaseReader.country(inetAddress);
            return response.getCountry().getName(); // например, "Germany"
        } catch (Exception e) {
            return "Unknown"; // localhost / приватные IP не резолвятся, это нормально в dev
        }
    }
}

