package br.com.shareaccount.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@ToString
@Getter
@Setter
@Builder
public class WalletResponseDTO {
    public int id;
    @JsonProperty(value = "point_of_interaction")
    public PointOfInteraction pointOfInteraction;

    public static WalletResponseDTO buildWalletResponse(String type, String qrCode, String ticketUrl){
        var transactionData = TransactionData.builder()
                .qrCode(qrCode)
                .ticketUrl(ticketUrl).build();
        var pointOfInteraction = PointOfInteraction.builder()
                .type(type)
                .transactionData(transactionData).build();
        return WalletResponseDTO.builder().id(001).pointOfInteraction(pointOfInteraction).build();
    }

    public String getBillingWalletLink(){
        return this.pointOfInteraction.getTransactionData().getTicketUrl();
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
    @JsonProperty(value = "qr_code")
    public String qrCode;
    @JsonProperty(value = "transaction_id")
    public Object transactionId;
    @JsonProperty(value = "ticket_url")
    public String ticketUrl;
    @JsonProperty(value = "qr_code_base64")
    public String qrCodeBase64;
}

