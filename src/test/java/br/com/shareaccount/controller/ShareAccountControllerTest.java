package br.com.shareaccount.controller;

import br.com.shareaccount.dto.AccountRequestDTO;
import br.com.shareaccount.exception.ClientException;
import br.com.shareaccount.scenario.RequestScenario;
import br.com.shareaccount.scenario.ResponseScenario;
import br.com.shareaccount.service.ShareAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

@WebMvcTest(ShareAccountController.class)
public class ShareAccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ShareAccountService shareAccountService;

    ObjectMapper mapper;
    @BeforeEach
    void setUP(){
        mapper = new ObjectMapper();
    }

    public ResultActions makeRequest(String json, String httpMethod) throws Exception{
        return mockMvc.perform(MockMvcRequestBuilders.request(httpMethod, URI.create("/shareAccount"))
                .contentType(MediaType.APPLICATION_JSON).content(json));
    }

    @Test
    void callRoteShareAccountWithSuccess() throws Exception {
        var request = RequestScenario.getRequest();
        when(shareAccountService.execute(any(AccountRequestDTO.class))).thenReturn(ResponseScenario.getResponse());

        String json = mapper.writeValueAsString(request);

        makeRequest(json, "POST").andExpect(status().is(200))
                .andExpect(jsonPath("$.accountAmount").exists())
                .andExpect(jsonPath("$.quantityClients").exists())
                .andExpect(jsonPath("$.discountValue").exists())
                .andExpect(jsonPath("$.customersInvoiced").isArray())
                .andExpect(jsonPath("$.customersInvoiced[0].name").exists())
                .andExpect(jsonPath("$.customersInvoiced[0].valueToPay").exists())
                .andExpect(jsonPath("$.customersInvoiced[0].billingWalletLink").exists());
    }
    @Test
    void callRoteShareAccountAndClientNotFound() throws Exception{
        var request = RequestScenario.getRequest();
        when(shareAccountService.execute(any(AccountRequestDTO.class)))
                .thenThrow(ClientException.class);
        String json = mapper.writeValueAsString(request);

        makeRequest(json, "POST")
                .andExpect(status().is(400));
    }

}
