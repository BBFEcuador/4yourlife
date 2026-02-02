package com.foryourlife.clients.account.participantLevel.domain;

import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;

@Entity
@Table(name = "participant_level")
public class ParticipantLevel implements Serializable {

    @Id
    private String id;
    @Column(name = "roleName")
    private String roleName;
    private Boolean isStarted;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private CourseLevel courseLevel;

    protected ParticipantLevel() {

    }

    private ParticipantLevel(String id, String roleName, Boolean isStarted,CourseLevel courseLevel) {
        this.id = id;
        this.roleName = roleName;
        this.isStarted = isStarted;
        this.courseLevel = courseLevel;
    }

    public String getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public CourseLevel getCourseLevel() {
        return courseLevel;
    }

    public static ParticipantLevel create(String id, String roleName, Boolean isStarted, CourseLevel courseLevel) {
        return new ParticipantLevel(id, roleName, isStarted,courseLevel);
    }
}
