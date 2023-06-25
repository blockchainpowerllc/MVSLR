package com.example.hslrbackend;


import com.hedera.hashgraph.sdk.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.concurrent.TimeoutException;

// (slramount, hSLRaccount)

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

// @CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController

@Transactional
class APIController {
    @GetMapping("/")
    public String root() {
        return "must hit /setslr or /sendslr";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello!";
    }

    private static final String q = "2";
    private static final String r = "99";

    @GetMapping("/getnet")
    public String getnetstatus() {

        return HederaServices.getHEDERANETWORK();
    }


    // (slramount, hSLRaccount)

    @RequestMapping(value = "/setslr")
    @PostMapping("/setslr")
    public String setSlr(@RequestParam(value = "mainortest") String mainortest,
                         @RequestParam(value = "slramount") String slramountin,
                         @RequestParam(value = "hslraccount") String hslraccountin)
                        {

        String status =  null;

        if (mainortest.isEmpty() || mainortest.isBlank()) {
            status = " both parameters needed..  main or test and publickey";

        }

        if (!mainortest.equals("mainnet") && (!mainortest.equals("testnet"))) {
            status = " must be either 'mainnet' or 'testnet'";
        }

        if (mainortest.equals("mainnet")) {
            HederaServices.setmainnetintent();
        }

        if (mainortest.equals("testnet")) {
            HederaServices.settestnetintent();
        }


        Long slramounttoset = Long.parseLong(slramountin);

        try {

                HederaServices.setslr(hslraccountin, slramounttoset);

        } catch (TimeoutException e) {
            return e.toString();
        } catch (PrecheckStatusException e) {
            return e.toString();
        } catch (ReceiptStatusException e) {
            return e.toString();
        } catch (UnsupportedEncodingException e) {
            return e.toString();
        } catch (NoSuchPaddingException e) {
            return e.toString();
        } catch (IllegalBlockSizeException e) {
            return e.toString();
        } catch (NoSuchAlgorithmException e) {
            return e.toString();
        } catch (BadPaddingException e) {
            return e.toString();
        } catch (InvalidKeyException e) {
            return e.toString();
        }

        return status;
    }




    @RequestMapping(value = "/sendhslr")
    @PostMapping("/sendhslr")
    public String sendSlr(@RequestParam(value = "mainortest") String mainortest,
                          @RequestParam(value = "hslraccount") String hslraccountin)
                          {


                              String success = null;

        if (mainortest.isEmpty() || mainortest.isBlank()) {
            success = " both parameters needed..  main or test and publickey";

        }

        if (!mainortest.equals("mainnet") && (!mainortest.equals("testnet"))) {
            success = " must be either 'mainnet' or 'testnet'";
        }

        if (mainortest.equals("mainnet")) {
            HederaServices.setmainnetintent();
        }

        if (mainortest.equals("testnet")) {
            // return " in test toggle";
            HederaServices.settestnetintent();
        }


        try {

            HederaServices.sendslr(hslraccountin);


        } catch (UnsupportedEncodingException e) {
            return e.toString();

        } catch (NoSuchPaddingException e) {
            return e.toString();

        } catch (IllegalBlockSizeException e) {
            return e.toString();

        } catch (NoSuchAlgorithmException e) {
            return e.toString();

        } catch (BadPaddingException e) {
            return e.toString();

        } catch (InvalidKeyException e) {
            return e.toString();

        } catch (ReceiptStatusException e) {
            return e.toString();

        } catch (PrecheckStatusException e) {
            return e.toString();

        } catch (TimeoutException e) {
            return e.toString();

        }

        return success;
    }




    /* to read the mapping and SLR amount - TBD
    @RequestMapping(value = "/getaccount")
    @PostMapping("/getaccount")
    public ByteArrayResource getAccount(@RequestParam(value = "mainortest") String mainortest,
                                        @RequestParam(value = "fileid") String fileidin,
                                        @RequestParam(value = "chksum") String chksum){

        // no parms can be empty

        ByteArrayResource fileoutstream2 = null;

        HttpHeaders headers = new HttpHeaders();

        if (!chksum.equals(r+q)) {
            return fileoutstream2;
        }

        if (mainortest.isEmpty() || mainortest.isBlank()) {
            return fileoutstream2;

        }

        if (!mainortest.equals("mainnet") && (!mainortest.equals("testnet"))) {
            return fileoutstream2;
        }

        if (mainortest.equals("mainnet")) {

            HederaServices.setmainnetintent();

        }

        if (mainortest.equals("testnet")) {

            HederaServices.settestnetintent();

        }

        ByteString filebytes = null;

      //  System.out.println("before call");
        try {
            filebytes = HederaServices.clientlogonbytestring(FileId.fromString(fileidin));

            // null check

          //  if (filebytes == null) {
           //     System.out.println(" null filebytes");

           // }

          //  System.out.println("filebytes back from hederaServices on server " + filebytes);
          //  System.out.println("filebytes back from hederaServices on server to string utf8 " + filebytes.toStringUtf8());
          //  System.out.println("filebytes back from hederaServices on server is empty ? " + filebytes.isEmpty());
           // System.out.println("filebytes back from hederaServices on server side " + filebytes.size());

            fileoutstream2 = new ByteArrayResource(filebytes.toByteArray());

           // System.out.println("fileoutstream is back from hederaServices on server  " + fileoutstream2);
           // System.out.println("fileoutstream is back from hederaServices on server cootent length " + fileoutstream2.contentLength());


        } catch (PrecheckStatusException e) {
            //  exceptions will just return a null filebytes Bytestring;
        } catch (TimeoutException e) {

        } catch (UnsupportedEncodingException e) {

        } catch (NoSuchPaddingException e) {

        } catch (IllegalBlockSizeException e) {

        } catch (NoSuchAlgorithmException e) {

        } catch (BadPaddingException e) {

        } catch (InvalidKeySpecException e) {

        } catch (InvalidKeyException e) {

        }
       // System.out.println("before return");

        return fileoutstream2;
    }

*/


}
