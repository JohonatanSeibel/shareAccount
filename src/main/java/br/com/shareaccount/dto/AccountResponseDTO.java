package br.com.shareaccount.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private BigDecimal accountAmount;
    private Integer quantityClients;
    private BigDecimal discountValue;
    private List<CustomerDTO> customersInvoiced;
}
