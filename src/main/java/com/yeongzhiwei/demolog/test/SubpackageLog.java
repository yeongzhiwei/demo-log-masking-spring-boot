package com.yeongzhiwei.demolog.test;

import com.yeongzhiwei.demolog.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubpackageLog {

    private SubpackageLog() {}

    private static final Logger LOGGER = LoggerFactory.getLogger(SubpackageLog.class);

    public static void logSomething() {
        Customer customer = new Customer("Ah Beng Wee", "S1234567A", "alpha@demo.com", "98765432");
        LOGGER.info("{}", customer);
    }
}
