# spring_security_jwt_token_architecture_implementation
- implement registration flow -> basic controller - service - save user in repositiry
- implement authentication flow -> basic login controller - generate UsernamePasswordAuthenticationToken (setAuthenticated(false)) - AuthenticationManager - DaoAuthenticationProvider - UserDetailsService - if success generate JWT token (save) and return it to user
- implement authentication plus authorization flow (to access Role based endpoints) -> role based endpoints/ controllers - custom JwtAuthenticationFilter extends OncePerRequestFilter - extract JWT Token and validate it - UsernamePasswordAuthenticationToken (setAuthenticated(true)) - update SecurityContextHolder (Spring Security Authorization process is automatically invoked)
 
