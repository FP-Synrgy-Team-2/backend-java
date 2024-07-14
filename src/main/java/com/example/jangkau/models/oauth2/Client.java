package com.example.jangkau.models.oauth2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "oauth_client")
public class Client implements ClientDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;

    private String clientSecret;

    private String scopes;

    private String grantTypes;

    private String redirectUris;

    private boolean approved;

    @Column(name = "access_token_expired")
    private Integer accessTokenValiditySeconds;

    @Column(name = "refresh_token_expired")
    private Integer refreshTokenValiditySeconds;

    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "oauth_client_role",
            joinColumns = {
                    @JoinColumn(name = "client_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id")
            }
    )
    private Set<GrantedAuthority> authorities = new HashSet<>();

    @Override
    public Set<String> getResourceIds() {
        return Collections.singleton("oauth2-resource");
    }

    @Override
    public boolean isSecretRequired() {
        return !Objects.isNull(clientSecret) && !clientSecret.isEmpty();
    }

    @Override
    public boolean isScoped() {
        return !Objects.isNull(scopes) && !scopes.isEmpty();
    }

    @Override
    public Set<String> getScope() {
        if (isScoped()) {
            return new HashSet<>(Arrays.asList(scopes.split("\\s")));
        }
        return Collections.emptySet();
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        if (!Objects.isNull(grantTypes)) {
            return new HashSet<>(Arrays.asList(grantTypes.split("\\s")));
        }
        return Collections.emptySet();
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        if (!Objects.isNull(redirectUris)) {
            return new HashSet<>(Arrays.asList(redirectUris.split("\\s")));
        }
        return Collections.emptySet();
    }

    @Override
    public boolean isAutoApprove(String s) {
        return approved;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return new HashMap<>();
    }
}
