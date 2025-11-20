package com.foryourlife.admin.programs.charts.chartNodes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChart;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserType;
import jakarta.persistence.*;

import java.util.ArrayList;
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
    @OneToMany(
            mappedBy = "parentNodeId",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<ChartNode> children = new ArrayList<>();
    @ManyToOne
    @JoinColumn(
        name = "member_id",
        referencedColumnName = "id"
    )
    private User members;
    @Enumerated(EnumType.STRING)
    private UserType level;
    @ManyToOne
    @JoinColumn(
        name = "organization_chart_id",
        referencedColumnName = "id",
        nullable = false
    )
    @JsonIgnore
    private OrganizationChart organizationChart;
    @Column(
        name = "is_captain"
    )
    private Boolean isCaptain = false;


    public ChartNode(){

    }

    public ChartNode(String id, String parentId, String parentNodeId, List<ChartNode> children, User members, UserType level, OrganizationChart organizationChart, Boolean isCaptain) {
        this.id = id;
        this.parentId = parentId;
        this.parentNodeId = parentNodeId;
        this.children = children;
        this.members = members;
        this.level = level;
        this.organizationChart = organizationChart;
        this.isCaptain = isCaptain;
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

    public UserType getLevel() {
        return level;
    }

    public void setLevel(UserType level) {
        this.level = level;
    }

    public List<ChartNode> getChildren() {
        return children;
    }

    public OrganizationChart getOrganizationChart() {
        return organizationChart;
    }

    public void setOrganizationChart(OrganizationChart organizationChart) {
        this.organizationChart = organizationChart;
    }

    public void setChildren(List<ChartNode> children) {
        this.children = children;
    }

    public Boolean getCaptain() {
        return isCaptain;
    }

    public void setCaptain(Boolean captain) {
        isCaptain = captain;
    }
}
