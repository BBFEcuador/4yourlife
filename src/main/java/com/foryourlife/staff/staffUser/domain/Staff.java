package com.foryourlife.staff.staffUser.domain;

import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.user.domain.Participant;
import jakarta.persistence.*;
import org.springframework.security.core.userdetails.User;

@Entity
@Table(name = "staffs")
public class Staff {
    @Id
    private String id;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              @ManyToOne
    @JoinColumn(name = "training_id", referencedColumnName = "id")
    private Training trainingId;
    @OneToOne
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    public Participant participant;
}
