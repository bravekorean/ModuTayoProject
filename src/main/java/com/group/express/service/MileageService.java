package com.group.express.service;

import com.group.express.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MileageService {

    public int insertMileage (int mileage, int paidAmount) {

        int bonusMileage = (int) (paidAmount * 0.05); // 결제한 금액의 5% 마일리지 적립

        int totalMileage = mileage + bonusMileage;


        return totalMileage;
    }

    public int rollbackMileage (int usedMileage, int OwendMileage) {

        double refundAmount = usedMileage * 0.8; // 사용한 마일리지의 80%만 환불

        int newOwnedMileage = OwendMileage + (int)refundAmount; // 환불 후의 마일리지

        return newOwnedMileage;
    }

}
