package br.com.shareaccount.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {
    @JsonProperty(value = "transaction_amount")
    public BigDecimal transactionAmount;
    public String description;
    @JsonProperty(value = "payment_method_id")
    public String paymentMethodId;
    public Payer payer;
    @JsonIgnore
    public String name;

    public WalletDTO(BigDecimal transactionAmount, String description, String name){
        var payer = Payer.builder().firstName(name).email("teste@teste.com").build();
        this.transactionAmount = transactionAmount;
        this.description = description;
        this.paymentMethodId = "pix";
        this.payer = payer;
        this.name = name;
    }
}
@Builder
@Getter
@Setter
class Payer {
    public String email;
    @JsonProperty(value = "first_name")
    public String firstName;
}