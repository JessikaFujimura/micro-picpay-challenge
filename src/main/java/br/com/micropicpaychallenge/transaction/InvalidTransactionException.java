package br.com.micropicpaychallenge.transaction;

public class InvalidTransactionException extends RuntimeException{

    public InvalidTransactionException(String message){
        super(message);
    }
    
}
