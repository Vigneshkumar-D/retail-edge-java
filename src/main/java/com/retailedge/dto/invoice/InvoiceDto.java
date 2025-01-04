package com.retailedge.dto.invoice;

import com.retailedge.dto.credit.CreditReminderDto;
import com.retailedge.dto.customer.CustomerDto;
import com.retailedge.dto.emi.EMIDetailsDto;
import com.retailedge.entity.user.User;
import com.retailedge.enums.invoice.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InvoiceDto {
    private String invoiceNumber;
    private Instant invoiceDate;
    private List<InvoiceLineItemDto> lineItems;
    private Double totalAmount;
    private Double sgstAmount;
    private Double cgstAmount;
    private Double igstAmount;
    private Double totalTaxAmount;
    private Double roundOff;
    private Double totDiscountAmount;
    private List<PaymentMethod> paymentMethod;
    private CreditReminderDto creditReminder;
    private EMIDetailsDto emiDetails;
    private CustomerDto customer;

    private User soldBy;

    private Double pricePerUnit;

    private String description;

    //Cash
    private Double cashPayment;

    //UPI
    private String upiId;
    private String upiTransactionId;
    private String upiApp;
    private Double upiPayment;

    //Card
    private String cardHolderName;
    private String cardType;
    private String cardTransactionId;
    private Double cardPayment;
    private Double remainingBalance;

    public InvoiceDto(Integer id, String invoiceNumber, Instant invoiceDate, Double totalAmount,
                      Double cashPayment, Double upiPayment, Double cardPayment, Double remainingBalance) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.cashPayment = cashPayment;
        this.upiPayment = upiPayment;
        this.cardPayment = cardPayment;
        this.remainingBalance = remainingBalance;
    }

}
