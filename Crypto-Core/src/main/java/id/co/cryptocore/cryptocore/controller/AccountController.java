package id.co.cryptocore.cryptocore.controller;

import id.co.cryptocore.cryptocore.model.Account;
import id.co.cryptocore.cryptocore.model.DTO.AccountDTO;
import id.co.cryptocore.cryptocore.model.DTO.AccountSecurityDTO;
import id.co.cryptocore.cryptocore.model.Wallet;
import id.co.cryptocore.cryptocore.service.AccountService;
import id.co.cryptocore.cryptocore.service.WalletService;
import id.co.cryptocore.cryptocore.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private WalletService walletService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<Account>> inquiryAll(){
        return accountService.getAllAccounts();
    }

    @GetMapping("/{userid}")
    public ApiResponse<Account> findAccountByUserId(@PathVariable("userid") String userId){
        return accountService.getAccountById(userId);
    }

    @GetMapping("/identify/{userid}")
    public ApiResponse<AccountSecurityDTO> findAccountByUserIdForSecurity(@PathVariable("userid") String userId){
        return accountService.getAccountForSecurity(userId);
    }

    @PostMapping("/register")
    public ApiResponse<Account> registerAccount(@RequestBody AccountDTO accountDTO){
        ApiResponse<Account> accountResponse = accountService.createAccount(accountDTO);
        if(accountResponse.isStatus()){
            ApiResponse<Wallet> walletResponse = walletService.createWallet(accountDTO.getUserId());
        }
        return accountResponse;
    }

    @PutMapping("/update")
    public ApiResponse<Account> updateAccount(@RequestBody AccountDTO accountDTO){
        return accountService.updateAccount(accountDTO);
    }

    @DeleteMapping("/{userid}")
    public ApiResponse<Account> deleteAccount(@PathVariable("userid") String userId){
        return accountService.deleteAccount(userId);
    }
}
