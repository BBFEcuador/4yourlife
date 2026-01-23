package com.foryourlife.clients.account.participant.domain;

import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.clients.account.contact.domain.Contact;
import com.foryourlife.clients.account.invitations.domain.Sender;
import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecord;
import com.foryourlife.shared.domain.user.User;

public class ParticipantContractData {
    private Participant participant;
    private Contact contact;
    private MedicalRecord medicalRecord;
    private Payment payment;
    private PaymentHistory paymentHistory;
    private User sender;
    private Sender trainingSender;

    public ParticipantContractData(Participant participant, Contact contact, MedicalRecord medicalRecord, Payment payment, PaymentHistory paymentHistory, User sender, Sender trainingSender) {
        this.participant = participant;
        this.contact = contact;
        this.medicalRecord = medicalRecord;
        this.payment = payment;
        this.paymentHistory = paymentHistory;
        this.sender = sender;
        this.trainingSender = trainingSender;
    }

    public ParticipantContractData() {

    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public PaymentHistory getPaymentHistory() {
        return paymentHistory;
    }

    public void setPaymentHistory(PaymentHistory paymentHistory) {
        this.paymentHistory = paymentHistory;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Sender getTrainingSender() {
        return trainingSender;
    }

    public void setTrainingSender(Sender trainingSender) {
        this.trainingSender = trainingSender;
    }
}
