package ru.mb.analytics.account.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.mb.analytics.account.entity.Account;
import ru.mb.analytics.account.mapper.AccountRowMapper;

import java.util.List;

@Repository
public class AccountRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Account> getResponseJobListCandidateWithoutMailing() {
        String selectQueryAllAccount = "select * from account where is_active = true";

        List<Account> accountList = namedParameterJdbcTemplate.query(selectQueryAllAccount, new AccountRowMapper());

        return accountList;
    }
}
