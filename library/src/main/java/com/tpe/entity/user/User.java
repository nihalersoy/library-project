package com.tpe.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tpe.entity.business.Loan;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Date;
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

    @Column(nullable = false) //TODO between -2 - +2 default 0
    private int score;//

    @Column(nullable = false)
    private String address;//

    @Column(unique = true,nullable = false) //TODO format 999-999-9999
    private String phone;//

    private Date birthDate;//

    @Email
    @Column(unique = true,nullable = false) //TODO min:10 max:80
    private String email;//

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;//

    @Column(nullable = false) //TODO format yyyy-MM-dd HH:mm timezone
    private LocalDateTime createDate;//

    private String resetPasswordCode; //TODO bu da password gibi hashlenecek

    @Column(nullable = false) //TODO default değeri false
    private Boolean builtIn;//

    @OneToMany//TODO cascade type veya bir preremoval gerekiyor mu?
    private Set<UserRole> userRoles;

    //bu sınıf parent oluyo ve parent silindiğinde loan kayıtları da silinsin istiyorum
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Loan> loanList;




}
