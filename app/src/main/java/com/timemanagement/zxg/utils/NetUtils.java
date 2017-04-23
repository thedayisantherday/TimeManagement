package com.timemanagement.zxg.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**网络相关工具类
 * Created by zxg on 2016/10/24.
 * QQ:1092885570
 */

public class NetUtils {

    public final static int CONNECTED_TYPE_MOBILE = 5;
    public final static int CONNECTED_TYPE_4G = 4;
    public final static int CONNECTED_TYPE_3G = 3;
    public final static int CONNECTED_TYPE_2G = 2;
    public final static int CONNECTED_TYPE_WIFI = 1;
    public final static int CONNECTED_TYPE_OTHER = 0;
    public final static int CONNECTED_TYPE_UNCONNECTED = -1;

    public static final String IP_DEFAULT = "0.0.0.0";

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return true if network is available, otherwise false
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isAvailable();

    }

    /**
     * 判断已连接的网络是否为wifi
     *
     * @param context
     * @return true if WIFI is connected, otherwise false
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetworkInfo!=null && wifiNetworkInfo.isConnected();
    }

    /**
     * 判断已连接的网络是否为移动网络（2G/3G/4G）
     *
     * @param context
     * @return true if Mobile is connected, otherwise false
     */
    public static boolean isMobileConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return mobileNetworkInfo!=null && mobileNetworkInfo.isConnected();
    }

    /**
     * 获取连接网络的类型名称
     * @param context
     * @return
     */
    public static int getNetConnectType(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //wifi网络
        if (isWifiConnected(context)){
            return NetUtils.CONNECTED_TYPE_WIFI;
        } else if (isMobileConnected(context)){
            NetworkInfo allNetwork = connectivityManager.getActiveNetworkInfo();
            NetworkInfo mobileNetworkInfo =
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            String strTypeName = mobileNetworkInfo.getSubtypeName();
            switch (allNetwork.getSubtype()){
                //4G网络
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return NetUtils.CONNECTED_TYPE_4G;
                //3G网络
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return NetUtils.CONNECTED_TYPE_3G;
                //2G网络
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return NetUtils.CONNECTED_TYPE_2G;

                default:
                    if (strTypeName.equalsIgnoreCase("TD-SCDMA") ||
                            strTypeName.equalsIgnoreCase("WCDMA") || strTypeName.equalsIgnoreCase("CDMA2000")) {
                        return NetUtils.CONNECTED_TYPE_3G;
                    } else {
                        return NetUtils.CONNECTED_TYPE_MOBILE;
                    }
            }
        } else if (isNetworkAvailable(context)){
            return NetUtils.CONNECTED_TYPE_OTHER;
        } else {
            return NetUtils.CONNECTED_TYPE_UNCONNECTED;
        }
    }

    /**
     * 获取当前设备的IP地址
     * （需要开启权限：<uses-permission android:name="android.permission.INTERNET" />)
     *
     * @return current IP address of the device
     */
    public static String getCurrentIpAddr(){
        try {
            Enumeration<NetworkInterface> netInterfaceEnum = NetworkInterface.getNetworkInterfaces();
            while (netInterfaceEnum.hasMoreElements()) {
                NetworkInterface networkInterface = netInterfaceEnum.nextElement();
                Enumeration<InetAddress> inetAddrEnum = networkInterface.getInetAddresses();
                while (inetAddrEnum.hasMoreElements()) {
                    InetAddress inetAddr = inetAddrEnum.nextElement();
                    if (!inetAddr.isLoopbackAddress() &&
                            !inetAddr.isLinkLocalAddress() && inetAddr.isSiteLocalAddress()) {
                        return inetAddr.getHostAddress();
                    }
                }
            }
        }catch (SocketException e){
            e.printStackTrace();
        }
        return IP_DEFAULT;
    }

    /**
     * 通过WifiManager获取当前设备的IP地址（注意需要开启以下权限：
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
     * <uses-permission android:name="android.permission.WAKE_LOCK" />
     *
     * @param context
     * @return current wifi IP address of the device
     */
    public static String getCurrentWifiIPAddr(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddr = wifiInfo.getIpAddress();
        return intIP2StringIP(ipAddr);
    }

    /**将整数表示的ip转换成ip地址
     *
     * @param intIP 整数形式表示的ip地址
     * @return
     */
    public static String intIP2StringIP(int intIP) {
        StringBuilder strB_IP = new StringBuilder();
        strB_IP.append(intIP & 0xFF).append(".");
        strB_IP.append((intIP >> 8) & 0xFF).append(".");
        strB_IP.append((intIP >> 16) & 0xFF).append(".");
        strB_IP.append((intIP >> 24) & 0xFF);
        return strB_IP.toString();
    }
}
