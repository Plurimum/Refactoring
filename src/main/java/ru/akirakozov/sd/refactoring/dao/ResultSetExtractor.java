package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResultSetExtractor {
    public List<Product> extractProducts(ResultSet resultSet) {
        List<Product> products = new ArrayList<>();

        try {
            while (resultSet.next()) {
                products.add(getProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    public Optional<Product> extractProduct(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                return Optional.of(getProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public Product getProduct(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getString("name"),
                resultSet.getLong("price")
        );
    }

    public Optional<Long> extractLong(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }
}
