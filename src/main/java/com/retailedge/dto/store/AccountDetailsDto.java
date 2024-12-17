package com.retailedge.dto.store;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountDetailsDto {

    private String bankName;

    private String branch;

    private String accountNumber;

    private String upiId;

    private String ifscCode;

    private MultipartFile upiQRCodeImage;
}
