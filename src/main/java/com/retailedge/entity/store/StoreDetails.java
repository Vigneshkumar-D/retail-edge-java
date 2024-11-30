package com.retailedge.entity.store;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "store_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
