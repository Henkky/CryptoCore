package id.co.cryptocore.cryptocore.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "userid", nullable = false, length = 40)
    private String id;

    @Column(name = "name", nullable = false, length = 40)
    private String name;

    @Column(name = "email", length = 40)
    private String email;

    @Column(name = "password", length = 80)
    private String password;

    @Column(name = "role", length = 40)
    private String role;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Wallet wallet;
}