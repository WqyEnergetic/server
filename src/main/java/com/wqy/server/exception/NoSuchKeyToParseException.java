package com.wqy.server.exception;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/7/22 22:53
 */
public class NoSuchKeyToParseException extends RuntimeException{

    public NoSuchKeyToParseException(){
    }

    public NoSuchKeyToParseException(String message){
        super(message);
    }
}
