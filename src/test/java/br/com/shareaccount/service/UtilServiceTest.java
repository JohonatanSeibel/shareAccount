package br.com.shareaccount.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UtilServiceTest {

    @InjectMocks
    private UtilService utilService;

    @Test
    void getValueByPercentageWithSuccess(){
        var percentege = new BigDecimal(50);
        var value = new BigDecimal(30);
        var result = utilService.getValueByPercentage(percentege, value);

        assertAll(
                ()-> assertNotNull(result),
                ()-> assertEquals(result, new BigDecimal("15.0"))
        );
    }

    @Test
    void getPercentageByValueWIthSuccess(){
        var amount = new BigDecimal(50);
        var value = new BigDecimal(30);
        var result = utilService.getPercentageByValue(value, amount);

        assertAll(
                ()-> assertNotNull(result),
                ()-> assertEquals(result, new BigDecimal(60))
        );

    }

    @Test
    void getAmountValueWithSuccess(){
       var map = new HashMap<String, BigDecimal>();
       map.put("Lanche", new BigDecimal(40));
       map.put("Suco", new BigDecimal(2));
       var result = utilService.getAmountValue(map);

        assertAll(
                ()-> assertNotNull(result),
                ()-> assertEquals(result, new BigDecimal(42))
        );
    }
}
