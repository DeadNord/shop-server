package com.flamelab.shopserver.services.impl;

import com.flamelab.shopserver.entities.AuthToken;
import com.flamelab.shopserver.entities.User;
import com.flamelab.shopserver.enums.Roles;
import com.flamelab.shopserver.exceptions.ResourceException;
import com.flamelab.shopserver.mappers.AuthTokenMapper;
import com.flamelab.shopserver.repositories.AuthorizationRepository;
import com.flamelab.shopserver.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.flamelab.shopserver.enums.AuthTokenType.BEARER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthorizationRepository authorizationRepository;
    private final AuthTokenMapper authTokenMapper;
    private final int maxUsageAmount = 1000;

    @Override
    public AuthToken createToken(User user) {
        return authorizationRepository.save(authTokenMapper.generateAuthToken(user));
    }

    @Override
    public AuthToken validateTokenAndReturn(String token, List<Roles> availableRoles) {
        if (token == null || token.isEmpty() || !token.startsWith(BEARER.getTypeName())) {
            throw new ResourceException(UNAUTHORIZED, "Unauthorized");
        } else {
            token = token.replace(BEARER.getTypeName() + " ", "");
            Optional<AuthToken> optionalAuthToken = authorizationRepository.findByToken(token);
            if (optionalAuthToken.isPresent()) {
                AuthToken tokenFromDb = optionalAuthToken.get();
                validateRoles(tokenFromDb, availableRoles.stream().map(Objects::toString).toList());
                validateIsTokenExpire(tokenFromDb);
                increaseTokenUsageAmount(tokenFromDb);
                return tokenFromDb;
            } else {
                throw new ResourceException(UNAUTHORIZED, "UNAUTHORIZED");
            }
        }
    }

    private void validateRoles(AuthToken tokenFromDb, List<String> availableRoles) {
        if (!availableRoles.contains(tokenFromDb.getRole())) {
            throw new ResourceException(UNAUTHORIZED, "User not available for using this API");
        }
    }

    private void validateIsTokenExpire(AuthToken token) {
        if (token.getUsageAmount() >= maxUsageAmount) {
            deleteTokenByTokenId(token.getId());
            throw new ResourceException(UNAUTHORIZED, "Token is expired");
        }
    }

    private void increaseTokenUsageAmount(AuthToken token) {
        token.setUsageAmount(token.getUsageAmount() + 1);
        authorizationRepository.save(token);
    }

    @Override
    public AuthToken getTokenByEmail(String email) {
        Optional<AuthToken> optionalToken = authorizationRepository.findByEmail(email);
        if (optionalToken.isPresent()) {
            return optionalToken.get();
        } else {
            throw new ResourceException(UNAUTHORIZED, "UNAUTHORIZED");
        }
    }

    @Override
    public boolean isTokenExistsByEmail(String email) {
        return authorizationRepository.findByEmail(email).isPresent();
    }

    @Override
    public void deleteTokenByTokenId(String tokenId) {
        authorizationRepository.deleteById(tokenId);
    }

}
