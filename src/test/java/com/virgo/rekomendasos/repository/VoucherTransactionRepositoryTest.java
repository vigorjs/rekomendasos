package com.virgo.rekomendasos.repository;

import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import com.virgo.rekomendasos.repo.UserRepository;
import com.virgo.rekomendasos.repo.VoucherRepository;
import com.virgo.rekomendasos.repo.VoucherTransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class VoucherTransactionRepositoryTest {

    @Autowired
    private VoucherTransactionRepository voucherTransactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoucherRepository voucherRepository;

    @Test
    public void whenCreateVoucherTransaction_thenReturnCreatedVoucherTransaction() {
        User user1 = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user1);
        Voucher voucher1 = Voucher.builder()
                .name("Voucher 1")
                .price(10000)
                .quantity(10)
                .build();
        voucherRepository.save(voucher1);
        VoucherTransaction voucherTransaction = VoucherTransaction.builder()
                .user(user1)
                .voucher(voucher1)
                .voucherQuantity(10)
                .build();
        voucherTransactionRepository.save(voucherTransaction);

        VoucherTransaction foundVoucherTransaction = voucherTransactionRepository.findById(voucherTransaction.getId()).orElse(null);

        assertThat(foundVoucherTransaction).isNotNull();
    }

    @Test
    public void whenGetAllVoucherTransactions_thenReturnAllVoucherTransactions() {
        User user1 = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user1);
        Voucher voucher1 = Voucher.builder()
                .name("Voucher 1")
                .price(10000)
                .quantity(10)
                .build();
        voucherRepository.save(voucher1);
        VoucherTransaction voucherTransaction = VoucherTransaction.builder()
                .user(user1)
                .voucher(voucher1)
                .voucherQuantity(10)
                .build();
        voucherTransactionRepository.save(voucherTransaction);

        User user2 = User.builder()
                .firstname("Enigma 2")
                .lastname("Camp")
                .email("user2@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user2);
        Voucher voucher2 = Voucher.builder()
                .name("Voucher 2")
                .price(10000)
                .quantity(10)
                .build();
        voucherRepository.save(voucher2);
        VoucherTransaction voucherTransaction2 = VoucherTransaction.builder()
                .user(user2)
                .voucher(voucher2)
                .voucherQuantity(10)
                .build();
        voucherTransactionRepository.save(voucherTransaction2);

        List<VoucherTransaction> voucherTransactions = voucherTransactionRepository.findAll();

        assertThat(voucherTransactions).isNotNull();
        assertThat(voucherTransactions).hasSize(2);
    }

    @Test
    public void whenGetVoucherTransactionById_thenReturnVoucherTransaction() {
        User user1 = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user1);
        Voucher voucher1 = Voucher.builder()
                .name("Voucher 1")
                .price(10000)
                .quantity(10)
                .build();
        voucherRepository.save(voucher1);
        VoucherTransaction voucherTransaction = VoucherTransaction.builder()
                .user(user1)
                .voucher(voucher1)
                .voucherQuantity(10)
                .build();
        voucherTransactionRepository.save(voucherTransaction);

        VoucherTransaction foundVoucherTransaction = voucherTransactionRepository.findById(voucherTransaction.getId()).orElse(null);

        assertThat(foundVoucherTransaction).isNotNull();
    }

    @Test
    public void whenUpdateVoucherTransactionById_thenReturnUpdatedVoucherTransaction() {
        User user1 = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user1);
        Voucher voucher1 = Voucher.builder()
                .name("Voucher 1")
                .price(10000)
                .quantity(10)
                .build();
        voucherRepository.save(voucher1);
        VoucherTransaction voucherTransaction = VoucherTransaction.builder()
                .user(user1)
                .voucher(voucher1)
                .voucherQuantity(10)
                .build();
        voucherTransactionRepository.save(voucherTransaction);

        VoucherTransaction foundVoucherTransaction = voucherTransactionRepository.findById(voucherTransaction.getId()).orElse(null);
        assertThat(foundVoucherTransaction).isNotNull();

        User user2 = User.builder()
                .firstname("Enigma 2")
                .lastname("Camp")
                .email("user2@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user2);
        foundVoucherTransaction.setUser(user2);
        VoucherTransaction updatedVoucherTransaction = voucherTransactionRepository.save(foundVoucherTransaction);

        assertThat(updatedVoucherTransaction).isNotNull();
        assertThat(updatedVoucherTransaction.getUser()).isEqualTo(user2);
    }

    @Test
    public void whenDeleteVoucherTransactionById_thenVoucherTransactionShouldBeDeleted() {
        User user1 = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user1);
        Voucher voucher1 = Voucher.builder()
                .name("Voucher 1")
                .price(10000)
                .quantity(10)
                .build();
        voucherRepository.save(voucher1);
        VoucherTransaction voucherTransaction = VoucherTransaction.builder()
                .user(user1)
                .voucher(voucher1)
                .voucherQuantity(10)
                .build();
        voucherTransactionRepository.save(voucherTransaction);

        voucherTransactionRepository.deleteById(voucherTransaction.getId());
        VoucherTransaction foundVoucherTransaction = voucherTransactionRepository.findById(voucherTransaction.getId()).orElse(null);

        assertThat(foundVoucherTransaction).isNull();
    }
}
