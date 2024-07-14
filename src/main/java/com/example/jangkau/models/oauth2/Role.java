package com.example.jangkau.models.oauth2;

import com.example.jangkau.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "oauth_role",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "role_name_and_type",
                        columnNames = {"type", "name"}
                )
        }
)
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;

    private String type;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RolePath> rolePaths;

    @JsonIgnore
    @ManyToMany(targetEntity = User.class, mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

    @Override
    public String getAuthority() {
        return this.name;
    }
}

