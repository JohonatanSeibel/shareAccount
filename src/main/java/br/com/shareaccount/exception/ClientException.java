package br.com.shareaccount.exception;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;

public class ClientException extends ConvertibleException {

    public ClientException(String msg){super(msg);}

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
