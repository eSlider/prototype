package com.prototype.prototype;

import com.prototype.prototype.domain.Wallet;
import com.prototype.prototype.domain.dto.AdvertDTO;

import java.math.BigInteger;

public class Constants {
    public static final int TAB_ONE = 0;
    public static final int TAB_TWO = 1;
    public static final int TAB_THREE = 2;
    public static AdvertDTO advertDTO;
    public static int CART_COUNT = 0;
    public static Wallet wallet;
    public static BigInteger balance = new BigInteger("0");

    //saving preferences
    public static final String wallet_address = "wallet_address";
    public static final String wallet_password = "wallet_password";
    public static final String wallet_file = "wallet_file";
    public static final String wallet_publicKey = "wallet_publicKey";
    public static final String wallet_privateKey = "wallet_privateKey";

    public static class URL {
        public static String PORT = ":8080";
        public static String HOST = "http://192.168.0.101"+PORT+"/";
        public static String FIND_ALL_ADVERT = HOST+"findalladvert";
        public static String GET_TEST_ETH = HOST+"gettesteth/"+wallet.getAddress();
        public static String GET_STATUS = HOST+"getstatus/"+wallet.getAddress();
        public static String SAVE_TRANSACTION = HOST+"savetransaction/";
        public static String TRANSACTION_VIEWER = "https://rinkeby.etherscan.io/tx/";

    }

}
