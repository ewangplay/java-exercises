package cn.com.gfa.cloud;

import java.awt.Font;
import java.awt.FontMetrics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PdfSigner.CryptoStandard;
import com.itextpdf.signatures.PrivateKeySignature;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.font.FontDesignMetrics;

public class I7SignHighPdf {

    /**
     * 
     * @param password
     *                     秘钥密码
     * @param keyStorePath
     *                     秘钥文件路径
     * @param srcFile
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
    public static void sign0(String password, String keyStorePath, String srcFile, String destFile,
            float x, float y, String name, boolean isShowDate) {

        File signPdfSrcFile = new File(srcFile);
        PdfReader reader = null;
        PdfSigner signer = null;
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

            reader = new PdfReader(srcFile);
            signer = new PdfSigner(reader, new FileOutputStream(destFile), signPdfSrcFile.getParent(),
                    new StampingProperties());

            // 根据字体和名字自动计算签名区域大小
            PdfFont nameFont = PdfFontFactory.createFont("./fonts/simsun.ttc,0", PdfEncodings.IDENTITY_H);
            float nameFontSize = 28;
            float signFieldWidth = nameFont.getWidth(name, nameFontSize);
            float signFieldHeight = 60;
            System.out.println("width: " + signFieldWidth);

            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(d);
            PdfFont dateFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            float dateFontSize = 8;
            float dateFieldWidth = dateFont.getWidth(date, dateFontSize);
            if (isShowDate) {
                if (dateFieldWidth > signFieldWidth) {
                    signFieldWidth = dateFieldWidth;
                }
                signFieldHeight += 15;
            }

            signFieldWidth += 20;
            Rectangle rectSign = new Rectangle(x, y, signFieldWidth, signFieldHeight);

            PdfSignatureAppearance appearance = signer.getSignatureAppearance();
            appearance.setReason("人名章测试");
            appearance.setReuseAppearance(false);
            appearance.setPageNumber(1);
            appearance.setPageRect(rectSign);
            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);
            signer.setFieldName("sig");

            // 在layer0层画红色边框
            PdfFormXObject n0 = appearance.getLayer0();
            float lx = n0.getBBox().toRectangle().getLeft();
            float ly = n0.getBBox().toRectangle().getBottom();
            float width = n0.getBBox().toRectangle().getWidth();
            float height = n0.getBBox().toRectangle().getHeight();
            PdfCanvas canvas = new PdfCanvas(n0, signer.getDocument());
            canvas.setFillColor(ColorConstants.RED);
            // canvas.rectangle(lx, ly, width, height);
            canvas.rectangle(lx, ly, 5, height);
            canvas.rectangle(lx, ly, width, 5);
            canvas.rectangle(lx, ly + height - 5, width, 5);
            canvas.rectangle(lx + width - 5, ly, 5, height);
            canvas.fill();

            /////////////////////// layer 2
            PdfFormXObject n2 = appearance.getLayer2();
            Canvas seal = new Canvas(n2, signer.getDocument());

            // 签写名字
            Paragraph p = new Paragraph(name);
            p.setTextAlignment(TextAlignment.CENTER);
            p.setFont(nameFont);
            p.setFontColor(ColorConstants.RED);
            p.setFontSize(nameFontSize);
            seal.add(p);

            // 插入当前时间
            if (isShowDate) {
                Paragraph p1 = new Paragraph(date);
                // p1.setMarginTop(10);
                // p1.setPaddingTop(10);
                p1.setTextAlignment(TextAlignment.CENTER);
                p1.setFont(dateFont);
                p1.setFontColor(ColorConstants.PINK);
                p1.setFontSize(dateFontSize);
                seal.add(p1);
            }

            seal.close();

            // Sign the document using the detached mode, CMS or CAdES equivalent.
            IExternalSignature pks = new PrivateKeySignature(key, DigestAlgorithms.SHA512, provider.getName());
            IExternalDigest digest = new BouncyCastleDigest();
            signer.signDetached(digest, pks, chain, null, null, null, 0, CryptoStandard.CADES);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Rectangle getSignRect(float x, float y, String name, int nameFontSize, boolean isShowDate,
            String dateStr, int dateFontSize) {
        Font nameFont = new Font("宋体", Font.BOLD, nameFontSize);
        FontMetrics fmName = FontDesignMetrics.getMetrics(nameFont);
        int h = fmName.getHeight();
        int w = fmName.stringWidth(name);

        if (isShowDate) {
            Font dateFont = new Font("宋体", Font.PLAIN, dateFontSize);
            FontMetrics fmDate = FontDesignMetrics.getMetrics(dateFont);
            int dateHeight = fmDate.getHeight();
            int dateWidth = fmDate.stringWidth(dateStr);

            // 如果日期字符串的长度大于名字字符串的长度，则使用日期字符串的长度
            if (dateWidth > w) {
                w = dateWidth;
            }

            // 把日期字符串的高度叠加上去
            h += dateHeight;
        }

        w += 20;
        h *= 2;

        Rectangle rect = new Rectangle(x, y, w, h);
        return rect;
    }

    public static void sign1(String password, String keyStorePath, String srcFile, String destFile,
            float x, float y, String name, int nameFontSize, boolean isShowDate, int dateFontSize) {

        File signPdfSrcFile = new File(srcFile);
        PdfReader reader = null;
        PdfSigner signer = null;
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

            reader = new PdfReader(srcFile);
            signer = new PdfSigner(reader, new FileOutputStream(destFile), signPdfSrcFile.getParent(),
                    new StampingProperties());

            float edgeWidth = 3;
            edgeWidth = edgeWidth * nameFontSize / 28;

            // 根据字体和名字自动计算签名区域大小
            PdfFont nameFont = PdfFontFactory.createFont("./fonts/simsun.ttc,0", PdfEncodings.IDENTITY_H);

            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = sdf.format(d);
            PdfFont dateFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);

            Rectangle rectSign = getSignRect(x, y, name, nameFontSize, isShowDate, dateStr, dateFontSize);

            PdfSignatureAppearance appearance = signer.getSignatureAppearance();
            appearance.setReason("人名章测试");
            appearance.setReuseAppearance(false);
            appearance.setPageNumber(1);
            appearance.setPageRect(rectSign);
            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);
            signer.setFieldName("sig");

            // 在layer0层画红色边框
            /*
             * PdfFormXObject n0 = appearance.getLayer0();
             * float lx = n0.getBBox().toRectangle().getLeft();
             * float ly = n0.getBBox().toRectangle().getBottom();
             * float width = n0.getBBox().toRectangle().getWidth();
             * float height = n0.getBBox().toRectangle().getHeight();
             * PdfCanvas canvas = new PdfCanvas(n0, signer.getDocument());
             * canvas.setFillColor(ColorConstants.RED);
             * // canvas.rectangle(lx, ly, width, height);
             * canvas.rectangle(lx, ly, edgeWidth, height);
             * canvas.rectangle(lx, ly, width, edgeWidth);
             * canvas.rectangle(lx, ly + height - edgeWidth, width, edgeWidth);
             * canvas.rectangle(lx + width - edgeWidth, ly, edgeWidth, height);
             * canvas.fill();
             */

            /////////////////////// layer 2
            PdfFormXObject n2 = appearance.getLayer2();
            Canvas seal = new Canvas(n2, signer.getDocument());

            // 签写名字
            Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            Border border = new SolidBorder(ColorConstants.RED, edgeWidth);
            table.setBorder(border);
            table.setTextAlignment(TextAlignment.JUSTIFIED_ALL);
            table.setHeight(rectSign.getHeight());

            Paragraph pName = new Paragraph(name);
            pName.setFont(nameFont);
            pName.setFontColor(ColorConstants.RED);
            pName.setFontSize(nameFontSize);
            table.addCell(pName);

            if (isShowDate) {
                Paragraph pDate = new Paragraph(dateStr);
                pDate.setFont(dateFont);
                pDate.setFontColor(ColorConstants.PINK);
                pDate.setFontSize(dateFontSize);
                table.addCell(pDate);
            }

            seal.add(table);
            seal.close();

            // Sign the document using the detached mode, CMS or CAdES equivalent.
            IExternalSignature pks = new PrivateKeySignature(key, DigestAlgorithms.SHA512, provider.getName());
            IExternalDigest digest = new BouncyCastleDigest();
            signer.signDetached(digest, pks, chain, null, null, null, 0, CryptoStandard.CADES);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
