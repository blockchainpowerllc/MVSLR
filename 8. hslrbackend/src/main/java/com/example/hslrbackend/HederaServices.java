package com.example.hslrbackend;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class HederaServices {

    private static String HEDERANETWORK = null;

    //private static final TokenId HSLR = TokenId.fromString("0.0.14977730");  // tstnet hSLR  6/24/23  for ref

    private static final AccountId OP_ID_T = AccountId.fromString("0.0.3496688");

    private static final AccountId OP_ID_M = AccountId.fromString("0.0.955976");


    private static AccountId OPERID = null;
    private static PrivateKey OK = null;

    private static Client OPERATING_ACCOUNT = null;

    private static Boolean LOGGEDON = false;
    private static String OPIN = "MyPtjeOQwooWvbyDPkgbUMax9xYycSo8kWSeXRwRLYjjoNMfMzQHDt3Bj2M6iczBdV/1YSXcsZX+lcPfFquo61NAxbcsYMuaRiz9UZQWGYYUIGpXUSw8HR5GbGXp28e3Co5gpMNPxhdjdZ6lm6rlHQ==";
    private static String OPINM = "3SCJuPDRvX14Z5hnFZAvUwYKOIGK5RfNRyeTb0WQPlf4IJIAjAnY6XT8lv/zhQkNzE0sn7tvF8fGEgrw2ViUw8N6xAE0gy89BQ678I6sg4fss1dgmDZPU5DPn+eyTmP+rA/hm9PkBsJsTypMmfp1xA==";

    private static PrivateKey OK_TEST = PrivateKey.fromString("302e020100300506032b657004220420006f7f69e8e0aab5390a6dc8ce59ae88c1f4535c8db6a44685298730e63d1b6e");
    private static PrivateKey OK_MAIN = null;
    private static final String x = "m";
    private static final String y = "s";

    private static ContractId SLRBRIDGE = ContractId.fromString("0.0.14977746");  // 6/24/23


    public static void settestnetintent() {

        HEDERANETWORK = "testnet";
        OPERID = OP_ID_T;

        // System.out.println(".. connecting to Hedera Testnet nodes and contracts and tokens from Operating Account..");
    }


    public static void setmainnetintent() {

        HEDERANETWORK = "mainnet";
        OPERID = OP_ID_M;

        //  System.out.println(".. connecting to Hedera Mainnet nodes and contracts and tokens from Operating Account..");
    }


    public static void createoperatorClient() throws UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {


        // this goes on server ..

        if (Objects.equals(HEDERANETWORK, "testnet")) {


           // opindenc();   de-encrypt removed for test behind a safe REST GCP account.
            OK = OK_TEST;
            OPERATING_ACCOUNT = Client.forName(HEDERANETWORK);
            OPERATING_ACCOUNT.setOperator(OPERID, OK);
            /*
             for testing when some nodes are down
             try {
                OPERATING_ACCOUNT = Client.fromConfig("{\n" +
                        " \"network\": {\n" +
                        " \"0.0.3\" : \"0.testnet.hedera.com:50211\",\n" +   // REMOVE ALL THESE !! BEFORE RELEASE
                        //    " \"0.0.4\" : \"1.testnet.hedera.com:50211\",\n" +
                        " \"0.0.5\" : \"2.testnet.hedera.com:50211\",\n" +
                        " \"0.0.6\" : \"3.testnet.hedera.com:50211\",\n" +
                        " \"0.0.7\" : \"4.testnet.hedera.com:50211\",\n" +
                        " \"0.0.8\" : \"5.testnet.hedera.com:50211\",\n" +
                        " \"0.0.9\" : \"6.testnet.hedera.com:50211\"\n" +
                        "      }\n" +
                        "}");

            } catch (Exception e) {
                System.out.println("operator Client instantiate exception for specific nodes " + e);
                return;
            }
            */

        }
        else if (Objects.equals(HEDERANETWORK, "mainnet")) {

           // opindencm(); as above
            OK = OK_MAIN;
            OPERATING_ACCOUNT = Client.forName(HEDERANETWORK);
            OPERATING_ACCOUNT.setOperator(OPERID, OK);
        }


    }



    public static Boolean checknetconnection() {
        Boolean netconnectedok = false;

        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            netconnectedok = true;


        } catch (MalformedURLException e) {

        } catch (IOException e) {

        }
        return netconnectedok;
    }


    public static String getHEDERANETWORK() { return HEDERANETWORK;}



    public static void setslr(String hSLRaccountin, Long hSLRamountdue) throws TimeoutException, PrecheckStatusException, ReceiptStatusException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        createoperatorClient();


        AccountId hslraccount = AccountId.fromString(hSLRaccountin);

        String tosetaccountsol = hslraccount.toSolidityAddress();

        //  function setSLRburned(address account, int64 amount ) public onlyPlatform {

        TransactionResponse contractExectxnId = new ContractExecuteTransaction()
                .setContractId(SLRBRIDGE)
                .setGas(4000000)
                .setFunction("setSLRburned", new ContractFunctionParameters()
                        .addAddress(tosetaccountsol)
                        .addUint64(hSLRamountdue))
                .execute(OPERATING_ACCOUNT);

        contractExectxnId.getReceipt(OPERATING_ACCOUNT); // Will throw if call reverts

        OPERATING_ACCOUNT.close();

    }




    public static void sendslr(String hSLRaccountin) throws TimeoutException, PrecheckStatusException, ReceiptStatusException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        createoperatorClient();

        AccountId hslraccount = AccountId.fromString(hSLRaccountin);

        String payoutaccountsol = hslraccount.toSolidityAddress();

        TransactionResponse contractExectxnId = new ContractExecuteTransaction()
                .setContractId(SLRBRIDGE)
                .setGas(4000000)
                .setFunction("sendhSLR", new ContractFunctionParameters()
                        .addAddress(payoutaccountsol))
                .execute(OPERATING_ACCOUNT);

        contractExectxnId.getReceipt(OPERATING_ACCOUNT); // Will throw if call reverts

        OPERATING_ACCOUNT.close();

    }






    public static ByteString clientlogonbytestring(FileId fileidin) throws UnsupportedOperationException, PrecheckStatusException, TimeoutException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {

        createoperatorClient();

        // platform has to pay for this get ! since users keys are kept in the file QED!;

        ByteString hederafilecontents = new FileContentsQuery()
                .setFileId(fileidin)
                .execute(OPERATING_ACCOUNT);

        if (hederafilecontents.isEmpty()) {

            return hederafilecontents;
        }

        //

        OPERATING_ACCOUNT.close();
        return hederafilecontents;

    }




    public static void opindenc() throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        SecretKeySpec sec;
        byte[] y1;

        MessageDigest sha = null;
        String bl = x + y;
        y1 = bl.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        y1 = sha.digest(y1);
        y1 = Arrays.copyOf(y1, 16);
        sec = new SecretKeySpec(y1, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, sec);

        OK_TEST = PrivateKey.fromString( new String(cipher.doFinal(Base64.getDecoder().decode(OPIN))));

    }

    public static void opindencm() throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        SecretKeySpec secr;
        byte[] k;

        MessageDigest sha = null;

        String mn = x + y + "m";
        k = mn.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        k = sha.digest(k);
        k = Arrays.copyOf(k, 16);
        secr = new SecretKeySpec(k, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secr);


        OK_MAIN = PrivateKey.fromString( new String(cipher.doFinal(Base64.getDecoder().decode(OPINM))));

    }






}
