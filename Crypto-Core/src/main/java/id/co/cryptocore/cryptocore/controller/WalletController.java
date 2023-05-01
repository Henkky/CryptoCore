package id.co.cryptocore.cryptocore.controller;

import id.co.cryptocore.cryptocore.model.DTO.WalletBalanceDTO;
import id.co.cryptocore.cryptocore.model.DTO.WalletBalanceRegisDTO;
import id.co.cryptocore.cryptocore.model.Wallet;
import id.co.cryptocore.cryptocore.model.WalletBalance;
import id.co.cryptocore.cryptocore.service.WalletBalanceService;
import id.co.cryptocore.cryptocore.service.WalletService;
import id.co.cryptocore.cryptocore.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletBalanceService walletBalanceService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<Wallet>> getAllWallet() {
        return walletService.getAllWallets();
    }

    @GetMapping("/{userid}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Wallet> getWalletByUserId(@PathVariable("userid") String userid) {
        return walletService.getWalletByUserId(userid);
    }

    @GetMapping("/{userid}/{symbol}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<WalletBalance> getBalanceByUserIdAndSymbol(@PathVariable("userid") String userid,
                                                                    @PathVariable("symbol") String symbol) {
        return walletBalanceService.getBalanceByAccountIdAndCurrencySymbol(userid,symbol);
    }


    @PostMapping("/balance/register")
    public ApiResponse<WalletBalance> registerWalletBalance(@RequestBody WalletBalanceRegisDTO walletBalanceRegisDTO){
        return walletBalanceService.createWalletBalance(walletBalanceRegisDTO);
    }

    @PutMapping("/balance/add")
    public ApiResponse<WalletBalance> addWalletBalance(@RequestBody WalletBalanceDTO walletBalanceDTO){
        return walletBalanceService.addBalance(walletBalanceDTO);
    }

    @PutMapping("/balance/deduct")
    public ApiResponse<WalletBalance> deductWalletBalance(@RequestBody WalletBalanceDTO walletBalanceDTO){
        return walletBalanceService.deductBalance(walletBalanceDTO);
    }

    @DeleteMapping("/{userid}")
    public ApiResponse<Wallet> deleteWalletByUserId(@PathVariable("userid") String userid){
        return walletService.deleteWalletByUserId(userid);
    }

}


