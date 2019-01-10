package com.castellan;

import java.math.BigDecimal;

public class Test {

    public static void main(String[] args) {
        BigDecimal b1 = new BigDecimal("0.5");
        System.out.println(b1.multiply(new BigDecimal(new Integer(5))));
    }
}
