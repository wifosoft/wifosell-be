package com.wifosell.zeus.service.impl.batch_process;

import com.wifosell.zeus.service.BatchProductExcelService;
import org.springframework.stereotype.Service;

@Service
public class BatchProductExcelServiceImpl implements BatchProductExcelService {
    public void doSimpleJob(String anArgument) {
        System.out.println("Doing some work: " + anArgument);
    }
}
