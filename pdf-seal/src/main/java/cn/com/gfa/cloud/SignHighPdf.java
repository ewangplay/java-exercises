package cn.com.gfa.cloud;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.awt.AsianFontMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PrivateKeySignature;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SignHighPdf {

    /**
     * 
     * @param password
     *                     秘钥密码
     * @param keyStorePath
     *                     秘钥文件路径
     * @param pdfFile
     *                     签名的PDF文件
     * @param x
     *                     签名的X坐标
     * @param y
     *                     签名的Y坐标
     * @param name
     *                     签名的文字
     * @return
     * 
     */
    public static byte[] sign(String password, String keyStorePath, String pdfFile,
            float x, float y, String name, boolean isShowDate) {

        File signPdfSrcFile = new File(pdfFile);
        PdfReader reader = null;
        ByteArrayOutputStream signPDFData = null;
        PdfStamper stp = null;
        FileInputStream fos = null;

        try {
            // 使用bouncycastle密码库
            BouncyCastleProvider provider = new BouncyCastleProvider();
            Security.addProvider(provider);

            // 加载私钥证书
            KeyStore ks = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
            fos = new FileInputStream(keyStorePath);
            ks.load(fos, password.toCharArray()); // 私钥密码

            // 获取私钥和证书
            String alias = (String) ks.aliases().nextElement();
            PrivateKey key = (PrivateKey) ks.getKey(alias, password.toCharArray());
            Certificate[] chain = ks.getCertificateChain(alias);

            reader = new PdfReader(pdfFile);
            signPDFData = new ByteArrayOutputStream();

            // 临时pdf文件
            File temp = new File(signPdfSrcFile.getParent(), System.currentTimeMillis() + ".pdf");
            stp = PdfStamper.createSignature(reader, signPDFData, '\0', temp, true);
            PdfSignatureAppearance sap = stp.getSignatureAppearance();
            sap.setReason("人名章测试");

            // 是对应x轴和y轴坐标
            sap.setVisibleSignature(new Rectangle(x, y, x + 150, y + 65), 1,
                    "sr" + String.valueOf(System.nanoTime()));

            // 在layer0层画红色边框
            PdfTemplate layer0 = sap.getLayer(0);
            layer0.reset();
            float lx = layer0.getBoundingBox().getLeft();
            float by = layer0.getBoundingBox().getBottom();
            float width = layer0.getBoundingBox().getWidth();
            float height = layer0.getBoundingBox().getHeight();
            layer0.setRGBColorFill(255, 0, 0);
            layer0.rectangle(lx, by, 5, height);
            layer0.rectangle(lx, by, width, 5);
            layer0.rectangle(lx, by + height - 5, width, 5);
            layer0.rectangle(lx + width - 5, by, 5, height);
            layer0.fill();

            /////////////////////// layer 2
            PdfTemplate layer2 = sap.getLayer(2);
            layer2.setCharacterSpacing(0.5f);
            layer2.setRGBColorFill(255, 0, 0);

            ColumnText ct = new ColumnText(layer2);
            ct.setSimpleColumn(layer2.getBoundingBox());

            BaseFont bf = BaseFont.createFont(AsianFontMapper.ChineseSimplifiedFont,
                    AsianFontMapper.ChineseSimplifiedEncoding_H,
                    BaseFont.NOT_EMBEDDED);

            // 签写名字
            Paragraph p = new Paragraph(name);
            p.setAlignment(Element.ALIGN_CENTER);
            Font nameFont = new Font(bf, 28, Font.BOLD, BaseColor.RED);
            p.setFont(nameFont);
            ct.addElement(p);

            // 插入当前时间
            if (isShowDate) {
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Paragraph p1 = new Paragraph(sdf.format(d));
                p1.setAlignment(Element.ALIGN_CENTER);
                p1.setSpacingBefore(3f);
                Font dateFont = new Font(bf, 8, Font.BOLD, BaseColor.PINK);
                p1.setFont(dateFont);
                ct.addElement(p1);
            }

            ct.go();

            stp.getWriter().setCompressionLevel(PdfStream.BEST_COMPRESSION);

            ExternalDigest digest = new BouncyCastleDigest();
            ExternalSignature signature = new PrivateKeySignature(key, DigestAlgorithms.SHA512, provider.getName());
            MakeSignature.signDetached(sap, digest, signature, chain, null, null, null, 0, CryptoStandard.CADES);

            stp.close();
            reader.close();

            return signPDFData.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (signPDFData != null) {
                try {
                    signPDFData.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}
