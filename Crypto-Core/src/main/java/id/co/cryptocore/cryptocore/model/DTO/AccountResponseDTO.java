package id.co.cryptocore.cryptocore.model.DTO;

import id.co.cryptocore.cryptocore.model.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountResponseDTO {
    String userid;
    List<WalletBalanceResponseDTO> balances;
    public  AccountResponseDTO(Account account){
        this.userid = account.getId();
    }

}
