package kata.bank.demo;

import kata.bank.exception.NoAccountFoundException;
import kata.bank.exception.NotEnoughFundsException;
import kata.bank.model.Account;
import kata.bank.model.Client;
import kata.bank.service.OperationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class AccountTest {

    static OperationService operationService = new OperationService();

    @BeforeAll
    static void init (){
        // init Account
        Stream.iterate(1L, n -> n + 1).limit(6).forEach(i -> {
            operationService.getAllAccount().add(new Account(i,new Client(i,"Client "+i)));
        });
    }


    @ParameterizedTest(name = "{index} ==> Description du test : {0} {1}")
    @MethodSource("data_test")
    void test_findAccount(Long accountId,Exception exception) throws NotEnoughFundsException, NoAccountFoundException {
        Assertions.assertThrows(exception.getClass(),()->operationService.getAccountById(accountId));
    }

    static Stream<Arguments> data_test() {
        return Stream.of(
                arguments(300L,new NoAccountFoundException())
        );
    }

}
