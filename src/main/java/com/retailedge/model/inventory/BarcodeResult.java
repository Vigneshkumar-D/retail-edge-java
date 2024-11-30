package com.retailedge.model.inventory;

public class BarcodeResult {
    private String barcodeValue;
    private byte[] barcodeImage;

    public BarcodeResult(String barcodeValue, byte[] barcodeImage) {
        this.barcodeValue = barcodeValue;
        this.barcodeImage = barcodeImage;
    }

    public String getBarcodeValue() {
        return barcodeValue;
    }

    public byte[] getBarcodeImage() {
        return barcodeImage;
    }
}
