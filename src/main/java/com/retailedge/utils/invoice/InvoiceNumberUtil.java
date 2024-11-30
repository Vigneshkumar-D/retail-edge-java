package com.retailedge.utils.invoice;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class InvoiceNumberUtil {
    public static String generateInvoiceNumber(Integer invoiceId) {
        String datePrefix = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return "INV-" + datePrefix + "-" + String.format("%04d", invoiceId);
    }
}
