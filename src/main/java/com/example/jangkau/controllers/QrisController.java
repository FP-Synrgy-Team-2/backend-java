package com.example.jangkau.controllers;


import org.springframework.http.MediaType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.jangkau.dto.QrisRequestDTO;
import com.example.jangkau.dto.QrisResponseDTO;
import com.example.jangkau.dto.QrisTransactionRequest;
import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.dto.auth.QrisRequest;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.Transactions;
import com.example.jangkau.services.AccountService;
import com.example.jangkau.services.QrisService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;




@RestController
@RequestMapping("/qris")
public class QrisController {

    @Autowired QrisService qrisService;
    @Autowired ModelMapper modelMapper;    
    @Autowired AccountService accountService;



    @PostMapping(value = "/merchant/generate-qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> generateQRCode(@RequestBody QrisRequestDTO qrisMerchantDTO )throws Exception {
        String text = qrisService.encrypStringMerchant(qrisMerchantDTO);
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        return new ResponseEntity<>(MatrixToImageWriter.toBufferedImage(bitMatrix),HttpStatus.OK);
    }

    @PostMapping(value = "/user/generate-qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> userGenerateQRCode(@RequestBody QrisRequestDTO qrisMerchantDTO, Principal principal)throws Exception {
        String text = qrisService.encrypStringUser(qrisMerchantDTO, principal);
        if (text != null) {
            qrisService.addQrCode(qrisMerchantDTO.getId(), text);
        }
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        return new ResponseEntity<>(MatrixToImageWriter.toBufferedImage(bitMatrix),HttpStatus.OK);
    }

    @PostMapping("/scan-qr")
    public ResponseEntity<?> qrisTransaction(@RequestBody QrisRequest qrisRequest) throws Exception {
        
        String encryptedData = qrisRequest.getEncryptedData();
        if (encryptedData == null || encryptedData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data terenkripsi tidak boleh kosong");
        }
        QrisResponseDTO response = qrisService.decrypt(encryptedData);
        return new ResponseEntity<>(response, HttpStatus.OK);        
    }
    @PostMapping()
    public ResponseEntity<Map<String, Object>> createNewTransaction(@RequestBody QrisTransactionRequest request, Principal principal){
        Map<String, Object> response = new HashMap<>();
        TransactionsResponseDTO newTransaction = qrisService.createTransaction(request, principal);
        if (newTransaction != null) {
            Account accountId = accountService.getAccountByAccountId(newTransaction.getFrom().getAccountId());
            Account beneficiary = accountService.getAccountByAccountId(newTransaction.getTo().getAccountId());

            Transactions trans = modelMapper.map(newTransaction, Transactions.class);
            trans.setAccountId(accountId);
            trans.setBeneficiaryAccount(beneficiary);
            accountService.updateBalance(trans);
        }
        response.put("status", "success");
        response.put("data", newTransaction);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
