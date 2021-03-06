package noah.memo.authorityservice.repository;

import noah.memo.authorityapi.bean.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 描述:
 * 账号dao层
 *
 * @author Noah
 * @create 2020-01-28 17:06
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {


    Account findByLoginName(String loginName);

    Account findByLoginNameAndLoginPassword(String loginName, String password);
}
