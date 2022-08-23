package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.option.OptionModel;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends SoftRepository<OptionModel, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.OPTION_NOT_FOUND;
    }
}
