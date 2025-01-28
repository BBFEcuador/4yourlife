package com.foryourlife.admin.programs.training.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.clients.account.invitations.domain.Sender;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "training")
public class Training extends AggregateRoot {
    @Id
    private String id;
    private Integer number;
    private String name;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "startDate"))
    private StartDate startDate;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "endDate"))
    private EndDate endDate;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private CourseLevel courseLevel;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "nextLevel", referencedColumnName = "id")
    private Training nextLevel;
    @ManyToOne
    private Campus campus;
    private Boolean state;
    @JsonIgnore
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Team originalTeam;

    protected Training() {
    }

    private Training(String id, Integer number, String name, LocalDate startDate, LocalDate endDate, CourseLevel courseLevel, Training nextLevel, Campus campus, Boolean state) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.startDate = new StartDate(startDate);
        this.endDate = new EndDate(endDate);
        this.courseLevel = courseLevel;
        this.nextLevel = nextLevel;
        this.campus = campus;
        this.state = state;
    }

    public static Training create(
            String id,
            Integer number,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            CourseLevel courseLevel,
            Training nextLevel,
            Campus campus,
            Boolean state
    ) {
        var training = new Training(
                id,
                number,
                name,
                startDate,
                endDate,
                courseLevel,
                nextLevel,
                campus,
                state
        );
        return training;
    }

    public String getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }


    public LocalDate getEndDate() {
        return endDate.getValue();
    }

    public LocalDate getStartDate() {
        return startDate.getValue();
    }

    public CourseLevel getCourseLevel() {
        return courseLevel;
    }

    public Training getNextLevel() {
        return nextLevel;
    }

    public Campus getCampus() {
        return campus;
    }

    public Boolean getState() {
        return state;
    }

    public List<Training> generateNextLevel(Integer numberOfFocus) {
        if (this.courseLevel != CourseLevel.FOCUS) {
            throw new BaseException("Level problem", List.of("Only focus"));
        }
        var trainingList = new ArrayList<Training>();
        var life = new Training(UUID.randomUUID().toString(), this.number, setName(this.number), setDates(this.startDate.getValue(), 4), setDates(this.endDate.getValue(), 4), CourseLevel.LIFE, null, campus, true);

        this.nextLevel = new Training(
                UUID.randomUUID().toString(),
                this.number,
                setName(this.number),
                setDates(this.startDate.getValue(), 3),
                setDates(this.endDate.getValue(), 3),
                CourseLevel.YOUR,
                life,
                campus,
                true
        );
        trainingList.add(this);
        for (int i = 1; i < numberOfFocus; i++) {
            trainingList.add(this.geneNext(this.startDate.getValue().plusWeeks(i * 4L), i + this.number));
        }
        return trainingList;
    }

    private Training geneNext(LocalDate startDate, Integer nexFocusNumber) {
        var life = new Training(
                UUID.randomUUID().toString(),
                nexFocusNumber,
                setName(nexFocusNumber),
                startDate.plusWeeks(4),
                startDate.plusWeeks(4).plusDays(2),
                CourseLevel.LIFE,
                null,
                campus,
                true
        );

        var your = new Training(
                UUID.randomUUID().toString(),
                nexFocusNumber,
                setName(nexFocusNumber),
                startDate.plusWeeks(3),
                startDate.plusWeeks(3).plusDays(2),
                CourseLevel.YOUR,
                life,
                campus,
                true
        );

        return new Training(
                UUID.randomUUID().toString(),
                nexFocusNumber,
                setName(nexFocusNumber),
                startDate,
                startDate.plusDays(2),
                CourseLevel.FOCUS,
                your,
                campus,
                true
        );
    }

    private String setName(Integer number) {
        return this.campus.getCity() + "-" + number;
    }

    private LocalDate setDates(LocalDate date, Integer weeks) {
        return date.plusWeeks(weeks);
    }

    public Team getOriginalTeam() {
        return originalTeam;
    }

    public void setOriginalTeam(Team originalTeam) {
        this.originalTeam = originalTeam;
    }

    @Override
    public String toString() {
        var s = "Training{" +
                "id='" + id + '\'' +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", courseLevel=" + courseLevel +
                ", nextLevel=" + nextLevel +
                ", campus=" + campus +
                ", state=" + state +
                ", originalTeam=" + originalTeam +
                '}';
        System.out.println(s);
        return s;
    }
}
