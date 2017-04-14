package com.team.wordreaderdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.team.wordreaderdemo.file.tool.PoiTool;

import java.io.File;

import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private String BaseName=null;
    private String FileName="testDoc";
    //private String FileName="我是测试文档";
    //private String FileName="textDoc";
    //private String FileName="动态改变App桌面图标";
    private String sourceFileName=null;
    private String targetFileName=null;
    private static  final  String Tag="MainActivity";
    public static final int READ_CONTACTS_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView= ((WebView) this.findViewById(R.id.webview));
        //判断运行版本,大于等于6.0,请求动态运行时权限
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.M){
            initData();
        }else{
            initPerssion();
        }

        initWebSeting();

    }

    /*
    运行时权限
     */
    private void initPerssion() {

        // 如果权限没有被授予
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_CONTACTS_REQUEST_CODE);
        } else {
            // TODO 权限已经被授予
            initData();
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_CONTACTS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO 用户已经授予了权限
                    initData();
                } else {
                    // TODO 用户拒绝授予权限

                }
                break;
        }

    }
    private void initWebSeting() {
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

    //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

    //支持插件
        //webSettings.setPluginsEnabled(true);

    //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        // 缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件


        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }

    private void initData() {
        /*********************************poi*********************************************/
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){

            File filepath=new File(getExternalStorageDirectory().getAbsolutePath());
            if(filepath.canRead()&&filepath.canWrite()){
                BaseName= getExternalStorageDirectory().getAbsolutePath();
            }else {
                Log.e(Tag,"内存卡错误====>"+BaseName);
            }

        }else{
            BaseName=getFilesDir().getAbsolutePath();
        }

        //sourceFileName=BaseName+FileName+".doc";
        sourceFileName=FileName+".doc";
       // sourceFileName=FileName+".docx";
        targetFileName=BaseName+ File.separator+FileName+".html";
        PoiTool poi=new PoiTool(MainActivity.this);
        Log.e(Tag,"targetFileName====>"+targetFileName);
        poi.docToHtml(sourceFileName,targetFileName);
        //poi.docToHtml(sourceFileName,targetFileName,FileName+"html");
        //poi.docxToHtml(sourceFileName,targetFileName);


        /*********************************poi*********************************************/
       /* ViewFile viewFile=new ViewFile(FileName);
        viewFile.getRange();
        viewFile.makeFile();
        viewFile.readAndWrite();*/

        File targetFile=new File(sourceFileName);
        if(targetFile.exists()){
            Log.e(Tag,"targetFileName====>"+targetFileName+" is exists!");
            webView.loadUrl("file://"+targetFileName);
        }
    }
}
