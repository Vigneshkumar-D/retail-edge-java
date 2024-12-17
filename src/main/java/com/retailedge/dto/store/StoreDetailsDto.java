package com.retailedge.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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

    private MultipartFile storeLogoImage;
}
