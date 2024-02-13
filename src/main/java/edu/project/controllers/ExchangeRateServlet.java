package edu.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.project.dto.ExchangeRateDto;
import edu.project.services.ExchangeRateService;
import edu.project.services.ExchangeRateServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService service = ExchangeRateServiceImpl.getExchangeRateService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = getCodeFromUri(req);
        ExchangeRateDto rateDto = service.getElementByCode(code);
        String jsonResponse = mapper.writeValueAsString(rateDto);
        resp.getWriter().write(jsonResponse);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = getCodeFromUri(req);
        String rate = req.getParameter("rate");
        ExchangeRateDto rateDto = service.updateElement(code, rate);
        String jsonResponse = mapper.writeValueAsString(rateDto);
        resp.getWriter().write(jsonResponse);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private String getCodeFromUri(HttpServletRequest req) {
        String uri = req.getRequestURI();
        return uri.substring("/exchangeRate/".length()).toUpperCase();
    }
}
