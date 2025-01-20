package com.foryourlife.account.role.seeders;

import com.foryourlife.account.permission.domain.PermissionRepository;
import com.foryourlife.account.permission.domain.Permissions;
import com.foryourlife.account.role.domain.Role;
import com.foryourlife.account.role.domain.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Configuration
public class RoleSeeders {

    private final RoleRepository _roleRepository;
    private final PermissionRepository _permissionRepository;

    public RoleSeeders(RoleRepository _roleRepository, PermissionRepository _permissionRepository) {
        this._roleRepository = _roleRepository;
        this._permissionRepository = _permissionRepository;
    }

    @Bean
    CommandLineRunner initRoles(){
        return args ->{
            List<Permissions> permissionsList = Arrays.asList(
                    Permissions.create("4672150a-a2ba-472c-b818-c2484838a952","Epic Permission"),
                    Permissions.create("85fd81cf-c183-4bcf-8746-b8bdbaf8a9cd","Low Permission"));
            this._permissionRepository.saveAll(permissionsList);
            Set<Permissions> permissions = Set.copyOf(permissionsList);
            List<Role> roles = Arrays.asList(
                    Role.create("55c3da1c-b516-4a55-9fdd-21317ee6e4c0","ROLE_INIT",permissions,true),
                    Role.create("3024c8f1-d603-47fc-8369-0e90cd2e703e","ROLE_FOCUS",permissions,false),
                    Role.create("3024c8f1-d603-47fc-8369-0e90cd2e703e","ROLE_YOUR",permissions,false),
                    Role.create("3024c8f1-d603-47fc-8369-0e90cd2e703e","ROLE_LIFE",permissions,false)
            );
            this._roleRepository.saveAll(roles);
        };
    }
}
