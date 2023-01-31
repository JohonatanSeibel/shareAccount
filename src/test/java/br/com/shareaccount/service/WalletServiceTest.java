package br.com.shareaccount.service;

import br.com.shareaccount.client.WalletClient;
import br.com.shareaccount.domain.WalletDTO;
import br.com.shareaccount.domain.WalletResponseDTO;
import br.com.shareaccount.exception.WalletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletClient walletClient;

    private final String QRCODE = "00020126580014br.gov.bcb.pix0136b76aa9c2-2ec4-4110-954e-ebfe34f05b6152040";

    private final String LINK = "https://www.mercadopago.com.br/sandbox/payments/1311426186/ticket?caller_id=1201321128&hash=fd5b332f-7117-483e-9940-2e8d60b143f6";

    private String TOKEN = "token" ;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(walletService, "TOKEN", TOKEN);
    }


    @Test
    void getBillingWalletWithSuccess(CapturedOutput capture){
        var value = new BigDecimal(10);
        var name = "Rodrigo";
        var wallet = new WalletDTO(value, "Account", name);
        var walletResponse = WalletResponseDTO.buildWalletResponse("OPENPLATFORM", QRCODE, LINK);
        when(walletClient.getPixCode(anyString(), any(WalletDTO.class))).thenReturn(walletResponse);
        var response = walletService.getBillingWallet(value, name);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(response, LINK),
                () -> assertTrue(capture.getOut().contains(format("Get billing wallet link for client %s", name))),
                () -> assertTrue(capture.getOut().contains(format("Link generated with sucess for client %s", name)))
        );
    }

    @Test
    void shouldThrowExceptionIfCouldNotEstablishConnectionWithWallet(CapturedOutput capture){
        var value = new BigDecimal(10);
        var name = "Rodrigo";
        when(walletClient.getPixCode(anyString(), any(WalletDTO.class))).thenThrow(WalletException.class);
        var e = assertThrows(WalletException.class,
                                    () -> walletService.getBillingWallet(value, name));

        assertAll(
                () -> assertTrue(capture.getOut().contains(format("Get billing wallet link for client %s", name))),
                () -> assertTrue(e.getMessage().contains("Could not establish connection with wallet"))
        );

    }
}
