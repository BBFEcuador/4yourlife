package com.foryourlife.admin.programs.teams.application;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.staff.domain.Staff;
import com.foryourlife.visionary.domain.Visionary;
import com.itextpdf.io.image.ImageDataFactory;
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
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class TeamBadgePdfService {
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
        var users = team.getUsers().stream().map(Participant::getUser).toList();
        var ml = team.getMasterLife().stream().map(MasterLife::getUser).toList();
        var visionary = team.getVisionaries().stream().map(Visionary::getUser).toList();
        var staff = team.getStaffs().stream().map(Staff::getUser).toList();

        for (int i = 0; i < users.size(); i++) {
            table.addCell(createBadge(users.get(i), i + 1, "PARTICIPANTES"));
        }
        for (int i = 0; i < visionary.size(); i++) {
            table.addCell(createBadge(visionary.get(i), i + 1, "VISIONARIOS"));
        }
        for (int i = 0; i < staff.size(); i++) {
            table.addCell(createBadge(staff.get(i), i + 1, "STAFF"));
        }
        for (int i = 0; i < ml.size(); i++) {
            table.addCell(createBadge(ml.get(i), i + 1, "MASTER LIFE"));
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }
    public byte[] generatePdf(List<User> users, String role) throws IOException {

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

        for (int i = 0; i < users.size(); i++) {
            table.addCell(createBadge(users.get(i), i + 1, role));
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }

    private Cell createBadge(User user, Integer number, String role) {

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
        } catch (Exception ignored) {}

        Cell lineCellTop = new Cell()
                .setHeight(4)
                .setPaddings(0F,8F,0F,8F)
                .setBorder(Border.NO_BORDER);
        lineCellTop.add(new LineSeparator(new SolidLine(0.6f)));

        // ===== NOMBRE =====
        Cell nameCell = textCell(user.getName1().toUpperCase(), 38, true, 38);

        // ===== APELLIDO =====
        Cell lastNameCell = textCell(user.getLastname1()+" "+user.getLastname2(), 12, false, 12);

        // ===== NUMERO =====
        Cell numberCell = textCell("-- " + number + " --", 9, false, 9);

        // ===== LINEA =====
        Cell lineCell = new Cell()
                .setHeight(4)
                .setPaddings(0F,8F,0F,8F)
                .setBorder(Border.NO_BORDER);
        lineCell.add(new LineSeparator(new SolidLine(0.6f)));

        // ===== ROL =====
        Cell roleCell = textCell("PARTICIPANTE", 14, true, 14);

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

    private Cell textCell(String text, int fontSize, boolean bold, int height) {

        Paragraph p = new Paragraph(text)
                .setFontSize(fontSize)
                .setTextAlignment(TextAlignment.CENTER);

        if (bold) p.setBold();

        return new Cell()
                .setMinHeight(height)
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .add(p);
    }
}
