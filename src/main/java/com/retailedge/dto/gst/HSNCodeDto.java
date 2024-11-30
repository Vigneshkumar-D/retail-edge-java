package com.retailedge.dto.gst;


import com.retailedge.entity.gst.TaxSlab;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HSNCodeDto {

    private String code;

    private String description;

    private TaxSlab taxSlab;

}
