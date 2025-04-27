package org.das.event_manager.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.das.event_manager.domain.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
