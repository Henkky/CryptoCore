package id.co.cryptocore.cryptocore.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WalletBalanceDTO {
    private String userId;
    private String currency;
    private String balance;
}
