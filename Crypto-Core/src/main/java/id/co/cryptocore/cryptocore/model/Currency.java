package id.co.cryptocore.cryptocore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "symbol", nullable = false, unique = true, length = 3)
    private String symbol;

    @Column(name = "rate", nullable = false, precision = 15, scale = 10)
    private BigDecimal rate;

    @OneToMany(mappedBy = "currency")
    @JsonBackReference
    private List<WalletBalance> walletBalances = new ArrayList<>();
}