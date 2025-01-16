package com.foryourlife.account.user.infrastructure;

import com.foryourlife.account.user.domain.LoginResponse;
import com.foryourlife.account.user.domain.UserRepository;
import com.foryourlife.account.user.domain.Users;
import com.foryourlife.shared.JWTUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserRepositoryImpl implements UserRepository {

    private final JPAUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private Users loadUser;

    public UserRepositoryImpl(JPAUserRepository repository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponse login(String username, String password) {
        Authentication authentication = this.authenticate(username, password);
        var token = jwtUtils.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new LoginResponse(token, this.loadUser);
    }

    private Users loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("The user " + username + " does not exist."));
        loadUser = user;
        return user;
    }

    private Authentication authenticate(String username, String password) {
        this.loadUserByUsername(username);
        var userDetails = this.loadUserByUsername(username);
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(username, password, authorityList);
    }

    public Optional<Users> findByEmail(){

    }
}
