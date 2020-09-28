package com.gfa.fdfs;

import java.io.IOException;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "upload file..." );
        upload();
        System.out.println( "upload file done" );

        System.out.println( "download file..." );
        download();
        System.out.println( "download file done" );

        System.out.println( "delete file..." );
        delete();
        System.out.println( "delete file done" );
    }

    public static void upload() {

        StorageClient sc = null;

        try {
            // 初始化配置
            ClientGlobal.init("fastdfs.conf");

            // 生成客户端对象
            TrackerClient tc = new TrackerClient();
            TrackerServer ts =  tc.getTrackerServer();
            StorageServer ss = tc.getStoreStorage(ts);
            sc = new StorageClient(ts, ss);

            // 上传文件
            String[] results = sc.upload_file("test.png", "png", null);
            for (String str:results) {
                System.out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sc != null) {
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void download() {

        StorageClient sc = null;

        try {
            // 初始化配置
            ClientGlobal.init("fastdfs.conf");

            // 生成客户端对象
            TrackerClient tc = new TrackerClient();
            TrackerServer ts =  tc.getTrackerServer();
            StorageServer ss = tc.getStoreStorage(ts);
            sc = new StorageClient(ts, ss);

            // 下载文件
            String groupName = "group1";
            String remoteFilename = "M00/00/00/01gZlV8FhueAZ7N2AAdb8zkQxrM959.png";
            String localFilename = "/Users/wangxiaohui/tmp/aa.png";
            int r = sc.download_file(groupName, remoteFilename, localFilename);
            System.out.println(r);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sc != null) {
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void delete() {

        StorageClient sc = null;

        try {
            // 初始化配置
            ClientGlobal.init("fastdfs.conf");

            // 生成客户端对象
            TrackerClient tc = new TrackerClient();
            TrackerServer ts =  tc.getTrackerServer();
            StorageServer ss = tc.getStoreStorage(ts);
            sc = new StorageClient(ts, ss);

            // 下载文件
            String groupName = "group1";
            String remoteFilename = "M00/00/00/01gZlV8Fi3qAMS_iAAdb8zkQxrM138.png";
            int r = sc.delete_file(groupName, remoteFilename);
            System.out.println(r);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sc != null) {
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
