package com.spring.techserv.dataClient;

import feign.RetryableException;
import feign.Retryer;

public class CustomRetryer implements Retryer {

    private int attempt = 1;
    private final int maxAttempts = 3;

    @Override
    public void continueOrPropagate(RetryableException e) {
        if (attempt++ >= maxAttempts) {
            throw e;
        }
        try {
            Thread.sleep(1000);// Задержка между повторными попытками
            System.out.println("повторная отправка");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Retryer clone() {
        return new CustomRetryer();
    }
}
