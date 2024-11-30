package com.retailedge.dto.gst;

import com.retailedge.entity.inventory.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaxSlabDto {

    private String region;
    private Category category;
    private BigDecimal sgst;
    private BigDecimal cgst;
    private BigDecimal igst;
    private String serviceType;

}
