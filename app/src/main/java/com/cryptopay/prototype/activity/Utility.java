package com.cryptopay.prototype.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class Utility {
    public static void displayAlertDialogMessage(Activity activity, String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .show();
    }

//    public static class UpdateStatus extends AsyncTask<Void, Void, String>{
//
//        @Override
//        protected Void doInBackground(Context... contexts) {
//            while (true){
//                try {
//                    TimeUnit.SECONDS.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Toast.makeText(contexts[0],"Status",Toast.LENGTH_LONG).show();
//            }
//
//        }
//    }

//    public static class GenerateNewWallet extends AsyncTask<String, Void, Wallet> {
//
//        @Override
//        protected Wallet doInBackground(String... args) {
//            Wallet wallet = new Wallet();
//            String TAG = "web3";
//            Log.d(TAG, "testWeb3: ");
//            // We start by creating a new web3j instance to connect to remote nodes on the network.
//            Web3j web3j = Web3jFactory.build(new HttpService(
//                     Constants.URL.ETH_NETWORK));  // FIXME: Enter your Infura token here;
//            try {
//                Log.d(TAG, "Connected to Ethereum client version: "
//                        + web3j.web3ClientVersion().send().getWeb3ClientVersion());
//                Log.d(TAG, "connect");
////                File key_1 = new File(context[0].getFilesDir().getAbsolutePath());
//                File key_1 = new File(args[0]);
//
//                String s = org.web3j.crypto.WalletUtils.generateLightNewWalletFile(args[1]
//                        , key_1);
//                Log.d(TAG, s);
//                String filePath = args[0] + "/" + s;
////
//                File fileKey = new File(filePath);
//                Log.d(TAG, fileKey.getAbsolutePath());
////        // We then need to load our Ethereum wallet file
////        // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html
//                Credentials credentials =
//                        org.web3j.crypto.WalletUtils.loadCredentials(
//                                args[1],
//                                fileKey);
//                wallet.setAddress(credentials.getAddress());
//                wallet.setFile(filePath);//TODO переписать на правильный путь
//                wallet.setPassword(args[1]);
//                wallet.setPublicKey(credentials.getEcKeyPair().getPublicKey());
//                wallet.setPrivateKey(credentials.getEcKeyPair().getPrivateKey());
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (CipherException e) {
//                e.printStackTrace();
//            } catch (NoSuchProviderException e) {
//                e.printStackTrace();
//            } catch (InvalidAlgorithmParameterException e) {
//                e.printStackTrace();
//            }
//
////        // FIXME: Request some Ether for the Rinkeby test network at https://www.rinkeby.io/#faucet
//////        log.info("Sending 1 Wei ("
//////                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
////        Log.d(TAG,"Sending 1 Eth ("
////                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
////        TransactionReceipt transferReceipt = Transfer.sendFunds(
////                web3j, credentials,
////                "0x3596ddf5181c9F6Aa1bcE87D967Bf227DDE70ddf",  // you can put any address here
////                BigDecimal.ONE, Convert.Unit.ETHER)  // 1 wei = 10^-18 Ether
////                .send();
////        Log.d(TAG,"Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
////                + transferReceipt.getTransactionHash());
//            return wallet;
//        }
//
//        @Override
//        protected void onPostExecute(Wallet wallet) {
//
//
//        }
//    }
}
