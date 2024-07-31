package com.virgo.rekomendasos.controller;

import com.virgo.rekomendasos.utils.responseWrapper.PaginationResponse;
import com.virgo.rekomendasos.utils.responseWrapper.Response;
import com.virgo.rekomendasos.utils.responseWrapper.WebResponse;
import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.service.VoucherService;
import com.virgo.rekomendasos.service.VoucherTransactionService;
import com.virgo.rekomendasos.utils.dto.VoucherDTO;
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
@Tag(name = "Voucher", description = "Voucher management APIs")
public class VoucherController {

    //    @Autowired
    private final VoucherService voucherService;
    private final VoucherTransactionService voucherTransactionService;

    @Operation(summary = "Get all vouchers", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success!", content = {@Content(schema = @Schema(implementation = WebResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/admin/vouchers")
    public ResponseEntity<?> findAll(@PageableDefault Pageable pageable) {
        Page<Voucher> voucherPage = voucherService.findAll(pageable);
        PaginationResponse<Voucher> res = new PaginationResponse<>(voucherPage);
        return Response.renderJSON(res);
    }

    @Operation(summary = "Get voucher by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success!", content = {@Content(schema = @Schema(implementation = WebResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/admin/vouchers/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        return Response.renderJSON(voucherService.findById(id));
    }

    @Operation(summary = "Create a new voucher", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success!", content = {@Content(schema = @Schema(implementation = WebResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @PostMapping("/admin/vouchers")
    public ResponseEntity<?> create(@RequestBody VoucherDTO voucherDTO) {
        return Response.renderJSON(voucherService.create(voucherDTO));
    }

    @Operation(summary = "Update voucher by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success!", content = {@Content(schema = @Schema(implementation = WebResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @PutMapping("/admin/vouchers/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody VoucherDTO voucherDTO) {
        return Response.renderJSON(voucherService.updateById(id, voucherDTO));
    }

    @Operation(summary = "Delete voucher by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success!", content = {@Content(schema = @Schema(implementation = WebResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @DeleteMapping("/admin/vouchers/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        voucherService.deleteById(id);
        return Response.renderJSON(null, "Voucher with id " + id + " deleted");
    }

    @Operation(summary = "Get user vouchers", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success!", content = {@Content(schema = @Schema(implementation = WebResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/user/vouchers")
    public ResponseEntity<?> getUserVouches(@PageableDefault Pageable pageable) {
        Page<Voucher> voucherPage = voucherService.findAllVoucherByUserId(pageable);
        PaginationResponse<Voucher> res = new PaginationResponse<>(voucherPage);
        return Response.renderJSON(res);
    }

    @Operation(summary = "Get user voucher by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success!", content = {@Content(schema = @Schema(implementation = WebResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/user/vouchers/{voucherId}")
    public ResponseEntity<?> getUserVoucherById(@PathVariable Integer voucherId) {
        return Response.renderJSON(voucherService.findByUserIdAndId(voucherId));
    }

    @Operation(summary = "Get all public vouchers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success!", content = {@Content(schema = @Schema(implementation = WebResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/vouchers")
    public ResponseEntity<?> findAllPublicVouchers(@PageableDefault Pageable pageable) {
        Page<Voucher> voucherPage = voucherService.findAll(pageable);
        PaginationResponse<Voucher> res = new PaginationResponse<>(voucherPage);
        return Response.renderJSON(res);
    }

}
