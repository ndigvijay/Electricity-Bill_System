package com.electricity.billing.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.electricity.billing.system.entity.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

//    double findTotalAmountByMeterNumber(String meterNumber);
    @Query("SELECT SUM(totalAmount) FROM Bill b WHERE b.customer.meterNumber = :meterNumber")
    double findTotalAmountByMeterNumber(@Param("meterNumber") String meterNumber);

}
