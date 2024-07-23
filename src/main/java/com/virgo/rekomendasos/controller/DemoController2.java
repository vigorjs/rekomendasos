//package com.virgo.rekomendasos.controller;
//
//import com.virgo.rekomendasos.utils.dto.TaskRequestDTO;
//import com.virgo.rekomendasos.service.TaskService;
//import com.virgo.rekomendasos.utils.response.PaginationResponse;
//import com.virgo.rekomendasos.utils.response.Response;
//import com.virgo.rekomendasos.utils.response.WebResponse;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/task")
//@RestControllerAdvice
//@Tag(name = "Task", description = "Task management APIs")
//public class DemoController2 {
//    private final TaskService taskService;
//
//    public DemoController2(TaskService taskService) {
//        this.taskService = taskService;
//    }
//
//    @Operation(summary = "Create a new task", security = @SecurityRequirement(name = "bearerAuth"))
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Task berhasil dibuat!", content = { @Content(schema = @Schema(implementation = WebResponse.class)) }),
//            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
//            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
//            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
//    })
//    @PostMapping("/create")
//    public ResponseEntity<?> create(@RequestBody TaskRequestDTO req) {
//        return Response.renderJSON(
//                taskService.create(req),
//                "Task berhasil dibuat!",
//                HttpStatus.CREATED
//        );
//    }
//
//    @Operation(summary = "Get all task", security = @SecurityRequirement(name = "bearerAuth"))
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Berhasil get all Task", content = { @Content(schema = @Schema(implementation = WebResponse.class)) }),
//            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
//            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
//            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
//    })
//    @GetMapping
//    public ResponseEntity<?> getAll(@PageableDefault Pageable pageable, @RequestParam(required = false) String name) {
//        return Response.renderJSON(new PaginationResponse<>(taskService.getAll(pageable, name)));
//    }
//
//    @Operation(summary = "Get task by ID", security = @SecurityRequirement(name = "bearerAuth"))
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Berhasil get Task", content = { @Content(schema = @Schema(implementation = WebResponse.class)) }),
//            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
//            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
//            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
//    })
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getById(@PathVariable Integer id) {
//        return Response.renderJSON(taskService.getById(id));
//    }
//
//    @Operation(summary = "Delete task by ID", security = @SecurityRequirement(name = "bearerAuth"))
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Task berhasil dihapus", content = { @Content(schema = @Schema(implementation = WebResponse.class)) }),
//            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
//            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
//            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
//    })
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> delete(@PathVariable Integer id) {
//        taskService.delete(id);
//        return Response.renderJSON(null,"Task berhasil dihapus", HttpStatus.OK);
//    }
//
//    @Operation(summary = "Update task by ID", security = @SecurityRequirement(name = "bearerAuth"))
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Task berhasil diupdate", content = { @Content(schema = @Schema(implementation = WebResponse.class)) }),
//            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
//            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
//            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
//    })
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateById(@PathVariable Integer id, @RequestBody TaskRequestDTO req){
//        return Response.renderJSON(
//                taskService.updateById(id, req),
//                "Berhasil diupdate!!"
//        );
//    }
//
//}
