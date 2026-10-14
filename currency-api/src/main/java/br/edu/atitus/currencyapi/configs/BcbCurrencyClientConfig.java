package br.edu.atitus.currencyapi.configs;

import br.edu.atitus.currencyapi.clients.BcbCurrencyClient;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class BcbCurrencyClientConfig {

    @Bean
    BcbCurrencyClient getBcbCurrencyClient(RestClient.Builder builder) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(2));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));

        RestClient restClient = builder
                .baseUrl("https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata")
                .requestFactory(requestFactory)
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter).build();

        return factory.createClient(BcbCurrencyClient.class);
    }
}