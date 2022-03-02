package com.wifosell.zeus.repository;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepository extends SoftDeleteCrudRepository<OptionModel, Long> {
    @Transactional
    @Query("select opt from OptionModel opt where opt.isActive = true and opt.generalManager.id = ?1")
    List<OptionModel> findOptionsByGeneralManagerId(Long generalManagerId);

    default OptionModel findOptionById(Long optionId) {
        return this.findOptionById(optionId, false);
    }

    default OptionModel findOptionById(Long optionId, boolean includeInactive) {
        Optional<OptionModel> optionalOption = includeInactive ? this.findById(optionId) : this.findOne(optionId);
        return optionalOption.orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.OPTION_NOT_FOUND))
        );
    }
}
