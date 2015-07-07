package ua.george_nika.webtester.util;

import org.apache.log4j.Logger;

/**
 * Created by George on 08.06.2015.
 */
public class WebTesterLogger {

    public static void info(String logger, String message) {
        Logger log = Logger.getLogger(logger);
        log.info(message);
    }

    public static void error(String logger, String message) {
        Logger log = Logger.getLogger(logger);
        log.error(message);
    }

    public static void warn(String logger, String message) {
        Logger log = Logger.getLogger(logger);
        log.warn(message);
    }



}
