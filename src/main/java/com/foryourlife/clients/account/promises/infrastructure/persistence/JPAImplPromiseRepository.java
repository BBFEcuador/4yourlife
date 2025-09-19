package com.foryourlife.clients.account.promises.infrastructure.persistence;

import com.foryourlife.clients.account.promises.domain.Promise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JPAImplPromiseRepository extends JpaRepository<Promise, String>, JpaSpecificationExecutor<Promise> {
}
