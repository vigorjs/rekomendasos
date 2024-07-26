package com.virgo.rekomendasos.controller;

import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import com.virgo.rekomendasos.service.VoucherTransactionService;
import com.virgo.rekomendasos.utils.dto.VoucherTransactionDTO;
import com.virgo.rekomendasos.utils.response.PaginationResponse;
import com.virgo.rekomendasos.utils.response.Response;
import com.virgo.rekomendasos.utils.response.WebResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@RestControllerAdvice
@Tag(name = "Voucher Transaction", description = "Voucher Transaction management APIs")
public class VoucherTransactionController {

    //    @Autowired
    private final VoucherTransactionService voucherTransactionService;

    @Operation(summary = "Get all voucher transactions", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success!", content = {@Content(schema = @Schema(implementation = WebResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/admin/voucher_transactions")
    public ResponseEntity<?> findAll(@PageableDefault Pageable pageable) {
        Page<VoucherTransaction> voucherPage = voucherTransactionService.findAll(pageable);
        PaginationResponse<VoucherTransaction> res = new PaginationResponse<>(voucherPage);
        return Response.renderJSON(res);
    }

    @Operation(summary = "Get voucher transaction by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success!", content = {@Content(schema = @Schema(implementation = WebResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/admin/voucher_transactions/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        return Response.renderJSON(voucherTransactionService.findById(id));
    }

    @Operation(summary = "Create a new voucher transaction", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success!", content = {@Content(schema = @Schema(implementation = WebResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @PostMapping("/admin/voucher_transactions")
    public ResponseEntity<?> create(@RequestBody VoucherTransactionDTO request) {
        return Response.renderJSON(voucherTransactionService.create(request));
    }

    @Operation(summary = "Update a voucher transaction", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success!", content = {@Content(schema = @Schema(implementation = WebResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @PutMapping("/admin/voucher_transactions/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody VoucherTransactionDTO request) {
        return Response.renderJSON(voucherTransactionService.updateById(id, request));
    }

    @Operation(summary = "Delete voucher transaction by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success!", content = {@Content(schema = @Schema(implementation = WebResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @DeleteMapping("/admin/voucher_transactions/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        voucherTransactionService.deleteById(id);
        return Response.renderJSON(null, "Voucher deleted");
    }

    @PostMapping("/user/voucher_transactions")
    @Operation(summary = "Create a new voucher transaction", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success!", content = {@Content(schema = @Schema(implementation = WebResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    public ResponseEntity<?> createVoucherTransaction(@RequestBody VoucherTransactionDTO request) {
        return Response.renderJSON(voucherTransactionService.create(request));
    }
}
