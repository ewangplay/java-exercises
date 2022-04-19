package cn.com.gfa.cloud;

import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class PDF {

    public static boolean createPDF(String outPath, String content) {

        // 新建文档
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));
            // 设置每行的间距
            writer.setInitialLeading(30);

            // 设置文档属性
            // 作者
            document.addAuthor("Ray Wang");
            // 创建日期
            document.addCreationDate();
            // 创建程序
            document.addCreator("GFA ESS");
            // 关键字
            document.addKeywords("测试");
            document.addLanguage("中文");
            // 生产商
            document.addProducer();
            // 标题
            document.addTitle("你好");
            // 主题
            document.addSubject("测试PDF创建");

            // 打开文档
            document.open();

            // 如果输出中文，需要设置中文字体
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font font = new Font(baseFont, 12, Font.NORMAL);
            font.setSize(14);
            font.setColor(new BaseColor(255, 0, 0));
            font.setStyle(Font.ITALIC);

            // 设置块
            Chunk chunk = new Chunk();
            chunk.setFont(new Font(baseFont, 4));
            chunk.setBackground(new BaseColor(0xFf, 0xFF, 0x00));
            chunk.setTextRise(-3f);

            // 写入内容
            Paragraph paragraph = new Paragraph(content, font);
            paragraph.add(chunk);
            document.add(paragraph);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // 关闭文档
            document.close();
        }

        return true;
    }

}
