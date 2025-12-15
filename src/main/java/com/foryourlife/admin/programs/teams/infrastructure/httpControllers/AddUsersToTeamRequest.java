package com.foryourlife.admin.programs.teams.infrastructure.httpControllers;

import java.util.List;

public class AddUsersToTeamRequest {
    List<String> userIds;
    List<String> masterLifeIds;
    List<String> staffIds;
    List<String> visitorIds;

    public List<String> getUserIds() {
        return userIds;
    }

    public List<String> getMasterLifeIds() {
        return masterLifeIds;
    }

    public List<String> getStaffIds() {
        return staffIds;
    }


    public List<String> getVisitorIds() {
        return visitorIds;
    }
}
