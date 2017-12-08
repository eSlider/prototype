package com.prototype.prototype;

import com.prototype.prototype.domain.dto.AdvertDTO;

public class Constants {
    public static final int TAB_ONE = 0;
    public static final int TAB_TWO = 1;
    public static final int TAB_THREE = 2;
    public static AdvertDTO advertDTO;
    public static int CART_COUNT = 0;

    public static class URL {
        public static String PORT = ":8080";
        public static String HOST = "http://192.168.0.101"+PORT+"/";
        public static String FIND_ALL_ADVERT = HOST+"findalladvert";
    }

}
