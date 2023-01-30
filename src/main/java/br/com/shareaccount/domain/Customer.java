package br.com.shareaccount.domain;

import br.com.shareaccount.enumeration.CustomerTypeEnum;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private String name;
    private CustomerTypeEnum customerType;
    private List<Transaction> transactionPostings;
}
