package com.foryourlife.admin.sales.programs.domain;

import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "programs")
public class Program {
    @Id
    private String id;

    private String name;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private CourseLevel courseLevel;

    protected Program(){}

    private Program(String id, String name, CourseLevel courseLevel) {
        this.id = id;
        this.name = name;
        this.courseLevel = courseLevel;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CourseLevel getCourseLevel() {
        return courseLevel;
    }

    public static Program create(String id, String name, CourseLevel courseLevel) {
        return new Program(id, name, courseLevel);
    }
}
