package id.co.cryptocore.cryptocore.repository;

import id.co.cryptocore.cryptocore.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Integer> {
    public Wallet findWalletById(Integer walletId);
}
