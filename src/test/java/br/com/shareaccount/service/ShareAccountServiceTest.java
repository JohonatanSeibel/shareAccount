package br.com.shareaccount.service;

import br.com.shareaccount.domain.Customer;
import br.com.shareaccount.domain.Transaction;
import br.com.shareaccount.domain.AccountRequestDTO;
import br.com.shareaccount.enumeration.CustomerTypeEnum;
import br.com.shareaccount.enumeration.OperationTypeEnum;
import br.com.shareaccount.enumeration.TransactionTypeEnum;
import br.com.shareaccount.enumeration.ValueTypeEnum;
import br.com.shareaccount.exception.ClientException;
import br.com.shareaccount.exception.EstablishmentException;
import br.com.shareaccount.exception.ShareAccountException;
import br.com.shareaccount.scenario.CustomerScenario;
import br.com.shareaccount.scenario.RequestScenario;
import br.com.shareaccount.scenario.TransactionScenario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(OutputCaptureExtension.class)
@ExtendWith(MockitoExtension.class)
public class ShareAccountServiceTest {

    @InjectMocks
    private ShareAccountService shareAccountService;

    @Mock
    private CustomerService customerService;

    @Mock
    private CalculateAccountService calculateAccountService;

    @Test
    void shareAccountSuccessfullyExecuted(CapturedOutput capture){
        var linkMarcelo = "https://www.mercadopago.com.br/sandbox/payments/1311426608/marcelo608";
        var linkRodrigo = "https://www.mercadopago.com.br/sandbox/payments/1311426608/rodrigo3192";
        var accountAmount = new BigDecimal(50);
        var request = RequestScenario.getRequest();

        when(customerService.getMapAmountByClient(request.getCustomerInvoices()))
                .thenReturn(CustomerScenario.getMapClients());

        when(customerService.getMapEstablishment(request.getCustomerInvoices(),
                OperationTypeEnum.DEBIT,
                accountAmount)).thenReturn(TransactionScenario.getMapDebit());

        when(customerService.getMapEstablishment(request.getCustomerInvoices(),
                OperationTypeEnum.CREDIT,
                accountAmount)).thenReturn(TransactionScenario.getMapCredit());

        when(calculateAccountService.getCalculatedCustomers(CustomerScenario.getMapClients(),
                TransactionScenario.getMapDebit(),
                TransactionScenario.getMapCredit())).thenReturn(CustomerScenario.calculatedCustomers());
        var totalAmountWithCredits = accountAmount
                .add(UtilService.getAmountValueByTransectionType(TransactionScenario.getMapCredit()));
        var response = shareAccountService.execute(request);

        assertAll(
                ()-> assertNotNull(response),
                ()-> assertEquals(response.getAccountAmount(), totalAmountWithCredits),
                ()-> assertEquals(response.getQuantityClients(), 2),
                ()-> assertEquals(response.getDiscountValue(), new BigDecimal(20)),
                ()-> assertEquals(response.getCustomersInvoiced().get(0).getName(), "Rodrigo"),
                ()-> assertEquals(response.getCustomersInvoiced().get(0).getValueToPay(), new BigDecimal("31.92")),
                ()-> assertEquals(response.getCustomersInvoiced().get(0).getBillingWalletLink(), linkRodrigo),
                ()-> assertEquals(response.getCustomersInvoiced().get(1).getName(), "Marcelo"),
                ()-> assertEquals(response.getCustomersInvoiced().get(1).getValueToPay(), new BigDecimal("6.08")),
                ()-> assertEquals(response.getCustomersInvoiced().get(1).getBillingWalletLink(), linkMarcelo),
                ()-> assertTrue(capture.getOut().contains("Beginning of the account split process...")),
                ()-> assertTrue(capture.getOut().contains("End of account split process..."))
        );
    }

    @Test
    void shouldThrowExceptionIfCustomersHaveSameName(CapturedOutput capture){
        var customers = CustomerScenario.getCustomersSameName();
        var request = AccountRequestDTO.builder().customerInvoices(customers).build();

        when(customerService.getMapAmountByClient(request.getCustomerInvoices()))
                        .thenThrow(ClientException.class);
        assertAll(
                ()-> assertThrows(ClientException.class, ()-> shareAccountService.execute(request)),
                ()-> assertTrue(capture.getOut().contains("Beginning of the account split process...")),
                ()-> assertFalse(capture.getOut().contains("End of account split process..."))
        );
    }

    @Test
    void shouldThrowExceptionIfHaveMoreToOneEstablishment(CapturedOutput capture){
        var accountAmount = new BigDecimal(50);
        var customers = CustomerScenario.getMoreOneEstablishment();
        var request = AccountRequestDTO.builder().customerInvoices(customers).build();

        when(customerService.getMapAmountByClient(request.getCustomerInvoices()))
                .thenReturn(CustomerScenario.getMapClients());

        when(customerService.getMapEstablishment(request.getCustomerInvoices(),
                OperationTypeEnum.DEBIT,
                accountAmount)).thenThrow(EstablishmentException.class);

        assertAll(
                ()-> assertThrows(EstablishmentException.class, ()-> shareAccountService.execute(request)),
                ()-> assertTrue(capture.getOut().contains("Beginning of the account split process...")),
                ()-> assertFalse(capture.getOut().contains("End of account split process..."))
        );
    }

    @Test
    void shouldThrowExceptionIfDiscountAmountIsGreaterThanCreditAmount(CapturedOutput capture){
        var accountAmount = BigDecimal.ZERO;
        var request = RequestScenario.getRequestWithAccountAmountIsZero();
        var transaction = Transaction.builder().description("Covert")
                .value(new BigDecimal(10))
                .transactionType(TransactionTypeEnum.PARTIAL)
                .operationType(OperationTypeEnum.CREDIT)
                .valueType(ValueTypeEnum.CURRENCY)
                .build();
        for(Customer c : request.getCustomerInvoices()){
            if(CustomerTypeEnum.ESTABLISHMENT.equals(c.getCustomerType())) {
                c.getTransactionPostings().add(transaction);
            }
        }

        when(customerService.getMapAmountByClient(request.getCustomerInvoices()))
                .thenReturn(CustomerScenario.getMapClientsZeroValue());

        when(customerService.getMapEstablishment(request.getCustomerInvoices(),
                OperationTypeEnum.DEBIT,
                accountAmount)).thenReturn(TransactionScenario.getMapDebit());

        when(customerService.getMapEstablishment(request.getCustomerInvoices(),
                OperationTypeEnum.CREDIT,
                accountAmount)).thenReturn(TransactionScenario.getMapCredit());

        var e = assertThrows(ShareAccountException.class, ()-> shareAccountService.execute(request));

        assertAll(
                ()-> assertTrue(capture.getOut().contains("Beginning of the account split process...")),
                () -> assertTrue(e.getMessage().contains("The discount cannot be greater than the credit amounts"))
        );
    }

    @Test
    void shouldThrowExceptionIfAmountValueIsZero(CapturedOutput capture){
        var accountAmount = BigDecimal.ZERO;
        var request = RequestScenario.getRequestWithAccountAmountIsZero();

        when(customerService.getMapAmountByClient(request.getCustomerInvoices()))
                .thenReturn(CustomerScenario.getMapClientsZeroValue());

        when(customerService.getMapEstablishment(request.getCustomerInvoices(),
                OperationTypeEnum.DEBIT,
                accountAmount)).thenReturn(TransactionScenario.getMapDebit());

        when(customerService.getMapEstablishment(request.getCustomerInvoices(),
                OperationTypeEnum.CREDIT,
                accountAmount)).thenReturn(TransactionScenario.getMapCreditZeroVallue());

        var e = assertThrows(ShareAccountException.class, ()-> shareAccountService.execute(request));

        assertAll(
                ()-> assertTrue(capture.getOut().contains("Beginning of the account split process...")),
                () -> assertTrue(e.getMessage().contains("Account value must be greater than zero"))
        );
    }
}
