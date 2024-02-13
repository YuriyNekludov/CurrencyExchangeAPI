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
import java.math.BigDecimal;
import java.util.Collections;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeRateService service = ExchangeRateServiceImpl.getExchangeRateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String baseCode = req.getParameter("from");
        String targetCode = req.getParameter("to");
        String amount = req.getParameter("amount");
        ExchangeRateDto rateDto = service.getExchangeRateForConversion(baseCode, targetCode);
        BigDecimal amountValue = service.amountExchange(rateDto, amount);
        String jsonResponse = mapper.writeValueAsString(rateDto);
        String jsonAmount = mapper.writeValueAsString(Collections.singletonMap("amount", amount));
        String jsonAmountValue = mapper.writeValueAsString(Collections.singletonMap("convertedAmount", amountValue));
        resp.getWriter().write(jsonResponse + '\n' + jsonAmount + '\n' + jsonAmountValue);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
