package cn.com.gfa.cloud;

import org.junit.Test;

public class PDFTest {
    
    @Test
    public void testCreatePDF() {
        PDF.createPDF("hello.pdf", "你好，中国!");
    }

}
