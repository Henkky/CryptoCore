package id.co.cryptocore.cryptocore.model.DTO;


import id.co.cryptocore.cryptocore.model.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSecurityDTO {
    String userId;
    String password;
    String role;

    public AccountSecurityDTO(Account account){
        this.userId = account.getId();
        this.password = account.getPassword();
        this.role = account.getRole();
    }
}
