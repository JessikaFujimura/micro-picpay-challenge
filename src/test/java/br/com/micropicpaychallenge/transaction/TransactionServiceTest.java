package br.com.micropicpaychallenge.transaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import br.com.micropicpaychallenge.authorization.AuthorizeService;
import br.com.micropicpaychallenge.notification.NotificationService;
import br.com.micropicpaychallenge.wallet.Wallet;
import br.com.micropicpaychallenge.wallet.WalletRepository;
import br.com.micropicpaychallenge.wallet.WalletType;

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
    public void testCreateTransaction(){
        var transaction = new Transaction(null, 2L, 1L, BigDecimal.valueOf(10L), LocalDateTime.now());
        var payee = new Wallet(1L, "Fulano", 111111111L, "email@test.com", "12345", WalletType.SHOPKEEPER.getValue(), BigDecimal.valueOf(1000));
        var payer = new Wallet(2L, "Fulano", 111111111L, "email@test.com", "12345", WalletType.COMMON.getValue(), BigDecimal.valueOf(1000));

        when(walletRepository.findById(anyLong()))
        .thenReturn(Optional.of(payee))
        .thenReturn(Optional.of(payer));

        when(transactionRepository.save(any())).thenReturn(transaction);
        when(walletRepository.save(any()))
        .thenReturn(payer)
        .thenReturn(payee);

        doNothing().when(authorizeService).authorize(transaction);
        doNothing().when(notificationService).notify(transaction);

        var response = transactionService.createTransaction(transaction);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    public void testCreateInvalidTransactionWhenBalancePayerIsNotEnougth(){
        var transaction = new Transaction(null, 2L, 1L, BigDecimal.valueOf(10000L), LocalDateTime.now());
        var payee = new Wallet(1L, "Fulano", 111111111L, "email@test.com", "12345", WalletType.SHOPKEEPER.getValue(), BigDecimal.valueOf(1000));
        var payer = new Wallet(2L, "Fulano", 111111111L, "email@test.com", "12345", WalletType.COMMON.getValue(), BigDecimal.valueOf(100));

        when(walletRepository.findById(anyLong()))
        .thenReturn(Optional.of(payee))
        .thenReturn(Optional.of(payer));
        
        Assertions.assertThatThrownBy(() -> transactionService.createTransaction(transaction))
        .isInstanceOf(InvalidTransactionException.class)
        .hasMessageStartingWith("Invalid Transaction");
    }

    @Test
    public void testCreateInvalidTransactionWhenPayerIsNotCommonType(){
        var transaction = new Transaction(null, 2L, 1L, BigDecimal.valueOf(100L), LocalDateTime.now());
        var payee = new Wallet(1L, "Fulano", 111111111L, "email@test.com", "12345", WalletType.SHOPKEEPER.getValue(), BigDecimal.valueOf(1000));
        var payer = new Wallet(2L, "Fulano", 111111111L, "email@test.com", "12345", WalletType.SHOPKEEPER.getValue(), BigDecimal.valueOf(1000));

        when(walletRepository.findById(anyLong()))
        .thenReturn(Optional.of(payee))
        .thenReturn(Optional.of(payer));
        
        Assertions.assertThatThrownBy(() -> transactionService.createTransaction(transaction))
        .isInstanceOf(InvalidTransactionException.class)
        .hasMessageStartingWith("Invalid Transaction");
    }

    @Test
    public void testCreateInvalidTransactionWhenPayerIdIsEqualPayeeId(){
        var transaction = new Transaction(null, 1L, 1L, BigDecimal.valueOf(100L), LocalDateTime.now());
        var payee = new Wallet(1L, "Fulano", 111111111L, "email@test.com", "12345", WalletType.SHOPKEEPER.getValue(), BigDecimal.valueOf(1000));
        var payer = new Wallet(1L, "Fulano", 111111111L, "email@test.com", "12345", WalletType.COMMON.getValue(), BigDecimal.valueOf(1000));

        when(walletRepository.findById(anyLong()))
        .thenReturn(Optional.of(payee))
        .thenReturn(Optional.of(payer));
        
        Assertions.assertThatThrownBy(() -> transactionService.createTransaction(transaction))
        .isInstanceOf(InvalidTransactionException.class)
        .hasMessageStartingWith("Invalid Transaction");
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
