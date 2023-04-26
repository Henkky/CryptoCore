# CryptoCore
Crypto System for Spring Training Mini Project

----------------------------------------------
ACCOUNT
Henkky


Wallet
001
List<WalletBalance>


Henkky - wallet001 	- walletbalance1 IDR 300000
						- walletbalance2 USD 100
						- walletbalance3 BTC 1
						- walletbalance4 ETH 0.5

Penjual - wallet002	- walletbalance5 BTC 10



-------------------------------------------
convert(currency A, currency B)

void transaction(DTO buyer, DTO seller)
DTO : userid, currency, balance/amount
-------------------------------------------
buyer : Henkky, IDR, 
seller : Penjual, BTC, 2

buyer.deduct(IDR-500000)
buyer.add(BTC+2)

seller.deduct(BTC-2)
seller.add(IDR+500000)

############################################
DTO : 	userid seller,
		userid buyer,
		currency seller,
		currency buyer,
		amount

void trans(DTO trans)
amountConvert = convert(amount,currencyFrom, currencyTo) //cek misal 2 BTC berapa IDR

seller.hasEnough(amount)
buyer.hasEnough(amountConvert)

seller.add(amountConvert)
seller.deduct(amount)

buyer.add(amount)
buyer.deduct(amountConvert)


-------------------------------------------
kurangin duit (IDR/BTC) - dapat produk travel
produk travel A -> cost idr 500000 / btc 2

lempar userid henkky - IDR - 500000
henkky - btc - 2
