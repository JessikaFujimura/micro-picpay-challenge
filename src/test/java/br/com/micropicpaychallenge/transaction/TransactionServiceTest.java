package br.com.micropicpaychallenge.transaction;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import br.com.micropicpaychallenge.authorization.AuthorizeService;
import br.com.micropicpaychallenge.notification.NotificationService;
import br.com.micropicpaychallenge.wallet.WalletRepository;

public class TransactionServiceTest {


    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private AuthorizeService authorizeService;

    @Mock
    private NotificationService notificationService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(transactionRepository, walletRepository, authorizeService, notificationService);
    }

    @Test
    public void testGetAllTransactionsShouldReturnList(){
        var transaction = new Transaction(1L, 1L, 1L, BigDecimal.valueOf(10L), LocalDateTime.now());
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction, transaction));

        List<Transaction> resuList = transactionService.list();

        Assertions.assertThat(resuList.size()).isEqualTo(2);
    }
    
    @Test
    public void testGetAllTransactionsShouldReturnNullList(){
        when(transactionRepository.findAll()).thenReturn(new ArrayList<>());

        List<Transaction> resuList = transactionService.list();

        Assertions.assertThat(resuList).isEmpty();
    }
}
