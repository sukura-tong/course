package com.swust.server.dto;


import java.util.List;

public class RoleResourcesDto {
    /**
    * 角色id
    */
    private String roleId;
    /**
    * 资源id
    */
    private List<String> resourceIds;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RoleResourcesDto{");
        sb.append("roleId='").append(roleId).append('\'');
        sb.append(", resourceIds=").append(resourceIds);
        sb.append('}');
        return sb.toString();
    }
}