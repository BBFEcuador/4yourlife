package com.foryourlife.admin.training.program.domain;

import com.foryourlife.admin.training.campus.domain.Campus;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.relational.core.sql.In;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table()
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
    @OneToOne(cascade = {CascadeType.MERGE})
    private Training nextLevel;
    @OneToMany
    private Campus campus;
    private Boolean state;

    protected Training() {
    }

    private Training(String id, Integer number, String name, Date startDate, Date endDate, CourseLevel courseLevel, Training nextLevel, Campus campus, Boolean state) {
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
            Date startDate,
            Date endDate,
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


    public Date getEndDate() {
        return endDate.getValue();
    }

    public Date getStartDate() {
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

    public Training generateNextLevel() {
        if (this.courseLevel == CourseLevel.FOCUS) {
            var life = new Training(UUID.randomUUID().toString(), this.number, setName(this.number), setDates(this.startDate.getValue(), 3), setDates(this.endDate.getValue(), 3), CourseLevel.YOUR, null, campus, true);
            return new Training(
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
        } else {
            throw new BaseException("Level problem", List.of("Only focus"));
        }
    }

    private String setName(Integer number) {
        return this.campus.getCity() + "-" + number;
    }

    private Date setDates(Date date, Integer weeks) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.startDate.getValue());
        calendar.add(Calendar.WEEK_OF_YEAR, weeks);
        return calendar.getTime();
    }
}
