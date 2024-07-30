package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.model.meta.VoucherStock;
import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import com.virgo.rekomendasos.repo.UserRepository;
import com.virgo.rekomendasos.repo.VoucherStockRepository;
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
    private final VoucherStockRepository voucherStockRepository;
    private final VoucherService voucherService;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public VoucherTransaction create(VoucherTransactionDTO newVoucherTransaction) {
        Voucher voucher = voucherService.findById(newVoucherTransaction.getVoucherId());
        VoucherStock stock = voucher.getVoucherStock();

        // Check if the stock is sufficient
        if (stock.getQuantity() < newVoucherTransaction.getVoucherQuantity()) {
            throw new RuntimeException("Insufficient voucher stock");
        }

        // Update stock
        stock.setQuantity(stock.getQuantity() - newVoucherTransaction.getVoucherQuantity());
        voucherStockRepository.save(stock);

        User user = authenticationService.getUserAuthenticated();

        // Deduct points from user
        if (user.getPoint() < voucher.getPrice() * newVoucherTransaction.getVoucherQuantity()) {
            throw new RuntimeException("Insufficient points");
        }
        user.setPoint(user.getPoint() - voucher.getPrice() * newVoucherTransaction.getVoucherQuantity());
        userRepository.save(user);

        return voucherTransactionRepository.save(
                VoucherTransaction.builder()
                        .user(user)
                        .voucher(voucher)
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
        return voucherTransactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Voucher Transaction Not Found"));
    }

    @Override
    public VoucherTransaction updateById(Integer id, VoucherTransactionDTO updatedVoucherTransactionDTO) {
        VoucherTransaction selectedVoucherTransaction = findById(id);
        selectedVoucherTransaction.setUser(authenticationService.getUserAuthenticated());

        if (updatedVoucherTransactionDTO.getVoucherId().describeConstable().isPresent()) {
            selectedVoucherTransaction.setVoucher(voucherService.findById(updatedVoucherTransactionDTO.getVoucherId()));
        }
        if (updatedVoucherTransactionDTO.getVoucherQuantity().describeConstable().isPresent()) {
            selectedVoucherTransaction.setVoucherQuantity(updatedVoucherTransactionDTO.getVoucherQuantity());
        }
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
