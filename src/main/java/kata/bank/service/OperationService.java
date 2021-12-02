package kata.bank.service;

import kata.bank.exception.NoAccountFoundException;
import kata.bank.exception.NotEnoughFundsException;
import kata.bank.exception.NotValidAmountException;
import kata.bank.model.Account;
import kata.bank.model.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OperationService {

    //DATA
    List<Account> accounts =new ArrayList<>();

    /**
     * permettant de mouvementer un compte bancaire
     * Deposit and Withdrawal
     * @param accountId transaction
     */
    public Account addOperation(Long accountId, Transaction transaction) throws NotEnoughFundsException, NoAccountFoundException,NotValidAmountException {
        checkAmountIsGreaterThanZero(transaction.getAmount());
        //check account
        Account lAccount = getAccountById(accountId);
        // check balance is enough
        checkBalanceIsEnough(transaction.getAmount(),transaction.getOperationType(), lAccount.currentBalance());
        // calculte new balance
        BigDecimal newBalance = calculateBalance(lAccount.currentBalance(), transaction.getOperationType(),transaction.getAmount());
        transaction.setBalance(newBalance);
        lAccount.addTransaction(transaction);
        //update current balance
        lAccount.setBalance(newBalance);
        return lAccount;
    }

    /**
     * permettant de rehcercher un compte bancaire
     * @param accountId
     * @return
     * @throws NoAccountFoundException
     */
    public Account getAccountById(Long accountId) throws NoAccountFoundException {
        return accounts.stream().filter(account -> accountId.equals(account.getId()))
                .findFirst().orElseThrow(NoAccountFoundException::new);
    }


    /**
     * permettant d'afficher le relevé bancaire
     * @param accountId
     * @return
     * @throws NoAccountFoundException
     */
    public List<Transaction> statementByAccount(Long accountId) throws NoAccountFoundException {
        List<Transaction> transactions = new ArrayList<>(getAccountById(accountId).getTransactions());
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getOperationDate))
                .collect(Collectors.toList());
    }

   private BigDecimal calculateBalance(BigDecimal balance,Transaction.OperationType operationType,BigDecimal amount) {
        return balance.add(operationType.equals(Transaction.OperationType.CREDIT) ? amount : amount.negate());
    }

    private void checkBalanceIsEnough(BigDecimal amount, Transaction.OperationType operationType, BigDecimal balance) throws NotEnoughFundsException {
        if (operationType.equals(Transaction.OperationType.DEBIT) && balance.compareTo(amount) < 0)
            throw new NotEnoughFundsException();
    }

    private void checkAmountIsGreaterThanZero(BigDecimal amount) throws NotValidAmountException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NotValidAmountException();
        }
    }

    public List<Account> getAllAccount(){
        return accounts;
    }



}
