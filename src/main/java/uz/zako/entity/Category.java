package uz.zako.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.zako.entity.superEntity.SuperEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category extends SuperEntity {

    @JoinColumn(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JsonIgnore
    private User users;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Payment> payments;

    @Column(nullable = true)
    private Boolean active;

}

