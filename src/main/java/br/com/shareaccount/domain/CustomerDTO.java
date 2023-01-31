package br.com.shareaccount.domain;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private String name;
    private BigDecimal valueToPay;
    private String billingWalletLink;
}
