package com.example.up.common;
import android.os.AsyncTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
public class GetQuery {
    public interface Inter {
        void returner(String str);
    }
    public static String url;

    public GetQuery(String url) {
        GetQuery.url = url;
    }


    public static class GetQueryJsoup extends AsyncTask<Inter, Void, Void> {
        String json = "";
        Inter inter;

        @Override
        protected Void doInBackground(Inter... inters) {
            Document doc = null;
            inter = inters[0];

            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            json = doc.text();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            inter.returner(json);
        }
    }
}
