package edu.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.project.dto.ExchangeRateDto;
import edu.project.services.ExchangeRateService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRateService service;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        service = (ExchangeRateService) config.getServletContext().getAttribute("rateService");
        mapper = (ObjectMapper) config.getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRateDto> rateList = service.getAllElements();
        String jsonResponse = mapper.writeValueAsString(rateList);
        resp.getWriter().write(jsonResponse);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCode = req.getParameter("baseCurrencyCode").toUpperCase();
        String targetCode = req.getParameter("targetCurrencyCode").toUpperCase();
        String rate = req.getParameter("rate");
        ExchangeRateDto rateDto = service.addElement(baseCode, targetCode, rate);
        String jsonResponse = mapper.writeValueAsString(rateDto);
        resp.getWriter().write(jsonResponse);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}
