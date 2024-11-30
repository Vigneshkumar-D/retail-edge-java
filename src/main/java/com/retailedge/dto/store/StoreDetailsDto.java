package com.retailedge.dto.store;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StoreDetailsDto {

    private String storeName;

    private String Address;

    private String state;

    private Integer pinCode;

    private String primaryPhone;

    private String secondaryPhone;

    private Double ifscCode;

    @Lob
    private byte[] storeLogo;
}
