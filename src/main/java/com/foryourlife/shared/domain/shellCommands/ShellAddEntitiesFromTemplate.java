package com.foryourlife.shared.domain.shellCommands;

import com.foryourlife.clients.account.invitations.applications.CommandInvitationService;
import com.foryourlife.clients.account.invitations.applications.QueryInvitationServices;
import com.foryourlife.clients.account.invitations.infrastructure.InvitationRequest;
import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecord;
import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecordRepository;
import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.module.domain.ClientModuleRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.clients.account.participantLevel.application.ParticipantLevelService;
import com.foryourlife.clients.account.profileDetails.infrastructure.ProfileDetailRequest;
import com.foryourlife.masterLife.domain.MasterLifeRepository;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.staff.domain.Staff;
import com.foryourlife.staff.domain.StaffRepository;
import com.foryourlife.visionary.domain.Visionary;
import com.foryourlife.visionary.domain.VisionaryRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ShellComponent
@Service
public class ShellAddEntitiesFromTemplate {
    private final ParticipantRepository participantRepository;
    private final ClientModuleRepository clientModuleRepository;
    private final VisionaryRepository visionaryRepository;
    private final StaffRepository staffRepository;
    private final MasterLifeRepository masterLifeRepository;
    private final CommandInvitationService commandInvitationService;
    private final PasswordEncoder passwordEncoder;
    private final QueryInvitationServices queryInvitationServices;
    private final MedicalRecordRepository medicalRecordRepository;
    private final ParticipantLevelService participantLevelRepository;
    private final UserRepository userRepository;


    public ShellAddEntitiesFromTemplate(ParticipantRepository participantRepository, ClientModuleRepository clientModuleRepository, VisionaryRepository visionaryRepository, StaffRepository staffRepository, MasterLifeRepository masterLifeRepository, CommandInvitationService commandInvitationService, PasswordEncoder passwordEncoder, QueryInvitationServices queryInvitationServices, MedicalRecordRepository medicalRecordRepository, ParticipantLevelService participantLevelRepository, UserRepository userRepository) {
        this.participantRepository = participantRepository;
        this.clientModuleRepository = clientModuleRepository;
        this.visionaryRepository = visionaryRepository;
        this.staffRepository = staffRepository;
        this.masterLifeRepository = masterLifeRepository;
        this.commandInvitationService = commandInvitationService;
        this.passwordEncoder = passwordEncoder;
        this.queryInvitationServices = queryInvitationServices;
        this.medicalRecordRepository = medicalRecordRepository;
        this.participantLevelRepository = participantLevelRepository;
        this.userRepository = userRepository;
    }

    @ShellMethod(key = "app:import-entities")
    public void importEntities() {
        try {

            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("plantillav3.xlsx");

            if (is == null) {
                throw new RuntimeException("Archivo no encontrado en resources");
            }

            Workbook workbook = new XSSFWorkbook(is);

            importParticipants(workbook.getSheetAt(0));
            importStaff(workbook.getSheetAt(1));
            importVisionaries(workbook.getSheetAt(2));
            importMasterLife(workbook.getSheetAt(3));
        } catch (Exception e) {
            System.out.println("Error al importar entidades: " + e.getMessage());
            return;
        }

    }

    public void importParticipants(Sheet sheet) {

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;
            if (row.getRowNum() < 2) continue;
            var cell = row.getCell(0);
            if (cell == null) continue;
            String nombre1 = getCellString(row.getCell(2));
            String nombre2 = getCellString(row.getCell(3));
            String apellido1 = getCellString(row.getCell(0));
            String apellido2 = getCellString(row.getCell(1));
            String telefono = getCellString(row.getCell(4));
            String direccion = getCellString(row.getCell(5));
            String email = "participane.cue"+(i+1)+"@gmail.com";
            var g = getCellString(row.getCell(7));
            String genero = g == "H" ? "H" : "M";
            String ci = getCellString(row.getCell(8));
            String ciudad = getCellString(row.getCell(9));
            String fecha = "2001-09-11"; // "2000-07-21"

            LocalDate localDate = LocalDate.parse(fecha);
            Date fechaNacimiento = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            String ocupacion = getCellString(row.getCell(11));


            var invitationToken = commandInvitationService.createInvitationByAdminWithQuantity(
                    new InvitationRequest("3936ae5e-0cc1-4375-abc7-520d16999110",
                            "600",
                            "61d88b2a-a22e-4cb0-8e43-e036483039d6")
            );

            var userOptional = userRepository.findByEmail(email);

            UserEntities newEntity = new UserEntities(
                    UUID.randomUUID().toString(),
                    UserType.PARTICIPANT.toString()
            );

            User user = userOptional.orElseGet(() -> {
                return userRepository.save(new User(
                        UUID.randomUUID().toString(),
                        email,
                        passwordEncoder.encode("focus2025"),
                        nombre1,
                        nombre2,
                        apellido1,
                        apellido2,
                        nombre1 + " " + nombre2 + " " + apellido1 + " " + apellido2,
                        telefono,
                        List.of(newEntity)
                ));
            });

            if (userOptional.isPresent()) {

                if (user.getEntityMap() == null) {
                    user.setEntityMap(new ArrayList<>());
                }

                boolean exists = user.getEntityMap()
                        .stream()
                        .anyMatch(e -> e.getEntity().equals(UserType.PARTICIPANT.toString()));

                if (!exists) {
                    user.getEntityMap().add(newEntity);
                    user = userRepository.save(user);
                }
            }

            var profile = new ProfileDetailRequest(
                    UUID.randomUUID().toString(),
                    fechaNacimiento,
                    direccion,
                    ocupacion,
                    genero,
                    "SOLTERO",
                    ci,
                    ciudad
            ).toDomain();

            var participant = Participant.create(
                    UUID.randomUUID().toString(),
                    user,
                    participantLevelRepository.getRolByLevel(CourseLevel.LIFE),
                    profile,
                    invitationToken,
                    false,
                    false,
                    queryInvitationServices.findInvitationByToken(invitationToken).getCampus()
            );


            participantRepository.save(participant);
            var medicalRecord = new MedicalRecord(
                    UUID.randomUUID().toString(),
                    "N/A",
                    "N/A",
                    "N/A",
                    participant
            );
            var clientModule = new ClientModule(
                    UUID.randomUUID().toString(),
                    true,
                    false,
                    false,
                    participant
            );
            clientModuleRepository.save(clientModule);
            medicalRecordRepository.save(medicalRecord);
        }

    }

    public void importVisionaries(Sheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;
            var cell = row.getCell(0);
            if (cell == null) continue;
            String nombre1 = getCellString(row.getCell(0));
            String nombre2 = getCellString(row.getCell(1));
            String apellido1 = getCellString(row.getCell(2));
            String apellido2 = getCellString(row.getCell(3));
            String telefono = getCellString(row.getCell(4));
            String email = getCellString(row.getCell(5));

            var userOptional = userRepository.findByEmail(email);

            UserEntities newEntity = new UserEntities(
                    UUID.randomUUID().toString(),
                    UserType.PARTICIPANT.toString()
            );

            User user = userOptional.orElseGet(() -> {
                return userRepository.save(new User(
                        UUID.randomUUID().toString(),
                        email,
                        passwordEncoder.encode("focus2025"),
                        nombre1,
                        nombre2,
                        apellido1,
                        apellido2,
                        nombre1 + " " + nombre2 + " " + apellido1 + " " + apellido2,
                        telefono,
                        List.of(newEntity)
                ));
            });

            if (userOptional.isPresent()) {

                if (user.getEntityMap() == null) {
                    user.setEntityMap(new ArrayList<>());
                }

                boolean exists = user.getEntityMap()
                        .stream()
                        .anyMatch(e -> e.getEntity().equals(UserType.VISIONARY.toString()));

                if (!exists) {
                    user.getEntityMap().add(newEntity);
                    user = userRepository.save(user);
                }
            }

            visionaryRepository.save(Visionary.create(
                    UUID.randomUUID().toString(),
                    true,
                    user
            ));
        }
    }

    public void importStaff(Sheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;
            var cell = row.getCell(0);
            if (cell == null) continue;
            String nombre1 = getCellString(row.getCell(0));
            String nombre2 = getCellString(row.getCell(1));
            String apellido1 = getCellString(row.getCell(2));
            String apellido2 = getCellString(row.getCell(3));
            String telefono = getCellString(row.getCell(4));
            String email = getCellString(row.getCell(5));
            String rol = getCellString(row.getCell(6));

            var userOptional = userRepository.findByEmail(email);

            UserEntities newEntity = new UserEntities(
                    UUID.randomUUID().toString(),
                    UserType.PARTICIPANT.toString()
            );

            User user = userOptional.orElseGet(() -> {
                return userRepository.save(new User(
                        UUID.randomUUID().toString(),
                        email,
                        passwordEncoder.encode("focus2025"),
                        nombre1,
                        nombre2,
                        apellido1,
                        apellido2,
                        nombre1 + " " + nombre2 + " " + apellido1 + " " + apellido2,
                        telefono,
                        List.of(newEntity)
                ));
            });

            if (userOptional.isPresent()) {

                if (user.getEntityMap() == null) {
                    user.setEntityMap(new ArrayList<>());
                }

                boolean exists = user.getEntityMap()
                        .stream()
                        .anyMatch(e -> e.getEntity().equals(UserType.STAFF.toString()));

                if (!exists) {
                    user.getEntityMap().add(newEntity);
                    user = userRepository.save(user);
                }
            }

            if (rol.equals("C")){
                rol = "CAPITAN";
            }else  {
                rol = "STAFF";
            }
            staffRepository.save(Staff.create(
                    UUID.randomUUID().toString(),
                    rol,
                    true,
                    user
            ));
        }
    }

    public void importMasterLife(Sheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;
            var cell = row.getCell(0);
            if (cell == null) continue;
            String nombre1 = getCellString(row.getCell(0));
            String nombre2 = getCellString(row.getCell(1));
            String apellido1 = getCellString(row.getCell(2));
            String apellido2 = getCellString(row.getCell(3));
            String telefono = getCellString(row.getCell(4));
            String email = getCellString(row.getCell(6));

            var userOptional = userRepository.findByEmail(email);

            UserEntities newEntity = new UserEntities(
                    UUID.randomUUID().toString(),
                    UserType.PARTICIPANT.toString()
            );

            User user = userOptional.orElseGet(() -> {
                return userRepository.save(new User(
                        UUID.randomUUID().toString(),
                        email,
                        passwordEncoder.encode("focus2025"),
                        nombre1,
                        nombre2,
                        apellido1,
                        apellido2,
                        nombre1 + " " + nombre2 + " " + apellido1 + " " + apellido2,
                        telefono,
                        List.of(newEntity)
                ));
            });

            if (userOptional.isPresent()) {

                if (user.getEntityMap() == null) {
                    user.setEntityMap(new ArrayList<>());
                }

                boolean exists = user.getEntityMap()
                        .stream()
                        .anyMatch(e -> e.getEntity().equals(UserType.MASTER_LIFE.toString()));

                if (!exists) {
                    user.getEntityMap().add(newEntity);
                    user = userRepository.save(user);
                }
            }

            masterLifeRepository.save(com.foryourlife.masterLife.domain.MasterLife.create(
                    UUID.randomUUID().toString(),
                    true,
                    user
            ));
        }
    }

    private String getCellString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                // Si es fecha
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }

                // Convertir número sin notación científica y sin .0
                long numericValue = (long) cell.getNumericCellValue();
                return String.valueOf(numericValue);

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                return getCellString(cell, cell.getCachedFormulaResultType());

            case BLANK:
            default:
                return "";
        }
    }

    private String getCellString(Cell cell, CellType type) {
        switch (type) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                long numericValue = (long) cell.getNumericCellValue();
                return String.valueOf(numericValue);
            default:
                return "";
        }
    }

}
