package com.cryptopay.prototype;

import com.cryptopay.prototype.domain.Advert;
import com.cryptopay.prototype.domain.Wallet;
import com.cryptopay.prototype.domain.dto.AdvertDTO;
import com.cryptopay.prototype.domain.dto.ItemDTO;
import com.cryptopay.prototype.domain.dto.TransactionDTO;
import com.cryptopay.prototype.domain.dto.TypeItemDTO;

public class Constants {

    public static AdvertDTO advertDTO = new AdvertDTO();
    public static ItemDTO itemDTO;
    public static TypeItemDTO typeItemDTO;
    public static TransactionDTO transactionDTO = new TransactionDTO();
    public static int CART_COUNT = 0;
    public static Advert advert;
    public static Wallet wallet = new Wallet();
    ;
    public static float balance = 0l;

    //saving preferences
    public static final String wallet_address = "wallet_address";
    public static final String wallet_password = "wallet_password";
    public static final String wallet_file = "wallet_file";
    public static final String wallet_publicKey = "wallet_publicKey";
    public static final String wallet_privateKey = "wallet_privateKey";
    public static final String save_recovery = "save_recovery";
    //saving settings preferences
    public static final String settings_show_splash = "settings_show_splash";


    public static class URL {
        public static String PORT = "";

        //PRODUCTION

//        public static String HOST = "http://95.85.8.20" + PORT + "/";
//        public static String ETH_NETWORK = "https://mainnet.infura.io/oShbYdHLGQhi0rn1audL";
//        public static String COMISSION_WALLET = "0x73246796E98b8d1991473180246290f78A569a0c";

        //TEST

        public static String HOST = "http://192.168.0.104:8080" + PORT + "/";
        public static String ETH_NETWORK = "https://rinkeby.infura.io/oShbYdHLGQhi0rn1audL ";
        public static String COMISSION_WALLET = "0xB2a7a85E7a104B069508594b255da3f9aA0ecbaF";


        public static String FIND_ALL_ADVERT = HOST + "findalladvert";
        public static String FIND_ALL_ITEM = HOST + "findallitembyid";
        public static String GET_ETH_PRICE = HOST + "getethprice";
        public static String GET_TEST_ETH = HOST + "gettesteth/" + wallet.getAddress();
        public static String FIND_ALL_TRANSACTION = HOST + "findalltransactionbyaddress/" + wallet.getAddress();
        public static String FIND_ALL_TYPE = HOST + "findalltype";
        public static String GET_STATUS = HOST + "getstatus/" + wallet.getAddress();
        public static String SAVE_TRANSACTION = HOST + "savetransaction/";
        public static String SAVE_PAID = HOST + "savepaid/";
        public static String TRANSACTION_VIEWER = "https://etherscan.io/tx/";


    }

}
