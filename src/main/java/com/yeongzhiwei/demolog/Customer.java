package com.yeongzhiwei.demolog;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {

    private String customerName;
    private String customerIdentifier;
    private String email;
    private String contactNumber;

}
