package com.foryourlife.account.role.application;

import com.foryourlife.account.role.domain.Role;
import com.foryourlife.account.role.domain.RoleRepository;
import com.foryourlife.account.user.application.CreateUser;
import com.foryourlife.shared.domain.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {
    private final RoleRepository repository;
    private final Logger logger = LoggerFactory.getLogger(CreateUser.class);

    public RolService(RoleRepository repository) {
        this.repository = repository;
    }

    public Role getInitRole() throws BaseException {
        Specification<Role> specification = (root, query, cb) -> cb.equal(root.get("isStarted").as(Boolean.class), true);
        return this.repository.findOneByCriteria(specification).orElseThrow(() ->
                new BaseException("AdminRole Problem", List.of("The initial role is not on database"))
        );
    }

    public Role getRoleById(String id) {
        return this.repository.findById(id).orElseThrow(() ->
                new BaseException("AdminRole Not found", List.of("The role with id "+id+" does not exist"))
        );
    }
}
