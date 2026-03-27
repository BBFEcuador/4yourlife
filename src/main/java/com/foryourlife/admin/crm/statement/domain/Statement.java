package com.foryourlife.admin.crm.statement.domain;

import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.level.CourseLevel;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.apache.xmpbox.type.TextType;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Table(name = "statements")
public class Statement {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(
            referencedColumnName = "id", name = "training_id"
    )
    private Training training;
    @ManyToOne
    @JoinColumn(
            referencedColumnName = "id", name = "participant_id"
    )
    private Participant participant;
    @Enumerated(EnumType.STRING)
    private StatementStatusEnum status = StatementStatusEnum.EMPTY;
    @Column(
            name = "course_level"

    )
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;
    private String comment;
    @Column(
            name = "statement_status_history",
            columnDefinition = "jsonb"
    )
    @Type(JsonType.class)
    private List<StatementStatusHistory> statementStatusHistory;

    protected Statement() {
    }

    public Statement(String id, Training training, Participant participant, StatementStatusEnum status, CourseLevel courseLevel, String comment) {
        this.id = id;
        this.training = training;
        this.participant = participant;
        this.status = status;
        this.courseLevel = courseLevel;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public StatementStatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatementStatusEnum status) {
        this.status = status;
    }

    public CourseLevel getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(CourseLevel courseLevel) {
        this.courseLevel = courseLevel;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<StatementStatusHistory> getStatementStatusHistory() {
        return statementStatusHistory;
    }

    public void setStatementStatusHistory(List<StatementStatusHistory> statementStatusHistory) {
        this.statementStatusHistory = statementStatusHistory;
    }
}
