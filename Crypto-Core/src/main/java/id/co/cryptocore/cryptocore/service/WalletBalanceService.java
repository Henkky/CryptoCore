package id.co.cryptocore.cryptocore.service;

import id.co.cryptocore.cryptocore.model.Account;
import id.co.cryptocore.cryptocore.model.Currency;
import id.co.cryptocore.cryptocore.model.DTO.WalletBalanceDTO;
import id.co.cryptocore.cryptocore.model.DTO.WalletBalanceRegisDTO;
import id.co.cryptocore.cryptocore.model.Wallet;
import id.co.cryptocore.cryptocore.model.WalletBalance;
import id.co.cryptocore.cryptocore.repository.AccountRepository;
import id.co.cryptocore.cryptocore.repository.CurrencyRepository;
import id.co.cryptocore.cryptocore.repository.WalletBalanceRepository;
import id.co.cryptocore.cryptocore.repository.WalletRepository;
import id.co.cryptocore.cryptocore.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class WalletBalanceService {
    @Autowired
    private WalletBalanceRepository walletBalanceRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    public ApiResponse<WalletBalance> createWalletBalance(WalletBalanceRegisDTO walletBalanceRegisDTO){
        ApiResponse<WalletBalance> response = new ApiResponse<>();
        boolean isError = false;
        Account account = new Account();
        Wallet wallet = new Wallet();
        Currency currency = new Currency();

        Optional<Account> checkAccount = accountRepository.findById(walletBalanceRegisDTO.getUserId());
        if(checkAccount.isEmpty()){
            response.setStatus(false);
            response.setMessage("Insert new balance failed. Account "
                    + walletBalanceRegisDTO.getUserId()
                    + " does not exist");
            isError = true;
        }

        if(!isError){
            account = checkAccount.get();
            wallet = account.getWallet();
            if(wallet == null){
                response.setStatus(false);
                response.setMessage("Insert new balance failed. Account "
                        + walletBalanceRegisDTO.getUserId()
                        + " does not have a wallet");
                isError = true;
            }
        }

        if(!isError){
            List<WalletBalance> existBalancesInWallet = wallet.getBalances();
            Currency checkCurrencyExist = new Currency();
            for (WalletBalance e: existBalancesInWallet) {
                checkCurrencyExist = e.getCurrency();
                if(checkCurrencyExist.getSymbol().equalsIgnoreCase(walletBalanceRegisDTO.getCurrency())){
                    response.setStatus(false);
                    response.setMessage("Insert new balance failed. Currency "
                            + walletBalanceRegisDTO.getCurrency()
                            + " already exists");
                    isError = true;
                    break;
                }
            }
        }

        if(!isError){
            Optional<Currency> checkCurrency = currencyRepository.findCurrencyBySymbolEqualsIgnoreCase
                    (walletBalanceRegisDTO.getCurrency());
            if(checkCurrency.isEmpty()){
                response.setStatus(false);
                response.setMessage("Insert new balance failed. Currency "
                        + walletBalanceRegisDTO.getCurrency()
                        + " invalid");
                isError = true;
            } else {
                currency = checkCurrency.get();
            }
        }

        if(!isError){
            WalletBalance newWalletBalance = new WalletBalance();
            newWalletBalance.setWallet(wallet);
            newWalletBalance.setCurrency(currency);
            BigDecimal newBalance = new BigDecimal(0);
            newWalletBalance.setBalance(newBalance);
            walletBalanceRepository.save(newWalletBalance);

            //add new WalletBalance into the existing List of WalletBalance inside the Wallet
            List<WalletBalance> existBalancesInWallet = wallet.getBalances();
            existBalancesInWallet.add(newWalletBalance);
            wallet.setBalances(existBalancesInWallet);
            walletRepository.save(wallet);

            //add new WalletBalance into the existing List of WalletBalance inside the Currency
            List<WalletBalance> existBalancesInCurrency = currency.getWalletBalances();
            existBalancesInCurrency.add(newWalletBalance);
            currency.setWalletBalances(existBalancesInCurrency);
            currencyRepository.save(currency);

            response.setStatus(true);
            response.setMessage("Insert new balance success for "
                    +"User " + walletBalanceRegisDTO.getUserId()
                    +" on " + walletBalanceRegisDTO.getCurrency() + " currency");
        }

        return response;
    }

    public ApiResponse<WalletBalance> getBalanceByAccountIdAndCurrencySymbol(String userId, String currencySymbol){
        ApiResponse<WalletBalance> response = new ApiResponse<>();
        boolean isError = false;
        Account account = new Account();
        Wallet wallet = new Wallet();
        Currency currency = new Currency();
        Optional<Account> checkAccount = accountRepository.findById(userId);
        if(checkAccount.isEmpty()){
            response.setStatus(false);
            response.setMessage("Retrieve balance failed. Account "
                    + userId
                    + " does not exist");
            isError = true;
        }

        if(!isError){
            account = checkAccount.get();
            wallet = account.getWallet();
            if(wallet == null){
                response.setStatus(false);
                response.setMessage("Retrieve balance failed. Account "
                        + userId
                        + " does not have a wallet");
                isError = true;
            }
        }

        if(!isError){
            Optional<WalletBalance> walletBalance =
                    walletBalanceRepository.findWalletBalanceByWallet_IdAndCurrency_Symbol
                            (wallet.getId(), currencySymbol);
            if(walletBalance.isEmpty()){
                response.setStatus(false);
                response.setMessage("Retrieve balance failed. Account does not have " + currencySymbol + " balance");
            } else {
                response.setStatus(true);
                response.setMessage("Balance succesfully retrieved");
                response.setData(walletBalance.get());
            }
        }

        return response;
    }

    public ApiResponse<WalletBalance> addBalance(WalletBalanceDTO walletBalanceDTO){
        ApiResponse<WalletBalance> response = new ApiResponse<>();
        ApiResponse<WalletBalance> checkWalletBalance = getBalanceByAccountIdAndCurrencySymbol
                (walletBalanceDTO.getUserId(), walletBalanceDTO.getCurrency());
        if(!checkWalletBalance.isStatus()){
            response.setStatus(false);
            response.setMessage(checkWalletBalance.getMessage());

        } else {
            WalletBalance walletBalance = checkWalletBalance.getData();
            BigDecimal amount = new BigDecimal(walletBalanceDTO.getBalance());
            BigDecimal newBalance = walletBalance.getBalance().add(amount);
            walletBalance.setBalance(newBalance);
            walletBalanceRepository.save(walletBalance);

            response.setStatus(true);
            response.setMessage("Add balance success");
            response.setData(walletBalance);
        }
        return response;
    }

    public ApiResponse<WalletBalance> deductBalance(WalletBalanceDTO walletBalanceDTO){
        ApiResponse<WalletBalance> response = new ApiResponse<>();
        ApiResponse<WalletBalance> checkWalletBalance = getBalanceByAccountIdAndCurrencySymbol
                (walletBalanceDTO.getUserId(), walletBalanceDTO.getCurrency());
        if(!checkWalletBalance.isStatus()){
            response.setStatus(false);
            response.setMessage(checkWalletBalance.getMessage());
        } else {
            WalletBalance walletBalance = checkWalletBalance.getData();
            BigDecimal amount = new BigDecimal(walletBalanceDTO.getBalance());
            if(walletBalance.isEnoughBalance(amount)){
                BigDecimal newBalance = walletBalance.getBalance().subtract(amount);
                walletBalance.setBalance(newBalance);
                walletBalanceRepository.save(walletBalance);
                response.setStatus(true);
                response.setMessage("Substract balance success");
                response.setData(walletBalance);
            } else{
                response.setStatus(false);
                response.setMessage("Insufficient balance");
            }
        }
        return response;
    }
}
