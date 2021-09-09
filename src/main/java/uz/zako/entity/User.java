package uz.zako.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import uz.zako.entity.superEntity.SuperEntity;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data

public class User extends SuperEntity {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;


    @OneToMany(mappedBy = "users")
    @JsonIgnore
    private List<Payment> payments;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @OneToMany(mappedBy = "users")
    @JsonIgnore
    private List<Category> categories;


    @Column(nullable = true)
    private Boolean active;


}
