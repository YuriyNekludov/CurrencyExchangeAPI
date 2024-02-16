package edu.project.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.project.dto.CurrencyDto;
import edu.project.services.CurrencyServiceImpl;
import edu.project.services.ExchangeRateService;
import edu.project.services.ExchangeRateServiceImpl;
import edu.project.services.Service;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConnectionManager.initPool();
        ScriptReader.initScripts();
        ServletContext context = sce.getServletContext();
        Service<CurrencyDto> currencyService = CurrencyServiceImpl.getCurrencyService();
        ObjectMapper objectMapper = new ObjectMapper();
        ExchangeRateService rateService = ExchangeRateServiceImpl.getExchangeRateService();
        context.setAttribute("currencyService", currencyService);
        context.setAttribute("objectMapper", objectMapper);
        context.setAttribute("rateService", rateService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ScriptReader.initDeleteScript();
        ConnectionManager.closeConnectionPool();
    }
}
