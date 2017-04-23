package com.timemanagement.zxg.config;

import com.timemanagement.zxg.timemanagement.BuildConfig;

/**
 * 参数配置
 * Created by zxg on 2016/9/30.
 * QQ:1092885570
 */
public class Config {
    //true:测试环境，false:生产环境
//    public static final boolean DEBUG = true;
    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static String URL_HEAD;

    public static class url {
        public final static String URL_LOGIN = URL_HEAD + "/rest/user/dataQA";
    }


    static {
        if (DEBUG){
            URL_HEAD = "http://localhost:4478"; //测试环境
        }else {
            URL_HEAD = ""; //生产环境
        }
    }
}
