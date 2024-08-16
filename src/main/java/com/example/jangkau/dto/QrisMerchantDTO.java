package com.example.jangkau.dto;


import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class QrisMerchantDTO {
    private UUID merchantId;
}
