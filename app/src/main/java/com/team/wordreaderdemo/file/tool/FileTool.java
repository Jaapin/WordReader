package com.team.wordreaderdemo.file.tool;


import com.team.wordreaderdemo.core.exception.XyException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class FileTool {
    private static Logger log = LoggerFactory.getLogger(FileTool.class);
    private FileTool(){}

    /**
     * 从文件名中截取文件扩展名
     * @param fileName 文件名
     * @return 文件扩展名的小写形式，如果文件没有扩展名，则返回长度为0的空字符串
     */
    public static String getFileExtension(String fileName){
        int index = fileName.lastIndexOf('.');
        String extension = "";
        if(index>0){
            extension = fileName.substring(index+1).toLowerCase();
        }
        return extension;
    }
    
    /**
     * 根据指定编码读取纯文本文件的内容
     * @param fileName 文件名
     * @param charset 编码
     * @return 文件内容
     */
    public static String readTextFile(String fileName,Charset charset){
        File file = new File(fileName);
        StringBuilder sb = new StringBuilder((int)file.length());
        try{
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader in = new InputStreamReader(inputStream,charset);

            char[] b = new char[1024];
            int n;
            while((n=in.read(b))!=-1){
                sb.append(b,0,n);
            }
        }catch(Exception e){
            log.error("写入文件时出现错误",e);
            throw new XyException("写入文件时出现错误",e);
        }
        return sb.toString();
    }

    /**
     * 根据指定编码写文件
     * @param fileName 文件名,如果文件已经存在，则会覆盖原文件
     * @param fileContent 文件名
     * @param charset 编码
     */
    public static void writeFile(String fileName,String fileContent,Charset charset){
        File file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
        try{
            FileOutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter out = new OutputStreamWriter(outputStream,charset);

        	out.write(fileContent);
        }catch (Exception e) {
            log.error("写文件时出错", e);
            throw new XyException("写文件时出错!",e);
        }
    }
    
    /**
	 * 复制文件
	 * @param sourceFile 源文件
	 * @param targetFile 目标文件，如果文件不存在会自动创建一个新文件
	 */
	public static void copyFile(String sourceFile, String targetFile) {
		try {
            FileInputStream inputStream = new FileInputStream(sourceFile);
            FileChannel fromChannel = inputStream.getChannel();
            FileOutputStream outputStream = new FileOutputStream(targetFile);
            FileChannel toChannel = outputStream.getChannel();

			toChannel.transferFrom(fromChannel, 0, fromChannel.size());
		} catch (Exception e) {
			String message = "复制文件出错！源文件:"+sourceFile+",目标文件:"+targetFile;
			log.error(message,e);
			throw new XyException(message,e);
		}
	}
    
}
