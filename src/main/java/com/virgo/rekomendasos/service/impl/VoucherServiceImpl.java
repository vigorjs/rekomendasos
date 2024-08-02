package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import com.virgo.rekomendasos.repo.VoucherRepository;
import com.virgo.rekomendasos.repo.VoucherTransactionRepository;
import com.virgo.rekomendasos.service.AuthenticationService;
import com.virgo.rekomendasos.service.VoucherService;
import com.virgo.rekomendasos.utils.dto.VoucherDTO;
import com.virgo.rekomendasos.utils.specification.VoucherTransactionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherTransactionRepository voucherTransactionRepository;
    private final AuthenticationService authenticationService;

    @Override
    public Voucher create(VoucherDTO newVoucher) {
        return voucherRepository.save(
                Voucher.builder()
                        .name(newVoucher.getName())
                        .price(newVoucher.getPrice() > 0 ? newVoucher.getPrice() : 0)
                        .quantity(newVoucher.getQuantity() > 0 ? newVoucher.getQuantity() : 0)
                        .build()
                );
    }

    @Override
    public Page<Voucher> findAll(Pageable pageable) {
        Page<Voucher> voucherPage = voucherRepository.findAll(pageable);
        if (voucherPage.getNumberOfElements() == 0) {
            throw new RuntimeException("Voucher Not Found");
        }
        return voucherPage;
    }

    @Override
    public Page<Voucher> findAllVoucherByUserId(Pageable pageable) {
        Specification<VoucherTransaction> spec = VoucherTransactionSpecification.voucherByUser(authenticationService.getUserAuthenticated());
        Page<VoucherTransaction> voucherTransactions = voucherTransactionRepository.findAll(spec, pageable);
        List<Voucher> vouchers = voucherTransactions.getContent().stream()
                .map(VoucherTransaction::getVoucher)
                .toList();
        return new PageImpl<>(vouchers, pageable, voucherTransactions.getTotalElements());
    }

    @Override
    public Voucher findById(Integer id) {
        return voucherRepository.findById(id).orElseThrow(() -> new RuntimeException("Voucher Not Found"));
    }

    @Override
    public Voucher findByUserIdAndId(Integer id) {
        Specification<VoucherTransaction> spec = VoucherTransactionSpecification.voucherByUser(authenticationService.getUserAuthenticated());
        List<VoucherTransaction> voucherTransactions = voucherTransactionRepository.findAll(spec);
        List<Voucher> vouchers = voucherTransactions.stream()
                .map(VoucherTransaction::getVoucher)
                .toList();
        AtomicBoolean isVoucherFound = new AtomicBoolean(false);
        vouchers.forEach(voucher -> {
            if (voucher.getId().equals(id)) isVoucherFound.set(true);
        });
        if (!isVoucherFound.get()) {
            throw new RuntimeException("Voucher Not Found");
        }
        return voucherRepository.findById(id).orElseThrow(() -> new RuntimeException("Voucher Not Found"));
    }


    @Override
    public Voucher updateById(Integer id, VoucherDTO updatedVoucher) {
        Voucher selectedVoucher = findById(id);
        if (!updatedVoucher.getName().isEmpty()) selectedVoucher.setName(updatedVoucher.getName());
        if (updatedVoucher.getPrice().describeConstable().isPresent()) selectedVoucher.setPrice(updatedVoucher.getPrice());
        if (updatedVoucher.getQuantity().describeConstable().isPresent()) selectedVoucher.setQuantity(updatedVoucher.getQuantity());
        return voucherRepository.save(selectedVoucher);
    }

    @Override
    public void deleteById(Integer id) {
        voucherRepository.deleteById(id);
    }
}
