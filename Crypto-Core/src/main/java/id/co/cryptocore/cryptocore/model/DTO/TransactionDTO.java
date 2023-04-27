package id.co.cryptocore.cryptocore.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {
    //account id of the seller and buyer
    private String sellerId;
    private String buyerId;
    //sellerCurrency : the currency that the buyer want to buy, buyerCurrency : the payment currency used to pay it
    private String sellerCurrency;
    private String buyerCurrency;
    //amount : the amount of how much sellerCurrency the buyer want to buy
    private String amount;
}
