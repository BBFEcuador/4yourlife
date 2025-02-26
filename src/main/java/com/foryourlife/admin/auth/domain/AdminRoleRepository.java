package com.foryourlife.admin.auth.domain;

import java.util.List;
import java.util.Optional;

public interface AdminRoleRepository
{
    public Optional<AdminRole> findById(String id);
    public List<AdminRole> getAll();
    public void saveAll(List<AdminRole> roles);
}
