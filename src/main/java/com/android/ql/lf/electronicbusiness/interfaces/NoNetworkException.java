package com.android.ql.lf.electronicbusiness.interfaces;

/**
 * Created by lf on 2017/12/20 0020.
 *
 * @author lf on 2017/12/20 0020
 */

public class NoNetworkException extends Exception {

    private String message;

    public NoNetworkException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
