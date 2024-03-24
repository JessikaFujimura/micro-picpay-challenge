package br.com.micropicpaychallenge.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.micropicpaychallenge.authorization.AuthorizeService;
import br.com.micropicpaychallenge.wallet.Wallet;
import br.com.micropicpaychallenge.wallet.WalletRepository;
import br.com.micropicpaychallenge.wallet.WalletType;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizeService authorizeService;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository, AuthorizeService authorizeService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizeService = authorizeService;
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        // Validar a transação
        validadeTransaction(transaction);

        // Criar a transação
        var transactionPersisted = transactionRepository.save(transaction);

        // Debitar da carteira
        var wallet = walletRepository.findById(transaction.payer()).get();
        walletRepository.save(wallet.debit(transaction.value()));

        // Chamar serviços externos
            // Autorizar transação
        authorizeService.authorize(transaction);
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
                        .orElseThrow())
                .orElseThrow(() -> new InvalidTransactionException("Invalid Transaction - %s".formatted(transaction)));
    }

    private boolean isTransactionValid(Transaction transaction, Wallet payer) {
        return payer.type() == WalletType.COMMON.getValue()
                && payer.balance().compareTo(transaction.value()) >= 0 &&
                !payer.id().equals(transaction.payee());
    }

}
