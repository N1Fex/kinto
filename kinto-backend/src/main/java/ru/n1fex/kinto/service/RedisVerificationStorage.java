package ru.n1fex.kinto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import ru.n1fex.kinto.dto.VerificationCode;
import ru.n1fex.kinto.dto.VerificationRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisVerificationStorage implements VerificationStorage {

    private final StringRedisTemplate redisTemplate;

    private String getKey(VerificationRequest request) {
        return "verification:" + request.getChannel().toString() + ":" + request.getRecipient();
    }

    @Override
    public void save(VerificationRequest request, VerificationCode code) {
        String key = getKey(request);
        redisTemplate.opsForValue().set(key, code.getCode(), code.getTtl());
    }

    @Override
    public Optional<VerificationCode> get(VerificationRequest request) {
        String key = getKey(request);

        String code = redisTemplate.opsForValue().get(key);
        if (code == null) {
            return Optional.empty();
        }
        return Optional.of(new VerificationCode(code));
    }

    @Override
    public void delete(VerificationRequest request) {
        String key = getKey(request);
        redisTemplate.delete(key);
    }
}
