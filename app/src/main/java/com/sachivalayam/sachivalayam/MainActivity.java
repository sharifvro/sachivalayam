package com.sachivalayam.sachivalayam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.sachivalayam.sachivalayam.download.WebDownloader;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    private WebDownloader webDownloader;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            IntentHandler handler = new IntentHandler(MainActivity.this);
            if (url.startsWith("intent://")) {
                try {
                    Intent intent = Intent.parseUri(url, IntentHandler.type);

                    if (intent != null) {
                        handler.startIntent(view, intent);
                        return true;
                    }
                } catch (URISyntaxException e) {
                    Toast.makeText(MainActivity.this, "Unable to open", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
         }
        });
        webView.setWebChromeClient(new FullScreenClient(MainActivity.this){
            @Override
            public void onHideCustomView() {
                hideFullScreen(webView);
            }

            @Override
            public Bitmap getDefaultVideoPoster() {
                return videoBitmap();
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                showFullScreen(view,callback);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://sachivalayam.com/");

        webDownloader = new WebDownloader(MainActivity.this);
        webDownloader.setTo(webView);
        webDownloader.setDownloadListener(new WebDownloader.StateListener() {
            @Override
            public void onDownloadStart(String url, String mimeType, String filename) {
                Toast.makeText(MainActivity.this, "Download has started", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDownloadFailed(String errorMessage) {
                Toast.makeText(MainActivity.this, "errorMessage", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WebDownloader.downloadCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                webDownloader.startDownload(webDownloader.sURL, webDownloader.sUA, webDownloader.sFileName, webDownloader.sMM);
            }
        }
    }
        @Override
        public void onBackPressed() {
            if (webView.canGoBack())
                webView.goBack();
            else
                finish();
    }
}