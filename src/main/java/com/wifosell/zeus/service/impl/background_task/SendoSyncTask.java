package com.wifosell.zeus.service.impl.background_task;

import com.wifosell.lazada.modules.product.LazadaProductAPI;
import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.repository.ecom_sync.EcomAccountRepository;
import com.wifosell.zeus.service.SendoProductService;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.spring.annotations.Recurring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendoSyncTask {
    private static final Logger logger = LoggerFactory.getLogger(SendoSyncTask.class);

    @Autowired
    public EcomAccountRepository ecomAccountRepository;
    @Autowired
    public SendoProductService sendoProductService;

    @Recurring(id = "fetch-sendo-product", cron = "*/3 * * * *")
    @Job(name = "fetchSendoProductRecurring Task")
    public void fetchSendoProductRecurring() {
        List<EcomAccount> listEcoms  = ecomAccountRepository.findAllByEcomName(EcomAccount.EcomName.SENDO);
        try {
            listEcoms.stream().forEach( e ->  {
                logger.info("fetchSendoProductRecurring begin process  {}" , e.getId());
                sendoProductService.fetchAndSyncSendoProducts(e.getId());
                logger.info("fetchSendoProductRecurring end process  {}" , e.getId());
            });
        } catch (Exception exception) {
            exception.printStackTrace();
            logger.error("fetchSendoProductRecurring error ");
        }
    }
}
