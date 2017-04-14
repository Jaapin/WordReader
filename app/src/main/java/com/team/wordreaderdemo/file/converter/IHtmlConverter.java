package com.team.wordreaderdemo.file.converter;

import java.util.List;

/**
 * 将其它格式的文件转换为html
 */
public interface IHtmlConverter {

   /* /**
     * 文件转换为html文件
     * @param sourceFileName 源文件的文件名
     * @param targetFileName 目标文件的文件名
     * @param title          html的标题
     *//*
    default void toHtml(String sourceFileName, String targetFileName, String title) {
        String extension = FileTool.getFileExtension(sourceFileName);
        switch (extension) {
            case "doc":
                docToHtml(sourceFileName, targetFileName);
                break;
            case "docx":
                docxToHtml(sourceFileName, targetFileName);
                break;
            case "xls":
                xlsToHtml(sourceFileName, targetFileName);
                break;
            case "xlsx":
                xlsxToHtml(sourceFileName, targetFileName);
                break;
            default:
                throw new XyException("不支持的文件类型:" + extension);
        }
        setTitleAndCharset(targetFileName,title);
    }*/

    /* /**
      * 设置html文件的标题和字符集
      * @param fileName 文件名
      * @param title 标题
      */
    /*
    default void setTitleAndCharset(String fileName, String title){
        try{
            File htmlFile = new File(fileName);
            Document doc = Jsoup.parse(htmlFile, "utf-8");
            Elements elements = doc.head().getElementsByAttributeValue("http-equiv","Content-Type");
            if(elements.size()==0){  //如果html中没有设置编码，则设置编码
                doc.charset(Charset.forName("utf-8"));
            }
            doc.title(title);
            String html = doc.html();
            FileTool.writeFile(fileName,html, Charset.forName("utf-8"));
        }catch(XyException e){
            throw e;
        }catch(Exception e){
            throw new XyException("设置html的标题时出错",e);
        }
    }*/

      /**
      * 设置html文件的标题和字符集
      * @param fileName 文件名
      * @param title 标题
      */

     void setTitleAndCharset(String fileName, String title);

    /**
     * doc文件转换为html文件
     * @param sourceFileName 源文件的文件名
     * @param targetFileName 目标文件的文件名
     */
    //void docToHtml(String sourceFileName, String targetFileName,String title);
    void docToHtml(String sourceFileName, String targetFileName);


    /**
     * docx文件转换为html文件
     * @param sourceFileName 源文件的文件名
     * @param targetFileName 目标文件的文件名
     */
    void docxToHtml(String sourceFileName, String targetFileName);

    /**
     * @return 获得Html转换器支持的文件后缀名, 不区分大小写，""表示没有扩展名的文件
     */
    List<String> getSupportedExtensionsForHtml();

   /* *//**
     * 判断转换器是否支持一个扩展名
     * @param extension 文件扩展名
     * @return true 支持，false 不支持
     *//*
    default boolean isSupportedExtensionsForHtml(String extension) {
        List<String> list = getSupportedExtensionsForHtml();
        boolean is=list.stream().anyMatch(new Predicate<String>() {
            @Override
            public boolean test(String item) {
                return item.equalsIgnoreCase(extension);
            }
        });
        return list != null && is;
    }*/

    /**
     * @param extensions 设置Html转换器支持的文件后缀名,不区分大小写，""表示没有扩展名的文件
     */
    void setSupportedExtensionsForHtml(List<String> extensions);

    /**
     * xls文件转换为html文件
     * @param sourceFileName 源文件的文件名
     * @param targetFileName 目标文件的文件名
     */
    void xlsToHtml(String sourceFileName, String targetFileName);

    /**
     * xlsx文件转换为html文件
     * @param sourceFileName 源文件的文件名
     * @param targetFileName 目标文件的文件名
     */
    void xlsxToHtml(String sourceFileName, String targetFileName);

}
