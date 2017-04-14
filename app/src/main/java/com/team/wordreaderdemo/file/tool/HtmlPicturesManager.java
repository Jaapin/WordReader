package com.team.wordreaderdemo.file.tool;

import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;

public class HtmlPicturesManager implements PicturesManager {
    private Logger log = LoggerFactory.getLogger(HtmlPicturesManager.class);
    private String imageRealPath;
    private String imageHtmlPath;

    /**
     * @param imageRealPath 存放图片的文件夹名称
     * @param imageHtmlPath 图片在html文件中的路径
     */
    public HtmlPicturesManager(String imageRealPath,String imageHtmlPath){
        this.imageHtmlPath = imageHtmlPath;
        this.imageRealPath = imageRealPath;
    }

    @Override
    public String savePicture(byte[] content, PictureType pictureType, String name, float widthInches, float heightInches) {
        try{
            FileOutputStream out = new FileOutputStream(imageRealPath+name);
            out.write(content);
        }catch(Exception e){
            log.error("保存图片出错",e);
        }
        return imageHtmlPath+"/"+name;
    }

}
