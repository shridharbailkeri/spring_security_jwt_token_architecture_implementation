package com.alibou.security.auth;


import com.alibou.security.user.LoginDTO;
import com.alibou.security.user.User;
import com.alibou.security.user.UserRepository;
import com.alibou.security.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {


    private final UserRepository userRepository;

    private final AuthenticationService authenticationService;


    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> admin() {
        return ResponseEntity.ok("Hello admin only");
    }

    @GetMapping("/manager")
    public ResponseEntity<String> manager() {
        return ResponseEntity.ok("Hello manager only");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        authenticationService.register(registerRequest);
        return null;
    }

    // even during login it hits the custome JwtAuthenticationFilter
    // but rember in postman? before loging in we are not giving any Bearer token  so JwtAuthenticationFilter will check String authorizationHeader = request.getHeader("Authorization");
    // it does not find any Authorization Bearer token so it just returns
    // after loging in for example if you want to access manager specific portal, remember in postman this time we are selecting Authorization and Bearer and copying JWT token which got generated during login
    // so this time to access manager specific portal the request again hits JwtAuthenticationFilter and String authorizationHeader = request.getHeader("Authorization"); is not null or empty
    // so now the http request passes through JwtAuthenticationFilter (any other request uses JwtAuthenticationFilter) see architecture
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        authenticationManager.authenticate(token); // auth manager will try to find the user if successfull , will return success or exception
        // if successfull return JWT token
        User user = userRepository.findByEmail(loginDTO.getUsername()).get();
        String jwtToken = jwtUtil.generate(loginDTO.getUsername());
        authenticationService.saveUserToken(user, jwtToken);
        return ResponseEntity.ok(jwtToken);

        // we created a login end point, we created username and password wrap it in UsernamePasswordAuthentication token
        // and then give it to authentication manager, authentication manager  now will call provider default provider (DAOAUTHPROVIDER) because we dont touch that
        // we know that we want to check this user with our implementation , so we need to implemenr custom user details service
    }
}
