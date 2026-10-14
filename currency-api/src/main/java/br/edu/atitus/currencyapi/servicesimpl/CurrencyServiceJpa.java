package br.edu.atitus.currencyapi.servicesimpl;

import br.edu.atitus.currencyapi.clients.BcbCurrencyClientService;
import br.edu.atitus.currencyapi.dtos.CurrencyResponse;
import br.edu.atitus.currencyapi.repositories.CurrencyRepository;
import br.edu.atitus.currencyapi.services.CurrencyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceJpa implements CurrencyService {

    private final CurrencyRepository repository;
    private final BcbCurrencyClientService bcbCurrencyClientService;

    @Value("${server.port:8100}")
    private String serverPort;

    public CurrencyServiceJpa(CurrencyRepository repository, BcbCurrencyClientService bcbCurrencyClientService) {
        this.repository = repository;
        this.bcbCurrencyClientService = bcbCurrencyClientService;
    }

    @Override
    public CurrencyResponse findBySourceCurrencyAndTargetCurrency(String sourceCurrency, String targetCurrency) throws Exception {
        String source = sourceCurrency.toUpperCase();
        String target = targetCurrency.toUpperCase();

        // Data útil com cotação garantida na API do BCB (MM-DD-YYYY)
        String defaultDate = "09-20-2024";

        try {
            if ("BRL".equals(target) && !"BRL".equals(source)) {
                var rateOptional = bcbCurrencyClientService.getSellRate(source, defaultDate);

                if (rateOptional.isPresent()) {
                    double rate = rateOptional.get().doubleValue();
                    String env = "Currency API running in port " + serverPort + " (BCB Source)";
                    return new CurrencyResponse(source, target, rate, env);
                }
            }
        } catch (Exception ex) {
            // Em caso de erro de rede, timeout ou circuit breaker aberto, segue para o fallback local
        }

        // FALLBACK: Consulta no banco PostgreSQL local
        var currency = repository.findBySourceCurrencyAndTargetCurrency(source, target)
                .orElseThrow(() -> new EntityNotFoundException("Currency não encontrado no BCB nem no banco local"));

        String environment = "Currency API running in port " + serverPort + " (Local DB Fallback)";
        return new CurrencyResponse(currency.getSourceCurrency(), currency.getTargetCurrency(), currency.getConversionRate(), environment);
    }
}