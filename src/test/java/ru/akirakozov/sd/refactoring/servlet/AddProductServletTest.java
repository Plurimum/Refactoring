package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.dao.ProductDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddProductServletTest {
    @Test
    void testAddProduct() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(new ResponseWriter());
        when(request.getParameter("name")).thenReturn("milk");
        when(request.getParameter("price")).thenReturn("50");

        new AddProductServlet(new ProductDao()).doGet(request, response);
        assertEquals("OK", response.getWriter().toString().trim());
    }
}
