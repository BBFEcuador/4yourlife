package com.foryourlife.admin.programs.charts.organizationChart.application;

import com.foryourlife.admin.programs.charts.chartNodes.application.ChartNodeCommandService;
import com.foryourlife.admin.programs.charts.chartNodes.application.ChartNodeQueryService;
import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNode;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChart;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChartRepository;
import com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http.OrganizationalChartRequest;
import com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http.StaffNodeRequest;
import com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http.VisionaryNodeRequest;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.shared.domain.user.UserType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrganizationChartCommandService {
    private final OrganizationChartRepository organizationChartRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ChartNodeCommandService chartNodeCommandService;
    private final ChartNodeQueryService chartNodeQueryService;

    public OrganizationChartCommandService(OrganizationChartRepository organizationChartRepository, TeamRepository teamRepository, UserRepository userRepository, ChartNodeCommandService chartNodeCommandService, ChartNodeQueryService chartNodeQueryService) {
        this.organizationChartRepository = organizationChartRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.chartNodeCommandService = chartNodeCommandService;
        this.chartNodeQueryService = chartNodeQueryService;
    }

    @Transactional
    public void saveOrganizationChart(OrganizationalChartRequest request) {

        OrganizationChart organizationChart;

        if (request.getId() != null) {
            organizationChart = organizationChartRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Organigrama no encontrado"));

            organizationChart.getNodes().clear();

        } else {
            organizationChart = new OrganizationChart();
            organizationChart.setId(UUID.randomUUID().toString());

            Team team = teamRepository.findById(request.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

            if (organizationChartRepository.getOrganizationChartByTeamIdAndCourseLevel(
                    request.getTeamId(),
                    team.getTraining().getCourseLevel()
            ).isPresent()) {
                throw new RuntimeException("Ya existe un organigrama para ese equipo y nivel");
            }

            organizationChart.setTeam(team);
            organizationChart.setCourseLevel(team.getTraining().getCourseLevel());
        }

        List<ChartNode> allNodes = new ArrayList<>();
        var chartNodes = chartNodeQueryService.findAllByOrganizationId(organizationChart.getId());
        chartNodes.forEach(chartNode -> {
            chartNodeCommandService.deleteNode(chartNode.getId());
        });

        if (request.getVisionaries() != null) {
            for (var vReq : request.getVisionaries()) {
                ChartNode visionary = createNode(
                        vReq.getUserId(),
                        null,
                        UserType.VISIONARY,
                        organizationChart
                );
                allNodes.add(visionary);

                if (vReq.getStaff() != null) {
                    for (var sReq : vReq.getStaff()) {

                        ChartNode staff = createNode(
                                sReq.getUserId(),
                                visionary,
                                UserType.STAFF,
                                organizationChart
                        );
                        allNodes.add(staff);

                        if (sReq.getParticipantsIds() != null) {
                            for (String pId : sReq.getParticipantsIds()) {
                                ChartNode part = createNode(
                                        pId,
                                        staff,
                                        UserType.PARTICIPANT,
                                        organizationChart
                                );
                                allNodes.add(part);
                            }
                        }
                    }
                }
            }
        } else if (request.getStaff() != null) {
            for (var sReq : request.getStaff()) {

                ChartNode staff = createNode(
                        sReq.getUserId(),
                        null,
                        UserType.STAFF,
                        organizationChart
                );
                allNodes.add(staff);

                if (sReq.getParticipantsIds() != null) {
                    for (String pId : sReq.getParticipantsIds()) {
                        ChartNode part = createNode(
                                pId,
                                staff,
                                UserType.PARTICIPANT,
                                organizationChart
                        );
                        allNodes.add(part);
                    }
                }
            }
        }

        organizationChart.setNodes(allNodes);
        organizationChartRepository.save(organizationChart);
    }

    private ChartNode createNode(
            String userId,
            ChartNode parentNode,
            UserType level,
            OrganizationChart chart
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException("Usuario no encontrado: " + userId, List.of()));

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

    private void validateUserLevel(User user, UserType level) {

        String requiredEntity = switch (level) {
            case UserType.VISIONARY -> "VISIONARY";
            case UserType.STAFF -> "STAFF";
            case UserType.PARTICIPANT -> "PARTICIPANT";
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
