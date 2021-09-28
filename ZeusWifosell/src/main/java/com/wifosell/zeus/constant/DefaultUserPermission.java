package com.wifosell.zeus.constant;

import com.wifosell.zeus.model.permission.UserPermission;
import com.wifosell.zeus.model.role.RoleName;
import com.wifosell.zeus.model.user.User;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class DefaultUserPermission {
    public static List<UserPermission> getDefaultPermissionFromRole(RoleName role) {
        if (role.equals(RoleName.ROLE_GENERAL_MANAGER)) {
            return Arrays.asList(UserPermission.values());
        }
        return null;
    }
}
