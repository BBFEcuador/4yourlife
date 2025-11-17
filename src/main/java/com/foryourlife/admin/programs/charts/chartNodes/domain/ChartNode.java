package com.foryourlife.admin.programs.charts.chartNodes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChart;
import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "chart_nodes")
public class ChartNode {
    @Id
    private String id;
    @Column(
        name = "parent_id",
        nullable = true
    )
    private String parentId;
    @Column(
        name = "parent_node_id",
        nullable = true
    )
    private String parentNodeId;
    @ManyToOne
    @JoinColumn(
        name = "member_id",
        referencedColumnName = "id"
    )
    private User members;
    private String level;
    @ManyToOne
    @JoinColumn(
        name = "organization_chart_id",
        referencedColumnName = "id"
    )
    @JsonIgnore
    private OrganizationChart organizationChart;


    public ChartNode(){

    }

    public ChartNode(String id, String parentId, String parentNodeId, User members, String level, OrganizationChart organizationChart) {
        this.id = id;
        this.parentId = parentId;
        this.parentNodeId = parentNodeId;
        this.members = members;
        this.level = level;
        this.organizationChart = organizationChart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(String parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public User getMembers() {
        return members;
    }

    public void setMembers(User members) {
        this.members = members;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public OrganizationChart getOrganizationChart() {
        return organizationChart;
    }

    public void setOrganizationChart(OrganizationChart organizationChart) {
        this.organizationChart = organizationChart;
    }
}
