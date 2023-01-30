package br.com.shareaccount.scenario;

import br.com.shareaccount.domain.Customer;
import br.com.shareaccount.domain.Transaction;
import br.com.shareaccount.dto.CustomerDTO;
import br.com.shareaccount.enumeration.CustomerTypeEnum;
import br.com.shareaccount.enumeration.OperationTypeEnum;
import br.com.shareaccount.enumeration.TransactionTypeEnum;
import br.com.shareaccount.enumeration.ValueTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerScenario {

    public static List<Customer> getCustomers(){
        var Customers = new ArrayList<Customer>();
        var transactionsOne = new ArrayList<Transaction>();
        transactionsOne.add(Transaction.builder().description("Lanche")
                .value(new BigDecimal(40))
                .transactionType(TransactionTypeEnum.PARTIAL)
                .operationType(OperationTypeEnum.CREDIT)
                .valueType(ValueTypeEnum.CURRENCY)
                .build());
        transactionsOne.add(Transaction.builder().description("Suco")
                .value(new BigDecimal(2))
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
                .value(new BigDecimal(8))
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
        transactionsThree.add(Transaction.builder().description("Entrega")
                .value(new BigDecimal(8))
                .transactionType(TransactionTypeEnum.PARTIAL)
                .operationType(OperationTypeEnum.CREDIT)
                .valueType(ValueTypeEnum.CURRENCY)
                .build());
        var customerThree = Customer.builder().name("Estabelecimento")
                .customerType(CustomerTypeEnum.ESTABLISHMENT)
                .transactionPostings(transactionsThree)
                .build();

        Customers.add(customerOne);
        Customers.add(customerTwo);
        Customers.add(customerThree);

        return Customers;
    }

    public static List<Customer> getCustomersSameName(){
        var Customers = new ArrayList<Customer>();
        var transactionsOne = new ArrayList<Transaction>();
        transactionsOne.add(Transaction.builder().description("Lanche")
                .value(new BigDecimal(40))
                .transactionType(TransactionTypeEnum.PARTIAL)
                .operationType(OperationTypeEnum.CREDIT)
                .valueType(ValueTypeEnum.CURRENCY)
                .build());
        transactionsOne.add(Transaction.builder().description("Suco")
                .value(new BigDecimal(2))
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
                .value(new BigDecimal(8))
                .transactionType(TransactionTypeEnum.PARTIAL)
                .operationType(OperationTypeEnum.CREDIT)
                .valueType(ValueTypeEnum.CURRENCY)
                .build());
        var customerTwo = Customer.builder().name("Rodrigo")
                .customerType(CustomerTypeEnum.FRIEND)
                .transactionPostings(transactionsTwo)
                .build();

        Customers.add(customerOne);
        Customers.add(customerTwo);

        return Customers;
    }

    public static List<Customer> getMoreOneEstablishment(){
        var Customers = new ArrayList<Customer>();
        var transactionsOne = new ArrayList<Transaction>();
        transactionsOne.add(Transaction.builder().description("Acrescimo")
                .value(new BigDecimal(10))
                .transactionType(TransactionTypeEnum.PARTIAL)
                .operationType(OperationTypeEnum.CREDIT)
                .valueType(ValueTypeEnum.CURRENCY)
                .build());
        var customerOne = Customer.builder().name("Empresa 2")
                .customerType(CustomerTypeEnum.ESTABLISHMENT)
                .transactionPostings(transactionsOne)
                .build();

        var transactionsTwo = new ArrayList<Transaction>();
        transactionsTwo.add(Transaction.builder().description("Lanche")
                .value(new BigDecimal(8))
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
        transactionsThree.add(Transaction.builder().description("Entrega")
                .value(new BigDecimal(8))
                .transactionType(TransactionTypeEnum.PARTIAL)
                .operationType(OperationTypeEnum.CREDIT)
                .valueType(ValueTypeEnum.CURRENCY)
                .build());
        var customerThree = Customer.builder().name("Estabelecimento")
                .customerType(CustomerTypeEnum.ESTABLISHMENT)
                .transactionPostings(transactionsThree)
                .build();

        Customers.add(customerOne);
        Customers.add(customerTwo);
        Customers.add(customerThree);

        return Customers;
    }

    public static Map<String, BigDecimal> getMapClients(){
        var map = new HashMap<String,BigDecimal>();
        map.put("Rodrigo", new BigDecimal(42));
        map.put("Marcelo", new BigDecimal(8));
        return map;
    }

    public static Map<String, BigDecimal> getMapClientsZeroValue(){
        var map = new HashMap<String,BigDecimal>();
        map.put("Rodrigo", new BigDecimal(0));
        map.put("Marcelo", new BigDecimal(0));
        return map;
    }

    public static Map<TransactionTypeEnum, BigDecimal> getMapTransactionsOfEstablishmentDebit(){
        var map = new HashMap<TransactionTypeEnum, BigDecimal>();
        map.put(TransactionTypeEnum.PARTIAL, new BigDecimal(20));
        return map;
    }

    public static List<Customer> getClientAccount(List<Customer> customers){
        var clients = customers.stream()
                .filter(a -> !CustomerTypeEnum.ESTABLISHMENT.equals(a.getCustomerType()))
                .collect(Collectors.toList());
        return clients;
    }

    public static List<Customer> getEstablishment(List<Customer> customers){
        var establishments = customers.stream()
                .filter(a -> CustomerTypeEnum.ESTABLISHMENT.equals(a.getCustomerType()))
                .collect(Collectors.toList());
        return establishments;
    }

    public static List<CustomerDTO> calculatedCustomers(){
        var linkMarcelo = "https://www.mercadopago.com.br/sandbox/payments/1311426608/marcelo608";
        var linkRodrigo = "https://www.mercadopago.com.br/sandbox/payments/1311426608/rodrigo3192";
        var customers = new ArrayList<CustomerDTO>();
        customers.add(CustomerDTO.builder().name("Rodrigo")
                .valueToPay(new BigDecimal("31.92"))
                .billingWalletLink(linkRodrigo)
                .build());
        customers.add(CustomerDTO.builder().name("Marcelo")
                .valueToPay(new BigDecimal("6.08"))
                .billingWalletLink(linkMarcelo)
                .build());
        return customers;

    }

}
