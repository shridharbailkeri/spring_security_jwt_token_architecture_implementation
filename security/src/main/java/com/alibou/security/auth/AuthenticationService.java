package com.alibou.security.auth;


import com.alibou.security.token.Token;
import com.alibou.security.token.TokenRepository;
import com.alibou.security.user.Role;
import com.alibou.security.user.User;
import com.alibou.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest registerRequest) {
        var user = User.builder()
                .firstname(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .build();
        tokenRepository.save(token);
    }
}
