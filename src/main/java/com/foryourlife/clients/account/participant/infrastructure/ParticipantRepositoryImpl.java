package com.foryourlife.clients.account.participant.infrastructure;

import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.clients.account.participant.domain.LoginResponse;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.JWTUtils;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

@Service
public class ParticipantRepositoryImpl implements ParticipantRepository {

    private final JPAParticipantRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private Participant loadUser;
    private final JPACriteriaConverter<Participant> criteriaConverter;

    public ParticipantRepositoryImpl(JPAParticipantRepository repository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils, JPACriteriaConverter<Participant> criteriaConverter) {
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
                .orElseThrow(() -> new BaseException("Usuario no encontrado", List.of("The user " + username + " does not exist.")));
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
    public Page<Participant> getAll(Pageable pageable, Criteria criteria) {
        return repository.findAll(criteriaConverter.getJpaSpecifications(criteria), pageable);
    }

    @Override
    public List<Participant> match(Criteria criteria) {
        var jpaCriteria = criteriaConverter.getJpaSpecifications(criteria);
        return repository.findAll(jpaCriteria);
    }

    @Override
    public void save(Participant user) {
        this.repository.saveAndFlush(user);
    }

    @Override
    public Optional<Participant> findByUserId(String userId) {
        return repository.findByUser_Id(userId);
    }

    @Override
    public List<Participant> findAllByUserIds(List<String> userIds) {
        return repository.findAllByUserIds(userIds);
    }

    @Override
    public String getContract(String participantId, Product product, Training training) throws IOException {

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        Participant participant = repository
                .findById(participantId)
                .orElseThrow(() -> new BaseException(
                        "Participant not found",
                        List.of("Participant with id " + participantId + " not found")
                ));
        Context context = new Context(new Locale("es", "EC"));

        var actualDate = LocalDate.now();

        context.setVariable("participant", participant);
        context.setVariable("date", actualDate);
        context.setVariable("product", product);
        context.setVariable("training", training);
        String base64Image = Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of("src/main/resources/static/images/Picture1.png")));

        context.setVariable("signatureImage", base64Image);
        return templateEngine.process(
                "templates/contract-template",
                context
        );
    }

    @Override
    public List<Participant> saveAll(List<Participant> participants) {
        return repository.saveAll(participants);
    }
}
