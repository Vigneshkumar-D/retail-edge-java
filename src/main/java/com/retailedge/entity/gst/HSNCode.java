package com.retailedge.entity.gst;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="hsn_code")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HSNCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category")
    private TaxSlab taxSlab;

}
