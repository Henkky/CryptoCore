package id.co.cryptocore.cryptocore.service;

import id.co.cryptocore.cryptocore.model.Account;
import id.co.cryptocore.cryptocore.model.Wallet;
import id.co.cryptocore.cryptocore.model.WalletBalance;
import id.co.cryptocore.cryptocore.repository.AccountRepository;
import id.co.cryptocore.cryptocore.repository.WalletBalanceRepository;
import id.co.cryptocore.cryptocore.repository.WalletRepository;
import id.co.cryptocore.cryptocore.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AccountRepository accountRepository;

    public ApiResponse<Wallet> createWallet(String userId){
        ApiResponse<Wallet> response = new ApiResponse<>();
        Optional<Account> checkAccount = accountRepository.findById(userId);
        if(checkAccount.isEmpty()){
            response.setStatus(false);
            response.setMessage("Create wallet failed. Account " + userId + " does not exist");
        }else{
            Wallet newWallet = new Wallet();
            Account account = checkAccount.get();
            newWallet.setAccount(account);
            newWallet.setBalances(new ArrayList<>());
            walletRepository.save(newWallet);
            //to link the Wallet inside Account class with the newly created Wallet
            account.setWallet(newWallet);
            accountRepository.save(account);
            response.setStatus(true);
            response.setMessage("Wallet for Account " + userId + " successfully created");
            response.setData(newWallet);
        }
        return response;
    }

    public ApiResponse<Wallet> deleteWalletByWalletId(Integer walletId){
        ApiResponse<Wallet> response = new ApiResponse<>();
        Optional<Wallet> checkWallet = walletRepository.findById(walletId);
        if(checkWallet.isEmpty()){
            response.setStatus(false);
            response.setMessage("Delete wallet failed. Wallet " + walletId + " does not exist");
        }else{
            Wallet wallet = checkWallet.get();
            walletRepository.delete(wallet);
            //to set back the wallet inside the Account class to null
            Account account = wallet.getAccount();
            account.setWallet(null);
            accountRepository.save(account);
            response.setStatus(true);
            response.setMessage("Wallet for Wallet " + walletId + " successfully deleted");
            response.setData(wallet);
        }
        return response;
    }

    public ApiResponse<Wallet> deleteWalletByUserId(String userId){
        ApiResponse<Wallet> response = new ApiResponse<>();
        Optional<Account> checkAccount = accountRepository.findById(userId);
        if(checkAccount.isEmpty()){
            response.setStatus(false);
            response.setMessage("Delete wallet failed. Account " + userId + " does not exist");
        }else{
            Account account = checkAccount.get();
            Wallet wallet = account.getWallet();
            //https://stackoverflow.com/questions/22688402/delete-not-working-with-jparepository
            wallet.removeAccount(account);
            walletRepository.delete(wallet);
            //to set back the wallet inside the Account class to null
            account.setWallet(null);
            accountRepository.save(account);
            response.setStatus(true);
            response.setMessage("Wallet for Account " + userId + " successfully deleted");
            response.setData(wallet);
        }
        return response;
    }

    public ApiResponse<List<Wallet>> getAllWallets(){
        ApiResponse<List<Wallet>> response = new ApiResponse<>();
        List<Wallet> wallets = walletRepository.findAll();
        response.setStatus(true);
        response.setMessage("Wallets successfully retrieved");
        response.setData(wallets);
        return response;
    }

    public ApiResponse<Wallet> getWalletByUserId(String userId){
        ApiResponse<Wallet> response = new ApiResponse<>();
        Optional<Account> checkAccount = accountRepository.findById(userId);
        if(checkAccount.isEmpty()){
            response.setStatus(false);
            response.setMessage("Get wallet failed. Account " + userId + " does not exist");
        }else{
            Account account = checkAccount.get();
            Wallet wallet = account.getWallet();
            response.setStatus(true);
            response.setMessage("Wallet for Account " + userId + " successfully retrieved");
            response.setData(wallet);
        }
        return response;
    }

}
