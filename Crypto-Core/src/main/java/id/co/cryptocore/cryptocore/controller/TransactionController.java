package id.co.cryptocore.cryptocore.controller;


import id.co.cryptocore.cryptocore.model.DTO.TransactionDTO;
import id.co.cryptocore.cryptocore.service.TransactionService;
import id.co.cryptocore.cryptocore.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/purchase")
    ApiResponse<String> purchaseCurrency (@RequestBody TransactionDTO transactionDTO){
        return transactionService.currencyTransaction(transactionDTO);
    }
}
