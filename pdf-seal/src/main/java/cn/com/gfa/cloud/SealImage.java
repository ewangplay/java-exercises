package cn.com.gfa.cloud;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import sun.font.FontDesignMetrics;

public class SealImage {

    /**
     * 
     * @param doctorName
     *                     String 医生名字
     * @param hospitalName
     *                     String 医生名称
     * @param date
     *                     String 签名日期
     * @param filename
     *                     String jpg图片名
     * @return
     * 
     */

    public static boolean createSealImage(
            String doctorName,
            String hospitalName,
            String date,
            String filename) {

        int width = 255;
        int height = 100;
        int edgeWidth = 5;

        // 字色
        Color fontcolor = Color.RED;

        Font doctorNameFont = new Font(null, Font.BOLD, 20);

        Font othorTextFont = new Font(null, Font.BOLD, 18);

        try {

            BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // 创建一个2D图形对象
            Graphics2D g = bimage.createGraphics(); 

            // 画一个白色的背景矩形
            g.setColor(Color.WHITE); // 设置背景色
            g.fillRect(0, 0, width, height); // 画一个矩形
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 去除锯齿(当设置的字体过大的时候,会出现锯齿)

            // 为上面的背景矩形画一个edgeWidth像素的红色的边框
            g.setColor(Color.RED);
            g.fillRect(0, 0, edgeWidth, height);
            g.fillRect(0, 0, width, edgeWidth);
            g.fillRect(0, height - edgeWidth, width, height);
            g.fillRect(width - edgeWidth, 0, width, height);

            // 在矩形内写医生的名字
            g.setColor(fontcolor); // 字的颜色
            g.setFont(doctorNameFont); // 字体字形字号
            FontMetrics fm = FontDesignMetrics.getMetrics(doctorNameFont);
            int font1_Hight = fm.getHeight();
            int strWidth = fm.stringWidth(doctorName);
            int y = 35;
            int x = (width - strWidth) / 2;
            g.drawString(doctorName, x, y); // 在指定坐标除添加文字

            // 在矩形内写医院的名字
            g.setFont(othorTextFont); // 字体字形字号
            fm = FontDesignMetrics.getMetrics(othorTextFont);
            int font2_Hight = fm.getHeight();
            strWidth = fm.stringWidth(hospitalName);
            x = (width - strWidth) / 2;
            g.drawString(hospitalName, x, y + font1_Hight); // 在指定坐标除添加文字

            // 在矩形内写日期
            strWidth = fm.stringWidth(date);
            x = (width - strWidth) / 2;
            g.drawString(date, x, y + font1_Hight + font2_Hight); // 在指定坐标除添加文字

            // 释放图形对象资源
            g.dispose();

            // 把生成的印章图片写入文件
            ImageIO.write(bimage, "jpg", new File(filename));

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}