package id.co.cryptocore.cryptocore.model.DTO;


import id.co.cryptocore.cryptocore.model.WalletBalance;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;

@Getter
@Setter
public class WalletBalanceResponseDTO {
    public String id;
    public String symbol;
    public String balance;

    public WalletBalanceResponseDTO(WalletBalance balance){
        this.id = balance.getId().toString();
        this.symbol = balance.getCurrency().getSymbol();
        DecimalFormat df = new DecimalFormat("#,###.##########");
        this.balance = df.format(balance.getBalance());
    }
}
