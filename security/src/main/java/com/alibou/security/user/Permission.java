package com.alibou.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"), // this is for get mapping
    ADMIN_UPDATE("admin:update"), // this is for put mapping
    ADMIN_CREATE("admin:create"),  // this is for post mapping
    ADMIN_DELETE("admin:delete"),
    MANAGER_READ("management:read"), // this is for get mapping
    MANAGER_UPDATE("management:update"), // this is for put mapping
    MANAGER_CREATE("management:create"),  // this is for post mapping
    MANAGER_DELETE("management:delete")

    ;

    @Getter
    private final String permission;
}
