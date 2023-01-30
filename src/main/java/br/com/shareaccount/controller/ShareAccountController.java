package br.com.shareaccount.controller;

import br.com.shareaccount.dto.AccountRequestDTO;
import br.com.shareaccount.dto.AccountResponseDTO;
import br.com.shareaccount.service.ShareAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "ShareAccount")
@Validated
@RequiredArgsConstructor
public class ShareAccountController {

    private final ShareAccountService shareAccountService;
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(path = "shareAccount", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns Share Account With Friends")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Account successfully listed."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error when listing account to customers.")})
    public AccountResponseDTO shareAccount(@RequestBody AccountRequestDTO request){
        log.info("Starting invoice calculation...");
        var response = shareAccountService.execute(request);
        log.info("Invoice calculation completed successfully for {} customers...", response.getQuantityClients());
        return response;
    }
}
