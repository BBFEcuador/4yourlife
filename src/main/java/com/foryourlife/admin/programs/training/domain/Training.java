package com.foryourlife.admin.programs.training.domain;

import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @JoinColumn(name = "nextLevel",referencedColumnName = "id")
    private Training nextLevel;
    @ManyToOne
    private Campus campus;
    private Boolean state;

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
        return List.of(this);
    }

    private String setName(Integer number) {
        return this.campus.getCity() + "-" + number;
    }

    private LocalDate setDates(LocalDate date, Integer weeks) {
        return date.plusWeeks(weeks);
    }
}
