package br.com.shareaccount.service;

import br.com.shareaccount.client.WalletClient;
import br.com.shareaccount.domain.Customer;
import br.com.shareaccount.dto.CustomerDTO;
import br.com.shareaccount.dto.WalletDTO;
import br.com.shareaccount.enumeration.TransactionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Validated
public class CalculateAccountService {

    @Autowired
    private UtilService utilService;

    @Autowired
    private WalletService walletService;
    public List<CustomerDTO> getCalculatedCustomers(Map<String, BigDecimal> mapClients,
                                                    Map<TransactionTypeEnum, BigDecimal> mapDebit,
                                                    Map<TransactionTypeEnum, BigDecimal> mapCredit){
        var accountAmount = utilService.getAmountValue(mapClients);

        var quantityClients = mapClients.size();

        var customers = new ArrayList<CustomerDTO>();

        for(Map.Entry map : mapClients.entrySet()){
            var name = map.getKey();

            var fullValueToClient = new BigDecimal(map.getValue().toString());

            var percentageAccountByClient = utilService.getPercentageByValue(fullValueToClient, accountAmount);

            var additionalCreditValue = this.calculateTotalAdd(mapCredit,
                    percentageAccountByClient, quantityClients);

            var additionalDebitValue = this.calculateTotalAdd(mapDebit,
                    percentageAccountByClient, quantityClients);

            var valueToPay = this.calculateIndividualValue(fullValueToClient,
                    additionalCreditValue, additionalDebitValue);

            var customer = CustomerDTO.builder()
                    .name(name.toString())
                    .valueToPay(valueToPay)
                    .billingWalletLink(walletService.getBillingWallet(valueToPay, name.toString()))
                    .build();
            customers.add(customer);
        }
        return customers;
    }

    private BigDecimal calculateTotalAdd(Map<TransactionTypeEnum, BigDecimal> map,
                                         BigDecimal percentage, Integer quantityClients){
        var partialValue = utilService.getValueByPercentage(percentage, map.get(TransactionTypeEnum.PARTIAL));
        var fullValue = map.get(TransactionTypeEnum.FULL).divide(new BigDecimal(quantityClients));
        var total = partialValue.add(fullValue);
        return total;
    }

    private BigDecimal calculateIndividualValue(BigDecimal fullValue,
                                                BigDecimal additionalCreditValue,
                                                BigDecimal additionalDebitValue){
        var totalToPay = (fullValue.add(additionalCreditValue)).subtract(additionalDebitValue);
        return totalToPay;
    }
}
