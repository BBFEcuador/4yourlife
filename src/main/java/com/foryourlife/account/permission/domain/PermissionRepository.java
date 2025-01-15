package com.foryourlife.account.permission.domain;

import java.util.List;

public interface PermissionRepository {
    void save(Permissions permissions);
    List<Permissions> getAll();
}
