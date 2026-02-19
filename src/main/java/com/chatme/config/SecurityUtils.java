package com.chatme.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;
import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static Optional<Jwt> getCurrentJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            return Optional.of(jwt);
        }
        return Optional.empty();
    }

    public static String getAuthorName(String fallback) {
        return getCurrentJwt().map(jwt -> {
            Map<String, Object> meta = jwt.getClaim("user_metadata");
            if (meta != null) {
                Object fullName = meta.get("full_name");
                if (fullName != null && !fullName.toString().isBlank()) return fullName.toString();
                Object name = meta.get("name");
                if (name != null && !name.toString().isBlank()) return name.toString();
            }
            String email = jwt.getClaim("email");
            return (email != null && !email.isBlank()) ? email : fallback;
        }).orElse(fallback);
    }

    public static Optional<String> getUserId() {
        return getCurrentJwt().map(Jwt::getSubject);
    }

    public static boolean isAuthenticated() {
        return getCurrentJwt().isPresent();
    }
}
