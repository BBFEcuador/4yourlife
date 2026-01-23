package com.foryourlife.clients.account.participant.infrastructure;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.clients.account.participant.domain.LoginResponse;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.JWTUtils;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
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
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private Participant loadUser;
    private final InvitationRepository invitationRepository;
    private final JPACriteriaConverter<Participant> criteriaConverter;

    public ParticipantRepositoryImpl(JPAParticipantRepository repository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils, JPACriteriaConverter<Participant> criteriaConverter, PaymentRepository paymentRepository, InvitationRepository invitationRepository, UserRepository userRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.criteriaConverter = criteriaConverter;
        this.paymentRepository = paymentRepository;
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
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
    public String getContract(Training training, Product product, Participant participant)
            throws IOException {

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        var payment = paymentRepository.findFirstByParticipantIdAndStatusOrderByCreatedDateAsc(participant.getId(), PaymentStatus.COMPLETED).orElse(null);

        var contact = participant.getContacts() != null && !participant.getContacts().isEmpty()
                ? participant.getContacts().getFirst()
                : null;
        var medicalRecord = participant.getMedicalRecord() != null
                ? participant.getMedicalRecord()
                : null;
        var invitation = invitationRepository.findByToken(participant.getInvitationToken()).orElse(null);
        var sender = invitation != null ? invitation.getEnrolled() : null;
        var user = sender != null ?
                userRepository.findById(sender.getId()).orElse(null) : null;
        var paymentHistory = payment != null && payment.getPaymentshistory()!= null && !payment.getPaymentshistory().isEmpty()
                ? payment.getPaymentshistory().getFirst()
                : null;
        Context context = new Context(new Locale("es", "EC"));
        context.setVariable("contact", contact);
        context.setVariable("trainingSender", sender);
        context.setVariable("sender", user);
        context.setVariable("payment", payment);
        context.setVariable("paymentHistory", paymentHistory);
        context.setVariable("participant", participant);
        context.setVariable("date", LocalDate.now());
        context.setVariable("product", product);
        context.setVariable("training", training);
        context.setVariable("medicalRecord", medicalRecord);

        return templateEngine.process(
                "templates/contract-template",
                context
        );
    }

    @Override
    public String getContractByTeam(Training training, Product product, List<Participant> participants) throws IOException {

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context(new Locale("es", "EC"));
        context.setVariable("participants", participants);
        context.setVariable("date", LocalDate.now());
        context.setVariable("product", product);
        context.setVariable("training", training);

        return templateEngine.process(
                "templates/contracts-template",
                context
        );
    }

    @Override
    public List<Participant> saveAll(List<Participant> participants) {
        return repository.saveAll(participants);
    }
}
