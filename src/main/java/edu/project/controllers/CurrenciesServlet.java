package edu.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.project.dto.CurrencyDto;
import edu.project.services.CurrencyService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrencyService service;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        service = (CurrencyService) config.getServletContext().getAttribute("currencyService");
        mapper = (ObjectMapper) config.getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CurrencyDto> currencies = service.getAllElements();
        String jsonResponse = mapper.writeValueAsString(currencies);
        resp.getWriter().write(jsonResponse);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        CurrencyDto currency = service.addElement(code, name, sign);
        String jsonResponse = mapper.writeValueAsString(currency);
        resp.getWriter().write(jsonResponse);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}
