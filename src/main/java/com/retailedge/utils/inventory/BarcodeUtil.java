package com.retailedge.utils.inventory;

import com.retailedge.model.inventory.BarcodeResult;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class BarcodeUtil {

    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 10;

    private static String generateAlphanumericBarcode() {
        if (8 < MIN_LENGTH || 8 > MAX_LENGTH) {
            throw new IllegalArgumentException("Length must be between 6 and 10 characters");
        }

        StringBuilder barcode = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(CHARACTERS.length());
            barcode.append(CHARACTERS.charAt(index));
        }

        return barcode.toString();
    }



//    public static BarcodeResult generateBarcode(String productName, String model) {
//        try {
//            // Generate an 8-character alphanumeric barcode value
//            String alphanumericBarcode = generateAlphanumericBarcode(); // Make sure to pass the desired length
//
//            String directoryPath = "barcodes";
//            String filePath = directoryPath + File.separator + "product_" + productName + ".png";
//
//            // Create directory if it does not exist
//            File directory = new File(directoryPath);
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }
//
//            int width = 300; // Width of the barcode
//            int height = 100; // Height of the barcode
//            BitMatrix bitMatrix = new MultiFormatWriter().encode(productName + " " + model, BarcodeFormat.CODE_128, width, height);
//
//            BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
//
//            // Create a new image with extra space for the text
//            BufferedImage finalImage = new BufferedImage(width, height + 30, BufferedImage.TYPE_INT_RGB);
//            Graphics2D g = finalImage.createGraphics();
//            g.setColor(Color.WHITE);
//            g.fillRect(0, 0, finalImage.getWidth(), finalImage.getHeight());
//            g.drawImage(barcodeImage, 0, 8, null); // Adjusts the image placement
//
//            // Set font for the barcode text
//            g.setColor(Color.BLACK);
//            g.setFont(new Font("Arial", Font.BOLD, 12));
//            FontMetrics fm = g.getFontMetrics();
//            int textWidth = fm.stringWidth(alphanumericBarcode);
//            int x = (width - textWidth) / 2; // Calculate X position for center alignment
//
//            // Draw the text below the barcode
//            g.drawString(alphanumericBarcode, x, height + 20);
//
//            // Write the final image to a file (optional)
//            try (FileOutputStream fos = new FileOutputStream(filePath)) {
//                ImageIO.write(finalImage, "PNG", fos);
//            }
//
//            // Write the final image to the ByteArrayOutputStream
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(finalImage, "PNG", baos);
//            byte[] imageBytes = baos.toByteArray(); // Now contains the image data
//
//            // Return a BarcodeResult containing both the barcode value and image
//            return new BarcodeResult(alphanumericBarcode, imageBytes);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null; // Return null if there's an exception
//    }

    public static BarcodeResult generateBarcode(String productName, String model) {
        try {
            int fixedLength = 12; // Define a standard length for the barcode input
            String barcodeValue = (productName + " " + model).replaceAll("\\s+", ""); // Remove extra spaces
            if (barcodeValue.length() > fixedLength) {
                barcodeValue = barcodeValue.substring(0, fixedLength); // Truncate if longer
            } else {
                barcodeValue = String.format("%-" + fixedLength + "s", barcodeValue).replace(' ', '0'); // Pad with zeros if shorter
            }

            String alphanumericBarcode = generateAlphanumericBarcode(); // Your logic for a secondary unique value

            String directoryPath = "barcodes";
            String filePath = directoryPath + File.separator + "product_" + productName + ".png";

            // Create directory if it does not exist
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            int width = 190; // Width of the barcode
            int height = 40; // Height of the barcode
            BitMatrix bitMatrix = new MultiFormatWriter().encode(barcodeValue, BarcodeFormat.CODE_128, width, height);

            BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Create a new image with extra space for the text
            BufferedImage finalImage = new BufferedImage(width, height + 30, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = finalImage.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, finalImage.getWidth(), finalImage.getHeight());
            g.drawImage(barcodeImage, 0, 8, null); // Adjusts the image placement

            // Set font for the barcode text
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(alphanumericBarcode);
            int x = (width - textWidth) / 2; // Calculate X position for center alignment

            // Draw the text below the barcode
            g.drawString(alphanumericBarcode, x, height + 20);

            // Write the final image to a file (optional)
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                ImageIO.write(finalImage, "PNG", fos);
            }

            // Write the final image to the ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(finalImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray(); // Now contains the image data

            // Return a BarcodeResult containing both the barcode value and image
            return new BarcodeResult(alphanumericBarcode, imageBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if there's an exception
    }


}

