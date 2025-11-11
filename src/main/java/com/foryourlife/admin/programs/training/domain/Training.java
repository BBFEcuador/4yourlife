package com.foryourlife.admin.programs.training.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "training")
public class Training extends AggregateRoot implements Serializable {
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
    @OneToOne(
            cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST}
    )
    @JsonIncludeProperties({"originalTeam"})
    @JoinColumn(name = "nextLevel", referencedColumnName = "id")
    private Training nextLevel;
    @ManyToOne
    private Campus campus;
    private Boolean state;
    @JsonIgnoreProperties(value = {"training"})
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
        return new Training(
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

    public void setStartDate(StartDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(EndDate endDate) {
        this.endDate = endDate;
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
        var lifeG = new Training(
                UUID.randomUUID().toString(),
                this.number,
                computedLifeName(this.number,4),
                computedDate(this.startDate.getValue(), 15),
                computedDate(this.endDate.getValue(), 15),
                CourseLevel.LIFE_GRADUATE,
                null,
                campus,
                true
        );
        var life3 = new Training(
                UUID.randomUUID().toString(),
                this.number,
                computedLifeName(this.number,3),
                computedDate(this.startDate.getValue(), 13),
                computedDate(this.endDate.getValue(), 13),
                CourseLevel.LIFE_3,
                lifeG,
                campus,
                true
        );
        var life2 = new Training(
                UUID.randomUUID().toString(),
                this.number,
                computedLifeName(this.number,2),
                computedDate(this.startDate.getValue(), 8),
                computedDate(this.endDate.getValue(), 8),
                CourseLevel.LIFE_2,
                life3,
                campus,
                true
        );
        var life = new Training(
                UUID.randomUUID().toString(),
                this.number,
                computedLifeName(this.number,1),
                computedDate(this.startDate.getValue(), 3),
                computedDate(this.endDate.getValue(), 3),
                CourseLevel.LIFE,
                life2,
                campus,
                true
        );

        this.nextLevel = new Training(
                UUID.randomUUID().toString(),
                this.number,
                computedName(this.number),
                computedDate(this.startDate.getValue(), 2),
                computedDate(this.endDate.getValue(), 2),
                CourseLevel.YOUR,
                life,
                campus,
                true
        );
        trainingList.add(this);
        for (int i = 1; i < numberOfFocus; i++) {
            trainingList.add(this.geneNext(this.startDate.getValue().plusWeeks(i * 5L), i + this.number));
        }
        return trainingList;
    }

    private Training geneNext(LocalDate startDate, Integer nexFocusNumber) {
        var lifeG = new Training(
                UUID.randomUUID().toString(),
                nexFocusNumber,
                computedLifeName(nexFocusNumber,4),
                startDate.plusWeeks(15),
                startDate.plusWeeks(15).plusDays(2),
                CourseLevel.LIFE_GRADUATE,
                null,
                campus,
                true
        );
        var life3 = new Training(
                UUID.randomUUID().toString(),
                nexFocusNumber,
                computedLifeName(nexFocusNumber,3),
                startDate.plusWeeks(13),
                startDate.plusWeeks(13).plusDays(2),
                CourseLevel.LIFE_3,
                lifeG,
                campus,
                true
        );
        var life2 = new Training(
                UUID.randomUUID().toString(),
                nexFocusNumber,
                computedLifeName(nexFocusNumber,2),
                startDate.plusWeeks(8),
                startDate.plusWeeks(8).plusDays(2),
                CourseLevel.LIFE_2,
                life3,
                campus,
                true
        );
        var life = new Training(
                UUID.randomUUID().toString(),
                nexFocusNumber,
                computedLifeName(nexFocusNumber,1),
                startDate.plusWeeks(3),
                startDate.plusWeeks(3).plusDays(2),
                CourseLevel.LIFE,
                life2,
                campus,
                true
        );

        var your = new Training(
                UUID.randomUUID().toString(),
                nexFocusNumber,
                computedName(nexFocusNumber),
                startDate.plusWeeks(2),
                startDate.plusWeeks(2).plusDays(2),
                CourseLevel.YOUR,
                life,
                campus,
                true
        );

        return new Training(
                UUID.randomUUID().toString(),
                nexFocusNumber,
                computedName(nexFocusNumber),
                startDate,
                startDate.plusDays(2),
                CourseLevel.FOCUS,
                your,
                campus,
                true
        );
    }

    private String computedName(Integer number) {
        return this.campus.getCity() + "-" + number;
    }
    private String computedLifeName(Integer number,Integer fds) {
        return this.campus.getCity() + "-" + number + " FDS-"+fds;
    }

    private LocalDate computedDate(LocalDate date, Integer weeks) {
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
