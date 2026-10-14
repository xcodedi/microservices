package br.edu.atitus.currencyapi.clients;

public class BcbQuoteUnavailableException extends RuntimeException {

    public BcbQuoteUnavailableException(String message) {
        super(message);
    }
}