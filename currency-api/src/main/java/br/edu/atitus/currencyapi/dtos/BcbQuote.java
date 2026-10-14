package br.edu.atitus.currencyapi.dtos;

import java.math.BigDecimal;

public record BcbQuote(
        BigDecimal cotacaoCompra,
        BigDecimal cotacaoVenda,
        String dataHoraCotacao
) {}