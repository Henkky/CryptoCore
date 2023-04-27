package id.co.cryptocore.cryptocore.service;

import id.co.cryptocore.cryptocore.model.DTO.TransactionDTO;
import id.co.cryptocore.cryptocore.model.WalletBalance;
import id.co.cryptocore.cryptocore.repository.AccountRepository;
import id.co.cryptocore.cryptocore.repository.WalletBalanceRepository;
import id.co.cryptocore.cryptocore.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    WalletBalanceRepository walletBalanceRepository;

    @Autowired
    AccountRepository accountRepository;

    public ApiResponse<String> currencyTransaction(TransactionDTO transactionDTO){
        ApiResponse<String> response = new ApiResponse<>();

        return response;
    }

}
