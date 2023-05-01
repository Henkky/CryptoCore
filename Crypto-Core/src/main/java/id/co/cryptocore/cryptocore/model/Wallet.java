package id.co.cryptocore.cryptocore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import id.co.cryptocore.cryptocore.model.Serializer.CurrencySymbolSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id", nullable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "userid",referencedColumnName = "userid")
    @JsonBackReference
    private Account account;

    @OneToMany(mappedBy = "wallet",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    //@JsonSerialize(contentUsing = CurrencySymbolSerializer.class)
    private List<WalletBalance> balances = new ArrayList<>();

}