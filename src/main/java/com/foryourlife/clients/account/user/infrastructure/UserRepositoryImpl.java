package com.foryourlife.clients.account.user.infrastructure;

import com.foryourlife.clients.account.user.domain.LoginResponse;
import com.foryourlife.clients.account.user.domain.UserRepository;
import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.shared.JWTUtils;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
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
public class UserRepositoryImpl implements UserRepository {

    private final JPAUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private Participant loadUser;
    private final JPACriteriaConverter<Participant> criteriaConverter;

    public UserRepositoryImpl(JPAUserRepository repository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils, JPACriteriaConverter<Participant> criteriaConverter) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.criteriaConverter = criteriaConverter;
    }

    @Override
    public LoginResponse login(String username, String password) throws BaseException {
        Authentication authentication = this.authenticate(username, password);
        var token = jwtUtils.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new LoginResponse(token, this.loadUser);
    }

    private Participant loadUserByUsername(String username) throws BaseException {
        var user = repository.findByUser_email(username)
                .orElseThrow(() -> new BaseException("Login Error", List.of("The user " + username + " does not exist.")));
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
        authorityList.add(new SimpleGrantedAuthority(userDetails.getParticipantLevel().getRoleName()));
        return new UsernamePasswordAuthenticationToken(username, password, authorityList);
    }

    @Override
    public Optional<Participant> findByEmail(String email) {
        return repository.findByUser_email(email);
    }

    @Override
    public Optional<Participant> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Participant> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Participant> match(Criteria criteria) {
        var jpaCriteria = criteriaConverter.getJpaSpecifications(criteria);
        return repository.findAll(jpaCriteria);
    }

    @Override
    public void save(Participant user) {
        this.repository.save(user);
    }
}
