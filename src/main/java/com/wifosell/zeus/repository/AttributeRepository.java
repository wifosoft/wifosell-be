package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.attribute.Attribute;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends SoftRepository<Attribute, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.ATTRIBUTE_NOT_FOUND;
    }
}
