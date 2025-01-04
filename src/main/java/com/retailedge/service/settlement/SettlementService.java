package com.retailedge.service.settlement;

import com.retailedge.dto.invoice.InvoiceDto;
import com.retailedge.entity.expense.Expense;
import com.retailedge.entity.invoice.Invoice;
import com.retailedge.entity.service.PaidService;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.expense.ExpenseRepository;
import com.retailedge.repository.invoice.InvoiceRepository;
import com.retailedge.repository.service.PaidServiceRepository;
import com.retailedge.repository.settlement.SettlementRepository;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SettlementService {

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PaidServiceRepository paidServiceRepository;

    public ResponseEntity<ResponseModel<?>> list() {
        try {
            Double serviceAmount = calculatePaidServices();
            Map<String, Double> invoiceAmount = calculateSales();
            Double expenseAmount = calculateExpenses();
            return ResponseEntity.ok(new ResponseModel<>(true, serviceAmount+" "+invoiceAmount+" "+expenseAmount, 200, settlementRepository.findAll()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving settlement details: " + (e.getMessage()), 500));
        }
    }

//    public Double calculatePaidServices() {
//
////        LocalDate today = LocalDate.now();
////        Instant instant = Instant.now();
////        // Define the start and end of the day
////        ZoneId zoneId = ZoneId.systemDefault(); // Use system default or specific time zone
////        Instant startOfDay = today.atStartOfDay(zoneId).toInstant(); // 12:00:00 AM
////        Instant endOfDay = today.atTime(23, 59, 59).atZone(zoneId).toInstant(); // 11:59:59 PM
//
//
//        List<PaidService> paidServiceList = paidServiceRepository.findAll();
//        System.out.println("service line "+paidServiceList.size());
//        // Calculate advances paid today
//        Double advancesToday = paidServiceList.stream()
////                .filter(service -> service.getServiceDate().isEqual(today))
//                .mapToDouble(PaidService::getAdvancePayment)
//                .sum();
//
//        // Calculate payments completed today
//        Double paymentsToday = paidServiceList.stream()
////                .filter(service -> service.getCompletionDate() != null && service.getCompletionDate().isEqual(today))
//                .mapToDouble(PaidService::getPostServicePayment)
//                .sum();
//
//        // Return total paid services for today
//        return advancesToday + paymentsToday;
//    }

    public Double calculatePaidServices() {
        System.out.println("startOfDay "+LocalDate.now());
        List<PaidService> paidServiceList = paidServiceRepository.findServiceByExactDate(LocalDate.now());
        System.out.println("service line " + paidServiceList.size());

        LocalDate today = LocalDate.now();
        ZoneId zoneId = ZoneId.systemDefault();

        Double advancesToday = paidServiceList.stream()
                .filter(service -> service.getServiceDate() != null &&
                        service.getServiceDate().atZone(zoneId).toLocalDate().equals(today))
                .mapToDouble(PaidService::getAdvancePayment)
                .sum();

        // Filter and sum post-service payments for today
        Double paymentsToday = paidServiceList.stream()
                .filter(service -> service.getServiceCompletionDate() != null &&
                        service.getServiceCompletionDate().atZone(zoneId).toLocalDate().equals(today))
                .mapToDouble(PaidService::getPostServicePayment)
                .sum();

        // Return total paid services for today (advances + post-service payments)
        return advancesToday + paymentsToday;
    }

//    public Double calculateSales(){
//        List<Object[]> invoiceList = invoiceRepository.findInvoiceByExactDate(LocalDate.now());
//
//        return invoiceList.stream()
//                .mapToDouble(InvoiceDto::getTotalAmount)
//                .sum();
//    }

    public Map<String, Double> calculateSales() {
        // Fetch invoices for today's date
        List<InvoiceDto> invoiceList = invoiceRepository.findInvoiceByExactDate(LocalDate.now());

        // Initialize variables for total sales and payment types
        double totalSales = 0.0;
        double totalCreditPayments = 0.0;
        double totalCashPayments = 0.0;
        double totalUpiPayments = 0.0;
        double totalCardPayments = 0.0;

        // Iterate over invoices to calculate totals
        for (InvoiceDto invoice : invoiceList) {
            double invoiceTotal = invoice.getTotalAmount();
            double cashPayment = invoice.getCashPayment() != null ? invoice.getCashPayment() : 0.0;
            double upiPayment = invoice.getUpiPayment() != null ? invoice.getUpiPayment() : 0.0;
            double cardPayment = invoice.getCardPayment() != null ? invoice.getCardPayment() : 0.0;

            // Check if there's a credit payment and adjust accordingly
            if (invoice.getCreditReminder() != null) {
                totalCreditPayments += invoice.getCreditReminder().getRemainingBalance();
            }

            // Add to payment type totals
            totalCashPayments += cashPayment;
            totalUpiPayments += upiPayment;
            totalCardPayments += cardPayment;

            // Add to total sales
            totalSales += invoiceTotal;
        }

        // Subtract credit payments from total sales
        double netSales = totalSales - totalCreditPayments;

        // Return the results as a map
        Map<String, Double> salesSummary = new HashMap<>();
        salesSummary.put("Total Sales", netSales);
        salesSummary.put("Cash Payments", totalCashPayments);
        salesSummary.put("UPI Payments", totalUpiPayments);
        salesSummary.put("Card Payments", totalCardPayments);
        salesSummary.put("Credit Adjustments", totalCreditPayments);

        return salesSummary;
    }


    public Double calculateExpenses(){
        List<Expense> expenseList = expenseRepository.findExpenseByExactDate(LocalDate.now());
        return expenseList.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

}