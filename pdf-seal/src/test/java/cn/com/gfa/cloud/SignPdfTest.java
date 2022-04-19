package cn.com.gfa.cloud;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;

public class SignPdfTest {

    @Test
    public void testSign() {

        byte[] fileData = SignPdf.sign("123456", "keystore.p12",
                "origal.pdf",
                "seal.jpg", 100, 290);

        try {
            FileOutputStream f = new FileOutputStream(new File("signed.pdf"));
            f.write(fileData);
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
