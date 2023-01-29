package br.com.shareaccount.scenario;

import br.com.shareaccount.dto.AccountResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseScenario {

    public static AccountResponseDTO getResponse(){
        var customers = CustomerScenario.calculatedCustomers();
        var response = AccountResponseDTO.builder().accountAmount(new BigDecimal("50.0"))
                .quantityClients(2)
                .discountValue(new BigDecimal("20.0"))
                .customersInvoiced(customers).build();
        return response;
    }
}
