package com.foryourlife.clients.account.participant.domain;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ParticipantRepository {
    LoginResponse login(String username, String password) throws BaseException;

    Optional<Participant> findByEmail(String email);

    Optional<Participant> findById(String id);

    List<Participant> getAll();
    Page<Participant> getAll(Pageable pageable,Criteria criteria);

    List<Participant> match(Criteria criteria);

    void save(Participant user);
    void saveAllAndFlush(List<Participant> users);

    Optional<Participant> findByUserId(String userId);

    List<Participant> findAllByUserIds(List<String> userIds);

    String getContract(Training training,Product product, Participant participant) throws IOException;

    String getContractByTeam(Training training,Product product, List<Participant> participants) throws IOException;

    List<Participant> saveAll(List<Participant> participants);
}
