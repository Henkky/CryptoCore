package id.co.cryptocore.cryptocore.service;

import id.co.cryptocore.cryptocore.model.Currency;
import id.co.cryptocore.cryptocore.model.DTO.CurrencyDTO;
import id.co.cryptocore.cryptocore.model.DTO.CurrencyRateDTO;
import id.co.cryptocore.cryptocore.repository.CurrencyRepository;
import id.co.cryptocore.cryptocore.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
            Currency currency = checkCurrency.get();
            currencyRepository.delete(currency);
            response.setStatus(true);
            response.setMessage("Currency " + currency.getSymbol() + " successfully deleted");
            response.setData(currency);
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

    public ApiResponse<Currency> updateCurrencyRate(CurrencyRateDTO currencyRateDTO){
        ApiResponse<Currency> response = new ApiResponse<>();
        Optional<Currency> checkCurrency = currencyRepository.findCurrencyBySymbolEqualsIgnoreCase
                (currencyRateDTO.getSymbol());
        if(checkCurrency.isEmpty()){
            response.setStatus(false);
            response.setMessage("Currency " + currencyRateDTO.getSymbol() + " not found");
        }else {
            Currency updCurrency = checkCurrency.get();
            BigDecimal newRate = new BigDecimal(currencyRateDTO.getRate());
            updCurrency.setRate(newRate);
            currencyRepository.save(updCurrency);

            response.setStatus(true);
            response.setMessage("Currency " + currencyRateDTO.getSymbol() + " rate successfully updated");
            response.setData(updCurrency);
        }
        return response;
    }

    public ApiResponse<BigDecimal> convert(String amount, String symbolFrom, String symbolTo){
        ApiResponse<BigDecimal> response = new ApiResponse<>();
        BigDecimal result = new BigDecimal(0);
        BigDecimal amountNum = new BigDecimal(amount);
        Currency currencyFrom = new Currency();
        Currency currencyTo = new Currency();

        //to get the currency class of the source (symbolFrom)
        Optional<Currency> checkCurrency = currencyRepository.findCurrencyBySymbolEqualsIgnoreCase(symbolFrom);
        if(checkCurrency.isEmpty()){
            response.setStatus(false);
            response.setMessage("Currency " + symbolFrom + " not found");
            return response;
        } else{
            currencyFrom = checkCurrency.get();
        }

        //to get the currency class of the destination (symbolTo)
        checkCurrency = currencyRepository.findCurrencyBySymbolEqualsIgnoreCase(symbolTo);
        if(checkCurrency.isEmpty()){
            response.setStatus(false);
            response.setMessage("Currency " + symbolTo + " not found");
            return response;
        } else{
            currencyTo = checkCurrency.get();
        }

        amountNum = amountNum.multiply(currencyFrom.getRate());
        //https://docs.oracle.com/javase/7/docs/api/java/math/RoundingMode.html
        result = amountNum.divide(currencyTo.getRate(),10, RoundingMode.HALF_UP);
        response.setData(result);
        return response;
    }
}
