package com.team.wordreaderdemo.file.converter;

import java.util.List;



/**
 * 将其它格式的文件转换为pdf
 */
public interface IPdfConverter {
	
/*	 *//**
     * 将文件转换为pdf
     * @param sourceFileName 源文件的名称
     * @param targetFileName 目标文件的名称
     *//*
    default void toPdf(String sourceFileName, String targetFileName) {
        String extension = FileTool.getFileExtension(sourceFileName);
        switch (extension) {
            case "doc":
                docToPdf(sourceFileName, targetFileName);
                break;
            case "docx":
                docxToPdf(sourceFileName, targetFileName);
                break;
            case "xls":
                xlsToPdf(sourceFileName, targetFileName);
                break;
            case "xlsx":
                xlsxToPdf(sourceFileName, targetFileName);
                break;
            case "ppt":
                pptToPdf(sourceFileName, targetFileName);
                break;
            case "pptx":
                pptxToPdf(sourceFileName, targetFileName);
                break;
            default:
                throw new XyException("不支持的文件类型:" + extension);
        }
    }*/
    
	/**
     * doc文档转换为pdf
     * @param sourceFileName 源文件的名称
     * @param targetFileName 目标文件的名称
     */
    void docToPdf(String sourceFileName, String targetFileName);

	/**
     * docx文档转换为pdf
     * @param sourceFileName 源文件的名称
     * @param targetFileName 目标文件的名称
     */
    void docxToPdf(String sourceFileName, String targetFileName);

	/**
	 * 
	 * @return 获得PDF转换器支持的文件后缀名,不区分大小写，""表示没有扩展名的文件
	 */
	List<String> getSupportedExtensionsForPdf();

  /*  *//**
	 * 判断转换器是否支持一个扩展名
	 * @param extension 文件扩展名
	 * @return true 支持，false 不支持
	 *//*
	default boolean isSupportedExtensionsForPdf(String extension) {
		List<String> list = getSupportedExtensionsForPdf();

		return list != null && list.stream().anyMatch(item -> item.equalsIgnoreCase(extension));
	}*/

    /**
     * ppt转换为pdf
     * @param sourceFileName 源文件的名称
     * @param targetFileName 目标文件的名称
     */
    void pptToPdf(String sourceFileName, String targetFileName);

    /**
     * pptx转换为pdf
     * @param sourceFileName 源文件的名称
     * @param targetFileName 目标文件的名称
     */
    void pptxToPdf(String sourceFileName, String targetFileName);

    /**
	 * 
	 * @param extensions 设置PDF转换器支持的文件后缀名,不区分大小写，""表示没有扩展名的文件
	 */
	void setSupportedExtensionsForPdf(List<String> extensions);

    /**
     * xls文档转换为pdf
     * @param sourceFileName 源文件的名称
     * @param targetFileName 目标文件的名称
     */
    void xlsToPdf(String sourceFileName, String targetFileName);

    /**
     * xlsx文档转换为pdf
     * @param sourceFileName 源文件的名称
     * @param targetFileName 目标文件的名称
     */
    void xlsxToPdf(String sourceFileName, String targetFileName);

}
