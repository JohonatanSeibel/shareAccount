package br.com.shareaccount.exception;

import org.springframework.http.HttpStatus;

public class EstablishmentException extends ConvertibleException{

   public EstablishmentException(String msg){super(msg);}

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
