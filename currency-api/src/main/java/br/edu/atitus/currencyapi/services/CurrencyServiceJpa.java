package br.edu.atitus.currencyapi.services;

import br.edu.atitus.currencyapi.dtos.CurrencyResponse;
import br.edu.atitus.currencyapi.entities.CurrencyEntity;
import br.edu.atitus.currencyapi.repositories.CurrencyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("currencyServiceJpa")
public class CurrencyServiceJpa implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Value("${server.port:8100}")
    private String serverPort;

    public CurrencyServiceJpa(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public CurrencyResponse findBySourceCurrencyAndTargetCurrency(
            String sourceCurrency,
            String targetCurrency
    ) throws Exception {

        CurrencyEntity currency = currencyRepository
                .findBySourceCurrencyAndTargetCurrency(sourceCurrency, targetCurrency)
                .orElseThrow(() -> new EntityNotFoundException("Currency not found for " + sourceCurrency + " to " + targetCurrency));

        String environment = "Currency API running in port " + serverPort;

        return new CurrencyResponse(
                currency.getSourceCurrency(),
                currency.getTargetCurrency(),
                currency.getConversionRate(),
                environment
        );
    }
}