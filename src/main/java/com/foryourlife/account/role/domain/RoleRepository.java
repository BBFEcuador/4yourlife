package com.foryourlife.account.role.domain;

import java.util.List;

public interface RoleRepository {
    void save(Role role);
    void deleteById(String id);
    List<Role> getAll();
}
