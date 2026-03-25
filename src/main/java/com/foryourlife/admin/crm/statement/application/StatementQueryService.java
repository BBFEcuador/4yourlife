package com.foryourlife.admin.crm.statement.application;

import com.foryourlife.admin.crm.statement.domain.Statement;
import com.foryourlife.admin.crm.statement.domain.StatementDTO;
import com.foryourlife.admin.crm.statement.domain.StatementRepository;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChart;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChartRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementQueryService {
    private final StatementRepository statementRepository;
    private final OrganizationChartRepository organizationChartRepository;

    public StatementQueryService(StatementRepository statementRepository, OrganizationChartRepository organizationChartRepository) {
        this.statementRepository = statementRepository;
        this.organizationChartRepository = organizationChartRepository;
    }

    public Page<StatementDTO> findAll(Pageable pageable, Criteria criteria, String trainingId) {
        Page<Statement> statementsPage = statementRepository.findAll(pageable, criteria);
        criteria.filters.add(new Filter("id", trainingId, "training", Filter.Operation.EQUAL, Filter.LogicalOperator.AND));

        var organizationChart = organizationChartRepository.getOrganizationChartTrainingId(trainingId).orElseThrow(
                () -> new BaseException("No se encontro un organigrama para el entrenamiento", List.of(""))
        );

        return statementsPage.map(statement -> toDTO(statement, organizationChart));    }

    public List<Statement> findAllByTrainingId(String trainingId) {
        return statementRepository.findByTrainingId(trainingId);
    }

    public Statement findById(String id) {
        return statementRepository.findById(id).orElse(null);
    }

    private StatementDTO toDTO(Statement statement, OrganizationChart organizationChart) {
        String nickname = statement.getParticipant().getUser().getNickname();
        return null;
//                new StatementDTO(
//        );
    }
}
