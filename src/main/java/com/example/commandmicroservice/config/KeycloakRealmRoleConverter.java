package com.example.commandmicroservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class made to convert access token from keycloak and get the roles
 * (roles for a start might be more later)
 */
public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    /**
     * Denna metod är den som extraherar och konvertera access tokenen till roller som används inom metoden HttpsSecurity
     * @param jwt jwt version av access token
     * @return returnerar lista av alla Authoritys användaren har
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        final Map<String, Object> resourceAccess = ((Map<String, Object>) jwt.getClaims().get("resource_access"));
        final Map<String, Object> journalResourceAccess = (Map<String, Object>) resourceAccess.get("journal");
        final List<String> roles = (List<String>) journalResourceAccess.get("roles");
        return (roles).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
