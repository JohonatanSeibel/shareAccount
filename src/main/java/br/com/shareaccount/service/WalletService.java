package br.com.shareaccount.service;

import br.com.shareaccount.client.WalletClient;
import br.com.shareaccount.dto.WalletDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Service
@Slf4j
@Validated
public class WalletService {
    @Autowired
    private WalletClient client;

    public String getBillingWallet(BigDecimal value, String name){
        log.info("Get billing wallet link for client {}", name);
        var wallet = new WalletDTO(value, "teste", name.toString());
        var billingWallet =  client.getPixCode("Bearer TEST-2933677929195917-012709-99566d73916680b8b167cf1201699e3c-443418201", wallet);
        var billingWalletLink = billingWallet.getBillingWalletLink();
        log.info("Link generated with sucess for client {}", name);
        return billingWalletLink;
    }
}
