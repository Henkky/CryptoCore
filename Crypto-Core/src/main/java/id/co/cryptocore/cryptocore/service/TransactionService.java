package id.co.cryptocore.cryptocore.service;

import id.co.cryptocore.cryptocore.model.DTO.TransactionDTO;
import id.co.cryptocore.cryptocore.model.DTO.WalletBalanceDTO;
import id.co.cryptocore.cryptocore.model.WalletBalance;
import id.co.cryptocore.cryptocore.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.logging.Logger;

@Service
public class TransactionService {
    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private WalletBalanceService walletBalanceService;

    private Logger logger = Logger.getLogger("Transaction Service");

    @Transactional
    public ApiResponse<String> currencyTransaction(TransactionDTO transactionDTO){
        ApiResponse<String> response = new ApiResponse<>();
        BigDecimal balanceNeeded = new BigDecimal(0);

        //convert value of how much the buyer need to pay using buyer currency
        ApiResponse<BigDecimal> convertResult =
                currencyService.convert(transactionDTO.getAmount(),
                                        transactionDTO.getSellerCurrency(),
                                        transactionDTO.getBuyerCurrency());

        if(!convertResult.isStatus()){
            response.setStatus(false);
            response.setMessage(convertResult.getMessage());
            return response;
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
            if(transResult.getMessage().equalsIgnoreCase("Insufficient balance")){
                response.setMessage("Buyer does not have enough " + transactionDTO.getBuyerCurrency() + " balance");
            } else {
                response.setMessage(transResult.getMessage());
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return response;
        }
        //test transaction
        logger.info("Deduct balance from buyer wallet : success");
        System.out.println(transResult.getData().getBalance());

        //deduct the money from the seller wallet - later to be add to the buyer wallet
        transBalanceDTO.setUserId(transactionDTO.getSellerId());
        transBalanceDTO.setCurrency(transactionDTO.getSellerCurrency());
        transBalanceDTO.setBalance(transactionDTO.getAmount());
        transResult = walletBalanceService.deductBalance(transBalanceDTO);
        if(!transResult.isStatus()){
            response.setStatus(false);
            if(transResult.getMessage().equalsIgnoreCase("Insufficient balance")){
                response.setMessage("Seller does not have enough " + transactionDTO.getSellerCurrency() + " balance");
            } else {
                response.setMessage(transResult.getMessage());
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return response;
        }
        //test transaction
        logger.info("Deduct balance from seller wallet : success");
        System.out.println(transResult.getData().getBalance());

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
        //test transaction
        logger.info("Add balance to buyer wallet : success");
        System.out.println(transResult.getData().getBalance());

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
        //test transaction
        logger.info("Add balance to seller wallet : success");
        System.out.println(transResult.getData().getBalance());

        String notif = transactionDTO.getAmount() + " of " + transactionDTO.getSellerCurrency() +
                        " successfully transferred to " + transactionDTO.getBuyerId() + " wallet for " +
                        balanceNeeded.toString() + " of " + transactionDTO.getBuyerCurrency() +
                        " in payment.";
        response.setStatus(true);
        response.setMessage("Transaction successful !");
        response.setData(notif);
        return response;
    }

}
