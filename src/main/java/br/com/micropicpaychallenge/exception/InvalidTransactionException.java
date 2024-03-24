package br.com.micropicpaychallenge.exception;

public class InvalidTransactionException extends RuntimeException{

    public InvalidTransactionException(String message){
        super(message);
    }
    
}
