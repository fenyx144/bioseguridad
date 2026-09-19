package com.rinconadadelsur.sistemas.bioseguridad.Herramientas;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

public class hMetodos {

    public static String ipConexion(String ipDispositivo){
        String sIp,sIpPublico,sIpLocal,sIpLocal2,sIpLocalValida;

        sIpPublico="190.239.16.194";
        sIpLocal="192.168.150.2";
        sIpLocalValida="192.168.150";

        sIp= sIpPublico;
        try {

            if (ipDispositivo == null ){
                sIp= sIpPublico;
            }else if  (ipDispositivo.contains(sIpLocalValida)){
                sIp= sIpLocal;
            }else{
                sIp= sIpPublico;
            }
            //sIp="TU IP";
            return sIp;
        } catch (Exception e){
            return sIp;
        }
    }

    public static String getIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if ((intf.getName().contains("wlan")) || (intf.getName().contains("ap"))) {
                    for (Enumeration <InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() &&
                                (inetAddress.getAddress().length == 4)) {
                            Log.d("Bien", inetAddress.getHostAddress());
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("Error", ex.toString());
        }
        return "";
    }

    public static String getfechaActual(){
        Date fechaActual = new Date();
        SimpleDateFormat apptivaWeb = new SimpleDateFormat("yyyy-MM-dd");
        return apptivaWeb.format(fechaActual);
    }

    public static String gethoraActual(){
        Date horaActual = new Date();
        SimpleDateFormat apptivaWeb = new SimpleDateFormat("HH:mm:ss");
        return apptivaWeb.format(horaActual);
    }

    public static String getfechayhoraActual(){
        Date fechaActual = new Date();
        SimpleDateFormat apptivaWeb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return apptivaWeb.format(fechaActual);
    }

    public static String leftCadena(String str, int len) {
        if(isEmptyOrShort(str,len)) {
            return str;
        }
        return str.substring(0,len);
    }

    public static String midCadena(String str, int beginIndex, int len) {
        if(isEmptyOrShort(str,len)) {
            return str;
        }
        return str.substring(beginIndex,(beginIndex + len));
    }

    public static String rightCadena(String str, int len) {
        if(isEmptyOrShort(str,len)) {
            return str;
        }
        return str.substring((str.length() - len));
    }

    private static boolean isEmptyOrShort(String str, int len) {
        if(str == null || str.length() < len) {
            return true;
        }
        return false;
    }

}
