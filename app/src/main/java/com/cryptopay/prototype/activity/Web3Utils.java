package com.cryptopay.prototype.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.domain.Wallet;
import com.cryptopay.prototype.domain.dto.AdvertDTO;
import com.cryptopay.prototype.domain.dto.TypeItemDTO;

import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

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
                    Constants.URL.ETH_NETWORK));  // FIXME: Enter your Infura token here;
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
                Constants.wallet = wallet;
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
//            super.onPostExecute(wallet);
            Constants.wallet = wallet;
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(Constants.wallet_address, wallet.getAddress());
            ed.putString(Constants.wallet_password, wallet.getPassword());
            ed.putString(Constants.wallet_file, wallet.getFile());
            ed.putString(Constants.wallet_publicKey, wallet.getPublicKey().toString());
            ed.putString(Constants.wallet_privateKey, wallet.getPrivateKey().toString());
            ed.putBoolean(Constants.save_recovery, false);
            ed.commit();


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
                        Constants.URL.ETH_NETWORK));  // FIXME: Enter your Infura token here;
                try {
                    EthGetBalance ethGetBalance = web3j
                            .ethGetBalance(Constants.wallet.getAddress(), DefaultBlockParameterName.LATEST)
                            .sendAsync()
                            .get();

                    Constants.balance = Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER).floatValue();


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


    public static class GetAllTransactions extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... args) {

            try {
                // We start by creating a new web3j instance to connect to remote nodes on the network.
                Web3j web3j = Web3jFactory.build(new HttpService(
                        Constants.URL.ETH_NETWORK));  // FIXME: Enter your Infura token here;
                EthGetTransactionCount send = web3j.ethGetTransactionCount(Constants.wallet.getAddress(), DefaultBlockParameterName.LATEST).send();
                System.out.println(send.getTransactionCount());

            } catch (Exception e) {
                e.printStackTrace();
            }

//            RestTemplate template = new RestTemplate();
//            template.exchange(Constants.URL.GET_STATUS, HttpMethod.GET, null, Void.class);
            return null;
        }
    }

    public static class GetAllTypeItem extends AsyncTask<Void, Void, TypeItemDTO> {

        @Override
        protected TypeItemDTO doInBackground(Void... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                return template.getForObject(Constants.URL.FIND_ALL_TYPE, TypeItemDTO.class);
            } catch (RuntimeException exception) {
                System.out.println(exception.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(TypeItemDTO typeItemDTO) {
            if (typeItemDTO == null || typeItemDTO.getData() == null) {
                return;
            }

            Constants.typeItemDTO = typeItemDTO;


        }
    }
}
