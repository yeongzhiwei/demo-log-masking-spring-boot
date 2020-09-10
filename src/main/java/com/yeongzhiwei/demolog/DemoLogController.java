package com.yeongzhiwei.demolog;

import com.yeongzhiwei.demolog.test.SubpackageLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoLogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoLogController.class);

    @GetMapping(path = "/log")
    public String doLog() {
        Customer customer = new Customer("Ah Beng Wee", "S1234567A", "alpha@demo.com", "98765432");
        LOGGER.info("{}", customer);

        LOGGER.info("Customer(customerName= Ah Boon Wee, customerIdentifier=\tS1234567A\t,email=boon@wee.com,contactNumber=12345678 )");

        SubpackageLog.logSomething();

        return "see logs";
    }

}
