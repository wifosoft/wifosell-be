package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.option.OptionValue;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionValueRepository extends SoftRepository<OptionValue, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.OPTION_VALUE_NOT_FOUND;
    }
}
