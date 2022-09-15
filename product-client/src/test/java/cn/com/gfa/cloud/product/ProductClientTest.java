package cn.com.gfa.cloud.product;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProductClientTest {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    private static ProductClient c;

    @BeforeClass
    public static void setUp() {
        c = new ProductClient(HOST, PORT);
    }

    @AfterClass
    public static void tearDown() {
        c.close();
    }

    @Test
    public void testSend() {
        int operand = getRandomNumber(1, 1000);
        int expected = operand * 2;

        int result = c.send(operand);
        assertEquals(expected, result);
    }
    
    @Test
    public void testSendBatch() {
        final int MAX_NUM = 1000;
        int count = 0;
        while(count++ < MAX_NUM) {
            int operand = getRandomNumber(1, MAX_NUM);
            int expected = operand * 2;

            int result = c.send(operand);
            assertEquals(expected, result);
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
