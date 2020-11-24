package com.markstickel.helm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {

    private static final Logger logger = LoggerFactory.getLogger("EXEC");

    InputStream is;
    String type;

    StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null)
                log(type + "> " + line);
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
        }
    }

    private void log(String message, Object... args) {
        if ("INFO".equals(type)) {
            logger.info(message, args);
        } else {
            logger.error(message, args);
        }
    }
}