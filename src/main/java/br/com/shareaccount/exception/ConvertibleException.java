package br.com.shareaccount.exception;

import org.springframework.http.HttpStatus;

public abstract class ConvertibleException extends RuntimeException {

    private static final long serialVersionUID = 5785438108800543953L;

    protected ConvertibleException(String msg) {
        super(msg);
    }

    protected ConvertibleException(String msg, Exception inner) {
        super(msg, inner);
    }

    public abstract HttpStatus getStatus();
}
