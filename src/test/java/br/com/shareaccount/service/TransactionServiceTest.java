package br.com.shareaccount.service;

import br.com.shareaccount.domain.Customer;
import br.com.shareaccount.domain.Transaction;
import br.com.shareaccount.enumeration.OperationTypeEnum;
import br.com.shareaccount.enumeration.TransactionTypeEnum;
import br.com.shareaccount.enumeration.ValueTypeEnum;
import br.com.shareaccount.exception.ClientException;
import br.com.shareaccount.scenario.CustomerScenario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getMapAmountByClientWithSuccess(){
        var clients = CustomerScenario.getClientAccount(CustomerScenario.getCustomers());
        var map = transactionService.getMapAmountByClient(clients);
        assertAll(
                () -> assertNotNull(map),
                () -> assertEquals(map.get("Rodrigo"), new BigDecimal(42)),
                () -> assertEquals(map.get("Marcelo"), new BigDecimal(8))
        );
    }

    @Test
    void shouldReturnEmptyMap(){
        var clients = new ArrayList<Customer>();
        var map = transactionService.getMapAmountByClient(clients);
        assertAll(
                () -> assertNotNull(map),
                () -> assertEquals(map.size(), 0)
        );
    }

    @Test
    void shouldThrowExceptionIfCustomerTransactionTypeIsPartial(){

        var clients = CustomerScenario.getClientAccount(CustomerScenario.getCustomers());
        clients.get(0).getTransactionPostings().get(0).setTransactionType(TransactionTypeEnum.FULL);

        var e = assertThrows(ClientException.class,
                () -> transactionService.getMapAmountByClient(clients));

        assertTrue(e.getMessage().contains("Client cannot create transactions of type FULL"));
    }

    @Test
    void shouldThrowExceptionIfCustomerOperationTypeIsDebit(){

        var clients = CustomerScenario.getClientAccount(CustomerScenario.getCustomers());
        clients.get(0).getTransactionPostings().get(0).setOperationType(OperationTypeEnum.DEBIT);
        var e = assertThrows(ClientException.class,
                () -> transactionService.getMapAmountByClient(clients));

        assertTrue(e.getMessage().contains("Client cannot have debit transaction"));
    }

    @Test
    void shouldThrowExceptionIfCustomerValueTypeIsPercentage(){

        var clients = CustomerScenario.getClientAccount(CustomerScenario.getCustomers());
        clients.get(0).getTransactionPostings().get(0).setValueType(ValueTypeEnum.PERCENTAGE);
        var e = assertThrows(ClientException.class,
                () -> transactionService.getMapAmountByClient(clients));

        assertTrue(e.getMessage().contains("Client cannot have percentage currency transaction"));
    }

    @Test
    void getMapEstablishmentWithSuccess(){
        var establishment = CustomerScenario.getEstablishment(CustomerScenario.getCustomers());
        var transactionCredit = Transaction.builder().description("Covert")
                .value(new BigDecimal(8))
                .transactionType(TransactionTypeEnum.FULL)
                .operationType(OperationTypeEnum.CREDIT)
                .valueType(ValueTypeEnum.CURRENCY)
                .build();

        var transactionDebit = Transaction.builder().description("Others")
                .value(new BigDecimal(3))
                .transactionType(TransactionTypeEnum.PARTIAL)
                .operationType(OperationTypeEnum.DEBIT)
                .valueType(ValueTypeEnum.CURRENCY)
                .build();
        establishment.get(0).getTransactionPostings().add(transactionCredit);
        establishment.get(0).getTransactionPostings().add(transactionDebit);

        var mapDebit = transactionService.getMapEstablishment(establishment,
                OperationTypeEnum.DEBIT,
                new BigDecimal(50));

        var mapCredit = transactionService.getMapEstablishment(establishment,
                OperationTypeEnum.CREDIT,
                new BigDecimal(50));

        assertAll(
                () -> assertNotNull(mapDebit),
                () -> assertNotNull(mapCredit),
                () -> assertEquals(mapDebit.get(TransactionTypeEnum.PARTIAL), new BigDecimal(23)),
                () -> assertEquals(mapDebit.get(TransactionTypeEnum.FULL), new BigDecimal(0)),
                () -> assertEquals(mapCredit.get(TransactionTypeEnum.PARTIAL), new BigDecimal(8)),
                () -> assertEquals(mapCredit.get(TransactionTypeEnum.FULL), new BigDecimal(8))
        );
    }

    @Test
    void getMapEstablishmentPercentageValueWithSuccess(){
        var establishment = CustomerScenario.getEstablishment(CustomerScenario.getCustomers());
        establishment.get(0).getTransactionPostings().get(0).setValueType(ValueTypeEnum.PERCENTAGE);
        establishment.get(0).getTransactionPostings().get(0).setValue(new BigDecimal(40));

        var map = transactionService.getMapEstablishment(establishment,
                OperationTypeEnum.DEBIT,
                new BigDecimal(50));

        assertAll(
                () -> assertNotNull(map),
                () -> assertEquals(map.get(TransactionTypeEnum.PARTIAL), new BigDecimal("20.0")),
                () -> assertEquals(map.get(TransactionTypeEnum.FULL), BigDecimal.ZERO));
    }

    @Test
    void shouldReturnEstablishmentEmptyMap(){
        var establishment = new ArrayList<Customer>();
        var map = transactionService.getMapEstablishment(establishment,
                OperationTypeEnum.DEBIT,
                new BigDecimal(50));
        assertAll(
                () -> assertNotNull(map),
                () -> assertEquals(map.size(), 2),
                () -> assertEquals(map.get(TransactionTypeEnum.PARTIAL), BigDecimal.ZERO),
                () -> assertEquals(map.get(TransactionTypeEnum.FULL), BigDecimal.ZERO)
        );
    }
}
