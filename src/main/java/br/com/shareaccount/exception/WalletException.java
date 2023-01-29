package br.com.shareaccount.exception;

import org.springframework.http.HttpStatus;

public class WalletException  extends ConvertibleException {

    public WalletException(String msg){super(msg);}
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
