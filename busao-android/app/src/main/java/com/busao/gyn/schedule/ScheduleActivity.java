package com.busao.gyn.schedule;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.busao.gyn.R;
import com.busao.gyn.data.stop.BusStopWithLines;
import com.busao.gyn.util.FormatsUtils;

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleActivity.this.finish();
            }
        });

        final BusStopWithLines stop = (BusStopWithLines) getIntent().getSerializableExtra("stop");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchScheduleAsyncTask().execute(stop);
            }
        });

        mWebViewContainer = (FrameLayout) findViewById(R.id.webViewContainer);
        mWebViewContainer.addView((mScheduleWebView = new WebView(getApplicationContext())));

        TextView stopNumber = (TextView) findViewById(R.id.stopNumber);
        TextView districtName = (TextView) findViewById(R.id.districtName);
        TextView stopDescription = (TextView) findViewById(R.id.stopDescription);
        TextView linesAvailable = (TextView) findViewById(R.id.linesAvailable);

        stopNumber.setText(FormatsUtils.formatBusStop(stop.getStop().getCode()));
        districtName.setText(stop.getStop().getAddress());
        stopDescription.setText(stop.getStop().getReference());
        linesAvailable.setText(stop.getFormatedLines());

        new FetchScheduleAsyncTask().execute(stop);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebViewContainer.removeAllViews();
        mScheduleWebView.destroy();
    }

    private class FetchScheduleAsyncTask extends AsyncTask<BusStopWithLines,Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mScheduleWebView.loadData("<?xml version=\"1.0\" encoding=\"UTF-8\" ?> <b style=\"position: absolute;top: 50%;left: 50%;transform: translateX(-50%) translateY(-50%);\">Aguarde...</b>","text/html; charset=utf-8","UTF-8");
        }

        @Override
        protected String doInBackground(BusStopWithLines... params) {
            try {
                BusStopWithLines stop = params[0];
                Document doc = Jsoup.connect("http://m.rmtcgoiania.com.br/horariodeviagem/visualizar/ponto/" + stop.getStop().getCode())
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
