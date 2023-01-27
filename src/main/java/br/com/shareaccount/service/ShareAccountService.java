package br.com.shareaccount.service;

import br.com.shareaccount.dto.AccountRequestDTO;
import br.com.shareaccount.dto.AccountResponseDTO;
import br.com.shareaccount.enumeration.OperationTypeEnum;
import br.com.shareaccount.enumeration.TransactionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@Validated
public class ShareAccountService {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private UtilService utilService;

    @Autowired
    private CalculateAccountService calculateAccountService;
    public AccountResponseDTO execute(AccountRequestDTO request){
        log.info("Beginning of the account split process...");
        var mapAmountByClient = customerService.getMapAmountByClient(request.getCustomerInvoices());

        var accountAmount = utilService.getAmountValue(mapAmountByClient);

        var mapDebitTransactions =
                customerService.getMapEstablishment(request.getCustomerInvoices(),
                        OperationTypeEnum.DEBIT,
                        accountAmount);

        var mapCreditTransactions =
                customerService.getMapEstablishment(request.getCustomerInvoices(),
                        OperationTypeEnum.CREDIT,
                        accountAmount);

        var quantityClients = mapAmountByClient.size();

        var customersDto = calculateAccountService.getCalculatedCustomers(mapAmountByClient,
                mapDebitTransactions,
                mapCreditTransactions);

        var discountAmount = mapDebitTransactions.get(TransactionTypeEnum.PARTIAL)
                .add(mapDebitTransactions.get(TransactionTypeEnum.FULL));

        var response = AccountResponseDTO.builder()
                .quantityClients(quantityClients)
                .accountAmount(accountAmount)
                .customersInvoiced(customersDto)
                .discountValue(discountAmount)
                .build();
        log.info("End of account split process...");
        return response;
    }
}
