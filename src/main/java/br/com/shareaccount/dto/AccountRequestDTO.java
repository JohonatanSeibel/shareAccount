package br.com.shareaccount.dto;

import br.com.shareaccount.domain.Customer;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {
    private List<Customer> customerInvoices;
}
