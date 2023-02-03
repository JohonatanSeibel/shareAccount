package br.com.shareaccount.service;

import br.com.shareaccount.client.WalletClient;
import br.com.shareaccount.domain.WalletDTO;
import br.com.shareaccount.exception.WalletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Service
@Slf4j
@Validated
public class WalletService {
    @Autowired
    private WalletClient client;

    @Value("${walletclient.token}")
    private String TOKEN;

    public String getBillingWallet(BigDecimal value, String name){
        log.info("Get billing wallet link for client {}", name);
        var wallet = new WalletDTO(value, "Account", name);
        try {
            var billingWallet =  client.getPixCode(TOKEN, wallet);
            var billingWalletLink = billingWallet.getBillingWalletLink();
            log.info("Link generated with sucess for client {}", name);
            return billingWalletLink;
        }catch (Exception e){
            throw new WalletException("Could not establish connection with wallet");
        }
    }

}
