package id.co.cryptocore.cryptocore.service;

import id.co.cryptocore.cryptocore.model.DTO.TransactionDTO;
import id.co.cryptocore.cryptocore.model.DTO.WalletBalanceDTO;
import id.co.cryptocore.cryptocore.model.WalletBalance;
import id.co.cryptocore.cryptocore.repository.AccountRepository;
import id.co.cryptocore.cryptocore.repository.WalletBalanceRepository;
import id.co.cryptocore.cryptocore.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;

@Service
public class TransactionService {
    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private WalletBalanceService walletBalanceService;

    @Transactional
    public ApiResponse<String> currencyTransaction(TransactionDTO transactionDTO){
        ApiResponse<String> response = new ApiResponse<>();
        BigDecimal balanceNeeded = new BigDecimal(0);

        ApiResponse<BigDecimal> convertResult =
                currencyService.convert(transactionDTO.getAmount(),
                                        transactionDTO.getSellerCurrency(),
                                        transactionDTO.getBuyerCurrency());

        if(!convertResult.isStatus()){
            response.setStatus(false);
            response.setMessage(convertResult.getMessage());
        } else{
            balanceNeeded = convertResult.getData();
        }

        WalletBalanceDTO transBalanceDTO = new WalletBalanceDTO();
        ApiResponse<WalletBalance> transResult = new ApiResponse<>();

        //deduct the money from the buyer wallet - later to be add to the seller wallet
        transBalanceDTO.setUserId(transactionDTO.getBuyerId());
        transBalanceDTO.setCurrency(transactionDTO.getBuyerCurrency());
        transBalanceDTO.setBalance(balanceNeeded.toString());
        transResult = walletBalanceService.deductBalance(transBalanceDTO);
        if(!transResult.isStatus()){
            response.setStatus(false);
            response.setMessage(transResult.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return response;
        }

        //deduct the money from the seller wallet - later to be add to the buyer wallet
        transBalanceDTO.setUserId(transactionDTO.getSellerId());
        transBalanceDTO.setCurrency(transactionDTO.getSellerCurrency());
        transBalanceDTO.setBalance(transactionDTO.getAmount());
        transResult = walletBalanceService.deductBalance(transBalanceDTO);
        if(!transResult.isStatus()){
            response.setStatus(false);
            response.setMessage(transResult.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return response;
        }

        //add money to buyer wallet
        transBalanceDTO.setUserId(transactionDTO.getBuyerId());
        transBalanceDTO.setCurrency(transactionDTO.getSellerCurrency());
        transBalanceDTO.setBalance(transactionDTO.getAmount());
        transResult = walletBalanceService.addBalance(transBalanceDTO);
        if(!transResult.isStatus()){
            response.setStatus(false);
            response.setMessage(transResult.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return response;
        }

        //add money to seller wallet
        transBalanceDTO.setUserId(transactionDTO.getSellerId());
        transBalanceDTO.setCurrency(transactionDTO.getBuyerCurrency());
        transBalanceDTO.setBalance(balanceNeeded.toString());
        transResult = walletBalanceService.addBalance(transBalanceDTO);
        if(!transResult.isStatus()){
            response.setStatus(false);
            response.setMessage(transResult.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return response;
        }

        String notif = transactionDTO.getAmount() + " of " + transactionDTO.getSellerCurrency() +
                        "successfully transferred to " + transactionDTO.getBuyerId() + " wallet for " +
                        balanceNeeded.toString() + " of " + transactionDTO.getBuyerCurrency() +
                        "in payment.";
        response.setStatus(true);
        response.setMessage("Transaction successful !");
        response.setData(notif);
        return response;
    }

}
