package br.com.shareaccount.domain;

import br.com.shareaccount.enumeration.OperationTypeEnum;
import br.com.shareaccount.enumeration.TransactionTypeEnum;
import br.com.shareaccount.enumeration.ValueTypeEnum;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private String description;
    private BigDecimal value;
    private TransactionTypeEnum transactionType;
    private OperationTypeEnum operationType;
    private ValueTypeEnum valueType;
}
