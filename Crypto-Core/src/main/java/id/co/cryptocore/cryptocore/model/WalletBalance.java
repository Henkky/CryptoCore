package id.co.cryptocore.cryptocore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import id.co.cryptocore.cryptocore.model.Serializer.CurrencySymbolSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "wallet_balance")
public class WalletBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "walletbalance_id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "wallet_id")
    @JsonBackReference
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "currency_id", referencedColumnName = "currency_id")
    @JsonManagedReference
    @JsonSerialize(contentUsing = CurrencySymbolSerializer.class)
    private Currency currency;

    @Column(name = "balance", nullable = false, precision = 20, scale = 10)
    private BigDecimal balance;

    public boolean isEnoughBalance(BigDecimal amount){
        //0 means balance=amount, 1 means balance>amount, -1 means balance<amount
        return this.balance.compareTo(amount) >= 0;
    }
}
