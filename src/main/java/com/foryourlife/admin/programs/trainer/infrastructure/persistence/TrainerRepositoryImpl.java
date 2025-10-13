package com.foryourlife.admin.programs.trainer.infrastructure.persistence;

import com.foryourlife.admin.programs.trainer.domain.LoginTrainerResponse;
import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.trainer.domain.TrainerRepository;
import com.foryourlife.shared.JWTUtils;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerRepositoryImpl implements TrainerRepository {

    private final JPATrainerRepository repository;
    private Trainer loadTrainer;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final JPACriteriaConverter<Trainer> jpaCriteriaConverter;

    public TrainerRepositoryImpl(JPATrainerRepository repository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils, JPACriteriaConverter<Trainer> jpaCriteriaConverter) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.jpaCriteriaConverter = jpaCriteriaConverter;
    }

    @Override
    public void saveTrainer(Trainer trainer) {
        this.repository.save(trainer);
    }

    @Override
    public Optional<Trainer> findTrainerById(String id) {
        return this.repository.findById(id);
    }

    @Override
    public List<Trainer> getTrainers() {
        return this.repository.findAll();
    }

    @Override
    public Page<Trainer> getTrainers(Pageable pageable, Criteria criteria) {
        return this.repository.findAll(jpaCriteriaConverter.getJpaSpecifications(criteria), pageable);
    }

    @Override
    public List<Trainer> getAvailableTrainers(LocalDate startDate, LocalDate endDate) {
        return this.repository.findAvailableTrainers(startDate, endDate);
    }

    @Override
    public LoginTrainerResponse loginTrainer(String email, String password) throws BaseException {
        Authentication authentication = this.authenticate(email.toLowerCase(), password);
        var token = jwtUtils.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new LoginTrainerResponse(this.loadTrainer , token);
    }

    private Authentication authenticate(String username, String password) throws BaseException {
        var userDetails = this.loadTrainerByEmail(username);
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        if (userDetails == null) {
            throw new BadCredentialsException("Email o contraseña incorrecta");
        }
        if (!userDetails.getActive()){
            throw new BadCredentialsException("El entrenador no está activo en la organización");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Email o contraseña incorrecta");
        }
        authorityList.add(new SimpleGrantedAuthority("trainer"));
        return new UsernamePasswordAuthenticationToken(username, password, authorityList);
    }

    public Trainer loadTrainerByEmail(String email) throws BaseException {
        var trainer = repository.findByEmail(email).orElseThrow(
                () -> new BaseException("Login Error", List.of("Credenciales invalidas!."))
        );
        this.loadTrainer = (Trainer) trainer;
        return (Trainer) trainer;
    }
}
