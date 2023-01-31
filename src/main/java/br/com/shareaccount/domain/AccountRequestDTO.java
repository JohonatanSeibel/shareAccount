package br.com.shareaccount.domain;

import br.com.shareaccount.domain.Customer;
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
