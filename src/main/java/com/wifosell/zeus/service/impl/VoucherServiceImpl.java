package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.voucher.Voucher;
import com.wifosell.zeus.payload.request.voucher.VoucherRequest;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.VoucherRepository;
import com.wifosell.zeus.service.VoucherService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service("Voucher")
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;

    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository, UserRepository userRepository) {
        this.voucherRepository = voucherRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    @Override
    public List<Voucher> getVouchersByUserId(Long userId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return voucherRepository.findVouchersByGeneralManagerId(gm.getId());
    }

    @Override
    public List<Voucher> getVouchersByShopId(Long shopId) {
        return voucherRepository.findVouchersByShopId(shopId);
    }

    @Override
    public Voucher getVoucher(Long voucherId) {
        return voucherRepository.findVoucherById(voucherId);
    }

    @Override
    public Voucher addVoucher(Long userId, VoucherRequest voucherRequest) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Voucher voucher = new Voucher();
        this.updateVoucherByRequest(voucher, voucherRequest);
        voucher.setGeneralManager(gm);
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher updateVoucher(Long voucherId, VoucherRequest voucherRequest) {
        Voucher voucher = voucherRepository.findVoucherById(voucherId);
        this.updateVoucherByRequest(voucher, voucherRequest);
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher activateVoucher(Long voucherId) {
        Voucher voucher = voucherRepository.findVoucherById(voucherId, true);
        voucher.setIsActive(true);
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher deactivateVoucher(Long voucherId) {
        Voucher voucher = voucherRepository.findVoucherById(voucherId);
        voucher.setIsActive(false);
        return voucherRepository.save(voucher);
    }

    @Override
    public List<Voucher> activateVouchers(@NonNull Long userId, @NonNull List<Long> voucherIds) {
        return voucherIds.stream().map(this::activateVoucher).collect(Collectors.toList());
    }

    @Override
    public List<Voucher> deactivateVouchers(@NonNull Long userId, @NonNull List<Long> voucherIds) {
        return voucherIds.stream().map(this::deactivateVoucher).collect(Collectors.toList());
    }

    private void updateVoucherByRequest(Voucher voucher, VoucherRequest voucherRequest) {
        //Optional.ofNullable(voucherRequest.getName()).ifPresent(voucher::setName);
        //Optional.ofNullable(voucherRequest.getShortName()).ifPresent(voucher::setShortName);
        Optional.ofNullable(voucherRequest.getDescription()).ifPresent(voucher::setDescription);
        Optional.ofNullable(voucherRequest.getActive()).ifPresent(voucher::setIsActive);
    }
}
