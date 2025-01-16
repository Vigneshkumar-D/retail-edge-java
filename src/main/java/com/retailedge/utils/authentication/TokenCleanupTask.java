package com.retailedge.utils.authentication;

import com.retailedge.repository.authentication.TokenBlacklistRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TokenCleanupTask {

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        tokenBlacklistRepository.deleteByExpirationDateBefore(now);
    }
}
