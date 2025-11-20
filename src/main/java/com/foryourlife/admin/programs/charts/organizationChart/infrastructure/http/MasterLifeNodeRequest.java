package com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http;

import java.util.List;

public class MasterLifeNodeRequest {
    public String userId;
    public List<String> participantsIds;

    public String getUserId() {
        return userId;
    }

    public List<String> getParticipantsIds() {
        return participantsIds;
    }
}
