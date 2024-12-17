package com.retailedge.entity.store;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "account_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankName;

    private String branch;

    private String accountNumber;

    private String upiId;

    private String ifscCode;

    @Lob
    private byte[] upiQRCodeImage;
}
