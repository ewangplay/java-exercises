package cn.com.gfa.cloud;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.kernel.geom.Rectangle;

import org.junit.Test;

public class I7SignHighPdfTest {

    public static final String[] RESULT_FILES = new String[] {
        "signed1.pdf", "signed1_show_date.pdf"

    };

    @Test
    public void testSign0() {

        I7SignHighPdf.sign0("123456", "keystore.p12",
                "origal.pdf", "signed.pdf", 350, 290, "中华人民共和国", true);
    }

    @Test
    public void testSign1() {

        I7SignHighPdf.sign1("123456", "keystore.p12",
                "origal.pdf", RESULT_FILES[0], 350, 290, "王晓辉", 28, false, 8);

        I7SignHighPdf.sign1("123456", "keystore.p12",
                "origal.pdf", RESULT_FILES[1], 350, 290, "王晓辉", 28, true, 8);
    }

    @Test
    public void testGetSignRect() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(d);

        Rectangle rect =  I7SignHighPdf.getSignRect(350, 290, "中华人民共和国", 28, false, dateStr, 8);
        System.out.println("X:" + rect.getX());
        System.out.println("Y:" + rect.getY());
        System.out.println("height:" + rect.getHeight());
        System.out.println("width:" + rect.getWidth());
    }
}
