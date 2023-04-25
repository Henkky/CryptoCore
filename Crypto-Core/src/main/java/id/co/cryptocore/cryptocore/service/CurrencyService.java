package id.co.cryptocore.cryptocore.service;

import id.co.cryptocore.cryptocore.model.Account;
import id.co.cryptocore.cryptocore.model.Currency;
import id.co.cryptocore.cryptocore.model.DTO.CurrencyDTO;
import id.co.cryptocore.cryptocore.repository.CurrencyRepository;
import id.co.cryptocore.cryptocore.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    public ApiResponse<Currency> addCurrency(CurrencyDTO currencyDTO){
        ApiResponse<Currency> response = new ApiResponse<>();
        if(currencyRepository.findCurrencyBySymbolEqualsIgnoreCase(currencyDTO.getSymbol()).isPresent()){
            response.setStatus(false);
            response.setMessage("Currency " + currencyDTO.getSymbol() + " already exists");
        }else {
            Currency newCurrency = new Currency();
            newCurrency.setName(currencyDTO.getName());
            newCurrency.setSymbol(currencyDTO.getSymbol());
            BigDecimal newRate = new BigDecimal(currencyDTO.getRate());
            newCurrency.setRate(newRate);
            newCurrency.setWalletBalances(new ArrayList<>());
            currencyRepository.save(newCurrency);

            response.setStatus(true);
            response.setMessage("Currency " + currencyDTO.getSymbol() + " successfully created");
            response.setData(newCurrency);
        }
        return response;
    }

    public ApiResponse<Currency> updateCurrency(CurrencyDTO currencyDTO){
        ApiResponse<Currency> response = new ApiResponse<>();
        Optional<Currency> checkCurrency = currencyRepository.findCurrencyBySymbolEqualsIgnoreCase
                (currencyDTO.getSymbol());
        if(checkCurrency.isEmpty()){
            response.setStatus(false);
            response.setMessage("Currency " + currencyDTO.getSymbol() + " not found");
        }else {
            Currency updCurrency = checkCurrency.get();
            updCurrency.setName(currencyDTO.getName());
            updCurrency.setSymbol(currencyDTO.getSymbol());
            BigDecimal newRate = new BigDecimal(currencyDTO.getRate());
            updCurrency.setRate(newRate);
            currencyRepository.save(updCurrency);

            response.setStatus(true);
            response.setMessage("Currency " + currencyDTO.getSymbol() + " successfully updated");
            response.setData(updCurrency);
        }
        return response;
    }

    public ApiResponse<Currency> deleteCurrency(String symbol){
        ApiResponse<Currency> response = new ApiResponse<>();
        Optional<Currency> checkCurrency = currencyRepository.findCurrencyBySymbolEqualsIgnoreCase
                (symbol);
        if(checkCurrency.isEmpty()){
            response.setStatus(false);
            response.setMessage("Currency " + symbol + " not found");
        }else {
            currencyRepository.delete(checkCurrency.get());
            response.setStatus(true);
            response.setMessage("Currency " + symbol + " successfully deleted");
            response.setData(checkCurrency.get());
        }
        return response;
    }

    public ApiResponse<List<Currency>> getAllCurrencies(){
        ApiResponse<List<Currency>> response = new ApiResponse<>();
        List<Currency> currencies = currencyRepository.findAll();
        response.setStatus(true);
        response.setMessage("Currencies successfully retrieved");
        response.setData(currencies);
        return response;
    }

    public ApiResponse<Currency> getCurrencyBySymbol(String symbol){
        ApiResponse<Currency> response = new ApiResponse<>();
        Optional<Currency> checkCurrency = currencyRepository.findCurrencyBySymbolEqualsIgnoreCase
                (symbol);
        if(checkCurrency.isEmpty()){
            response.setStatus(false);
            response.setMessage("Currency " + symbol + " not found");
        }else {
            response.setStatus(true);
            response.setMessage("Currency " + symbol + " successfully retrieved");
            response.setData(checkCurrency.get());
        }
        return response;
    }

    public ApiResponse<Currency> updateCurrencyRate(String symbol, String inpRate){
        ApiResponse<Currency> response = new ApiResponse<>();
        Optional<Currency> checkCurrency = currencyRepository.findCurrencyBySymbolEqualsIgnoreCase
                (symbol);
        if(checkCurrency.isEmpty()){
            response.setStatus(false);
            response.setMessage("Currency " + symbol + " not found");
        }else {
            Currency updCurrency = checkCurrency.get();
            BigDecimal newRate = new BigDecimal(inpRate);
            updCurrency.setRate(newRate);
            currencyRepository.save(updCurrency);

            response.setStatus(true);
            response.setMessage("Currency " + symbol + " rate successfully updated");
            response.setData(updCurrency);
        }
        return response;
    }
}
