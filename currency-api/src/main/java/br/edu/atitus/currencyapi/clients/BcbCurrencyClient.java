package br.edu.atitus.currencyapi.clients;

import br.edu.atitus.currencyapi.dtos.BcbQuoteResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface BcbCurrencyClient {

    @GetExchange("/CotacaoMoedaDia(moeda=@moeda,dataCotacao=@dataCotacao)")
    BcbQuoteResponse getQuotes(
            @RequestParam(name = "@moeda") String currency,
            @RequestParam(name = "@dataCotacao") String quoteDate,
            @RequestParam(name = "$format") String format
    );
}