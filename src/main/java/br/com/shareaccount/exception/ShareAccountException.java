package br.com.shareaccount.exception;

import org.springframework.http.HttpStatus;

public class ShareAccountException  extends ConvertibleException{

    public ShareAccountException(String msg){super(msg);}

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
