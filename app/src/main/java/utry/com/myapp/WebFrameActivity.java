package utry.com.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;
import java.io.IOException;

public class WebFrameActivity extends AppCompatActivity {

    private WebView webView;

    private Uri fileFullPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_frame);

        webView = findViewById(R.id.webFrame);

        WebSettings webSettings = webView.getSettings();

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.loadUrl("file:///android_asset/web/index.html");

        System.out.println(Build.MODEL);
        System.out.println(Build.VERSION.SDK_INT);
        System.out.println(Build.VERSION.RELEASE);

        findViewById(R.id.callJsButton).setOnClickListener(v -> {

//            webView.post(() -> webView.loadUrl("javascript:hello()"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.post(() -> webView.evaluateJavascript("javascript:sum(1,2)", value -> {
                    System.out.println("==========================" +value);
                }));
            }

        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(WebFrameActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm());
                b.setCancelable(false);
                b.create().show();
                return true;
            }
        });
        webView.addJavascriptInterface(new Object(){

            @JavascriptInterface
            public String hello(){
                openCamera();
                return "hello world";
            }

        },"androidTest");
    }

    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, "myWebPhoto");
        intent.putExtra(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");


        File file = new File(getExternalCacheDir(),"myWebPhoto.jpg");

        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileFullPath = Build.VERSION.SDK_INT >= 24 ?
                FileProvider.getUriForFile(WebFrameActivity.this, "com.demo.zzz.fileProvider", file) :
                Uri.fromFile(file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileFullPath);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (10 == requestCode) {
            System.out.println("============" + fileFullPath);
            webView.post(() -> {
               webView.loadUrl("javascript:render(\""+ fileFullPath.toString() +"\")");
            });
        }
    }
}