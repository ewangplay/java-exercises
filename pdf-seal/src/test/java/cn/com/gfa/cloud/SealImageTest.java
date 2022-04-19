package cn.com.gfa.cloud;

import org.junit.Test;

public class SealImageTest {
    
    @Test
    public void testCreateSignImage() {
        SealImage.createSealImage("华佗", "在线医院", "2018.01.01", "seal.jpg");
    }
    
}
