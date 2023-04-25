package id.co.cryptocore.cryptocore.repository;

import id.co.cryptocore.cryptocore.model.WalletBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletBalanceRepository extends JpaRepository<WalletBalance, Integer> {
    public WalletBalance findWalletBalanceById(Integer id);
    public Optional<WalletBalance> findWalletBalanceByWallet_IdAndCurrency_Symbol(Integer walletId, String symbol);
    public List<WalletBalance> findWalletBalancesByWallet_Id(Integer walletId);
}
