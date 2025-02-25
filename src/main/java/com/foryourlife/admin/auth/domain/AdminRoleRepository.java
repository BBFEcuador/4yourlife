package com.foryourlife.admin.auth.domain;

import java.util.List;

public interface AdminRoleRepository
{
    public AdminRole findById(String id);
    public List<AdminRole> getAll();
    public void saveAll(List<AdminRole> roles);
}
