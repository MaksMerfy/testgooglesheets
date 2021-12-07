package ru.mb.analytics.account.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.mb.analytics.account.entity.Account;
import ru.mb.analytics.entity.Proxy;
import ru.mb.analytics.entity.Thematic;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<Account> {
    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Account()
            .setId(rs.getLong("id"))
            .setBrand(rs.getString("brand"))
            .setThematic(new Thematic().setId(rs.getLong("thematic_id")))
            .setSite(rs.getString("site"))
            .setLegalEntity(rs.getString("legal_entity"))
            .setPhone(rs.getString("phone"))
            .setMail(rs.getString("email"))
            .setAdress(rs.getString("adress"))
            .setPersonManager(rs.getString("person_manager"))
            .setNumberPhonePersonManager(rs.getString("number_phone_person_manager"))
            .setDateRegistration(rs.getDate("date_registration").toLocalDate())
            .setLogin(rs.getString("login"))
            .setPassword(rs.getString("password"))
            .setCookie(rs.getString("cookie"))
            .setFingerprint(rs.getString("fingerprint"))
            .setProxy(new Proxy()
                .setIp(rs.getString("proxy_ip"))
                .setPort(rs.getString("proxy_port"))
                .setLogin(rs.getString("proxy_login"))
                .setPassword(rs.getString("proxy_password")))
            .setIsActive(rs.getBoolean("is_active"));
    }
}
