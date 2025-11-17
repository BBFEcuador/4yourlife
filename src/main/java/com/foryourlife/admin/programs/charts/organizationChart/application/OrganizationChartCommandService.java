package com.foryourlife.admin.programs.charts.organizationChart.application;

import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNode;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChart;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChartRepository;
import com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http.OrganizationalChartRequest;
import com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http.StaffNodeRequest;
import com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http.VisionaryNodeRequest;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        if (organizationChartRepository.getOrganizationChartByTeamIdAndCourseLevel(
                request.getTeamId(), team.getTraining().getCourseLevel()).isPresent()) {

            throw new RuntimeException("Ya existe un organigrama para el equipo "+ team.getName() + " en el nivel " + team.getTraining().getCourseLevel());
        }

        organizationChart.setTeam(team);

        List<ChartNode> allNodes = new ArrayList<>();

        if (request.getVisionaries() != null && !request.getVisionaries().isEmpty()) {
            for (VisionaryNodeRequest vReq : request.getVisionaries()) {

                ChartNode visionaryNode = createNode(
                        vReq.getUserId(),
                        null,
                        "VISIONARY",
                        organizationChart
                );
                allNodes.add(visionaryNode);

                if (vReq.getStaff() != null) {
                    for (StaffNodeRequest sReq : vReq.getStaff()) {

                        ChartNode staffNode = createNode(
                                sReq.getUserId(),
                                visionaryNode,
                                "STAFF",
                                organizationChart
                        );
                        allNodes.add(staffNode);

                        if (sReq.getParticipantsIds() != null) {
                            for (String pId : sReq.getParticipantsIds()) {

                                ChartNode participantNode = createNode(
                                        pId,
                                        staffNode,
                                        "PARTICIPANT",
                                        organizationChart
                                );
                                allNodes.add(participantNode);
                            }
                        }
                    }
                }
            }
        } else if (request.getStaff() != null && !request.getStaff().isEmpty()) {

            for (StaffNodeRequest sReq : request.getStaff()) {

                ChartNode staffNode = createNode(
                        sReq.getUserId(),
                        null,
                        "STAFF",
                        organizationChart
                );
                allNodes.add(staffNode);

                if (sReq.getParticipantsIds() != null) {
                    for (String pId : sReq.getParticipantsIds()) {

                        ChartNode participantNode = createNode(
                                pId,
                                staffNode,
                                "PARTICIPANT",
                                organizationChart
                        );
                        allNodes.add(participantNode);
                    }
                }
            }
        }

        organizationChart.setNodes(allNodes);
        organizationChart.setCourseLevel(team.getTraining().getCourseLevel());
        organizationChartRepository.save(organizationChart);
    }


    private ChartNode createNode(
            String userId,
            ChartNode parentNode,
            String level,
            OrganizationChart chart
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userId));

        validateUserLevel(user, level);

        ChartNode node = new ChartNode();
        node.setId(UUID.randomUUID().toString());
        node.setMembers(user);
        node.setLevel(level);
        node.setOrganizationChart(chart);

        if (parentNode != null) {
            node.setParentNodeId(parentNode.getId());
            node.setParentId(parentNode.getMembers().getId());
        } else {
            node.setParentNodeId(null);
            node.setParentId(null);
        }

        return node;
    }

    private void validateUserLevel(User user, String level) {

        String requiredEntity = switch (level) {
            case "VISIONARY" -> "VISIONARY";
            case "STAFF" -> "STAFF";
            case "PARTICIPANT" -> "PARTICIPANT";
            default -> throw new RuntimeException("Nivel inválido: " + level);
        };

        boolean hasEntity = user.getEntityMap().stream()
                .anyMatch(e -> e.getEntity().equals(requiredEntity));

        if (!hasEntity) {
            throw new RuntimeException(
                    "El usuario " + user.getName() +
                            " no tiene el rol requerido: " + requiredEntity
            );
        }
    }

    public void deleteOrganizationChartById(String id) {
        organizationChartRepository.deleteOrganizationChartById(id);
    }

}
