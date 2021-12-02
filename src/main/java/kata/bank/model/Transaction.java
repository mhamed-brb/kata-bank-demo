package kata.bank.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

@Data
@NoArgsConstructor
public class Transaction {
    private LocalDateTime operationDate;
    private BigDecimal amount;
    private Transaction.OperationType operationType;
    private String libelle;
    private BigDecimal balance;

    public enum OperationType {
        CREDIT, DEBIT;
    }

    public Transaction(LocalDateTime operationDate, BigDecimal amount, OperationType operationType, String libelle) {
        requireNonNull(amount,"amount is required");
        requireNonNull(operationType,"operationType is required");
        this.operationDate = operationDate;
        this.amount = amount;
        this.operationType = operationType;
        this.libelle = libelle;
        this.balance = BigDecimal.ZERO;
    }


    @Override
    public String toString() {
        return String.format("|%s|%s|%s|%s|",
                operationType,
                operationDate.toLocalDate(),
                amount,
                balance);
    }

}
