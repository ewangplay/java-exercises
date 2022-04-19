package cn.com.gfa.cloud;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.junit.Test;

public class PKCSTest {

    @Test
    public void testCreateCert() {

        // CN: 名字与姓氏 
        // OU : 组织单位名称
        // O ：组织名称 
        // L : 城市或区域名称 
        // E : 电子邮件
        // ST: 州或省份名称 
        // C: 单位的两字母国家代码
        String issuerStr = "CN=在线医院,OU=gitbook研发部,O=gitbook有限公司,C=CN,E=gitbook@sina.com,L=北京,ST=北京";
        String subjectStr = "CN=huangjinjin,OU=gitbook研发部,O=gitbook有限公司,C=CN,E=huangjinjin@sina.com,L=北京,ST=北京";
        String certificateCRL = "https://gitbook.cn";

        Map<String, byte[]> result = PKCS.createCert("123456", issuerStr, subjectStr, certificateCRL);

        try {

            FileOutputStream fosP12 = new FileOutputStream("keystore.p12"); // ca.jks
            fosP12.write(result.get("keyStoreData"));
            fosP12.flush();
            fosP12.close();

            FileOutputStream fosCer = new FileOutputStream(new File("keystore.cer"));
            fosCer.write(result.get("certificateData"));
            fosCer.flush();
            fosCer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
