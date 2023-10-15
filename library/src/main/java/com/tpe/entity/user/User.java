package com.tpe.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tpe.entity.business.Loan;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)

@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;//

    @Column(nullable = false)
    private String lastName;//

    @Column(nullable = false)
    private int score;//

    @Column(nullable = false)
    private String address;//

    @Column(unique = true,nullable = false)
    private String phone;//

    private LocalDate birthDate;//

    @Email
    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false) //TODO format yyyy-MM-dd HH:mm timezone
    private LocalDateTime createDate;

    private String resetPasswordCode; //TODO bu da password gibi hashlenecek

    @Column(nullable = false) //TODO default değeri false
    private Boolean builtIn;

    @ManyToMany//TODO cascade type veya bir preremoval gerekiyor mu?
    private Set<UserRole> userRoles;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Loan> loanList;





}
