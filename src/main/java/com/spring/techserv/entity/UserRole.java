package com.spring.techserv.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_role")
public class UserRole {
    @Id
    @GeneratedValue
    private int id;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public enum RoleType {
        // может просматривать задачи, в которых является исполнителем
        // может просматривать задачи, в которых является исполнителем
        ROLE_USER,
        // может просматривать задачи, в которых является ответственным (автором)
        // может просматривать задачи, в которых является исполнителем
        ROLE_ADMIN, // может создавать задачи,
        ROLE_OPERATOR // может создавать задачи,
    }
}