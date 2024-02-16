package edu.project.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@EqualsAndHashCode
public class CurrencyDto {
    private final int id;
    private final String code;
    private final String fullName;
    private final String sign;
}