package com.prototype.prototype.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.prototype.prototype.Constants;
import com.prototype.prototype.domain.Wallet;
import com.prototype.prototype.domain.dto.AdvertDTO;

import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static android.content.Context.MODE_PRIVATE;

public class Web3Utils {

    private static SharedPreferences sPref;

    public static class GenerateNewWallet extends AsyncTask<Object, Void, Wallet> {

        @Override
        protected Wallet doInBackground(Object... args) {
            sPref = (SharedPreferences) args[2];
            Wallet wallet = new Wallet();
            String TAG = "web3";
            Log.d(TAG, "testWeb3: ");
            // We start by creating a new web3j instance to connect to remote nodes on the network.
            Web3j web3j = Web3jFactory.build(new HttpService(
                    "https://rinkeby.infura.io/oShbYdHLGQhi0rn1audL"));  // FIXME: Enter your Infura token here;
            try {
                Log.d(TAG, "Connected to Ethereum client version: "
                        + web3j.web3ClientVersion().send().getWeb3ClientVersion());
                Log.d(TAG, "connect");
//                File key_1 = new File(context[0].getFilesDir().getAbsolutePath());
                File key_1 = new File((String) args[0]);

                String s = org.web3j.crypto.WalletUtils.generateLightNewWalletFile((String) args[1]
                        , key_1);
                Log.d(TAG, s);
                String filePath = args[0] + "/" + s;
//
                File fileKey = new File(filePath);
                Log.d(TAG, fileKey.getAbsolutePath());
//        // We then need to load our Ethereum wallet file
//        // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html
                Credentials credentials =
                        org.web3j.crypto.WalletUtils.loadCredentials(
                                (String) args[1],
                                fileKey);
                wallet.setAddress(credentials.getAddress());
                wallet.setFile(filePath);//TODO переписать на правильный путь
                wallet.setPassword((String) args[1]);
                wallet.setPublicKey(credentials.getEcKeyPair().getPublicKey());
                wallet.setPrivateKey(credentials.getEcKeyPair().getPrivateKey());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (CipherException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }

//        // FIXME: Request some Ether for the Rinkeby test network at https://www.rinkeby.io/#faucet
////        log.info("Sending 1 Wei ("
////                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
//        Log.d(TAG,"Sending 1 Eth ("
//                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
//        TransactionReceipt transferReceipt = Transfer.sendFunds(
//                web3j, credentials,
//                "0x3596ddf5181c9F6Aa1bcE87D967Bf227DDE70ddf",  // you can put any address here
//                BigDecimal.ONE, Convert.Unit.ETHER)  // 1 wei = 10^-18 Ether
//                .send();
//        Log.d(TAG,"Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
//                + transferReceipt.getTransactionHash());
            return wallet;
        }

        @Override
        protected void onPostExecute(Wallet wallet) {
            Constants.wallet = wallet;
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(Constants.wallet_address, wallet.getAddress());
            ed.putString(Constants.wallet_password, wallet.getPassword());
            ed.putString(Constants.wallet_file, wallet.getFile());
            ed.putString(Constants.wallet_publicKey, wallet.getPublicKey().toString());
            ed.putString(Constants.wallet_privateKey, wallet.getPrivateKey().toString());
            ed.commit();
            new Web3Utils.GetBalanceWallet().execute();
        }
    }

    public static class GetBalanceWallet extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            Wallet wallet = Constants.wallet;
            if (wallet != null) {

                String TAG = "web3";
                // We start by creating a new web3j instance to connect to remote nodes on the network.
                Web3j web3j = Web3jFactory.build(new HttpService(
                        "https://rinkeby.infura.io/oShbYdHLGQhi0rn1audL"));  // FIXME: Enter your Infura token here;
                try {
                    EthGetBalance ethGetBalance = web3j
                            .ethGetBalance(Constants.wallet.getAddress(), DefaultBlockParameterName.LATEST)
                            .sendAsync()
                            .get();

                    Constants.balance = ethGetBalance.getBalance();


                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "testWeb3: compeleted update balance");
            }

            return null;

        }
    }

    public static class GetTestEth extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            RestTemplate template = new RestTemplate();
            template.exchange(Constants.URL.GET_TEST_ETH, HttpMethod.GET, null, Void.class);
            return null;
        }
    }

    public static class Send extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... args) {

            try {
                // We start by creating a new web3j instance to connect to remote nodes on the network.
                Web3j web3j = Web3jFactory.build(new HttpService(
                        "https://rinkeby.infura.io/oShbYdHLGQhi0rn1audL"));  // FIXME: Enter your Infura token here;
                Credentials credentials =
                        WalletUtils.loadCredentials(
                                Constants.wallet.getPassword(),
                                new File(Constants.wallet.getFile()));//TODO заменить на внутренний файл-ключ
                BigDecimal amount = Convert.toWei(args[1], Convert.Unit.ETHER);
                System.out.println("Sending "+args[1]+" ("
                        + Convert.fromWei(args[1], Convert.Unit.ETHER).toPlainString() + " wei)");
                TransactionReceipt transferReceipt = Transfer.sendFunds(
                        web3j, credentials,
                        args[0],  // you can put any address here
                        amount, Convert.Unit.WEI) // 1 wei = 10^-18 Ether
                        .send();
                System.out.println("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                        + transferReceipt.getTransactionHash());

                RestTemplate template = new RestTemplate();
                template.exchange(Constants.URL.SAVE_TRANSACTION
                        +transferReceipt.getBlockNumber()+"/"
                        +transferReceipt.getBlockNumber()+"/"
                        +transferReceipt.getFrom()+"/"
                        +transferReceipt.getTo()+"/"
                        +amount+"/"
                        +transferReceipt.getTransactionHash(), HttpMethod.GET, null, Void.class);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class GetAllTransactions extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... args) {

            try {
                // We start by creating a new web3j instance to connect to remote nodes on the network.
                Web3j web3j = Web3jFactory.build(new HttpService(
                        "https://rinkeby.infura.io/oShbYdHLGQhi0rn1audL"));  // FIXME: Enter your Infura token here;
                EthGetTransactionCount send = web3j.ethGetTransactionCount(Constants.wallet.getAddress(), DefaultBlockParameterName.LATEST).send();
                System.out.println(send.getTransactionCount());
//                Credentials credentials =
//                        WalletUtils.loadCredentials(
//                                Constants.wallet.getPassword(),
//                                new File(Constants.wallet.getFile()));//TODO заменить на внутренний файл-ключ
//                BigDecimal amount = Convert.toWei(args[1], Convert.Unit.ETHER);
//                System.out.println("Sending "+args[1]+" ("
//                        + Convert.fromWei(args[1], Convert.Unit.ETHER).toPlainString() + " wei)");
//                TransactionReceipt transferReceipt = Transfer.sendFunds(
//                        web3j, credentials,
//                        args[0],  // you can put any address here
//                        amount, Convert.Unit.WEI) // 1 wei = 10^-18 Ether
//                        .send();
//                System.out.println("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
//                        + transferReceipt.getTransactionHash());

            } catch (Exception e) {
                e.printStackTrace();
            }

//            RestTemplate template = new RestTemplate();
//            template.exchange(Constants.URL.GET_STATUS, HttpMethod.GET, null, Void.class);
            return null;
        }
    }
}
