package br.com.micropicpaychallenge.transaction;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.micropicpaychallenge.authorization.AuthorizeService;
import br.com.micropicpaychallenge.notification.NotificationService;
import br.com.micropicpaychallenge.wallet.Wallet;
import br.com.micropicpaychallenge.wallet.WalletRepository;
import br.com.micropicpaychallenge.wallet.WalletType;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizeService authorizeService;
    private final NotificationService notificationService;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository, AuthorizeService authorizeService, NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizeService = authorizeService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        //1- Validar a transação
        validadeTransaction(transaction);

        //2- Criar a transação
        var transactionPersisted = transactionRepository.save(transaction);

        //3- Debitar da carteira
        var walletPayer = walletRepository.findById(transaction.payer()).get();
        var walletPayee = walletRepository.findById(transaction.payee()).get();
        walletRepository.save(walletPayer.debit(transaction.value()));
        walletRepository.save(walletPayee.credit(transaction.value()));

        //4- Chamar serviços externos
        // Autorizar transação
        authorizeService.authorize(transaction);
        // Notificar
        notificationService.notify(transaction);
        return transactionPersisted;
    }

    /*
     * Rules for validation
     * - the payer has a common wallet
     * - the payer has enough balance
     * - the payer (pagador) is not the payee (recebedor)
     */
    private void validadeTransaction(Transaction transaction) {
        walletRepository.findById(transaction.payee())
                .map(payee -> walletRepository.findById(transaction.payer())
                        .map(payer -> isTransactionValid(transaction, payer) ? transaction : null)
                        .orElseThrow(() -> new InvalidTransactionException("Invalid Transaction - %s".formatted(transaction))))
                .orElseThrow(() -> new InvalidTransactionException("Invalid Transaction - %s".formatted(transaction)));
    }

    private boolean isTransactionValid(Transaction transaction, Wallet payer) {
        return payer.type() == WalletType.COMMON.getValue()
                && payer.balance().compareTo(transaction.value()) >= 0 &&
                !payer.id().equals(transaction.payee());
    }

    public List<Transaction> list() {
        return transactionRepository.findAll();
    }

}
