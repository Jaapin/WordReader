package com.team.wordreaderdemo.file.tool;

import android.content.Context;
import android.util.Log;

import com.team.wordreaderdemo.core.exception.NotImplementedException;
import com.team.wordreaderdemo.core.exception.XyException;
import com.team.wordreaderdemo.file.converter.IHtmlConverter;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * 使用POI作为转换器实现转换功能
 */
public class PoiTool implements IHtmlConverter {
    private static Logger log = LoggerFactory.getLogger(PoiTool.class);
    private List<String> supportedExtensionsForHtmlConverter;
    private Context context;
    /**
     * 存放图片的路径
     */
    private final static String IMAGE_PATH = "image";

   // public void docToHtml(String sourceFileName, String targetFileName) {

    public  PoiTool(Context context){
        this.context=context;
    }


    @Override
    public void setTitleAndCharset(String fileName, String title) {
        try{
            File htmlFile = new File(fileName);
            org.jsoup.nodes.Document doc = Jsoup.parse(htmlFile, "utf-8");
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
    }

    @Override
      //public void docToHtml(String sourceFileName, String targetFileName,String title) {
      public void docToHtml(String sourceFileName, String targetFileName) {

        initFolder(targetFileName);
        String imagePathStr = initImageFolder(targetFileName);
        InputStream in=null;
        try {
            in=context.getResources().getAssets().open(sourceFileName);

            //HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(sourceFileName));
            HWPFDocument wordDocument = new HWPFDocument(in);
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
            HtmlPicturesManager picturesManager = new HtmlPicturesManager(imagePathStr,IMAGE_PATH);
            wordToHtmlConverter.setPicturesManager(picturesManager);
            wordToHtmlConverter.processDocument(wordDocument);

            Document htmlDocument = wordToHtmlConverter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(new File(targetFileName));

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("将doc文件转换为html时出错", e);
            throw new XyException("将doc文件转换为html时出错!",e);
        } finally {
            if(null!=in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //setTitleAndCharset(targetFileName,title);
    }

    @Override
    public void docxToHtml(String sourceFileName, String targetFileName) {
        initFolder(targetFileName);
        String imagePathStr = initImageFolder(targetFileName);
        OutputStreamWriter outputStreamWriter = null;
        InputStream in=null;
        try {
            Log.e("====>sourceFileName",sourceFileName);
            in=context.getResources().getAssets().open(sourceFileName);
            XWPFDocument document = new XWPFDocument(in);
            XHTMLOptions options = XHTMLOptions.create();
            //存放图片的文件夹
            options.setExtractor( new FileImageExtractor( new File(imagePathStr ) ) );
            //html中图片的路径
            options.URIResolver( new BasicURIResolver(IMAGE_PATH));
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(targetFileName),"utf-8");
            XHTMLConverter xhtmlConverter = (XHTMLConverter)XHTMLConverter.getInstance();
            xhtmlConverter.convert(document, outputStreamWriter, options);
        } catch (Exception e) {
            log.error("将docx文件转换为html时出错", e);
            throw new XyException("将docx文件转换为html时出错!",e);
        }finally{
            try{
                if(outputStreamWriter!=null){
                    outputStreamWriter.close();
                }
                if(in!=null){
                    in.close();
                }
            }catch(Exception e){
                log.error("关闭文件流时出错", e);
            }
        }
    }

    @Override
    public void xlsToHtml(String sourceFileName, String targetFileName) {
        initFolder(targetFileName);
        try {
            Document doc = ExcelToHtmlConverter.process( new File( sourceFileName) );
            DOMSource domSource = new DOMSource( doc );
            StreamResult streamResult = new StreamResult( new File(targetFileName) );
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty( OutputKeys.ENCODING, "UTF-8" );
            serializer.setOutputProperty( OutputKeys.INDENT, "yes" );
            serializer.setOutputProperty( OutputKeys.METHOD, "html" );
            serializer.transform( domSource, streamResult );
        }catch (Exception e) {
            e.printStackTrace();
            log.error("将xls文件转换为html时出错", e);
            throw new XyException("将xls文件转换为html时出错!",e);
        }
    }

    @Override
    public void xlsxToHtml(String sourceFileName, String targetFileName) {
        throw new NotImplementedException();
    }

    /**
     * 初始化存放html文件的文件夹
     * @param targetFileName html文件的文件名
     */
    private void initFolder (String targetFileName){
        File targetFile = new File(targetFileName);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        String targetPathStr = context.getFilesDir()+targetFileName.substring(0, targetFileName.lastIndexOf(File.separator));
        File targetPath = new File(targetPathStr);
        //如果文件夹不存在，则创建
        if (!targetPath.exists()) {
            targetPath.mkdirs();
        }
    }

    /**
     * 初始化存放图片的文件夹
     * @param htmlFileName html文件的文件名
     * @return 存放图片的文件夹路径
     */
    private String initImageFolder(String htmlFileName){
        String targetPathStr = htmlFileName.substring(0, htmlFileName.lastIndexOf(File.separator));
        //创建存放图片的文件夹
        String imagePathStr = targetPathStr + File.separator + IMAGE_PATH+ File.separator;
        File imagePath = new File(imagePathStr);
        if (imagePath.exists()) {
            imagePath.delete();
        }
        imagePath.mkdir();
        return imagePathStr;
    }
    
    @Override
	public List<String> getSupportedExtensionsForHtml() {
		return supportedExtensionsForHtmlConverter;
	}

	@Override
	public void setSupportedExtensionsForHtml(List<String> supportedExtensionsForHtmlConverter) {
		this.supportedExtensionsForHtmlConverter = supportedExtensionsForHtmlConverter;
	}

    /**
     * 读取doc文件的文本，不带格式
     * @param fileName 文件名
     * @return 文件的文本内容
     */
    public static String readTextForDoc(String fileName){
        String text;
        try{
            FileInputStream in = new FileInputStream(fileName);
            WordExtractor wordExtractor = new WordExtractor(in);
            text = wordExtractor.getText();
        }catch(Exception e){
            log.error("读取文件出错:"+fileName, e);
            throw new XyException("读取文件出错",e);
        }
        return text;
    }

    /**
     * 读取docx文件的内容，只读取纯文本内容，不带格式
     * @param fileName 文件名
     * @return 文件内容
     */
    public static String readTextForDocx(String fileName) {
        String text = null;
        POIXMLTextExtractor ex = null;
        try{
            OPCPackage oPCPackage = POIXMLDocument.openPackage(fileName);
            XWPFDocument xwpf = new XWPFDocument(oPCPackage);
            ex = new XWPFWordExtractor(xwpf);
            text = ex.getText();
        }catch(Exception e){
            log.error("读取文件出错,文件名:"+fileName, e);
            throw new XyException("读取文件出错",e);
        }finally{
            try{
                if(ex!=null){
                    ex.close();
                }
            }catch(Exception e){
                log.error("关闭流出错:"+fileName, e);
            }
        }
        return text;
    }

}
