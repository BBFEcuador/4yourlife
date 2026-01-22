package com.foryourlife.clients.account.participant.application;

import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.admin.programs.campus.domain.CampusRepository;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.admin.sales.product.domain.ProductRepository;
import com.foryourlife.clients.account.contact.domain.ContactRepository;
import com.foryourlife.clients.account.contact.infrastructure.httpControllers.SaveContactRequest;
import com.foryourlife.clients.account.invitations.applications.QueryInvitationServices;
import com.foryourlife.clients.account.invoiceData.application.InvoiceDataCommandService;
import com.foryourlife.clients.account.invoiceData.domain.DataInvoice;
import com.foryourlife.clients.account.medicalRecord.application.MedicalRecordCreatorService;
import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecord;
import com.foryourlife.clients.account.module.application.ClientModuleCreatorService;
import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.participant.domain.*;
import com.foryourlife.clients.account.participant.infrastructure.httpControllers.MedicalRecordSaveRequest;
import com.foryourlife.clients.account.participantLevel.application.ParticipantLevelService;
import com.foryourlife.clients.account.profileDetails.domain.ProfileDetailsRepository;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.shared.domain.user.applications.CommandGeneralUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ParticipantCommandService {
    private final ParticipantRepository _participantRepository;
    private final UserRepository _userRepository;
    private final CommandGeneralUserService commandGeneralUserService;
    private final QueryInvitationServices queryInvitationServices;
    private final ParticipantLevelService _rolRepository;
    private final ClientModuleCreatorService _clientModuleRepository;
    private final ProfileDetailsRepository _profileDetailsRepository;
    private final ParticipantLevelService participantLevelService;
    private final ContactRepository _contactRepository;
    private final EventBus bus;
    private final Logger logger = LoggerFactory.getLogger(ParticipantCommandService.class);
    private final AdminRepository _adminRepository;
    private final MedicalRecordCreatorService medicalRecordCreatorService;
    private final InvoiceDataCommandService invoiceDataCommandService;
    private final CampusRepository campusRepository;
    private final TeamRepository teamRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;
    private final TrainingRepository trainingRepository;

    public ParticipantCommandService(ParticipantRepository _participantRepository, UserRepository _userRepository, CommandGeneralUserService commandGeneralUserService, QueryInvitationServices queryInvitationServices, ParticipantLevelService _rolRepository, ClientModuleCreatorService _clientModuleRepository, ProfileDetailsRepository profileDetailsRepository, ParticipantLevelService participantLevelService, ContactRepository contactRepository, EventBus bus, AdminRepository _adminRepository, MedicalRecordCreatorService medicalRecordCreatorService, InvoiceDataCommandService invoiceDataCommandService, CampusRepository campusRepository, TeamRepository teamRepository, PaymentRepository paymentRepository, PasswordEncoder passwordEncoder, ProductRepository productRepository, TrainingRepository trainingRepository) {
        this._participantRepository = _participantRepository;
        this._userRepository = _userRepository;
        this.commandGeneralUserService = commandGeneralUserService;
        this.queryInvitationServices = queryInvitationServices;
        this._rolRepository = _rolRepository;
        this._clientModuleRepository = _clientModuleRepository;
        _profileDetailsRepository = profileDetailsRepository;
        this.participantLevelService = participantLevelService;
        _contactRepository = contactRepository;
        this.bus = bus;
        this._adminRepository = _adminRepository;
        this.medicalRecordCreatorService = medicalRecordCreatorService;
        this.invoiceDataCommandService = invoiceDataCommandService;
        this.campusRepository = campusRepository;
        this.teamRepository = teamRepository;
        this.paymentRepository = paymentRepository;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
        this.trainingRepository = trainingRepository;
    }

//    @Transactional
//    public void createInitUser(Participant user, MedicalRecordSaveRequest medicalRecordRequest) {
//        createInitUser(user, medicalRecordRequest, null, null);
//    }

    @Transactional
    public void createInitUser(Participant user, MedicalRecordSaveRequest medicalRecordRequest, SaveContactRequest contact, DataInvoice dataInvoice) {
        var token = queryInvitationServices.findInvitationByToken(user.getInvitationToken());
        if (token.getUsed())
            throw new BaseException("Token expirado", List.of("El token ya fue utilizado"));

        commandGeneralUserService.save(user.getUser());

        var role = this._rolRepository.getInitRole();
        user.setParticipantLevel(role);
        user.setCampus(token.getCampus());
        this._participantRepository.save(user);

        this._clientModuleRepository.createClientModule(
                ClientModule.create(UUID.randomUUID().toString(), false, false, false, user)
        );

        var medicalRecord = MedicalRecord.create(
                UUID.randomUUID().toString(),
                medicalRecordRequest.psychiatric_history_detail,
                medicalRecordRequest.medical_history_detail,
                medicalRecordRequest.medication_history_detail,
                user
        );
        medicalRecordCreatorService.createMedicalRecord(medicalRecord);

        var newContact = contact.toDomain();
        newContact.setUser(user);
        _contactRepository.save(newContact);

        if (dataInvoice != null) {
            dataInvoice.setUser(user.getUser());
            invoiceDataCommandService.create(dataInvoice);
        }

        this.bus.publish(user.pullDomainEvents());
    }


    public void save(Participant user) {
        if (this._participantRepository.findByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyCreatedException("El email " + user.getEmail() + " ya esta registrado");
        try {
            var ensureRolExist = this._rolRepository.getRoleById(user.getRoleId());
            this._participantRepository.save(user);

            this.bus.publish(user.pullDomainEvents());
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    public void update(Participant participant) {
        try {
            _userRepository.findById(participant.getUser().getId()).orElseThrow(() -> new BaseException("No se encontró el usuario asociado al participante.", List.of()));
            var auxUser = participant.getUser();
            auxUser.setName(participant.getUser().getName1() + " " + participant.getUser().getName2() + " " + participant.getUser().getLastname1() + " " + participant.getUser().getLastname2());
            _userRepository.save(auxUser);
            participant.setCampus(participant.getCampus() != null ? participant.getCampus() : queryInvitationServices.findInvitationByToken(participant.getInvitationToken()).getCampus());
            _profileDetailsRepository.findById(participant.getProfile().getId()).orElseThrow(() -> new BaseException("No se encontró el perfil asociado al participante.", List.of()));
            _profileDetailsRepository.save(participant.getProfile());
            this._participantRepository.save(participant);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    public LoginResponse login(String username, String password) throws BaseException {
        return this._participantRepository.login(username, password);
    }

    public Participant getUser(String id) {
        return this._participantRepository.findById(id).orElseThrow(() -> new UserNotFoundException("El usuario no existe"));
    }

    public void setLevel(String userId, String levelId) {
        var user = this._participantRepository.findById(userId).get();
        user.setParticipantLevel(participantLevelService.getRoleById(levelId));
        _participantRepository.save(user);
    }

    public void createFromAdmin(Participant participant) {
        if (_participantRepository.findByUserId(participant.getUser().getId()) != null) {
            throw new BaseException("El usuario ya es un Participante", List.of("El usuario ya es un Participante Master Life"));
        }

        var admin = _adminRepository.findByUserId(participant.getUser().getId()).orElseThrow(() -> new BaseException("Usuario no encontrado", List.of("El usuario no existe")));
        var invitation = queryInvitationServices.findInvitationByToken(participant.getInvitationToken());
        var user = admin.getUser();
        participant.setCampus(invitation.getCampus());
        _userRepository.save(user);
        _participantRepository.save(participant);
    }

    public void promotionToMasterLife(String id) {
        var user = this._participantRepository.findById(id).orElseThrow(() -> new BaseException("Usuario no encontrado", List.of()));
//        var role = this._rolRepository.getRolByLevel(CourseLevel.MASTER_LIFE);
//        user.setParticipantLevel(role);
        this._participantRepository.save(user);
    }

    public void changeParticipantCampus(String participantId, String campusId) {
        var participant = this._participantRepository.findById(participantId)
                .orElseThrow(() -> new BaseException("Participante no encontrado", List.of("El participante con id " + participantId + " no existe")));
        if (participant.getTeam() != null) {
            throw new BaseException("No se puede cambiar el campus", List.of("El participante ya pertenece a un equipo"));
        }
        var campus = campusRepository.findById(campusId).orElseThrow(
                () -> new BaseException("Campus no encontrado", List.of("El campus con id " + campusId + " no existe"))
        );
        participant.setCampus(campus);
        this._participantRepository.save(participant);
    }

    @Transactional
    public void changeParticipantTeam(String participantId, String teamId) {
        var participant = this._participantRepository.findById(participantId)
                .orElseThrow(() -> new BaseException("Participante no encontrado", List.of("El participante con id " + participantId + " no existe")));

        var team = participant.getTeam();
        if (team == null || !team.getId().equals(teamId)) {
            throw new BaseException("El participante no pertenece a este equipo", List.of("El participante no pertenece al equipo con id " + teamId));
        }

        var newTeam = teamRepository.findById(teamId).orElseThrow(
                () -> new BaseException("Equipo no encontrado", List.of("El equipo con id " + teamId + " no existe"))
        );

        team.getUsers().stream().filter(u -> u.getId().equals(participantId)).findFirst().ifPresent(u -> {
            team.getUsers().remove(u);
        });

        participant.getTeams().remove(team);

        participant.getTeams().add(newTeam);
        newTeam.getUsers().add(participant);
        this._participantRepository.save(participant);
    }

    public ByteArrayOutputStream getContract(String participantId, String productId, String trainingId) {
        ByteArrayOutputStream pdf = new ByteArrayOutputStream();

        var product = productRepository.findById(productId).orElseThrow(
                () -> new BaseException("Producto no encontrado", List.of("El producto con id " + productId + " no existe"))
        );

        var training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new BaseException("Entrenamiento no encontrado", List.of("El entrenamiento con id " + trainingId + " no existe"))
        );

        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(_participantRepository.getContract(participantId, product, training));
            renderer.layout();
            renderer.createPDF(pdf);
            return pdf;
        } catch (Exception e) {
            throw new BaseException("Error generating invoice", List.of(e.getMessage()));
        }
    }

    public void changeParticipantPassword(String participantId, String newPassword) {
        var participant = this._participantRepository.findById(participantId)
                .orElseThrow(
                        () -> new BaseException("Participante no encontrado", List.of("El participante con id " + participantId + " no existe")
                        )
                );
        var user = participant.getUser();
        var hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        _userRepository.save(user);
    }
}
