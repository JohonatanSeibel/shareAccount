package br.com.shareaccount.service;

import br.com.shareaccount.exception.WalletException;
import br.com.shareaccount.scenario.CustomerScenario;
import br.com.shareaccount.scenario.TransactionScenario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculateAccountServiceTest {

    @InjectMocks
    private CalculateAccountService calculateAccountService;

    @Mock
    private WalletService walletService;

    @Test
    void getCalculatedCustomersWithSuccess(){
        var mapClient = CustomerScenario.getMapClients();
        var mapDebit = TransactionScenario.getMapDebit();
        var mapCredit = TransactionScenario.getMapCredit();
        var valueToPayMarcelo = new BigDecimal("6.08");
        var valueToPayRodrigo = new BigDecimal("31.92");
        var linkMarcelo = "https://www.mercadopago.com.br/sandbox/payments/1311426608/marcelo608";
        var linkRodrigo = "https://www.mercadopago.com.br/sandbox/payments/1311426608/rodrigo3192";
        when(walletService.getBillingWallet(valueToPayMarcelo, "Marcelo"))
                .thenReturn(linkMarcelo);
        when(walletService.getBillingWallet(valueToPayRodrigo, "Rodrigo"))
                .thenReturn(linkRodrigo);
        var customers = calculateAccountService.getCalculatedCustomers(mapClient,mapDebit, mapCredit);

        assertAll(
                ()-> assertNotNull(customers),
                ()-> assertEquals(customers.get(0).getName(), "Rodrigo"),
                ()-> assertEquals(customers.get(0).getValueToPay(), valueToPayRodrigo),
                ()-> assertEquals(customers.get(0).getBillingWalletLink(), linkRodrigo),
                ()-> assertEquals(customers.get(1).getName(), "Marcelo"),
                ()-> assertEquals(customers.get(1).getValueToPay(), valueToPayMarcelo),
                ()-> assertEquals(customers.get(1).getBillingWalletLink(), linkMarcelo)
        );
    }

    @Test
    void shouldThrowExceptionIfItHearsErrorToGeneratePaymentLink(){
        var mapClient = CustomerScenario.getMapClients();
        var mapDebit = TransactionScenario.getMapDebit();
        var mapCredit = TransactionScenario.getMapCredit();
        var valueToPayRodrigo = new BigDecimal("31.92");
        when(walletService.getBillingWallet(valueToPayRodrigo, "Rodrigo"))
                .thenThrow(WalletException.class);

        var e = assertThrows(WalletException.class,
                ()-> calculateAccountService.getCalculatedCustomers(mapClient,mapDebit, mapCredit)
        );
    }
}
