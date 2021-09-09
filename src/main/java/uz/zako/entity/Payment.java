package uz.zako.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.zako.entity.superEntity.SuperEntity;

import javax.persistence.*;

@Entity

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Payment extends SuperEntity {

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private boolean income;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnore
    private User users;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

    @OneToOne
    private Payment replayPayment;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = true)
    private Boolean active;


}


