package com.example.jangkau.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantRequest {
    @NotBlank(message = "Must not empty")
    private String name;
    private UUID accountId;
}
