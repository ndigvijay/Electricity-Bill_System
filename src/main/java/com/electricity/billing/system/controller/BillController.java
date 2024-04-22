package com.electricity.billing.system.controller;

import com.electricity.billing.system.service.BillService;
import com.electricity.billing.system.util.CustomerNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
//@RequestMapping("")
public class BillController {

    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/api/bills/generate")
    public ResponseEntity<String> generateBill(
            @RequestParam("meterNumber") String meterNumber,
            @RequestParam("month") String month,
            @RequestParam("unitsConsumed") int unitsConsumed) {
        try {
            billService.generateBill(meterNumber, month, unitsConsumed);
            return ResponseEntity.ok("Bill generated successfully for meter number: " + meterNumber + " for month: " + month);
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate bill. Please try again later.");
        }
    }
    @GetMapping("/admin/generatebill")
    public String GenerateBillpage(){
        return "generatebill.html";
    }

    @GetMapping("/api/bills/totalAmount")
    public ResponseEntity<Double> getTotalAmountByMeterNumber(@RequestParam("meterNumber") String meterNumber) {
        try {
            double totalAmount = billService.findTotalAmountByMeterNumber(meterNumber);
            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
