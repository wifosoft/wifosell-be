package com.wifosell.zeus.service.impl.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.SendoCategory;
import com.wifosell.zeus.repository.ecom_sync.SendoCategoryRepository;
import com.wifosell.zeus.service.SendoCategoryService;
import com.wifosell.zeus.specs.SendoCategorySpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("SendoCategoryService")
@Transactional
@RequiredArgsConstructor
public class SendoCategoryServiceImpl implements SendoCategoryService {
    private final SendoCategoryRepository sendoCategoryRepository;

    @Override
    public List<SendoCategory> getLeafCategories() {
        return sendoCategoryRepository.findAll(
                SendoCategorySpecs.isLeaf()
        );
    }

    @Override
    public List<SendoCategory> getRootCategories() {
        return sendoCategoryRepository.findAll(
                SendoCategorySpecs.isRoot()
        );
    }
}
