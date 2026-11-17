
package com.klef.soa.docker.controller;

import com.klef.soa.docker.dto.PlumbingRequestDTO;
import com.klef.soa.docker.entity.PlumbingRequest;
import com.klef.soa.docker.service.PlumbingRequestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")

@Tag(
    name = "Plumbing Request Management",
    description = "REST API for managing plumbing service requests"
)

public class PlumbingRequestController {

    @Autowired
    private PlumbingRequestService service;

    // HEALTH CHECK
    @GetMapping
    @Operation(
        summary = "Check API status",
        description = "Checks whether the Plumbing Request backend service is running."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is running")
    })
    public ResponseEntity<String> health() {
        return ResponseEntity.ok(
            "CRUD Service is running - Plumbing Request Backend"
        );
    }

    // ADD PLUMBING REQUEST
    @PostMapping("/Add")
    @Operation(
        summary = "Create a plumbing request",
        description = "Adds a new plumbing request. The request ID is generated automatically."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Plumbing request created successfully",
            content = @Content(
                schema = @Schema(implementation = PlumbingRequest.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request or error while creating request"
        )
    })
    public ResponseEntity<?> addRequest(
            @Valid @RequestBody PlumbingRequestDTO dto) {

        try {
            PlumbingRequest saved = service.addRequest(dto);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(
                "Error adding request: " + e.getMessage(),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    // DISPLAY ALL PLUMBING REQUESTS
    @GetMapping("/DisplayAll")
    @Operation(
        summary = "Retrieve all plumbing requests",
        description = "Fetches and displays all plumbing requests stored in the database."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Requests retrieved successfully",
            content = @Content(
                schema = @Schema(
                    type = "array",
                    implementation = PlumbingRequest.class
                )
            )
        )
    })
    public ResponseEntity<List<PlumbingRequest>> getAll() {

        List<PlumbingRequest> list = service.getAllRequests();
        return ResponseEntity.ok(list);
    }

    // DELETE PLUMBING REQUEST
    @DeleteMapping("/Delete/{id}")
    @Operation(
        summary = "Delete a plumbing request",
        description = "Deletes an existing plumbing request using its unique ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Plumbing request deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Plumbing request not found"
        )
    })
    public ResponseEntity<String> delete(
        @Parameter(
            description = "Unique ID of the plumbing request to delete",
            example = "1",
            required = true
        )
        @PathVariable Long id) {

        String result = service.deleteRequest(id);

        if (result.contains("not found")) {
            return new ResponseEntity<>(
                result,
                HttpStatus.NOT_FOUND
            );
        }

        return ResponseEntity.ok(result);
    }
}
