package com.foryourlife.admin.permission.infrastructure.http;

import com.foryourlife.admin.permission.application.PermissionCommandService;
import com.foryourlife.admin.permission.application.PermissionQueryService;
import com.foryourlife.admin.permission.domain.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
@RequestMapping("permission")
public class PermissionController {
    @Autowired
    private PermissionCommandService permissionCommandService;

    @Autowired
    private PermissionQueryService permissionQueryService;

    @GetMapping("")
    public EntityResponse<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionQueryService.findAll();
        return EntityResponse.fromObject(permissions).build();
    }
}
