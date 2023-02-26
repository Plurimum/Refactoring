package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.dao.ProductDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetProductsServletTest {
    @AfterEach
    @BeforeEach
    public final void cleanDatabase() throws SQLException {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate("DELETE FROM PRODUCT");
        }
    }

    @Test
    void testGetSingle() throws SQLException, IOException {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"milk\", 50)");
        }

        HttpServletRequest request = mock(HttpServletRequest.class);

        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new ResponseWriter());

        new GetProductsServlet(new ProductDao()).doGet(request, response);
        String expected = String.format("<html><body>%nmilk\t50</br>%n</body></html>%n");
        assertEquals(expected, response.getWriter().toString());
    }

    @Test
    void testGetMany() throws SQLException, IOException {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"milk\", 50)");
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"bread\", 30)");
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"meat\", 250)");
        }

        HttpServletRequest request = mock(HttpServletRequest.class);

        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new ResponseWriter());

        new GetProductsServlet(new ProductDao()).doGet(request, response);
        String expected = String.format("<html><body>%nmilk\t50</br>%nbread\t30</br>%nmeat\t250</br>%n</body></html>%n");
        assertEquals(expected, response.getWriter().toString());
    }
}
