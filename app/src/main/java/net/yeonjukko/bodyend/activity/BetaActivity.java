package net.yeonjukko.bodyend.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import net.yeonjukko.bodyend.R;

public class BetaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beta);
        setLayout();

    }

    public void setLayout() {
        WebView mWebView = (WebView) findViewById(R.id.webview);
        ImageButton ibExit = (ImageButton) findViewById(R.id.ib_exit);
        // 웹뷰에서 자바스크립트실행가능
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://goo.gl/forms/OgUwmPaC1W");
        mWebView.setWebChromeClient(new WebChromeClient());

        //나가기
        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
