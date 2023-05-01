package id.co.cryptocore.cryptocore.controller;

import id.co.cryptocore.cryptocore.model.Currency;
import id.co.cryptocore.cryptocore.model.DTO.CurrencyDTO;
import id.co.cryptocore.cryptocore.model.DTO.CurrencyRateDTO;
import id.co.cryptocore.cryptocore.service.CurrencyService;
import id.co.cryptocore.cryptocore.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<Currency>> inquiryAll(){
        return currencyService.getAllCurrencies();
    }

    @GetMapping("/{symbol}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Currency> findCurrencyBySymbol(@PathVariable("symbol") String symbol){
        return currencyService.getCurrencyBySymbol(symbol);
    }

    @PostMapping("/add")
    public ApiResponse<Currency> addCurrency(@RequestBody CurrencyDTO currencyDTO){
        return currencyService.addCurrency(currencyDTO);
    }

    @PutMapping("/update")
    public ApiResponse<Currency> updateCurrency(@RequestBody CurrencyDTO currencyDTO){
        return currencyService.updateCurrency(currencyDTO);
    }

    @PutMapping("/update/rate")
    public ApiResponse<Currency> updateRate(@RequestBody CurrencyRateDTO currencyRateDTO){
        return currencyService.updateCurrencyRate(currencyRateDTO);
    }

    @DeleteMapping("/{symbol}")
    public ApiResponse<Currency> deleteCurrency(@PathVariable("symbol") String symbol){
        return currencyService.deleteCurrency(symbol);
    }


}
