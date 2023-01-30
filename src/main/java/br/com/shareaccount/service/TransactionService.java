package br.com.shareaccount.service;

import br.com.shareaccount.domain.Customer;
import br.com.shareaccount.domain.Transaction;
import br.com.shareaccount.enumeration.OperationTypeEnum;
import br.com.shareaccount.enumeration.TransactionTypeEnum;
import br.com.shareaccount.enumeration.ValueTypeEnum;
import br.com.shareaccount.exception.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
public class TransactionService {

    public Map<String, BigDecimal> getMapAmountByClient(List<Customer> customers){
        var map = new HashMap<String,BigDecimal>();
        for(Customer customer: customers){
            var amountByCustomer = BigDecimal.ZERO;
            for (Transaction transaction : customer.getTransactionPostings()) {
                this.validateClientTransaction(transaction);
                amountByCustomer = amountByCustomer.add(transaction.getValue());
            }
            map.put(customer.getName(), amountByCustomer);
        }
        return map;
    }

    public Map<TransactionTypeEnum, BigDecimal> getMapEstablishment(List<Customer> customers,
                                                                    OperationTypeEnum operationTypeEnum,
                                                                    BigDecimal accountAmount){
        var map = new HashMap<TransactionTypeEnum,BigDecimal>();
        var partialValue = BigDecimal.ZERO;
        var fullValue = BigDecimal.ZERO;
        for(Customer customer: customers){
            var listTransactionTypes = customer.getTransactionPostings().stream()
                    .filter(t -> operationTypeEnum.equals(t.getOperationType())).collect(Collectors.toList());
            for (Transaction transaction : listTransactionTypes) {
                var value = this.getValueByPercentageOrCurrency(transaction, accountAmount);
               if(TransactionTypeEnum.PARTIAL.equals(transaction.getTransactionType())){
                   partialValue = partialValue.add(value);
               }else{
                   fullValue = fullValue.add(value);
               }
            }
        }
        map.put(TransactionTypeEnum.PARTIAL, partialValue);
        map.put(TransactionTypeEnum.FULL, fullValue);
        return map;
    }

    private BigDecimal getValueByPercentageOrCurrency(Transaction transaction, BigDecimal accountAmount){
      return ValueTypeEnum.CURRENCY.equals(transaction.getValueType()) ? transaction.getValue() :
              UtilService.getValueByPercentage(transaction.getValue(), accountAmount);
    }

    private void validateClientTransaction(Transaction transaction){
        if(transaction.getTransactionType() != null &&
                !TransactionTypeEnum.PARTIAL.equals(transaction.getTransactionType()))
            throw new ClientException("Client cannot create transactions of type FULL");

        if(transaction.getOperationType() != null
                && !OperationTypeEnum.CREDIT.equals(transaction.getOperationType()))
            throw new ClientException("Client cannot have debit transaction");

        if(transaction.getValueType() != null
                && !ValueTypeEnum.CURRENCY.equals(transaction.getValueType()))
            throw new ClientException("Client cannot have percentage currency transaction");
    }
}
