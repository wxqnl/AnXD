package com.cug.daijiaguanli.utils;

import java.io.*;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtil {

         /**
         * 解压
         *
         * @param zipFilePathUrl 带解压文件的Url
         * @param desDirectory 解压到的目录
         * @throws Exception
         */
        public static void unzip(String zipFilePathUrl, String desDirectory) throws Exception {

            URL url=new URL(zipFilePathUrl);
            File desDir = new File(desDirectory);
            if (!desDir.exists()) {
                boolean mkdirSuccess = desDir.mkdir();
                if (!mkdirSuccess) {
                    throw new Exception("创建解压目标文件夹失败");
                }
            }
            // 打开连接并获取输入流
            try (InputStream inputStream = url.openStream();
                 ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while (zipEntry != null) {
                    String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
                    if (zipEntry.isDirectory()) {
                        // 创建目录
                        mkdir(new File(unzipFilePath));
                    } else {
                        // 解压单个文件
                        File file = new File(unzipFilePath);
                        mkdir(file.getParentFile()); // 确保目录存在
                        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(unzipFilePath))) {
                            byte[] bytes = new byte[1024];
                            int readLen;
                            while ((readLen = zipInputStream.read(bytes)) != -1) {
                                bufferedOutputStream.write(bytes, 0, readLen);
                            }
                        }
                    }
                    zipInputStream.closeEntry();
                    zipEntry = zipInputStream.getNextEntry();
                }
            }
        }

        // 如果父目录不存在则创建
        private static void mkdir(File file) {
            if (null == file || file.exists()) {
                return;
            }
            mkdir(file.getParentFile());
            file.mkdir();
        }


        public static void UnZip(String zipFilePathurl,String desDirectory)
        {
            try {
                unzip(zipFilePathurl, desDirectory);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static void main(String[] args) throws Exception {
            String zipFilePath = "https://we-pan.oss-cn-wuhan-lr.aliyuncs.com/1c6dc348-c6e8-4521-89c3-28fe940b3143.zip";
            String desDirectory = "D:/LIHAOZE/localFile";
            unzip(zipFilePath, desDirectory);
        }
    }

