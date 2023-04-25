package id.co.cryptocore.cryptocore.service;

import id.co.cryptocore.cryptocore.model.Account;
import id.co.cryptocore.cryptocore.model.DTO.AccountDTO;
import id.co.cryptocore.cryptocore.model.Wallet;
import id.co.cryptocore.cryptocore.repository.AccountRepository;
import id.co.cryptocore.cryptocore.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public ApiResponse<Account> createAccount(AccountDTO accountDTO){
        ApiResponse<Account> response = new ApiResponse<>();
        if(accountRepository.findById(accountDTO.getUserId()).isPresent()){
            response.setStatus(false);
            response.setMessage("Account " + accountDTO.getUserId() + " already exists");
        }else {
            Account newAccount = new Account();
            newAccount.setId(accountDTO.getUserId());
            newAccount.setName(accountDTO.getName());
            newAccount.setEmail(accountDTO.getEmail());
            newAccount.setWallet(null);
            accountRepository.save(newAccount);
            response.setStatus(true);
            response.setMessage("Account " + accountDTO.getUserId() + " successfully created");
            response.setData(newAccount);
        }
        return response;
    }

    public ApiResponse<Account> updateAccount(AccountDTO accountDTO){
        ApiResponse<Account> response = new ApiResponse<>();
        Optional<Account> checkAccount = accountRepository.findById(accountDTO.getUserId());
        if(checkAccount.isEmpty()){
            response.setStatus(false);
            response.setMessage("Account " + accountDTO.getUserId() + " does not exist");
        }else {
            Account updAccount = checkAccount.get();
            updAccount.setId(accountDTO.getUserId());
            updAccount.setName(accountDTO.getName());
            updAccount.setEmail(accountDTO.getEmail());
            updAccount.setWallet(updAccount.getWallet());
            accountRepository.save(updAccount);
            response.setStatus(true);
            response.setMessage("Account " + accountDTO.getUserId() + " successfully updated");
            response.setData(updAccount);
        }
        return response;
    }

    public ApiResponse<Account> deleteAccount(String userId){
        ApiResponse<Account> response = new ApiResponse<>();
        Optional<Account> checkAccount = accountRepository.findById(userId);
        if(checkAccount.isEmpty()){
            response.setStatus(false);
            response.setMessage("Account " + userId + " does not exist");
        }else {
            accountRepository.delete(checkAccount.get());
            response.setStatus(true);
            response.setMessage("Account " + userId + " successfully deleted");
            response.setData(checkAccount.get());
        }
        return response;
    }

    public ApiResponse<List<Account>> getAllAccounts(){
        ApiResponse<List<Account>> response = new ApiResponse<>();
        List<Account> accounts = accountRepository.findAll();
        response.setStatus(true);
        response.setMessage("Accounts successfully retrieved");
        response.setData(accounts);
        return response;
    }

    public ApiResponse<Account> getAccountById(String userId){
        ApiResponse<Account> response = new ApiResponse<>();
        Optional<Account> checkAccount = accountRepository.findById(userId);
        if(checkAccount.isEmpty()){
            response.setStatus(false);
            response.setMessage("Account " + userId + " does not exist");
        }else {
            response.setStatus(true);
            response.setMessage("Account " + userId + " successfully retrieved");
            response.setData(checkAccount.get());
        }
        return response;
    }

    public void linkAccountAndWallet(String userId, Wallet wallet){
        Account account = accountRepository.findById(userId).get();
        account.setWallet(wallet);
        accountRepository.save(account);
    }

}
