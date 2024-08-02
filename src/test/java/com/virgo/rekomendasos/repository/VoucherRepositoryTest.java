package com.virgo.rekomendasos.repository;

import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.repo.VoucherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class VoucherRepositoryTest {

    @Autowired
    private VoucherRepository voucherRepository;

    @Test
    public void whenCreateVoucher_thenReturnCreatedVoucher() {
        Voucher voucher = Voucher.builder()
                .name("Makan Gratis")
                .price(10000)
                .build();
        voucherRepository.save(voucher);

        Voucher foundVoucher = voucherRepository.findById(voucher.getId()).orElse(null);

        assertThat(foundVoucher).isNotNull();
    }

    @Test
    public void whenGetAllVouchers_thenReturnAllVouchers() {
        Voucher voucher1 = Voucher.builder()
                .name("Voucher 1")
                .price(10000)
                .build();
        Voucher voucher2 = Voucher.builder()
                .name("Voucher 2")
                .price(20000)
                .build();
        voucherRepository.save(voucher1);
        voucherRepository.save(voucher2);

        List<Voucher> vouchers = voucherRepository.findAll();

        assertThat(vouchers).isNotNull();
        assertThat(vouchers).hasSize(2);
    }

    @Test
    public void whenGetVoucherById_thenReturnVoucher() {
        Voucher voucher = Voucher.builder()
                .name("Voucher 1")
                .price(10000)
                .build();
        voucherRepository.save(voucher);

        Voucher foundVoucher = voucherRepository.findById(voucher.getId()).orElse(null);

        assertThat(foundVoucher).isNotNull();
    }

    @Test
    public void whenUpdateVoucherById_thenReturnUpdatedVoucher() {
        Voucher voucher = Voucher.builder()
                .name("Voucher 1")
                .price(10000)
                .build();
        voucherRepository.save(voucher);

        Voucher foundVoucher = voucherRepository.findById(voucher.getId()).orElse(null);
        assertThat(foundVoucher).isNotNull();

        foundVoucher.setName("Updated Voucher");
        Voucher updatedVoucher = voucherRepository.save(foundVoucher);

        assertThat(updatedVoucher).isNotNull();
        assertThat(updatedVoucher.getName()).isEqualTo("Updated Voucher");
    }

    @Test
    public void whenDeleteVoucherById_thenVoucherShouldBeDeleted() {
        Voucher voucher = Voucher.builder()
                .name("Voucher 1")
                .price(10000)
                .build();
        voucherRepository.save(voucher);

        voucherRepository.deleteById(voucher.getId());
        Voucher foundVoucher = voucherRepository.findById(voucher.getId()).orElse(null);

        assertThat(foundVoucher).isNull();
    }
}
