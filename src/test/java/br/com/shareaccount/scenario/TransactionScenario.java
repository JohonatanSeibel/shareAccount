package br.com.shareaccount.scenario;

import br.com.shareaccount.enumeration.TransactionTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionScenario {

    public static Map<TransactionTypeEnum, BigDecimal> getMapDebit(){
        var map = new HashMap<TransactionTypeEnum, BigDecimal>();
        map.put(TransactionTypeEnum.PARTIAL, new BigDecimal(20));
        map.put(TransactionTypeEnum.FULL, BigDecimal.ZERO);
        return map;
    }

    public static Map<TransactionTypeEnum, BigDecimal> getMapCredit(){
        var map = new HashMap<TransactionTypeEnum, BigDecimal>();
        map.put(TransactionTypeEnum.PARTIAL, new BigDecimal(8));
        map.put(TransactionTypeEnum.FULL, BigDecimal.ZERO);
        return map;
    }

    public static Map<TransactionTypeEnum, BigDecimal> getMapCreditZeroVallue(){
        var map = new HashMap<TransactionTypeEnum, BigDecimal>();
        map.put(TransactionTypeEnum.PARTIAL, BigDecimal.ZERO);
        map.put(TransactionTypeEnum.FULL, BigDecimal.ZERO);
        return map;
    }

}
