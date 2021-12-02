package kata.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account{
    private Long id;
    private Client client;
    private BigDecimal balance ;
    private Set<Transaction> transactions;

    public Account(Long id, Client client) {
        this.id = id;
        this.client = client;
        this.balance = BigDecimal.ZERO;
    }

    public void addTransaction(Transaction transaction){
        if (isNull(transactions) )
        {
            transactions = new HashSet<>();
        }
        transactions.add(transaction);
    }

    public BigDecimal currentBalance(){
        return this.balance;
    }


}