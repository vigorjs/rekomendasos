package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import com.virgo.rekomendasos.repo.VoucherTransactionRepository;
import com.virgo.rekomendasos.service.AuthenticationService;
import com.virgo.rekomendasos.service.UserService;
import com.virgo.rekomendasos.service.VoucherService;
import com.virgo.rekomendasos.service.VoucherTransactionService;
import com.virgo.rekomendasos.utils.dto.VoucherTransactionConvert;
import com.virgo.rekomendasos.utils.dto.VoucherTransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoucherTransactionServiceImpl implements VoucherTransactionService {
    private final VoucherTransactionRepository voucherTransactionRepository;
    private final VoucherService voucherService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Override
    public VoucherTransaction create(VoucherTransactionDTO newVoucherTransaction) {
        // Kurang pengurangan point jika user take voucher
        return voucherTransactionRepository.save(
                VoucherTransaction.builder()
                        .user(authenticationService.getUserAuthenticated())
                        .voucher(voucherService.findById(newVoucherTransaction.getVoucherId()))
                        .voucherQuantity(newVoucherTransaction.getVoucherQuantity())
                        .build()
        );
    }

    @Override
    public Page<VoucherTransaction> findAll(Pageable pageable) {
        return voucherTransactionRepository.findAll(pageable);
    }

    @Override
    public VoucherTransaction findById(Integer id) {
        return voucherTransactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Voucher Not Found"));
    }

    @Override
    public VoucherTransaction updateById(Integer id, VoucherTransactionDTO updatedVoucherTransactionDTO) {
        VoucherTransaction selectedVoucherTransaction = findById(id);
        if (updatedVoucherTransactionDTO.getUserId().describeConstable().isPresent()) selectedVoucherTransaction.setUser(userService.getById(updatedVoucherTransactionDTO.getUserId()));
        if (updatedVoucherTransactionDTO.getVoucherId().describeConstable().isPresent()) selectedVoucherTransaction.setVoucher(voucherService.findById(updatedVoucherTransactionDTO.getVoucherId()));
        if (updatedVoucherTransactionDTO.getVoucherQuantity().describeConstable().isPresent()) selectedVoucherTransaction.setVoucherQuantity(updatedVoucherTransactionDTO.getVoucherQuantity());
        return voucherTransactionRepository.save(selectedVoucherTransaction);
    }

    @Override
    public void deleteById(Integer id) {
        voucherTransactionRepository.deleteById(id);
    }

    @Override
    public VoucherTransaction use(Integer id, Integer quantity) {
        VoucherTransaction voucherTransaction = findById(id);
        voucherTransaction.setVoucherQuantity(voucherTransaction.getVoucherQuantity() - quantity);
        return updateById(id, VoucherTransactionConvert.toDTO(voucherTransaction));
    }
}
