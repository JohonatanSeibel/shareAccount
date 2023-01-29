package br.com.shareaccount.service;

import br.com.shareaccount.domain.Customer;
import br.com.shareaccount.enumeration.CustomerTypeEnum;
import br.com.shareaccount.enumeration.OperationTypeEnum;
import br.com.shareaccount.enumeration.TransactionTypeEnum;
import br.com.shareaccount.exception.ClientException;
import br.com.shareaccount.exception.EstablishmentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@Validated
public class CustomerService {

    @Autowired
    private TransactionService transactionService;

    public Map<String, BigDecimal>  getMapAmountByClient(List<Customer> customers){
        log.info("Separating customers from the establishment");
        var clientsAccount = this.getClientAccount(customers);
        if(clientsAccount == null || clientsAccount.isEmpty())
            throw new ClientException("Client Not Found");

        var map = transactionService.getMapAmountByClient(clientsAccount);
        return map;
    }

    public Map<TransactionTypeEnum, BigDecimal> getMapEstablishment(List<Customer> customers,
                                                                    OperationTypeEnum operationType,
                                                                    BigDecimal accountAmount){
        log.info("Merging transaction type by establishment operation, of the type : {}", operationType);
        var clientsAccount = this.getEstablishment(customers);
        var map = transactionService.getMapEstablishment(clientsAccount,
                                                    operationType,
                                                    accountAmount);
        return map;
    }

    private List<Customer> getClientAccount(List<Customer> customers){
        var clients = customers.stream()
                .filter(a -> !CustomerTypeEnum.ESTABLISHMENT.equals(a.getCustomerType()))
                .collect(Collectors.toList());
        this.validateClientName(clients);
        return clients;
    }

    private List<Customer> getEstablishment(List<Customer> customers){
        var establishments = customers.stream()
                .filter(a -> CustomerTypeEnum.ESTABLISHMENT.equals(a.getCustomerType()))
                .collect(Collectors.toList());
        if(establishments.size() > 1)
            throw new EstablishmentException("There cannot be more than one establishment for account");
        return establishments;
    }

    private void validateClientName(List<Customer> clients){
        for(Customer clientInValidation : clients){
            var count = 0;
            for(Customer client : clients){
                if(clientInValidation.getName().equals(client.getName()))
                    count++;
            }
            if(count > 1)
                throw new ClientException("Client must have different identifying names");
        }
    }
}
