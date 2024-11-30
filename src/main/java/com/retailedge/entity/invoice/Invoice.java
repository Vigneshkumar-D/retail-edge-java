package com.retailedge.entity.invoice;

import com.retailedge.converter.PaymentMethodListConverter;
import com.retailedge.entity.credit.CreditReminder;
import com.retailedge.entity.customer.Customer;
import com.retailedge.entity.emi.EMIDetails;
import com.retailedge.entity.user.User;
import com.retailedge.enums.invoice.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
//
//
//
//
//
//@Entity
//@Table(name = "invoice")
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//public class Invoice {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @Column(name = "invoice_number", unique = true)
//    private String invoiceNumber;
//
//    @Column(name = "invoice_date", nullable = false)
//    private Instant invoiceDate;
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "invoice_id")
//    private List<InvoiceLineItem> lineItems;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "credit_reminder_id")
//    private CreditReminder creditReminder;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "emi_details_id")
//    private EMIDetails emiDetails;
//
//    @Column(name = "total_amount", nullable = false)
//    private Double totalAmount;
//
//    @Column(name = "sgst_amount", nullable = false)
//    private Double sgstAmount;
//
//    @Column(name = "cgst_amount", nullable = false)
//    private Double cgstAmount;
//
//    @Column(name = "igst_amount", nullable = false)
//    private Double igstAmount;
//
//    @Column(name = "round_off")
//    private Double roundOff;
//
//    @Column(name = "total_tax_amount", nullable = false)
//    private Double totalTaxAmount;
//
//    @Column(name = "tot_discount_amount")
//    private Double totDiscountAmount;
//
//    @Column(name = "payment_method", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private List<PaymentMethod> paymentMethod;
//
//    @ManyToOne(cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "customer_id")
//    private Customer customer;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    private User userId;
//
//    //Cash
//    private Double cashPayment;
//
//    //UPI
//    private String upiId;
//    private String upiTransactionId;
//    private String upiApp;
//    private Double upiPayment;
//
//    //Card
//    private String cardHolderName;
//    private String cardType;
//    private String cardTransactionId;
//    private Double cardPayment;
//
//
//}
//
//

@Entity
@Table(name = "invoice")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "invoice_number", unique = true)
    private String invoiceNumber;

    @Column(name = "invoice_date", nullable = false)
    private Instant invoiceDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "invoice_id")
    private List<InvoiceLineItem> lineItems;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "credit_reminder_id")
    private CreditReminder creditReminder;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "emi_details_id")
    private EMIDetails emiDetails;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "sgst_amount", nullable = false)
    private Double sgstAmount;

    @Column(name = "cgst_amount", nullable = false)
    private Double cgstAmount;

    @Column(name = "igst_amount", nullable = false)
    private Double igstAmount;

    @Column(name = "round_off", nullable = false)
    private Double roundOff = 0.0;

    @Column(name = "total_tax_amount", nullable = false)
    private Double totalTaxAmount;

    @Column(name = "tot_discount_amount")
    private Double totDiscountAmount;

//    @ElementCollection(fetch = FetchType.LAZY)
//    @CollectionTable(name = "invoice_payment_methods", joinColumns = @JoinColumn(name = "invoice_id"))
//    @Column(name = "payment_method")
    @Convert(converter = PaymentMethodListConverter.class)
    @Column(columnDefinition = "TEXT[]")
    private List<PaymentMethod> paymentMethod;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User soldBy;

    // Cash
    private Double cashPayment;

    // UPI
    private String upiId;
    private String upiTransactionId;
    private String upiApp;
    private Double upiPayment;

    // Card
    private String cardHolderName;
    private String cardType;
    private String cardTransactionId;
    private Double cardPayment;

}

