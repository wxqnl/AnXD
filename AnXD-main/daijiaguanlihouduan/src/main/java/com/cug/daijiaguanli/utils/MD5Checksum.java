package com.cug.daijiaguanli.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class MD5Checksum {

    public static String getMD5Checksum(String filePath) throws Exception {
        // 创建文件输入流
        InputStream fis = new FileInputStream(filePath);

        // 获取MD5摘要算法的实例
        MessageDigest md = MessageDigest.getInstance("MD5");

        // 创建字节数组用于存储读取的数据
        byte[] buffer = new byte[1024];
        int bytesRead;

        // 从文件中读取数据并更新到MD5摘要中
        while ((bytesRead = fis.read(buffer)) != -1) {
            md.update(buffer, 0, bytesRead);
        }
        fis.close();

        // 计算最终的MD5摘要
        byte[] md5Bytes = md.digest();

        // 将MD5字节数组转换为十六进制字符串
        StringBuilder hexString = new StringBuilder();
        for (byte b : md5Bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
