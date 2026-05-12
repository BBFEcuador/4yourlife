package com.foryourlife.admin.programs.teams.application;

import com.foryourlife.admin.crm.call.domain.Call;
import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.crm.callLogs.domain.CallStatus;
import com.foryourlife.admin.crm.callLogs.domain.CallType;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.staff.domain.Staff;
import com.foryourlife.visionary.domain.Visionary;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TeamBadgePdfService {

    private final CallRepository callsRepository;
    private final TeamRepository _teamRepository;
    private final InvitationRepository invitationRepository;

    private final UserRepository userRepository;

    public TeamBadgePdfService(CallRepository callsRepository, TeamRepository teamRepository, InvitationRepository invitationRepository, UserRepository userRepository) {
        this.callsRepository = callsRepository;
        _teamRepository = teamRepository;
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
    }

    public byte[] generatePdf(Team team) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        document.setMargins(20, 20, 20, 20);

        // Tabla sin padding
        Table table = new Table(UnitValue.createPercentArray(2))
                .useAllAvailableWidth()
                .setPadding(0)
                .setBorder(Border.NO_BORDER);  // SIN BORDE en la tabla principal
        var users = team.getUsers().stream().map(Participant::getUser).sorted(Comparator.comparing(User::getLastname1)).toList();
        var ml = team.getMasterLife().stream().map(MasterLife::getUser).toList();
        var visionary = team.getVisionaries().stream().map(Visionary::getUser).toList();
        var staff = team.getStaffs().stream().map(Staff::getUser).toList();

        var calls = callsRepository.findAllByTrainingId(team.getTraining().getId());
        Map<String, Call> callMap = calls.stream()
                .collect(Collectors.toMap(
                        call -> call.getCalledUser().getId(),
                        call -> call,
                        (c1, c2) -> c1
                ));

        for (int i = 0; i < users.size(); i++) {

            var user = users.get(i);
            var userCall = callMap.get(user.getId());

            if (userCall != null) {
                boolean hasUnconfirmedLogs = userCall.getCallLogs().stream()
                        .anyMatch(callLog -> callLog.getStatus() == CallStatus.CONFIRMED || callLog.getType() == CallType.LOGISTIC);
                if (!hasUnconfirmedLogs) {
                    continue;
                }
            } else {
                continue;
            }

            table.addCell(createBadge(user, i + 1, "PARTICIPANTES", new DeviceRgb(0, 0, 0)));
        }

        for (int i = 0; i < visionary.size(); i++) {
            table.addCell(createBadge(visionary.get(i), i + 1, "VISIONARIO", new DeviceRgb(140, 0, 250)));
        }
        for (int i = 0; i < staff.size(); i++) {
            table.addCell(createBadge(staff.get(i), i + 1, "STAFF", new DeviceRgb(16, 153, 14)));
        }
        for (int i = 0; i < ml.size(); i++) {
            table.addCell(createBadge(ml.get(i), i + 1, "MASTER LIFE", new DeviceRgb(173, 95, 0)));
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }

    private Cell createBadge(User user, Integer number, String role, DeviceRgb roleColor) {

        // La celda EXTERIOR es invisible y tiene MARGIN para crear el espacio
        Cell outer = new Cell()
                .setHeight(190)
                .setBorder(Border.NO_BORDER);  // SIN BORDE en la celda outer

        // Tabla interna que contiene el contenido con borde
        Table card = new Table(1)
                .useAllAvailableWidth()
                .setBorder(new DashedBorder(0.5F))  // El borde va AQUÍ
                .setPadding(0);  // Padding interno del gafete

        // ===== LOGO =====
        Cell logoCell = new Cell()
                .setMinHeight(50)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        try {
            Image logo = new Image(
                    ImageDataFactory.create("classpath:static/images/focusYourLife.png"))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setWidth(60);
            logoCell.add(logo);
        } catch (Exception ignored) {
        }

        Cell lineCellTop = new Cell()
                .setHeight(4)
                .setPaddings(0F, 8F, 0F, 8F)
                .setBorder(Border.NO_BORDER);
        lineCellTop.add(new LineSeparator(new SolidLine(0.6f)));

        var name = user.getNickname() != null ? user.getNickname().toUpperCase() : user.getName1().toUpperCase();

        // ===== NOMBRE =====
        Cell nameCell = textCell(name, 46, true, 46, new DeviceRgb(0, 0, 0));

        // ===== APELLIDO =====
        Cell lastNameCell = textCell(user.getLastname1() + " " + user.getLastname2(), 10, false, 10, new DeviceRgb(0, 0, 0));

        // ===== NUMERO =====
        Cell numberCell = textCell("-- " + number + " --", 9, false, 9, new DeviceRgb(0, 0, 0));

        // ===== LINEA =====
        Cell lineCell = new Cell()
                .setHeight(4)
                .setPaddings(0F, 8F, 0F, 8F)
                .setBorder(Border.NO_BORDER);
        lineCell.add(new LineSeparator(new SolidLine(0.6f)));

        // ===== ROL =====
        Cell roleCell = textCell(role, 8, true, 8, roleColor);

        card.addCell(logoCell);
        card.addCell(lineCellTop);
        card.addCell(nameCell);
        card.addCell(lastNameCell);
        card.addCell(numberCell);
        card.addCell(lineCell);
        card.addCell(roleCell);

        outer.add(card);
        return outer;
    }

    private Cell textCell(String text, int fontSize, boolean bold, int height, DeviceRgb color) {

        Paragraph p = new Paragraph(text)
                .setFontSize(fontSize)
                .setFontColor(color)
                .setTextAlignment(TextAlignment.CENTER);

        if (bold) p.setBold();

        return new Cell()
                .setMinHeight(height)
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .add(p);
    }

    @Transactional
    public ByteArrayOutputStream getParticipantList(String id) throws IOException {

        var team = _teamRepository.findById(id)
                .orElseThrow(() -> new BaseException("Error", List.of("Equipo no encontrado")));

        Hibernate.initialize(team.getUsers());

        var calls = callsRepository.findAllByTrainingId(team.getTraining().getId());

        Map<String, Call> callMap = calls.stream()
                .collect(Collectors.toMap(
                        call -> call.getCalledUser().getId(),
                        call -> call,
                        (c1, c2) -> c1
                ));

        Workbook workbook = new XSSFWorkbook();
        Sheet trainingSheet = workbook.createSheet("Asistencia");

        var users = team.getUsers().stream()
                .sorted(
                        Comparator.comparing(
                                        (Participant participant) -> participant.getUser().getLastname1(),
                                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                                )
                                .thenComparing(
                                        participant -> participant.getUser().getLastname2(),
                                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                                )
                                .thenComparing(
                                        participant -> participant.getUser().getName(),
                                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                                )
                )
                .toList();

        var invitationTokens = users.stream()
                .map(Participant::getInvitationToken)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        var invitations = invitationRepository.findAllByTokenIn(invitationTokens);

        Map<String, Invitation> invitationMap;
        invitationMap = invitations.stream()
                .collect(Collectors.toMap(
                        Invitation::getToken,
                        Function.identity()
                ));

        var senderIds = invitations.stream()
                .map(Invitation::getSenderId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        var senders = userRepository.findAllById(senderIds);

        Map<String, User> senderMap = senders.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        Function.identity()
                ));

        Row headerRow = trainingSheet.createRow(0);

        headerRow.createCell(0).setCellValue("Nombre");
        headerRow.createCell(1).setCellValue("Nickname");
        headerRow.createCell(2).setCellValue("Telefono");
        headerRow.createCell(3).setCellValue("Enrolador");
        headerRow.createCell(4).setCellValue("Telefono Enrolador");
        headerRow.createCell(5).setCellValue("Firma");

        var index = 1;

        for (Participant user : users) {

            var userCall = callMap.get(user.getUser().getId());

            if (userCall != null) {

                boolean hasConfirmedLogs = userCall.getCallLogs().stream()
                        .anyMatch(callLog ->
                                callLog.getStatus() == CallStatus.CONFIRMED
                                        || callLog.getType() == CallType.LOGISTIC
                        );

                if (!hasConfirmedLogs) {
                    continue;
                }

            } else {
                continue;
            }

            Row row = trainingSheet.createRow(index);

            var inv = invitationMap.get(user.getInvitationToken());

            var sender = inv != null
                    ? senderMap.get(inv.getSenderId())
                    : null;

            String fullName = Stream.of(
                            user.getUser().getLastname1(),
                            user.getUser().getLastname2(),
                            user.getUser().getName(),
                            user.getUser().getName2()
                    )
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining(" "));

            row.createCell(0).setCellValue(fullName);

            row.createCell(1).setCellValue(
                    user.getUser().getNickname() != null
                            ? user.getUser().getNickname()
                            : ""
            );

            row.createCell(2).setCellValue(
                    user.getUser().getPhone() != null
                            ? user.getUser().getPhone()
                            : ""
            );

            row.createCell(3).setCellValue(
                    sender != null
                            ? sender.getName()
                            : inv != null && inv.getEnrolled() != null
                              ? inv.getEnrolled().getName()
                              : ""
            );

            row.createCell(4).setCellValue(
                    sender != null && sender.getPhone() != null
                            ? sender.getPhone()
                            : ""
            );

            index++;
        }

        for (int i = 0; i <= 5; i++) {
            trainingSheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        workbook.write(out);

        workbook.close();

        return out;
    }
}
