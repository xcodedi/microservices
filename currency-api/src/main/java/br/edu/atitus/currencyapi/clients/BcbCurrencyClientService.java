package br.edu.atitus.currencyapi.clients;

import br.edu.atitus.currencyapi.dtos.BcbQuote;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class BcbCurrencyClientService {

    private final BcbCurrencyClient bcbCurrencyClient;

    public BcbCurrencyClientService(BcbCurrencyClient bcbCurrencyClient) {
        this.bcbCurrencyClient = bcbCurrencyClient;
    }

    @Cacheable(
            cacheNames = "bcb-quotes",
            key = "#currency + '-' + #quoteDate",
            unless = "#result == null"
    )
    @Retry(name = "bcb-api")
    @CircuitBreaker(name = "bcb-api")
    public Optional<BigDecimal> getSellRate(String currency, String quoteDate) {
        var response = bcbCurrencyClient.getQuotes(
                "'" + currency + "'",
                "'" + quoteDate + "'",
                "json"
        );

        if (response == null || response.value() == null) {
            return Optional.empty();
        }

        return response.value().stream()
                .filter(Objects::nonNull)
                .map(BcbQuote::cotacaoVenda)
                .filter(Objects::nonNull)
                .reduce((firstQuote, latestQuote) -> latestQuote);
    }
}