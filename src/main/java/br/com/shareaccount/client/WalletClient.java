package br.com.shareaccount.client;

import br.com.shareaccount.domain.WalletDTO;
import br.com.shareaccount.domain.WalletResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="WalletClient", url="${walletclient.baseUrl}")
public interface WalletClient {

    @PostMapping(value = "/v1/payments")
    WalletResponseDTO getPixCode(@RequestHeader("Authorization") String token, @RequestBody WalletDTO wallet);
}
