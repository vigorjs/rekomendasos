package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.model.enums.Gender;
import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import com.virgo.rekomendasos.repo.VoucherTransactionRepository;
import com.virgo.rekomendasos.service.UserService;
import com.virgo.rekomendasos.service.VoucherService;
import com.virgo.rekomendasos.service.VoucherTransactionService;
import com.virgo.rekomendasos.utils.dto.VoucherConvert;
import com.virgo.rekomendasos.utils.dto.VoucherDTO;
import com.virgo.rekomendasos.utils.dto.VoucherTransactionConvert;
import com.virgo.rekomendasos.utils.dto.VoucherTransactionDTO;
import com.virgo.rekomendasos.utils.specification.VoucherTransactionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoucherTransactionServiceImpl implements VoucherTransactionService {
    private final VoucherTransactionRepository voucherTransactionRepository;
    private final VoucherService voucherService;
    private final UserService userService;

    @Override
    public VoucherTransaction create(VoucherTransactionDTO newVoucherTransaction) {
        return voucherTransactionRepository.save(
                VoucherTransaction.builder()
                        .user(userService.getById(newVoucherTransaction.getUserId()))
                        .voucher(voucherService.getById(newVoucherTransaction.getVoucherId()))
                        .voucherQuantity(newVoucherTransaction.getVoucherQuantity())
                        .build()
        );
    }

    @Override
    public Page<VoucherTransaction> getAll(Pageable pageable) {
        return voucherTransactionRepository.findAll(pageable);
    }

    @Override
    public Page<VoucherTransaction> getAllByUserId(Pageable pageable, Integer userId) {
        Specification<VoucherTransaction> spec = VoucherTransactionSpecification.voucherByUser(userService.getById(userId));
        return voucherTransactionRepository.findAll(spec, pageable);
    }

    @Override
    public VoucherTransaction getById(Integer id) {
        return voucherTransactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Voucher Not Found"));
    }


    @Override
    public VoucherTransaction updateById(Integer id, VoucherTransactionDTO updatedVoucherTransactionDTO) {
        VoucherTransaction selectedVoucherTransaction = getById(id);
        if (updatedVoucherTransactionDTO.getUserId().describeConstable().isPresent()) selectedVoucherTransaction.setUser(userService.getById(updatedVoucherTransactionDTO.getUserId()));
        if (updatedVoucherTransactionDTO.getVoucherId().describeConstable().isPresent()) selectedVoucherTransaction.setVoucher(voucherService.getById(updatedVoucherTransactionDTO.getVoucherId()));
        if (updatedVoucherTransactionDTO.getVoucherQuantity().describeConstable().isPresent()) selectedVoucherTransaction.setVoucherQuantity(updatedVoucherTransactionDTO.getVoucherQuantity());
        return voucherTransactionRepository.save(selectedVoucherTransaction);
    }

    @Override
    public void deleteById(Integer id) {
        voucherTransactionRepository.deleteById(id);
    }

    @Override
    public VoucherTransaction use(Integer id, Integer quantity) {
        VoucherTransaction voucherTransaction = getById(id);
        voucherTransaction.setVoucherQuantity(voucherTransaction.getVoucherQuantity() - quantity);
        return updateById(id, VoucherTransactionConvert.toDTO(voucherTransaction));
    }
}
