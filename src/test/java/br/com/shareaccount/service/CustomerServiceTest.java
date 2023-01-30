package br.com.shareaccount.service;

import br.com.shareaccount.domain.Customer;
import br.com.shareaccount.domain.Transaction;
import br.com.shareaccount.enumeration.CustomerTypeEnum;
import br.com.shareaccount.enumeration.OperationTypeEnum;
import br.com.shareaccount.enumeration.TransactionTypeEnum;
import br.com.shareaccount.enumeration.ValueTypeEnum;
import br.com.shareaccount.exception.ClientException;
import br.com.shareaccount.exception.EstablishmentException;
import br.com.shareaccount.scenario.CustomerScenario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.math.BigDecimal;
import java.util.ArrayList;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private TransactionService transactionService;

    @Test
    void getMapAmountByClientWithSuccess(CapturedOutput capture){
        var listCustomers = CustomerScenario.getCustomers();
        when(transactionService.getMapAmountByClient(CustomerScenario.getClientAccount(listCustomers)))
                .thenReturn(CustomerScenario.getMapClients());
        var map = customerService.getMapAmountByClient(listCustomers);
        assertAll(
                () -> assertNotNull(map),
                () -> assertEquals(map.get("Rodrigo"), new BigDecimal(42)),
                () -> assertEquals(map.get("Marcelo"), new BigDecimal(8)),
                () -> assertTrue(capture.getOut().contains("Separating customers from the establishment"))
        );
    }

    @Test
    void shouldThrowExceptionIfThereAreNoClients(){
        var listCustomers = CustomerScenario.getCustomers();
        var e = assertThrows(ClientException.class,
                () -> customerService.getMapAmountByClient(CustomerScenario.getEstablishment(listCustomers)));

        assertTrue(e.getMessage().contains("Client Not Found"));
    }

    @Test
    void shouldThrowExceptionIfClientsHaveSameName(){
        var listCustomers = CustomerScenario.getCustomersSameName();
        var e = assertThrows(ClientException.class,
                () -> customerService.getMapAmountByClient(CustomerScenario.getClientAccount(listCustomers)));

        assertTrue(e.getMessage().contains("Client must have different identifying names"));
    }

    @Test
    void getMapEstablishmentWithSuccess(CapturedOutput capture){
        var listCustomers = CustomerScenario.getCustomers();
        when(transactionService.getMapEstablishment(CustomerScenario.getEstablishment(listCustomers),
                OperationTypeEnum.DEBIT,
                new BigDecimal(50)))
                .thenReturn(CustomerScenario.getMapTransactionsOfEstablishmentDebit());
        var map = customerService.getMapEstablishment(listCustomers,
                OperationTypeEnum.DEBIT,
                new BigDecimal(50));

        assertAll(
                () -> assertNotNull(map),
                () -> assertEquals(map.get(TransactionTypeEnum.PARTIAL), new BigDecimal(20)),
                () -> assertTrue(capture.getOut().contains(
                        format("Merging transaction type by establishment operation, of the type : %s",
                                OperationTypeEnum.DEBIT.name())))
        );
    }

    @Test
    void shouldThrowExceptionIfThereIsMoreThenOneEstablishment(){
        var listCustomers = CustomerScenario.getCustomers();
        var transactions = new ArrayList<Transaction>();
        transactions.add(Transaction.builder().description("Desconto")
                .value(new BigDecimal(20))
                .transactionType(TransactionTypeEnum.PARTIAL)
                .operationType(OperationTypeEnum.DEBIT)
                .valueType(ValueTypeEnum.CURRENCY)
                .build());
        var establishment = Customer.builder().name("Estabelecimento")
                .customerType(CustomerTypeEnum.ESTABLISHMENT)
                .transactionPostings(transactions)
                .build();
        listCustomers.add(establishment);

        var e = assertThrows(EstablishmentException.class,
                () -> customerService.getMapEstablishment(CustomerScenario.getEstablishment(listCustomers),
                        OperationTypeEnum.DEBIT,
                        new BigDecimal(50)));

        assertTrue(e.getMessage().contains("There cannot be more than one establishment for account"));
    }
}
