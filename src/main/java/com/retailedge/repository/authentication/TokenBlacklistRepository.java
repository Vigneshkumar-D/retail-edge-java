package com.retailedge.repository.authentication;

import com.retailedge.entity.authentication.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

    Boolean existsByToken(String token);

    void deleteByExpirationDateBefore(LocalDateTime now);
}
