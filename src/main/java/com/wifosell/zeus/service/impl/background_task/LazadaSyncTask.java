package com.wifosell.zeus.service.impl.background_task;

import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.repository.ecom_sync.EcomAccountRepository;
import com.wifosell.zeus.service.LazadaProductService;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.spring.annotations.Recurring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LazadaSyncTask {
    private static final Logger logger = LoggerFactory.getLogger(LazadaSyncTask.class);

    @Autowired
    public EcomAccountRepository ecomAccountRepository;
    @Autowired
    public LazadaProductService lazadaProductService;

    @Recurring(id = "auto-fetch-lazada-products", cron = "*/5 * * * *")
    @Job(name = "autoFetchLazadaProducts Task")
    public void autoFetchLazadaProducts() {
        List<EcomAccount> ecomAccounts = ecomAccountRepository.findAllByEcomName(EcomAccount.EcomName.LAZADA);
        ecomAccounts.forEach(ecomAccount -> {
            lazadaProductService.autoFetchLazadaProducts(ecomAccount);
        });
    }
}
