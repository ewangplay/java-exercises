package cn.com.gfa.cloud.svsclient;

import java.util.Base64;

import cn.com.gfa.svsmp.asn1.SVSMP;
import cn.com.gfa.svs.client.SVSClient;

public class SVSClientDemo 
{
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 3456;

    public static void main( String[] args )
    {
        /* 签名算法类型
        SGD_SHA256_RSA 基于SHA256算法和RSA算法的签名
        SGD_SM3_SM2    基于SM3算法和SM2算法的签名
        */
       int signMethod = SVSMP.SignAlgType.SGD_SM3_SM2;

       /**签名者私钥的索引值*/
       int keyIndex = 0;

       /**签名者私钥权限标识码*/
       String keyValue = "53d8c9158474948501847495a9b70002";

       /**待签名的数据原文*/
       byte inData[] = "1234567812345678".getBytes();

       SVSClient client = null;
       try {
           client = new SVSClient(HOST, PORT);

           byte[] signature = client.signData(signMethod, keyIndex, keyValue, inData);
           if (signature != null) {
               String signatureBase64 = Base64.getEncoder().encodeToString(signature);
               System.out.println("signData Succ: " + signatureBase64);
           }
       } finally {
           if (client != null)
               client.close();
       }

    }

}
