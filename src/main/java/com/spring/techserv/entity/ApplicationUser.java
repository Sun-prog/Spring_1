package com.spring.techserv.entity;// class User{}
// org.springframework.security.core.userdetails.User

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "application_user")
public class ApplicationUser {

    @Id
    @GeneratedValue
    private long id;

    private String username;// login | email | phone

    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private UserRole userRole;
}