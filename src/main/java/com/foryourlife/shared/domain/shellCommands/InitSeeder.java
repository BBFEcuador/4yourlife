package com.foryourlife.shared.domain.shellCommands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Service;

@ShellComponent
@Service
public class InitSeeder {
    private final ShellSriMethodsSeeder sriMethodsSeeder;
    private final ShellAdminSeeder adminSeeder;
    private final RolesAndPermissionSeederShell rolesAndPermissionsSeeder;

    public InitSeeder(ShellSriMethodsSeeder sriMethodsSeeder, ShellAdminSeeder adminSeeder, RolesAndPermissionSeederShell rolesAndPermissionsSeeder) {
        this.sriMethodsSeeder = sriMethodsSeeder;
        this.adminSeeder = adminSeeder;
        this.rolesAndPermissionsSeeder = rolesAndPermissionsSeeder;
    }

    @ShellMethod(key = "app:init-seed")
    public void seedAll() {
        rolesAndPermissionsSeeder.seedRoles();
        adminSeeder.seedAdmins();
        sriMethodsSeeder.seedSriMethods();
    }
}
