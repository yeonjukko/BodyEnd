package net.yeonjukko.bodyend.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import net.yeonjukko.bodyend.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class BulletinActivity extends AppCompatActivity {

    String html=null;
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin);
        mWebView = (WebView) findViewById(R.id.webview);
        setLayout();
        ImageButton ibExit = (ImageButton) findViewById(R.id.ib_exit);
        // 웹뷰에서 자바스크립트실행가능


        //나가기
        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setLayout() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {

                    //doc = Jsoup.connect("https://docs.google.com/document/d/1WICIWXApZe13sVN_92B-eVfTLUxA7D6Yahn2pPeQL-8/mobilebasic?pli=1").get();
                    doc = Jsoup.connect("https://docs.google.com/document/d/1WICIWXApZe13sVN_92B-eVfTLUxA7D6Yahn2pPeQL-8/edit?usp=sharing").get();

                    html = doc.html();

                    //원래 스타일
                    String style = doc.select("style").last().data();

                    //바뀐 스타일
                    String style2 = doc.select("style").last().data();
                    style2 = style2.replace("docs-ml-header{background:#fafafa;","docs-ml-header{background:#fafafa;display:none;");
                    html = html.replace(style,style2);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                           // mWebView.getSettings().setJavaScriptEnabled(true);
                            mWebView.loadData(html, "text/html; charset=utf-8", "UTF-8");
                            //mWebView.setWebChromeClient(new WebChromeClient());
                            mWebView.setWebViewClient(new myWebViewClient());
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();



    }
    private class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
