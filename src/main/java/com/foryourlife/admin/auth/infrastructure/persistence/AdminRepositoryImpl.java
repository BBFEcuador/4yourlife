package com.foryourlife.admin.auth.infrastructure.persistence;

import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminLoginResponse;
import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.shared.JWTUtils;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminRepositoryImpl implements AdminRepository {

    private final JPAAdminRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private Admin loadAdmin;

    public AdminRepositoryImpl(JPAAdminRepository repository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public AdminLoginResponse login(String username, String password) {
        Authentication authentication = this.authenticate(username, password);
        var token = jwtUtils.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new AdminLoginResponse(this.loadAdmin, token);
    }

    @Override
    public Optional<Admin> findById(String id) {
        return this.repository.findById(id);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return this.repository.findByEmail(email);
    }

    @Override
    public List<Admin> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Admin save(Admin admin) {
        return this.repository.save(admin);
    }

    @Override
    public void deleteById(String id) {
        this.repository.deleteById(id);
    }

    private Authentication authenticate(String username, String password) throws BaseException {
        this.loadAdminByUsername(username);
        var userDetails = this.loadAdminByUsername(username);
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        authorityList.add(new SimpleGrantedAuthority(userDetails.getRole().getType()));
        return new UsernamePasswordAuthenticationToken(username, password, authorityList);
    }

    private Admin loadAdminByUsername(String email) throws BaseException {
        var user = repository.findByEmail(email)
                .orElseThrow(() -> new BaseException("Login Error", List.of("The user " + email + " does not exist.")));
        loadAdmin = user;
        return user;
    }
}
