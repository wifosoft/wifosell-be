package com.wifosell.zeus.repository;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.option.Option;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepository extends SoftDeleteCrudRepository<Option, Long> {
    @Transactional
    @Query("select opt from Option opt where opt.isActive = true and opt.generalManager.id = ?1")
    List<Option> findOptionsByGeneralManagerId(Long generalManagerId);

    default Option findOptionById(Long optionId) {
        return this.findOptionById(optionId, false);
    }

    default Option findOptionById(Long optionId, boolean includeInactive) {
        Optional<Option> optionalOption = includeInactive ? this.findById(optionId) : this.findOne(optionId);
        return optionalOption.orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.OPTION_NOT_FOUND))
        );
    }
}
