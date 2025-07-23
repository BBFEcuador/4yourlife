package com.foryourlife.clients.account.invitations.domain;

import java.time.LocalDate;

public class EnrolledUsers {
    private String userId;
    private String name;
    private LocalDate enrolledDate;

    public EnrolledUsers() {
    }

    public EnrolledUsers(String userId, LocalDate enrolledDate, String name) {
        this.userId = userId;
        this.enrolledDate = enrolledDate;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getEnrolledDate() {
        return enrolledDate;
    }

    public String getName() {
        return name;
    }
}
