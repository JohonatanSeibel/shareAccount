package br.com.shareaccount.service;

import br.com.shareaccount.domain.AccountRequestDTO;
import br.com.shareaccount.domain.AccountResponseDTO;
import br.com.shareaccount.enumeration.OperationTypeEnum;
import br.com.shareaccount.enumeration.TransactionTypeEnum;
import br.com.shareaccount.exception.ShareAccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Map;

@Service
@Slf4j
@Validated
public class ShareAccountService {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CalculateAccountService calculateAccountService;
    public AccountResponseDTO execute(AccountRequestDTO request){
        log.info("Beginning of the account split process...");
        var mapAmountByClient = customerService.getMapAmountByClient(request.getCustomerInvoices());

        var accountAmount = UtilService.getAmountValue(mapAmountByClient);

        var mapDebitTransactions =
                customerService.getMapEstablishment(request.getCustomerInvoices(),
                        OperationTypeEnum.DEBIT,
                        accountAmount);

        var mapCreditTransactions =
                customerService.getMapEstablishment(request.getCustomerInvoices(),
                        OperationTypeEnum.CREDIT,
                        accountAmount);

        this.validateInputValues(accountAmount, mapDebitTransactions, mapCreditTransactions);

        var quantityClients = mapAmountByClient.size();

        var customersDto = calculateAccountService.getCalculatedCustomers(mapAmountByClient,
                mapDebitTransactions,
                mapCreditTransactions);

        var discountAmount = mapDebitTransactions.get(TransactionTypeEnum.PARTIAL)
                .add(mapDebitTransactions.get(TransactionTypeEnum.FULL));

        var amountWithAdditionalCredit = accountAmount.
                add(UtilService.getAmountValueByTransectionType(mapCreditTransactions));

        var response = AccountResponseDTO.builder()
                .quantityClients(quantityClients)
                .accountAmount(amountWithAdditionalCredit)
                .customersInvoiced(customersDto)
                .discountValue(discountAmount)
                .build();
        log.info("End of account split process...");
        return response;
    }

    private void validateInputValues(BigDecimal accountAmount,
                                     Map<TransactionTypeEnum, BigDecimal> mapDebit,
                                     Map<TransactionTypeEnum, BigDecimal> mapCredit){
        var amountDebit = UtilService.getAmountValueByTransectionType(mapDebit);
        var amountCredit = UtilService.getAmountValueByTransectionType(mapCredit);

        accountAmount = accountAmount.add(amountCredit);

        if(accountAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new ShareAccountException("Account value must be greater than zero");

        if(accountAmount.compareTo(amountDebit) < 0)
            throw new ShareAccountException("The discount cannot be greater than the credit amounts");
    }
}
