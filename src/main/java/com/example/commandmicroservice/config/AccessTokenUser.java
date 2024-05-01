package com.example.commandmicroservice.config;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccessTokenUser {

    public static AccessTokenUser convert(SecurityContext context) {
        Jwt jwt = (Jwt) context.getAuthentication().getPrincipal();
        AccessTokenUser user = new AccessTokenUser();
        user.setId(jwt.getSubject());
        user.setUsername(jwt.getClaim("preferred_username"));
        user.setGroups(jwt.getClaimAsStringList("groups")); // Directly fetch groups as a List<String>
        user.setToken(jwt.getTokenValue());
        user.setAudiences(jwt.getAudience());
        user.setScopes(Arrays.stream(jwt.getClaim("scope").toString().split(" ")).toList());
        user.setTokenIssuedAt(jwt.getIssuedAt());
        user.setTokenExpiresAt(jwt.getExpiresAt());
        LinkedTreeMap<String, LinkedTreeMap<String, ArrayList<String>>> resourceAccess = jwt.getClaim("resource_access");
        user.setResourceAccessRoles(resourceAccess.get("journal").values().stream().toList().get(0));
        LinkedTreeMap<String, ArrayList<String>> realmAccess = jwt.getClaim("realm_access");
        user.setRealmRoles(realmAccess.get("roles"));
        // You can add roles retrieval if needed
        return user;
    }

    private String id;
    private List<String> groups;
    // You can add roles field and getter/setter if needed
    private String username;

    private String token;
    private Instant tokenIssuedAt;
    private Instant tokenExpiresAt;

    private List<String> scopes;

    private List<String> audiences;

    private List<String> resourceAccessRoles;
    private List<String> realmRoles;

    private AccessTokenUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getTokenIssuedAt() {
        return tokenIssuedAt;
    }

    public void setTokenIssuedAt(Instant tokenIssuedAt) {
        this.tokenIssuedAt = tokenIssuedAt;
    }

    public Instant getTokenExpiresAt() {
        return tokenExpiresAt;
    }

    public void setTokenExpiresAt(Instant tokenExpiresAt) {
        this.tokenExpiresAt = tokenExpiresAt;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public List<String> getAudiences() {
        return audiences;
    }

    public void setAudiences(List<String> audiences) {
        this.audiences = audiences;
    }

    public List<String> getResourceAccessRoles() {
        return resourceAccessRoles;
    }

    public void setResourceAccessRoles(List<String> resourceAccessRoles) {
        this.resourceAccessRoles = resourceAccessRoles;
    }

    public List<String> getRealmRoles() {
        return realmRoles;
    }

    public void setRealmRoles(List<String> realmRoles) {
        this.realmRoles = realmRoles;
    }

    @Override
    public String toString() {
        return "AccessTokenUser{" +
                "id='" + id + '\'' +
                ", groups=" + groups +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", tokenIssuedAt=" + tokenIssuedAt +
                ", tokenExpiresAt=" + tokenExpiresAt +
                ", scopes=" + scopes +
                ", audiences=" + audiences +
                ", resourceAccessRoles=" + resourceAccessRoles +
                ", realmRoles=" + realmRoles +
                '}';
    }
}
