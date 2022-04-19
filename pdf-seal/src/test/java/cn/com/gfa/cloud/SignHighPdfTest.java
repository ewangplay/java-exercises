package cn.com.gfa.cloud;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;

public class SignHighPdfTest {

    @Test
    public void testSign() {

        // 对已经签章的signed.pdf文件再次签章，这次是高清签章
        byte[] fileData = SignHighPdf.sign("123456", "keystore.p12",
                "signed.pdf", 350, 290, "华佗\n2017-12-20");

        try {
            FileOutputStream f = new FileOutputStream(new File("signed2.pdf"));
            f.write(fileData);
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
