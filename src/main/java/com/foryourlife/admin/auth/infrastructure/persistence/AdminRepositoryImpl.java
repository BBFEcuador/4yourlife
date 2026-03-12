package com.foryourlife.admin.auth.infrastructure.persistence;

import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminLoginResponse;
import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.admin.sales.payments.cashBox.domain.CashBoxRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import com.foryourlife.shared.JWTUtils;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.infrastructure.JpaUserRepository;
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
    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private Admin loadAdmin;
    private final CashDrawerRepository cashDrawerRepository;

    public AdminRepositoryImpl(JPAAdminRepository repository, JpaUserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils, CashDrawerRepository cashDrawerRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.cashDrawerRepository = cashDrawerRepository;
    }

    @Override
    public AdminLoginResponse login(String username, String password) {
        Authentication authentication = this.authenticate(username.toLowerCase(), password);
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
        return this.repository.findByUser_email(email);
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

    @Override
    public Optional<Admin> findByUserId(String userId) {
            return repository.findByUser_id(userId);
    }

    private Authentication authenticate(String username, String password) throws BaseException {
        this.loadAdminByUsername(username);
        var userDetails = this.loadAdminByUsername(username);
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        if (userDetails == null) {
            throw new BadCredentialsException("Email o contraseña incorrecta");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Email o contraseña incorrecta");
        }
        authorityList.add(new SimpleGrantedAuthority(userDetails.getRole().getType()));
        userDetails.getRole().getPermissions().forEach(permission -> {
            authorityList.add(new SimpleGrantedAuthority(permission.getName()));
        });
        return new UsernamePasswordAuthenticationToken(username, password, authorityList);
    }

    private Admin loadAdminByUsername(String email) throws BaseException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException("Login Error", List.of("El usuario " + email + " no existe.")));
        var admin = repository.findByUser_id(user.getId())
                .orElseThrow(() -> new BaseException("Login Error", List.of("El usuario " + email + " no existe.")));
        loadAdmin = admin;
        return admin;
    }
}
