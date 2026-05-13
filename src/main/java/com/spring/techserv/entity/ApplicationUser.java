package com.spring.techserv.entity;// class User{}
// org.springframework.security.core.userdetails.User

import jakarta.persistence.*;
import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "application_user")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApplicationUser {

    @Id
    @GeneratedValue
    @Column(name = "idUser", nullable = false, unique = true)
    private long id;

    @Column(name = "username")
    private String username;// login | email | phone

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @Enumerated(value = EnumType.STRING)
    private UserRole role;
}