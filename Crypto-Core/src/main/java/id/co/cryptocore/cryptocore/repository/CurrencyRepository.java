package id.co.cryptocore.cryptocore.repository;

import id.co.cryptocore.cryptocore.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Integer> {
    public Currency findCurrencyById(Integer id);
    public Optional<Currency> findCurrencyBySymbolEqualsIgnoreCase(String symbol);
}
