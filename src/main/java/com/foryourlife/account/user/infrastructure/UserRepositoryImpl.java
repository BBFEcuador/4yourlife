package com.foryourlife.account.user.infrastructure;

import com.foryourlife.account.user.domain.LoginResponse;
import com.foryourlife.account.user.domain.UserRepository;
import com.foryourlife.account.user.domain.Users;
import com.foryourlife.shared.JWTUtils;
import com.foryourlife.shared.domain.exception.BaseException;
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
    public LoginResponse login(String username, String password) throws BaseException {
        Authentication authentication = this.authenticate(username, password);
        var token = jwtUtils.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new LoginResponse(token, this.loadUser);
    }

    private Users loadUserByUsername(String username) throws BaseException {
        var user = repository.findByEmail(username)
                .orElseThrow(() -> new BaseException("Login Error",List.of("The user " + username + " does not exist.")));
        loadUser = user;
        return user;
    }

    private Authentication authenticate(String username, String password) throws BaseException {
        this.loadUserByUsername(username);
        var userDetails = this.loadUserByUsername(username);
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        authorityList.add(new SimpleGrantedAuthority(userDetails.getRole().getRoleName()));
        return new UsernamePasswordAuthenticationToken(username, password, authorityList);
    }

    @Override
    public Optional<Users> findByEmail(String email){
        return repository.findByEmail(email);
    }

    @Override
    public Optional<Users> findById(String id){
        return repository.findById(id);
    }

    @Override
    public void save(Users user) {
        this.repository.save(user);
    }
}
