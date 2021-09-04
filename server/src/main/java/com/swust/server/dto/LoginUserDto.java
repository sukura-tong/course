package com.swust.server.dto;


import java.util.HashSet;
import java.util.List;

public class LoginUserDto {
    /**
    * id
    */
    private String id;
    /**
    * 登录名
    */
    private String loginName;
    /**
    * 昵称
    */
    private String name;

    /**
     * 登陆表示
     */
    private String token;

    /**
     * 所有资源、用于前端界面控制
     */
    private List<ResourcesDto> resources;

    /**
     * 所有资源中的请求，用于后端接口拦截
     */
    private HashSet<String> requests;

    public HashSet<String> getRequests() {
        return requests;
    }

    public void setRequests(HashSet<String> requests) {
        this.requests = requests;
    }

    public List<ResourcesDto> getResources() {
        return resources;
    }

    public void setResources(List<ResourcesDto> resources) {
        this.resources = resources;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getName() {
        return name;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
            sb.append(", id=").append(id);
            sb.append(", loginName=").append(loginName);
            sb.append(", name=").append(name);
        sb.append("]");
        return sb.toString();
    }
}