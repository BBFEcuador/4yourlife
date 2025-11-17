package com.foryourlife.admin.programs.charts.chartNodes.application;

import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNodeRepository;
import com.foryourlife.admin.programs.charts.chartNodes.infrastructure.http.ChartNodeRequest;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChartNodeCommandService {
    private final ChartNodeRepository repository;
    private final UserRepository userRepository;

    public ChartNodeCommandService(ChartNodeRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public void updateNode(ChartNodeRequest chartNodeRequest) {
        var chartNode = repository.findById(chartNodeRequest.getNodeId()).orElseThrow(
                () -> new BaseException(
                        "Chart Node not found",
                        List.of()
                )
        );
        var user = userRepository.findById(chartNodeRequest.getUserId()).orElseThrow(
                () -> new BaseException(
                        "User not found",
                        List.of()
                )
        );
        chartNode.setParentNodeId(chartNodeRequest.getParentNodeId());
        chartNode.setLevel(chartNodeRequest.getLevel());
        chartNode.setParentId(chartNodeRequest.getParentId());
        chartNode.setMembers(user);
        repository.updateNode(chartNode);
    }
}
