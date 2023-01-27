package br.com.shareaccount.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@ToString
@Getter
@Setter
@Builder
public class WalletResponseDTO {
    public int id;
    @JsonProperty(value = "point_of_interaction")
    public PointOfInteraction pointOfInteraction;

    public String getBillingWalletLink(){
        return this.pointOfInteraction.getTransactionData().getTicket_url();
    }
}

@ToString
@Getter
@Setter
@Builder
class PointOfInteraction{
    public String type;
    @JsonProperty(value = "transaction_data")
    public TransactionData transactionData;
}
@ToString
@Getter
@Setter
@Builder
class TransactionData{
    public String qr_code;
    public Object bank_transfer_id;
    public Object transaction_id;
    public Object e2e_id;
    public Object financial_institution;
    public String ticket_url;
    public String qr_code_base64;
}

