package br.com.shareaccount.service;

import br.com.shareaccount.enumeration.TransactionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Map;

@Service
@Slf4j
@Validated
public class UtilService {

    public static BigDecimal getValueByPercentage(BigDecimal percentage, BigDecimal value){
        return value.divide(new BigDecimal(100)).multiply(percentage);
    }

    public static BigDecimal getPercentageByValue(BigDecimal value, BigDecimal amount){
        return value.multiply(new BigDecimal(100)).divide(amount);
    }

    public static BigDecimal getPercentageByQuantityClients(Integer quantity){
        return new BigDecimal(100).divide(new BigDecimal(quantity));
    }

    public static BigDecimal getAmountValue(Map<String, BigDecimal> map){
        var value = BigDecimal.ZERO;
        for (BigDecimal v : map.values()){
            value = value.add(v);
        }
        return value;
    }

    public static BigDecimal getAmountValueByTransectionType(Map<TransactionTypeEnum, BigDecimal> map){
        var value = BigDecimal.ZERO;
        for (BigDecimal v : map.values()){
            value = value.add(v);
        }
        return value;
    }
}
