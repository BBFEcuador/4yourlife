package com.foryourlife.account.role.domain;

import java.util.List;

public interface RoleRepository {
    void save(Role role);
    void saveAll(List<Role> roles);
    void deleteById(String id);
    List<Role> getAll();
}
