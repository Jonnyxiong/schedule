package com.ucpaas.sms.service.reportRepush;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;


public abstract class AbstractTask implements Callable<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportRepushServiceImpl.class);

    @Override
    public String call() {

        String result = null;
        try{
            result = doAction();
        }catch(Exception e){
            LOGGER.error("AbstractTask.call catch exception = {}", e);
        }
        return result;

    }

    public abstract String doAction() throws Exception;

}
