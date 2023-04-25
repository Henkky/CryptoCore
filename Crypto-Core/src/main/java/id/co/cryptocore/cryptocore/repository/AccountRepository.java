package id.co.cryptocore.cryptocore.repository;


import id.co.cryptocore.cryptocore.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    public Account findAccountById(String userid);
}
