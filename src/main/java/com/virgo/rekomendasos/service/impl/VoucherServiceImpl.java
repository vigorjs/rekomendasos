package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.repo.VoucherRepository;
import com.virgo.rekomendasos.service.VoucherService;
import com.virgo.rekomendasos.utils.dto.VoucherConvert;
import com.virgo.rekomendasos.utils.dto.VoucherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;

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
    public Page<Voucher> getAll(Pageable pageable) {
        return voucherRepository.findAll(pageable);
    }

    @Override
    public Voucher getById(Integer id) {
        return voucherRepository.findById(id).orElseThrow(() -> new RuntimeException("Voucher Not Found"));
    }

    @Override
    public Voucher updateById(Integer id, VoucherDTO updatedVoucher) {
        Voucher selectedVoucher = getById(id);
        if (!updatedVoucher.getName().isEmpty()) selectedVoucher.setName(updatedVoucher.getName());
        if (updatedVoucher.getPrice().describeConstable().isPresent()) selectedVoucher.setPrice(updatedVoucher.getPrice());
        if (updatedVoucher.getQuantity().describeConstable().isPresent()) selectedVoucher.setQuantity(updatedVoucher.getQuantity());
        return voucherRepository.save(selectedVoucher);
    }

    @Override
    public void deleteById(Integer id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public Voucher use(Integer id, Integer quantity) {
        Voucher selectedVoucher = getById(id);
        selectedVoucher.setQuantity(selectedVoucher.getQuantity() - quantity);
        return updateById(id, VoucherConvert.toDTO(selectedVoucher));
    }
}
