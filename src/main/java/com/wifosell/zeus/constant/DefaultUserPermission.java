package com.wifosell.zeus.constant;

import com.wifosell.zeus.model.permission.UserPermission;
import com.wifosell.zeus.model.role.RoleName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DefaultUserPermission {
    public static List<UserPermission> getDefaultPermissionFromRole(RoleName role) {
        if (role.equals(RoleName.ROLE_GENERAL_MANAGER)) {
            return Arrays.asList(UserPermission.values());
        }
        return new ArrayList<>();
    }
}
