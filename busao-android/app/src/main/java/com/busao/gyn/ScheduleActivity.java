package com.busao.gyn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ScheduleActivity extends AppCompatActivity {

    private FrameLayout mWebViewContainer;
    private WebView mScheduleWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer code = getIntent().getIntExtra("code", 0);
                new FetchScheduleAsyncTask().execute(code);
            }
        });

        mWebViewContainer = (FrameLayout) findViewById(R.id.webViewContainer);
        mWebViewContainer.addView((mScheduleWebView = new WebView(getApplicationContext())));

        Integer code = getIntent().getIntExtra("code", 0);
        new FetchScheduleAsyncTask().execute(code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebViewContainer.removeAllViews();
        mScheduleWebView.destroy();
    }

    private class FetchScheduleAsyncTask extends AsyncTask<Integer,Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                Document doc = Jsoup.connect("http://m.rmtcgoiania.com.br/horariodeviagem/visualizar/ponto/" + params[0])
                        .userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:47.0) Gecko/20100101 Firefox/47.0")
                        .get();
                Elements elem = doc.select(".table.table-striped.subtab-previsoes");
                return elem.outerHtml();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><style>table{border-collapse:collapse;box-shadow: 0 0 3px #bbb;}table,th,td{border:1px solid #bbb;font-family:Roboto,Verdana,Arial;font-size:11pt;padding-top:8px;padding-bottom:8px;padding-right:4px;text-align:right;}</style><center>" + s + "</center>";
            mScheduleWebView.loadData(data,"text/html; charset=utf-8","UTF-8");
        }
    }

}
