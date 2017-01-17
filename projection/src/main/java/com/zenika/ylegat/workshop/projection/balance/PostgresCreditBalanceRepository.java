package com.zenika.ylegat.workshop.projection.balance;

import static java.sql.DriverManager.getConnection;
import static com.github.ylegat.uncheck.Uncheck.uncheck;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.Properties;
import org.postgresql.PGProperty;

public class PostgresCreditBalanceRepository implements CreditBalanceRepository {

    private final Connection connection;

    public PostgresCreditBalanceRepository() {
        String url = "jdbc:postgresql://localhost:5432/workshop";
        Properties props = new Properties();
        PGProperty.USER.set(props, "postgres");
        PGProperty.PASSWORD.set(props, "");
        connection = uncheck(() -> getConnection(url, props));
    }

    @Override
    public void writeCreditBalance(String bankAccountId, int credit) {
        uncheck(() -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO credit_balance(bank_account_id, value) VALUES(?, ?) ON CONFLICT(bank_account_id) DO UPDATE SET value = ?");
            statement.setString(1, bankAccountId);
            statement.setInt(2, credit);
            statement.setInt(3, credit);
            statement.executeUpdate();
        });
    }

    @Override
    public Optional<Integer> readCreditBalance(String bankAccountId) {
        return uncheck(() -> {
            PreparedStatement statement = connection.prepareStatement("SELECT value FROM credit_balance WHERE bank_account_id = ?");
            statement.setString(1, bankAccountId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return Optional.of(result.getInt(1));
            } else {
                return Optional.empty();
            }
        });
    }

    @Override
    public void clear() {
        uncheck(() -> connection.prepareStatement("DELETE FROM credit_balance").executeUpdate());
    }

}
