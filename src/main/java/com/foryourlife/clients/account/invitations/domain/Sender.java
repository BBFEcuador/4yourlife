package com.foryourlife.clients.account.invitations.domain;

public class Sender {
    private String name;
    private String contact;

    protected Sender() {
    }

    public Sender(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}
