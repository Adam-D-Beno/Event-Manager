package org.das.event_manager.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.das.event_manager.domain.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Setter
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login",unique = true, nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String passwordHash;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    private List<UserRegistration> userRegistrations = new ArrayList<>();

    public UserEntity() {
    }

    public UserEntity(
            Long id,
            String login,
            String passwordHash,
            Integer age,
            UserRole userRole
    ) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.age = age;
        this.userRole = userRole;
    }

    public UserEntity(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;
        return Objects.equals(login, that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(login);
    }
}
