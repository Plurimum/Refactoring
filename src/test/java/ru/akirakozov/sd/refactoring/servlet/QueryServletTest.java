package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

public class QueryServletTest {
    @AfterEach
    @BeforeEach
    public final void cleanDatabase() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement = c.createStatement();
            statement.executeUpdate("DELETE FROM PRODUCT");
        }
    }

    @Test
    void testQuerySingle() throws IOException, SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement = c.createStatement();
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"milk\", 50)");
        }
        testCommands("milk\t50</br>", "milk\t50</br>", 50, 1);
    }

    @Test
    void testQueryMany() throws IOException, SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement = c.createStatement();
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"milk\", 50)");
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"bread\", 30)");
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"meat\", 250)");
        }
        testCommands("meat\t250</br>", "bread\t30</br>", 50 + 30 + 250, 3);
    }

    private void testCommands(String maxProduct, String minProduct, int sumProduct, int countProduct) throws IOException {
        String max = String.format("<html><body>%n<h1>Product with max price: </h1>%n%s%n</body></html>%n", maxProduct);
        String min = String.format("<html><body>%n<h1>Product with min price: </h1>%n%s%n</body></html>%n", minProduct);
        String sum = String.format("<html><body>%nSummary price: %n%s%n</body></html>%n", sumProduct);
        String count = String.format("<html><body>%nNumber of products: %n%s%n</body></html>%n", countProduct);

        testCommand("max", max);
        testCommand("min", min);
        testCommand("sum", sum);
        testCommand("count", count);
        testCommand("log", String.format("Unknown command: log%n"));
    }

    private void testCommand(String command, String expectedResponse) throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("command")).thenReturn(command);
        when(response.getWriter()).thenReturn(new ResponseWriter());

        new QueryServlet().doGet(request, response);
        assertEquals(expectedResponse, response.getWriter().toString());
    }
}