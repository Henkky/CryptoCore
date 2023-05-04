package id.co.cryptocore.cryptocore.model.DTO;

import id.co.cryptocore.cryptocore.model.Account;
import id.co.cryptocore.cryptocore.model.WalletBalance;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountResponseDTO {
    String userid;
    String name;
    String email;
    List<WalletBalanceResponseDTO> balances;
    public  AccountResponseDTO(Account account){
        this.userid = account.getId();
        this.name = account.getName();
        this.email = account.getEmail();
        for (WalletBalance e : account.getWallet().getBalances()) {
            this.balances.add(new WalletBalanceResponseDTO(e));
        }
    }

}
