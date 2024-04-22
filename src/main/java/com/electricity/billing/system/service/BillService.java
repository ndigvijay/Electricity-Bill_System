package com.electricity.billing.system.service;

import com.electricity.billing.system.entity.Bill;
import com.electricity.billing.system.entity.CustomerModel;
import com.electricity.billing.system.util.CustomerNotFoundException;

public interface BillService {
 void generateBill(String meterNumber, String month, int unitsConsumed) throws CustomerNotFoundException;

 boolean addBillForCustomerBillForCustomer(Bill bill);

 CustomerModel findCustomerByMeterNumber(String meterNumber);

 double findTotalAmountByMeterNumber(String meterNumber);

 CustomerModel findCustomerDetailsByMeterNumber(String meterNumber);
// double findTotalAmountByMeterNumber(String meterNumber);

}

