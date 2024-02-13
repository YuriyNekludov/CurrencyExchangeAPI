package edu.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrencyDto {
    private int id;
    private String code;
    private String fullName;
    private String sign;
}