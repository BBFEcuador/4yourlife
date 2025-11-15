package com.foryourlife.admin.programs.charts.organizationChart.application;

import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNode;
import com.foryourlife.admin.programs.charts.chartNodes.infrastructure.http.ChartNodeRequest;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChart;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChartRepository;
import com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http.OrganizationalChartRequest;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrganizationChartCommandService {
    private final OrganizationChartRepository organizationChartRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public OrganizationChartCommandService(OrganizationChartRepository organizationChartRepository, TeamRepository teamRepository, UserRepository userRepository) {
        this.organizationChartRepository = organizationChartRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public void saveOrganizationChart(OrganizationalChartRequest request) {

        OrganizationChart organizationChart = new OrganizationChart();
        organizationChart.setId(UUID.randomUUID().toString());

        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        organizationChart.setTeam(team);

        validateUniqueParents(request.getChartNodeRequests());

        List<ChartNode> nodes = request.getChartNodeRequests().stream()
                .map(nodeRequest -> createValidatedNode(nodeRequest, organizationChart))
                .collect(Collectors.toList());

        organizationChart.setNodes(nodes);

        organizationChartRepository.save(organizationChart);
    }

    private void validateUniqueParents(List<ChartNodeRequest> requests) {
        Set<String> parentIds = new HashSet<>();

        for (ChartNodeRequest req : requests) {
            if (req.getOrganizationId() != null) {
                if (!parentIds.add(req.getOrganizationId())) {
                    throw new RuntimeException("El padre " + req.getOrganizationId() + " está repetido.");
                }
            }
        }
    }

    private ChartNode createValidatedNode(ChartNodeRequest request, OrganizationChart organizationChart) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        validateUserLevel(request, user);

        ChartNode node = new ChartNode();
        node.setId(UUID.randomUUID().toString());
        node.setParentNodeId(request.getOrganizationId());
        node.setLevel(request.getLevel());
        node.setMembers(user);
        node.setOrganizationChart(organizationChart);

        return node;
    }

    private void validateUserLevel(ChartNodeRequest req, User user) {

        int level = Integer.parseInt(req.getLevel());

        String requiredEntity = switch (level) {
            case 1 -> "VISIONARY";
            case 2 -> "STAFF";
            case 3 -> "PARTICIPANT";
            default -> throw new RuntimeException("Nivel inválido: " + req.getLevel());
        };

        boolean hasEntity = user.getEntityMap().stream()
                .anyMatch(e -> e.getEntity().equals(requiredEntity));

        if (!hasEntity) {
            throw new RuntimeException(
                    "El usuario " + user.getName() + " NO tiene el rol requerido: " + requiredEntity
            );
        }
    }

}
