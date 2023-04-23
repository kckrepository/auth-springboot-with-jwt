package org.example.security.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(	name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone", unique=true, length = 15)
    private String phone;

    @Column(name = "name", length = 70)
    private String name;

    @Column(name = "password", length = 120)
    private String password;

    @Column(name = "role", length = 10)
    private String role = "ROLE_USER";
}
