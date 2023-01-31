package br.com.shareaccount.scenario;

import br.com.shareaccount.domain.Customer;
import br.com.shareaccount.domain.Transaction;
import br.com.shareaccount.domain.AccountRequestDTO;
import br.com.shareaccount.enumeration.CustomerTypeEnum;
import br.com.shareaccount.enumeration.OperationTypeEnum;
import br.com.shareaccount.enumeration.TransactionTypeEnum;
import br.com.shareaccount.enumeration.ValueTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestScenario {

    public static AccountRequestDTO getRequest(){
        var customers = CustomerScenario.getCustomers();
        return AccountRequestDTO.builder().customerInvoices(customers).build();
    }

    public static AccountRequestDTO getRequestWithAccountAmountIsZero(){
        var customers = new ArrayList<Customer>();
        var transactionsOne = new ArrayList<Transaction>();
        transactionsOne.add(Transaction.builder().description("Lanche")
                .value(BigDecimal.ZERO)
                .transactionType(TransactionTypeEnum.PARTIAL)
                .operationType(OperationTypeEnum.CREDIT)
                .valueType(ValueTypeEnum.CURRENCY)
                .build());
        var customerOne = Customer.builder().name("Rodrigo")
                .customerType(CustomerTypeEnum.ME)
                .transactionPostings(transactionsOne)
                .build();

        var transactionsTwo = new ArrayList<Transaction>();
        transactionsTwo.add(Transaction.builder().description("Lanche")
                .value(BigDecimal.ZERO)
                .transactionType(TransactionTypeEnum.PARTIAL)
                .operationType(OperationTypeEnum.CREDIT)
                .valueType(ValueTypeEnum.CURRENCY)
                .build());
        var customerTwo = Customer.builder().name("Marcelo")
                .customerType(CustomerTypeEnum.FRIEND)
                .transactionPostings(transactionsTwo)
                .build();

        var transactionsThree = new ArrayList<Transaction>();
        transactionsThree.add(Transaction.builder().description("Desconto")
                .value(new BigDecimal(20))
                .transactionType(TransactionTypeEnum.PARTIAL)
                .operationType(OperationTypeEnum.DEBIT)
                .valueType(ValueTypeEnum.CURRENCY)
                .build());
        var customerThree = Customer.builder().name("Estabelecimento")
                .customerType(CustomerTypeEnum.ESTABLISHMENT)
                .transactionPostings(transactionsThree)
                .build();

        customers.add(customerOne);
        customers.add(customerTwo);
        customers.add(customerThree);
        var request = AccountRequestDTO.builder().customerInvoices(customers).build();
        return request;
    }
}
