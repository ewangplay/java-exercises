package cn.com.gfa.cloud;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1UTCTime;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.ofdrw.gm.cert.PEMLoader;
import org.ofdrw.gm.cert.PKCS12Tools;
import org.ofdrw.gm.ses.v1.SES_ESPictrueInfo;
import org.ofdrw.gm.ses.v1.SES_Header;
import org.ofdrw.gm.ses.v1.SES_SignInfo;
import org.ofdrw.gm.ses.v4.SES_CertList;
import org.ofdrw.gm.ses.v4.SES_ESPropertyInfo;
import org.ofdrw.gm.ses.v4.SES_SealInfo;
import org.ofdrw.gm.ses.v4.SESeal;
import org.ofdrw.layout.OFDDoc;
import org.ofdrw.layout.element.Paragraph;
import org.ofdrw.reader.OFDReader;
import org.ofdrw.sign.ExtendSignatureContainer;
import org.ofdrw.sign.NumberFormatAtomicSignID;
import org.ofdrw.sign.OFDSigner;
import org.ofdrw.sign.SignMode;
import org.ofdrw.sign.signContainer.DigitalSignContainer;
import org.ofdrw.sign.signContainer.GBT35275DSContainer;
import org.ofdrw.sign.signContainer.GBT35275PKCS9DSContainer;
import org.ofdrw.sign.signContainer.SESV1Container;
import org.ofdrw.sign.signContainer.SESV4Container;
import org.ofdrw.sign.stamppos.NormalStampPos;

public class OfdUtil {

    public static void createDoc() throws IOException {
        Path path = Paths.get("ofd-demo-resources", "helloworld.ofd");
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            Paragraph p = new Paragraph("你好呀，OFD Reader&Writer！");
            ofdDoc.add(p);
        }
        System.out.println(">> 生成文件位置: " + path.toAbsolutePath());
    } 

    public static void testDigestSign() throws Exception {

        Path keyPemFile = Paths.get("ofd-demo-resources", "sign_key.pem");
        final PrivateKey privateKey = PEMLoader.loadPrivateKey(keyPemFile);

        Path src = Paths.get("ofd-demo-resources", "helloworld.ofd");
        Path out = Paths.get("ofd-demo-resources", "DigitalSign.ofd");
        // 1. 构造签名引擎
        try (OFDReader reader = new OFDReader(src);
             OFDSigner signer = new OFDSigner(reader, out)) {
            DigitalSignContainer signContainer = new DigitalSignContainer(privateKey);
            // 2. 设置签名模式
//            signer.setSignMode(SignMode.WholeProtected);
            signer.setSignMode(SignMode.ContinueSign);
            // 3. 设置签名使用的扩展签名容器
            signer.setSignContainer(signContainer);
            // 4. 执行签名
            signer.exeSign();
            // 5. 关闭签名引擎，生成文档。
        }
        System.out.println(">> 生成文件位置: " + out.toAbsolutePath());
    }

    public static void testGBT35275DigestSign() throws Exception {
        Path certPemFile = Paths.get("ofd-demo-resources", "sign_cert.pem");
        Path keyPemFile = Paths.get("ofd-demo-resources", "sign_key.pem");
        final PrivateKey privateKey = PEMLoader.loadPrivateKey(keyPemFile);
        final Certificate certificate = PEMLoader.loadCert(certPemFile);

        Path src = Paths.get("ofd-demo-resources", "helloworld.ofd");
        Path out = Paths.get("ofd-demo-resources", "GBT35275DigitalSign.ofd");
        // 1. 构造签名引擎
        try (OFDReader reader = new OFDReader(src);
            OFDSigner signer = new OFDSigner(reader, out)) {
            ExtendSignatureContainer signContainer = new GBT35275DSContainer(certificate, privateKey);
            // 该参数用于兼容部分阅读只支持对Hash结果的Base64签名
            // signContainer.setEnableFileHashBase64(true);
            // 4. 设置签名使用的扩展签名容器
            signer.setSignContainer(signContainer);
            // 5. 执行签名
            signer.exeSign();
            // 6. 关闭签名引擎，生成文档。
        }
        System.out.println(">> 生成文件位置: " + out.toAbsolutePath());
    }

    public static void testGBT35275PKCS9DigestSign() throws Exception {
        Path certPemFile = Paths.get("ofd-demo-resources", "sign_cert.pem");
        Path keyPemFile = Paths.get("ofd-demo-resources", "sign_key.pem");
        final PrivateKey privateKey = PEMLoader.loadPrivateKey(keyPemFile);
        final Certificate certificate = PEMLoader.loadCert(certPemFile);

        Path src = Paths.get("ofd-demo-resources", "helloworld.ofd");
        Path out = Paths.get("ofd-demo-resources", "GBT35275PKCS9DigestSign.ofd");
        // 1. 构造签名引擎
        try (OFDReader reader = new OFDReader(src);
            OFDSigner signer = new OFDSigner(reader, out,  new NumberFormatAtomicSignID())) {
            ExtendSignatureContainer signContainer = new GBT35275PKCS9DSContainer(certificate, privateKey);
            // 3. 设置签名模式
            //  signer.setSignMode(SignMode.WholeProtected);
            signer.setSignMode(SignMode.ContinueSign);
            // 4. 设置签名使用的扩展签名容器
            signer.setSignContainer(signContainer);
            // 5. 执行签名
            signer.exeSign();
            // 6. 关闭签名引擎，生成文档。
        }
        System.out.println(">> 生成文件位置: " + out.toAbsolutePath());
    }

    public static void buildSealV1() throws Exception {
        Path certPemFile = Paths.get("ofd-demo-resources", "sign_cert.pem");
        Path keyPemFile = Paths.get("ofd-demo-resources", "sign_key.pem");
        final PrivateKey sealerPrvKey = PEMLoader.loadPrivateKey(keyPemFile);
        final Certificate sealerCert  = PEMLoader.loadCert(certPemFile);
        final Certificate userCert = sealerCert;

        Path picturePath = Paths.get("ofd-demo-resources", "StampImg1.png");
        Path out = Paths.get("ofd-demo-resources", "UserV1.esl");

        SES_Header header = new SES_Header(new ASN1Integer(1), new DERIA5String("OFD印章测试"));

        /*
         * 印章属性信息构造
         */
        ASN1EncodableVector v = new ASN1EncodableVector(1);
        v.add(new DEROctetString(userCert.getEncoded()));

        Calendar then = Calendar.getInstance();
        then.add(Calendar.YEAR, 2);
        org.ofdrw.gm.ses.v1.SES_ESPropertyInfo property = new org.ofdrw.gm.ses.v1.SES_ESPropertyInfo()
                .setType(org.ofdrw.gm.ses.v1.SES_ESPropertyInfo.OrgType)
                .setName(new DERUTF8String("OFD测试用印章"))
                .setCertList(new DERSequence(v))
                .setCreateDate(new ASN1UTCTime(new Date()))
                .setValidStart(new ASN1UTCTime(new Date()))
                .setValidEnd(new ASN1UTCTime(then.getTime()));

        /*
         * 印章图片信息 构造
         */
        SES_ESPictrueInfo picture = new SES_ESPictrueInfo()
                .setType("PNG")
                .setData(Files.readAllBytes(picturePath))
                .setWidth(40)
                .setHeight(40);

        /*
         * 印章信息构造
         */
        org.ofdrw.gm.ses.v1.SES_SealInfo sealInfo = new org.ofdrw.gm.ses.v1.SES_SealInfo()
                .setHeader(header)
                .setEsID(UUID.randomUUID().toString().replace("-", "").toUpperCase())
                .setProperty(property)
                .setPicture(picture);
        /*
         * 电子签章数据构造
         */
        DEROctetString signCert = new DEROctetString(sealerCert.getEncoded());

        // 印章信息、制章人证书、签名算法标识符组成的信息作为签名原文
        v = new ASN1EncodableVector(3);
        v.add(sealInfo);
        v.add(signCert);
        v.add(GMObjectIdentifiers.sm2sign_with_sm3);

        Signature signature = Signature.getInstance("SM3withSm2", "BC");
        signature.initSign(sealerPrvKey);
        signature.update(new DERSequence(v).getEncoded("DER"));
        byte[] sign = signature.sign();

        SES_SignInfo signInfo = new SES_SignInfo()
                .setCert(signCert)
                .setSignatureAlgorithm(GMObjectIdentifiers.sm2sign_with_sm3)
                .setSignData(sign);

        org.ofdrw.gm.ses.v1.SESeal seal = new org.ofdrw.gm.ses.v1.SESeal(sealInfo, signInfo);

        Files.write(out, seal.getEncoded("DER"));
        System.out.println(">> V1版本印章存储于: " + out.toAbsolutePath());
    }

    public static void testSESV1() throws Exception {
        Path certPemFile = Paths.get("ofd-demo-resources", "sign_cert.pem");
        Path keyPemFile = Paths.get("ofd-demo-resources", "sign_key.pem");
        final PrivateKey privateKey = PEMLoader.loadPrivateKey(keyPemFile);
        final Certificate certificate = PEMLoader.loadCert(certPemFile);

        Path sealPath = Paths.get("ofd-demo-resources", "UserV1.esl");
        org.ofdrw.gm.ses.v1.SESeal seal = org.ofdrw.gm.ses.v1.SESeal.getInstance(Files.readAllBytes(sealPath));

        Path src = Paths.get("ofd-demo-resources", "helloworld.ofd");
        Path out = Paths.get("ofd-demo-resources", "SESV1SignDoc.ofd");
        // 1. 构造签名引擎
        try (OFDReader reader = new OFDReader(src);
             OFDSigner signer = new OFDSigner(reader, out)
//             OFDSigner signer = new OFDSigner(reader, out, new NumberFormatAtomicSignID()
        ) {
            SESV1Container signContainer = new SESV1Container(privateKey, seal, certificate);
            // 2. 设置签名模式
//            signer.setSignMode(SignMode.WholeProtected);
            signer.setSignMode(SignMode.ContinueSign);
            // 3. 设置签名使用的扩展签名容器
            signer.setSignContainer(signContainer);
            // 4. 设置显示位置
            signer.addApPos(new NormalStampPos(1, 50, 50, 40, 40));
            // 5. 执行签名
            signer.exeSign();
            // 6. 关闭签名引擎，生成文档。
        }
        System.out.println(">> 生成文件位置: " + out.toAbsolutePath());
    }

    public static void buildSealV4() throws Exception {
        Path certPemFile = Paths.get("ofd-demo-resources", "sign_cert.pem");
        Path keyPemFile = Paths.get("ofd-demo-resources", "sign_key.pem");
        final PrivateKey sealerPrvKey = PEMLoader.loadPrivateKey(keyPemFile);
        final Certificate sealerCert  = PEMLoader.loadCert(certPemFile);
        final Certificate userCert = sealerCert;

        Path picturePath = Paths.get("ofd-demo-resources", "StampImg1.png");

        ASN1EncodableVector v = new ASN1EncodableVector(1);
        v.add(new DEROctetString(userCert.getEncoded()));

        SES_Header header = new SES_Header(SES_Header.V4, new DERIA5String("OFD印章测试"));
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, 2);
        Date then = now.getTime();
        SES_ESPropertyInfo propertyInfo = new SES_ESPropertyInfo()
                .setType(new ASN1Integer(3))
                .setName(new DERUTF8String("OFD测试用印章"))
                .setCertListType(SES_ESPropertyInfo.CertListType)
                .setCertList(SES_CertList.getInstance(SES_ESPropertyInfo.CertListType, new DERSequence(v)))
                .setCreateDate(new ASN1GeneralizedTime(new Date()))
                .setValidStart(new ASN1GeneralizedTime(new Date()))
                .setValidEnd(new ASN1GeneralizedTime(then));

        SES_ESPictrueInfo pictrueInfo = new SES_ESPictrueInfo()
                .setType("PNG")
                .setData(Files.readAllBytes(picturePath))
                .setWidth(40)
                .setHeight(40);

        SES_SealInfo sesSealInfo = new SES_SealInfo()
                .setHeader(header)
                .setEsID(new DERIA5String(UUID.randomUUID().toString().replace("-", "")))
                .setProperty(propertyInfo)
                .setPicture(pictrueInfo);

        Signature sg = Signature.getInstance("SM3WithSM2", new BouncyCastleProvider());
        sg.initSign(sealerPrvKey);
        sg.update(sesSealInfo.getEncoded("DER"));
        byte[] sigVal = sg.sign();

        SESeal seal = new SESeal()
                .seteSealInfo(sesSealInfo)
                .setCert(sealerCert)
                .setSignAlgID(GMObjectIdentifiers.sm2sign_with_sm3)
                .setSignedValue(sigVal);

        Path out = Paths.get("ofd-demo-resources", "UserV4.esl");
        Files.write(out, seal.getEncoded("DER"));
        System.out.println(">> V4版本印章存储于: " + out.toAbsolutePath());
    }

    public static void testSESV4() throws Exception {
        Path certPemFile = Paths.get("ofd-demo-resources", "sign_cert.pem");
        Path keyPemFile = Paths.get("ofd-demo-resources", "sign_key.pem");
        final PrivateKey privateKey = PEMLoader.loadPrivateKey(keyPemFile);
        final Certificate certificate = PEMLoader.loadCert(certPemFile);

        Path sealPath = Paths.get("ofd-demo-resources", "UserV4.esl");
        SESeal seal = SESeal.getInstance(Files.readAllBytes(sealPath));

        Path src = Paths.get("ofd-demo-resources", "helloworld.ofd");
        Path out = Paths.get("ofd-demo-resources", "SESV4SignDoc.ofd");
        // 1. 构造签名引擎
        try (OFDReader reader = new OFDReader(src);
//             OFDSigner signer = new OFDSigner(reader, out)
             OFDSigner signer = new OFDSigner(reader, out, new NumberFormatAtomicSignID())
        ) {
            SESV4Container signContainer = new SESV4Container(privateKey, seal, certificate);
            // 2. 设置签名模式
//            signer.setSignMode(SignMode.WholeProtected);
            signer.setSignMode(SignMode.ContinueSign);
            // 3. 设置签名使用的扩展签名容器
            signer.setSignContainer(signContainer);
            // 4. 设置显示位置
            signer.addApPos(new NormalStampPos(1, 50, 50, 40, 40));
            // 5. 执行签名
            signer.exeSign();
            // 6. 关闭签名引擎，生成文档。
        }
        System.out.println(">> 生成文件位置: " + out.toAbsolutePath().toAbsolutePath());
    }


    public static void testGFASESV4() throws Exception {
        Path userPath = Paths.get("src/test/resources", "zhangsan1.p12");
        PrivateKey privateKey = PKCS12Tools.ReadPrvKey(userPath, "SD", "12345678");
        //Certificate certificate = PKCS12Tools.ReadUserCert(userPath, "SD", "12345678");
        Path certPemFile = Paths.get("src/test/resources", "zhangsan1.cer");
        final Certificate certificate = PEMLoader.loadCert(certPemFile);

        Path sealPath = Paths.get("src/test/resources", "zhangsan1.esl");
        byte[] sealDat = Files.readAllBytes(sealPath);
        SESeal seal = SESeal.getInstance(Base64.decode(sealDat));

        Path src = Paths.get("src/test/resources", "helloworld.ofd");
        Path out = Paths.get("src/test/resources", "SESV4SignDoc.ofd");
        // 1. 构造签名引擎
        try (OFDReader reader = new OFDReader(src);
//             OFDSigner signer = new OFDSigner(reader, out)
             OFDSigner signer = new OFDSigner(reader, out, new NumberFormatAtomicSignID())
        ) {
            SESV4Container signContainer = new SESV4Container(privateKey, seal, certificate);
            // 2. 设置签名模式
//            signer.setSignMode(SignMode.WholeProtected);
            signer.setSignMode(SignMode.ContinueSign);
            // 3. 设置签名使用的扩展签名容器
            signer.setSignContainer(signContainer);
            // 4. 设置显示位置
            signer.addApPos(new NormalStampPos(1, 50, 50, 40, 40));
            // 5. 执行签名
            signer.exeSign();
            // 6. 关闭签名引擎，生成文档。
        }
        System.out.println(">> 生成文件位置: " + out.toAbsolutePath().toAbsolutePath());
    }

}
