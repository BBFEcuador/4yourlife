package com.foryourlife.shared.domain.shellCommands;

import com.foryourlife.admin.auth.domain.AdminRole;
import com.foryourlife.admin.auth.domain.AdminRoleRepository;
import com.foryourlife.admin.permission.domain.Permission;
import com.foryourlife.admin.permission.domain.PermissionRepository;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
@Service
public class RolesAndPermissionSeederShell {
    private final AdminRoleRepository adminRoleRepository;
    private final PermissionRepository permissionRepository;

    @ShellMethod(key = "app:seed-permissions")
    public void seedRoles() {
        try {

            var permissions = seedPermissions();
            List<AdminRole> adminRoles = new ArrayList<>();

            adminRoles.add(new AdminRole("f4dddf05-8fec-4551-8d93-d6309c17c206", "Gerente", "ROLE_ADMIN", new ArrayList<>(permissions)));
            adminRoles.add(new AdminRole("a3b2c1d4-e5f6-7890-1234-56789abcdef0", "Directivo Financiero Administrativo", "ROLE_FINANCIAL_DIRECTIVE", List.of(
                            permissions.stream().filter(p -> "SEE_EMISSION_POINTS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_USERS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_CAMPUS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_PRODUCTS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_DISCOUNTS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_PAYMENTS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_PAYMENT_METHODS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_TRAINERS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "CREATE_TRAINERS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "UPDATE_TRAINERS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "DELETE_TRAINERS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_TRAININGS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "CREATE_TRAININGS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "UPDATE_TRAININGS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "DELETE_TRAININGS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_ATTENDANCES_DECLARATIONS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "CREATE_ATTENDANCES_DECLARATIONS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "UPDATE_ATTENDANCES_DECLARATIONS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "DELETE_ATTENDANCES_DECLARATIONS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_TEAMS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "CREATE_TEAMS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "UPDATE_TEAMS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "DELETE_TEAMS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_PARTICIPANTS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "CREATE_PARTICIPANTS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "UPDATE_PARTICIPANTS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "DELETE_PARTICIPANTS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_MASTER_LIFES".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "CREATE_MASTER_LIFES".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "UPDATE_MASTER_LIFES".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "DELETE_MASTER_LIFES".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_VISIONARIES".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "CREATE_VISIONARIES".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "UPDATE_VISIONARIES".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "DELETE_VISIONARIES".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_STAFF".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "CREATE_STAFF".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "UPDATE_STAFF".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "DELETE_STAFF".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_CONFIGURATIONS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "UPDATE_CONFIGURATIONS".equals(p.getName())).findAny().orElseThrow()
                    ))
            );
            adminRoles.add(new AdminRole("d2c3b4a5-6e7f-8901-2345-6789abcdef01", "Director Entrenamiento", "ROLE_FINANCIAL_ADMINISTRATIVE_DIRECTIVE", List.of(
                    permissions.stream().filter(p -> "SEE_EMISSION_POINTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_EMISSION_POINTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_EMISSION_POINTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "DELETE_EMISSION_POINTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_USERS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_USERS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_USERS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_CAMPUS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_CAMPUS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_PRODUCTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_PRODUCTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_PRODUCTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "DELETE_PRODUCTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_DISCOUNTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_DISCOUNTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_DISCOUNTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_PAYMENTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_PAYMENTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_PAYMENTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_PAYMENT_METHODS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_PAYMENT_METHODS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_PAYMENT_METHODS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_TRAINERS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_TRAINERS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_TRAINERS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "DELETE_TRAINERS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_TRAININGS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_TRAININGS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_TRAININGS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "DELETE_TRAININGS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_ATTENDANCES_DECLARATIONS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_ATTENDANCES_DECLARATIONS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_ATTENDANCES_DECLARATIONS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "DELETE_ATTENDANCES_DECLARATIONS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_TEAMS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_TEAMS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_TEAMS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "DELETE_TEAMS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_PARTICIPANTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_PARTICIPANTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_PARTICIPANTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "DELETE_PARTICIPANTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_MASTER_LIFES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_MASTER_LIFES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_MASTER_LIFES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "DELETE_MASTER_LIFES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_VISIONARIES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_VISIONARIES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_VISIONARIES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "DELETE_VISIONARIES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_STAFF".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_STAFF".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_STAFF".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "DELETE_STAFF".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_CONFIGURATIONS".equals(p.getName())).findAny().orElseThrow()
            )));
            adminRoles.add(new AdminRole("c1b2a3d4-e5f6-7890-1234-56789abcdef0", "Asistente Operativo", "ROLE_OPERATIVE_ASSISTANT", List.of(
                    permissions.stream().filter(p -> "SEE_EMISSION_POINTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_EMISSION_POINTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_EMISSION_POINTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_PRODUCTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_DISCOUNTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_PAYMENTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_PAYMENTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_PAYMENTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_PAYMENT_METHODS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_TRAINERS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_TRAININGS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_TEAMS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_PARTICIPANTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_PARTICIPANTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_MASTER_LIFES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_VISIONARIES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_STAFF".equals(p.getName())).findAny().orElseThrow()
            )));
            adminRoles.add(new AdminRole("b0a1b2c3-d4e5-6789-0123-456789abcdef", "Coordinación Life ", "ROLE_LIFE_COORDINATOR", List.of(
                    permissions.stream().filter(p -> "SEE_TRAINERS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_TRAINERS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_TRAINERS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_TRAININGS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_TRAININGS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_TRAININGS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_ATTENDANCES_DECLARATIONS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_ATTENDANCES_DECLARATIONS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_ATTENDANCES_DECLARATIONS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_TEAMS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_TEAMS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_TEAMS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_PARTICIPANTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_PARTICIPANTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_PARTICIPANTS".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_MASTER_LIFES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_MASTER_LIFES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_MASTER_LIFES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_VISIONARIES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_VISIONARIES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_VISIONARIES".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "SEE_STAFF".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "CREATE_STAFF".equals(p.getName())).findAny().orElseThrow(),
                    permissions.stream().filter(p -> "UPDATE_STAFF".equals(p.getName())).findAny().orElseThrow()
            )));
            adminRoles.add(
                    new AdminRole("a9b8c7d6-e5f4-3210-9876-54321fedcba0", "Asistente Administrativo", "ROLE_ADMINISTRATIVE_ASSISTANT", List.of(
                            permissions.stream().filter(p -> "SEE_EMISSION_POINTS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_USERS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_CAMPUS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_PRODUCTS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_DISCOUNTS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_PAYMENTS".equals(p.getName())).findAny().orElseThrow(),
                            permissions.stream().filter(p -> "SEE_PAYMENT_METHODS".equals(p.getName())).findAny().orElseThrow()
                    ))
            );
            adminRoleRepository.saveAll(adminRoles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RolesAndPermissionSeederShell(AdminRoleRepository adminRoleRepository, PermissionRepository permissionRepository) {
        this.adminRoleRepository = adminRoleRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> seedPermissions() {
        try {

            List<Permission> permissions = List.of(
                    new Permission("1c5f1550-d487-4f56-9c40-d26287c2491e", "SEE_EMISSION_POINTS", "Ver los puntos de venta", true),
                    new Permission("4c0b117e-6118-4eb1-87de-6d59cdb5190c", "CREATE_EMISSION_POINTS", "Crear los puntos de venta", true),
                    new Permission("9a7e3b2c-3f4d-4e5f-8a1b-2c3d4e5f6a7b", "UPDATE_EMISSION_POINTS", "Actualizar los puntos de venta", true),
                    new Permission("2d5f3d94-3001-43c7-8932-f77972848e8c", "DELETE_EMISSION_POINTS", "Eliminar los puntos de venta", true),
                    new Permission("7e3f4a2b-8c1d-4f5e-9f3a-1c2b3d4e5f6a", "SEE_USERS", "Ver los usuarios", true),
                    new Permission("bdad0348-db31-44ba-9909-80b37ed6d103", "CREATE_USERS", "Crear los usuarios", true),
                    new Permission("3b2c1d4e-5f6a-7b8c-9d0e-1f2a3b4d5e6f", "UPDATE_USERS", "Actualizar los usuarios", true),
                    new Permission("6a7b8c9d-0e1f-2a3b-4c5d-6e7f8a9b0c12", "DELETE_USERS", "Eliminar los usuarios", true),
                    new Permission("03af8c84-e458-4833-9dfa-02384dcc5415", "SEE_CAMPUS", "Ver las cedes", true),
                    new Permission("f1e2d3c4-b5a6-7980-1234-56789abcdef0", "CREATE_CAMPUS", "Crear las cedes", true),
                    new Permission("9f8e7d6c-5b4a-3210-9876-54321fedcba0", "UPDATE_CAMPUS", "Actualizar las cedes", true),
                    new Permission("a1b2c3d4-e5f6-7890-1234-56789abcdef0", "DELETE_CAMPUS", "Eliminar las cedes", true),
                    new Permission("d4c3b2a1-0f9e-8d7c-6b5a-4e3f2a1b0c9d", "SEE_PRODUCTS", "Ver productos", true),
                    new Permission("e5f6a7b8-c9d0-1e2f-3a4b-5c6d7e8f9a0b", "CREATE_PRODUCTS", "Crear productos", true),
                    new Permission("1a2b3c4d-5e6f-7890-1234-56789abcdef0", "UPDATE_PRODUCTS", "Actualizar productos", true),
                    new Permission("0f9e8d7c-6b5a-4e3f-2a1b-0c9d8e7f6a5b", "DELETE_PRODUCTS", "Eliminar productos", true),
                    new Permission("5e4d3c2b-1a0f-9e8d-7c6b-5a4e3f2a1b0c", "SEE_DISCOUNTS", "Ver descuentos", true),
                    new Permission("6b5a4e3f-2a1b-0c9d-8e7f-6a5b4c3d2e1f", "CREATE_DISCOUNTS", "Crear descuentos", true),
                    new Permission("9e8d7c6b-5a4e-3f2a-1b0c-9d8e7f6a5b4c", "UPDATE_DISCOUNTS", "Actualizar descuentos", true),
                    new Permission("8d7c6b5a-4e3f-2a1b-0c9d-8e7f6a5b4c3d", "DELETE_DISCOUNTS", "Eliminar descuentos", true),
                    new Permission("2a1b0c9d-8e7f-6a5b-4c3d-2b1a0f9e8d7c", "SEE_PAYMENTS", "Ver cobros", true),
                    new Permission("3f2a1b0c-9d8e-7f6a-5b4c-3d2e1f0a9b8c", "CREATE_PAYMENTS", "Crear cobros", true),
                    new Permission("0c9d8e7f-6a5b-4c3d-2b1a-0f9e8d7c6b5a", "UPDATE_PAYMENTS", "Actualizar cobros", true),
                    new Permission("7f6a5b4c-3d2e-1f0a-9b8c-7e6d5c4b3a2f", "DELETE_PAYMENTS", "Eliminar cobros", true),
                    new Permission("4c3d2b1a-0f9e-8d7c-6b5a-4e3f2a1b0c9d", "SEE_PAYMENT_METHODS", "Ver metodos de pago", true),
                    new Permission("5b4c3d2e-1f0a-9b8c-7e6d-5c4b3a2f1e0d", "CREATE_PAYMENT_METHODS", "Crear metodos de pago", true),
                    new Permission("6a5b4c3d-2e1f-0a9b-8c7e-6d5c4b3a2f1e", "UPDATE_PAYMENT_METHODS", "Actualizar metodos de pago", true),
                    new Permission("9d8e7f6a-5b4c-3d2e-1f0a-9b8c7e6d5c4b", "DELETE_PAYMENT_METHODS", "Eliminar metodos de pago", true),
                    new Permission("1f0a9b8c-7e6d-5c4b-3a2f-1e0d9c8b7a6f", "SEE_TRAINERS", "Ver entrenadores", true),
                    new Permission("2e1f0a9b-8c7e-6d5c-4b3a-2f1e0d9c8b7a", "CREATE_TRAINERS", "Crear entrenadores", true),
                    new Permission("3d2e1f0a-9b8c-7e6d-5c4b-3a2f1e0d9c8b", "UPDATE_TRAINERS", "Actualizar entrenadores", true),
                    new Permission("8c7e6d5c-4b3a-2f1e-0d9c-8b7a6f5e4d3c", "DELETE_TRAINERS", "Eliminar entrenadores", true),
                    new Permission("5c4b3a2f-1e0d-9c8b-7a6f-5e4d3c2b1a0f", "SEE_TRAININGS", "Ver entrenamientos", true),
                    new Permission("6d5c4b3a-2f1e-0d9c-8b7a-6f5e4d3c2b1a", "CREATE_TRAININGS", "Crear entrenamientos", true),
                    new Permission("7e6d5c4b-3a2f-1e0d-9c8b-7a6f5e4d3c2b", "UPDATE_TRAININGS", "Actualizar entrenamientos", true),
                    new Permission("1e4aa33f-4131-41b3-89eb-633b69c1d0df", "DELETE_TRAININGS", "Eliminar entrenamientos", true),
                    new Permission("d3c2b1a0-f9e8-d7c6-b5a4-e3f2a1b0c9d8", "SEE_ATTENDANCES_DECLARATIONS", "Ver declaraciones y asistencias", true),
                    new Permission("e4d3c2b1-a0f9-e8d7-c6b5-4e3f2a1b0c9d", "CREATE_ATTENDANCES_DECLARATIONS", "Crear declaraciones y asistencias", true),
                    new Permission("f5e4d3c2-b1a0-f9e8-d7c6-b5a4e3f2a1b0", "UPDATE_ATTENDANCES_DECLARATIONS", "Actualizar declaraciones y asistencias", true),
                    new Permission("0a9b8c7d-6e5f-4d3c-2b1a-0f9e8d7c6b5a", "DELETE_ATTENDANCES_DECLARATIONS", "Eliminar declaraciones y asistencias", true),
                    new Permission("b2a1b0c9-d8e7-f6a5-b4c3-d2e1f0a9b8c7", "SEE_TEAMS", "Ver equipos", true),
                    new Permission("c3b2a1b0-c9d8-e7f6-a5b4-c3d2e1f0a9b8", "CREATE_TEAMS", "Crear equipos", true),
                    new Permission("d4c3b2a1-b0c9-d8e7-f6a5-b4c3d2e1f0a9", "UPDATE_TEAMS", "Actualizar equipos", true),
                    new Permission("f0f3652b-0ac1-42c3-af44-386787be8155", "DELETE_TEAMS", "Eliminar equipos", true),
                    new Permission("e1f0a9b8-c7e6-d5c4-b3a2-f1e0d9c8b7a6", "SEE_PARTICIPANTS", "Ver participantes", true),
                    new Permission("f2e1f0a9-b8c7-e6d5-c4b3-a2f1e0d9c8b7", "CREATE_PARTICIPANTS", "Crear participantes", true),
                    new Permission("4bde52e8-f6c3-43ac-a721-7bf86ede3dcb", "UPDATE_PARTICIPANTS", "Actualizar participantes", true),
                    new Permission("ce488bf1-22b6-4b6b-9468-3e158af1f646", "DELETE_PARTICIPANTS", "Eliminar participantes", true),
                    new Permission("3c2b1a0f-9e8d-7c6b-5a4e-3f2a1b0c9d8e", "SEE_MASTER_LIFES", "Ver Master Life", true),
                    new Permission("4d3c2b1a-0f9e-8d7c-6b5a-4e3f2a1b0c9d", "CREATE_MASTER_LIFES", "Crear Master Life", true),
                    new Permission("ab22a682-5326-4b53-86ff-3c4e3d224bd7", "UPDATE_MASTER_LIFES", "Actualizar Master Life", true),
                    new Permission("6dab1779-ebb6-41fd-8aa3-bb7b1d483560", "DELETE_MASTER_LIFES", "Eliminar Master Life", true),
                    new Permission("8a7b6c5d-4e3f-2a1b-0c9d-8e7f6a5b4c3d", "SEE_VISIONARIES", "Ver Visionarios", true),
                    new Permission("9b8c7d6e-5f4e-3f2a-1b0c-9d8e7f6a5b4c", "CREATE_VISIONARIES", "Crear Visionarios", true),
                    new Permission("d9027358-3160-4c02-925e-ee4fa7e16f92", "UPDATE_VISIONARIES", "Actualizar Visionarios", true),
                    new Permission("1d0e1f2a-3b4c-5d6e-7f8a-9b0c1d2e3f4a", "DELETE_VISIONARIES", "Eliminar Visionarios", true),
                    new Permission("2e3f4a5b-6c7d-8e9f-0a1b-2c3d4e5f6a7b", "SEE_STAFF", "Ver Staffs", true),
                    new Permission("3f4a5b6c-7d8e-9f0a-1b2c-3d4e5f6a7b8c", "CREATE_STAFF", "Crear Staffs", true),
                    new Permission("4a5b6c7d-8e9f-0a1b-2c3d-4e5f6a7b8c9d", "UPDATE_STAFF", "Actualizar Staffs", true),
                    new Permission("5b6c7d8e-9f0a-1b2c-3d4e-5f6a7b8c9d0e", "DELETE_STAFF", "Eliminar Staffs", true),
                    new Permission("6c7d8e9f-0a1b-2c3d-4e5f-6a7b8c9d0e1f", "SEE_CONFIGURATIONS", "Ver configuraciones", true),
                    new Permission("8e9f0a1b-2c3d-4e5f-6a7b-8c9d0e1f2a3b", "UPDATE_CONFIGURATIONS", "Actualizar configuraciones", true)
            );

            for (Permission permission : permissions) {
                if (permissionRepository.findById(permission.getId()).isEmpty()) {
                    permissionRepository.save(permission);
                }
            }

            return permissions;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

}
