package cn.com.gfa.cloud;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        // 创建OFD文档
        OfdUtil.createDoc();

        // 创建普通签名
        OfdUtil.testDigestSign();

        // 创建符合GB35275规范的数字签名
        OfdUtil.testGBT35275DigestSign();

        // 创建数科阅读器可验证的签名
        OfdUtil.testGBT35275PKCS9DigestSign();

        // 创建V1版本的印章
        OfdUtil.buildSealV1();

        // 在文档上加盖V1版本的印章
        OfdUtil.testSESV1();

        // 创建V4版本的印章
        OfdUtil.buildSealV4();

        // 在文档上加盖V4版本的印章
        OfdUtil.testSESV4();
    }

}
