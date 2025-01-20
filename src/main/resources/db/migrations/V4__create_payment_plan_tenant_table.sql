CREATE TABLE IF NOT EXISTS plan
(
        id text not null,
        image text not null,
        name text not null,
        type text not null,
        vouchers text not null,
        userOnline text not null,
        inventory boolean not null,
        isStarted boolean not null,
        pos boolean not null,
        price decimal not null,
        CONSTRAINT pk_plan PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS tenant
(
        id text not null,
        document text not null,
        ownerId text not null,
        isActive boolean not null,
        CONSTRAINT pk_tenant PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS payment
(
        id text not null,
        lastDigits text not null,
        clientTransactionId text not null,
        transactionId text not null,
        phoneNumber text not null,
        email text not null,
        cardType text not null,
        transactionStatus text not null,
        authorizationCode text not null,
        amount decimal not null,
        paymentDate date not null,
        planId text not null,
        tenantId text not null,
        CONSTRAINT fk_payment_on_plan FOREIGN KEY (planId) REFERENCES plan (id),
        CONSTRAINT fk_payment_on_tenant FOREIGN KEY (tenantId) REFERENCES tenant (id),
        CONSTRAINT pk_payment PRIMARY KEY (id)
);