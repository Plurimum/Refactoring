package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ProductDao {
    private final ResultSetExtractor resultSetExtractor;

    private static final String SQL_URL = "jdbc:sqlite:test.db";

    public ProductDao() {
        this.resultSetExtractor = new ResultSetExtractor();
    }

    public List<Product> findAll() {
        return executeQuery(
                "select * from product",
                resultSetExtractor::extractProducts
        );
    }

    public void addProduct(String name, long price) {
        String sql = String.format(
                """
                        INSERT INTO PRODUCT (NAME, PRICE)
                        VALUES ("%s", %d)
                        """,
                name,
                price
        );
        executeUpdate(sql);
    }

    public void createTable() {
        String sql = """
                create table if not exists product
                (
                id integer primary key autoincrement not null,
                name text not null,
                price int not null
                )
                """;
        executeUpdate(sql);
    }

    public Optional<Product> getMaxPriceProduct() {
        return executeQuery(
                "select * from product order by price desc limit 1",
                resultSetExtractor::extractProduct
        );
    }

    public Optional<Product> getMinPriceProduct() {
        return executeQuery(
                "select * from product order by price limit 1",
                resultSetExtractor::extractProduct
        );
    }

    public Optional<Long> getSumPrice() {
        return executeQuery(
                "select sum(price) from product",
                resultSetExtractor::extractLong
        );
    }

    public Optional<Long> getCountOfProducts() {
        return executeQuery(
                "SELECT COUNT(*) FROM PRODUCT",
                resultSetExtractor::extractLong
        );
    }

    private <T> T executeQuery(String sqlQuery, Function<ResultSet, T> resultHandler) {
        try (
                Connection connection = DriverManager.getConnection(SQL_URL);
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            return resultHandler.apply(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeUpdate(String sqlUpdate) {
        try (
                Connection connection = DriverManager.getConnection(SQL_URL);
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(sqlUpdate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
