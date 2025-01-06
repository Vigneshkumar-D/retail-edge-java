package com.retailedge.service.settlement;

import com.retailedge.dto.expense.ExpenseDto;
import com.retailedge.dto.invoice.InvoiceDto;
import com.retailedge.dto.settelement.SettlementDto;
import com.retailedge.entity.expense.Expense;
import com.retailedge.entity.invoice.Invoice;
import com.retailedge.entity.service.PaidService;
import com.retailedge.entity.settlement.Settlement;
import com.retailedge.entity.suppiler.PurchaseOrder;
import com.retailedge.entity.suppiler.Supplier;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.expense.ExpenseRepository;
import com.retailedge.repository.invoice.InvoiceRepository;
import com.retailedge.repository.service.PaidServiceRepository;
import com.retailedge.repository.settlement.SettlementRepository;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<ResponseModel<?>> calculateSettlement(LocalDate date) {
        try {
            Double serviceAmount = calculatePaidServices(date);
            Double expenseAmount = calculateExpenses(date);
            Map<String, Double> invoiceAmount = calculateSales(date);
            invoiceAmount.put("serviceAmount", serviceAmount);
            invoiceAmount.put("expenseAmount", expenseAmount);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, invoiceAmount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error calculating settlement for the date: "+date+" "+ (e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> list() {
        try {
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, settlementRepository.findAll()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving settlement details: " + (e.getMessage()), 500));
        }
    }

    public Double calculatePaidServices(LocalDate date) {
        List<PaidService> paidServiceList = paidServiceRepository.findServiceByExactDate(date);
        ZoneId zoneId = ZoneId.systemDefault();
        Double advancesToday = paidServiceList.stream()
                .filter(service -> service.getServiceDate() != null &&
                        service.getServiceDate().atZone(zoneId).toLocalDate().equals(date))
                .mapToDouble(PaidService::getAdvancePayment)
                .sum();

        // Filter and sum post-service payments for today
        Double paymentsToday = paidServiceList.stream()
                .filter(service -> service.getServiceCompletionDate() != null &&
                        service.getServiceCompletionDate().atZone(zoneId).toLocalDate().equals(date))
                .mapToDouble(PaidService::getPostServicePayment)
                .sum();

        // Return total paid services for today (advances + post-service payments)
        return advancesToday + paymentsToday;
    }

    public Map<String, Double> calculateSales(LocalDate date) {
        // Fetch invoices for today's date
        List<InvoiceDto> invoiceList = invoiceRepository.findInvoiceByExactDate(date);

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

    public Double calculateExpenses(LocalDate date){
        List<Expense> expenseList = expenseRepository.findExpenseByExactDate(date);
        return expenseList.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public ResponseEntity<ResponseModel<?>> add(SettlementDto settlementDto) {
        try {
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.getConfiguration().setPropertyCondition(conditions -> conditions.getSource() != null);
             Settlement settlement = modelMapper.map(settlementDto, Settlement.class);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, settlementRepository.save(settlement)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding settlement details: " + (e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> update(Integer settlementId, SettlementDto settlementDto) {
        try{
            Settlement settlement = settlementRepository.findById(settlementId).orElse(null);
            if (settlement == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Settlement Details not found", 500));
            }
            modelMapper.map(settlementDto, settlement);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, settlementRepository.save(settlement)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error updating settlement details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer settlementId) throws Exception {
        try {
            if (!settlementRepository.existsById(settlementId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Settlement details not found", 404));
            }
            settlementRepository.deleteById(settlementId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Settlement: " + e.getMessage(), 500));
        }
    }

}
