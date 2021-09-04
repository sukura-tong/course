package com.swust.server.dto;


import java.util.List;

public class RoleUserDto {
    /**
    * id
    */
    private String id;
    /**
    * 角色id
    */
    private String roleId;
    /**
    * 用户id
    */
    private String userId;

    private List<String> usersIds;


    public List<String> getUsersIds() {
        return usersIds;
    }

    public void setUsersIds(List<String> usersIds) {
        this.usersIds = usersIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
            sb.append(", id=").append(id);
            sb.append(", roleId=").append(roleId);
            sb.append(", userId=").append(userId);
        sb.append("]");
        return sb.toString();
    }
}