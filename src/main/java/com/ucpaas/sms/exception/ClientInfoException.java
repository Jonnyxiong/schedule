package com.ucpaas.sms.exception;

/**
 * @description 客户信息异常
 * @date 2017-08-16
 */
public class ClientInfoException extends  RuntimeException{


    private String errorCode;

    private String message;

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    // 两个参数都包含
    public ClientInfoException(String errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }

    // 只包含消息
    public ClientInfoException(String message) {
        super();
        this.message = message;
    }

    // 空的构造函数
    public ClientInfoException() {
        super();
    }

}
