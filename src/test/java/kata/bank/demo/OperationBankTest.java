package kata.bank.demo;

import kata.bank.exception.NoAccountFoundException;
import kata.bank.exception.NotEnoughFundsException;
import kata.bank.exception.NotValidAmountException;
import kata.bank.model.Account;
import kata.bank.model.Client;
import kata.bank.model.Transaction;
import kata.bank.service.OperationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class OperationBankTest {

    static OperationService operationService = new OperationService();

    @BeforeAll
    static void init (){
        Stream.iterate(1L, n -> n + 1).limit(6).forEach(i -> {
            operationService.getAllAccount().add(new Account(i,new Client(i,"Client "+i)));
        });

    }

    @ParameterizedTest(name = "{index} ==> Description du test : {0} {1} {2}")
    @MethodSource("data_test")
    @DisplayName("Tester le traitement permettant de mouvementer ( Deposit /Withdrawal) le compte bancaire ")
    void test_addOperation(Long accountId,Transaction transaction,BigDecimal balanceExpected) throws NotEnoughFundsException, NoAccountFoundException, NotValidAmountException {
        Account account = operationService.addOperation(accountId, transaction);
        Assertions.assertEquals(account.currentBalance(),balanceExpected);
    }


    @ParameterizedTest(name = "{index} ==> Description du test : {0} {1} {2}")
    @DisplayName("Permettant de tester les exceptions hrow exception when no provision sur le compte")
    @MethodSource("data_test_controles")
    void test_controles(Long accountId,Transaction transaction,Exception exception)  {
        Assertions.assertThrows(exception.getClass(),()->operationService.addOperation(accountId, transaction));
    }


    @Test
    void test_printStatement() throws NoAccountFoundException, NotValidAmountException, NotEnoughFundsException {
        Long accountId = 5L;
        Transaction transaction = initTransaction(Transaction.OperationType.CREDIT
                ,BigDecimal.valueOf(2000),"Virement");
        operationService.addOperation(accountId, transaction);
        transaction = initTransaction(Transaction.OperationType.DEBIT
                ,BigDecimal.valueOf(800),"Achat");
        operationService.addOperation(accountId, transaction);
        //
        List<Transaction> transactions = operationService.statementByAccount(accountId);
       Assertions.assertAll(
               ()->Assertions.assertEquals(transactions.get(0).toString(),"|CREDIT|"+LocalDateTime.now().toLocalDate()+"|2000|2000|"),
               ()->Assertions.assertEquals(transactions.get(1).toString(),"|DEBIT|"+LocalDateTime.now().toLocalDate()+"|800|1200|")
               );
    }


    static Stream<Arguments> data_test_controles() {
        return Stream.of(
                arguments(1L,initTransaction(Transaction.OperationType.CREDIT
                        ,BigDecimal.valueOf(-2000),"Virement"),new NotValidAmountException()),
                arguments(2L,initTransaction(Transaction.OperationType.DEBIT
                        ,BigDecimal.valueOf(9000),"Virement"),new NotEnoughFundsException())
        );
    }

    static Stream<Arguments> data_test() {
        return Stream.of(
                arguments(1L,initTransaction(Transaction.OperationType.CREDIT
                        ,BigDecimal.valueOf(2000),"Virement"),BigDecimal.valueOf(2000)),
                arguments(1L,initTransaction(Transaction.OperationType.CREDIT
                        ,BigDecimal.valueOf(500),"Virement"),BigDecimal.valueOf(2500)),
                arguments(1L,initTransaction(Transaction.OperationType.DEBIT
                        ,BigDecimal.valueOf(2000),"Achat"),BigDecimal.valueOf(500))
                );
    }


    static Transaction initTransaction(Transaction.OperationType operationType,BigDecimal amount,String libelle) {
        return new Transaction(LocalDateTime.now(),amount,operationType,libelle);
    }


}
