package com.foryourlife.admin.crm.statement.domain;

import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.participant.domain.Participant;
import jakarta.persistence.*;

@Entity
@Table
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
    private StatementStatusEnum status;

}
