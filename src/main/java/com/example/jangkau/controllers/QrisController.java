package com.example.jangkau.controllers;


import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.jangkau.dto.QrisMerchantDTO;
import com.example.jangkau.services.QrisService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;




@RestController
@RequestMapping("/qris")
public class QrisController {

    @Autowired QrisService qrisService;
    @GetMapping(value = "/generate-qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> generateQRCode(@RequestBody QrisMerchantDTO qrisMerchantDTO )throws Exception {
        String text = qrisService.encrypString(qrisMerchantDTO);
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = 
        barcodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

    return new ResponseEntity<>(MatrixToImageWriter.toBufferedImage(bitMatrix),HttpStatus.OK);
    }
        

}
