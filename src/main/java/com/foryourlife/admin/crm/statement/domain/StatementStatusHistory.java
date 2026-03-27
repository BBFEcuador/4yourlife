package com.foryourlife.admin.crm.statement.domain;

import java.time.LocalDateTime;

public class StatementStatusHistory {
    private LocalDateTime changedAt;
    private StatementStatusEnum status = StatementStatusEnum.EMPTY;

    public StatementStatusHistory(LocalDateTime changedAt, StatementStatusEnum status) {
        this.changedAt = changedAt;
        this.status = status;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public StatementStatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatementStatusEnum status) {
        this.status = status;
    }
}
