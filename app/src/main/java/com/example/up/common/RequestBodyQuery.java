package com.example.up.common;
import android.os.AsyncTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
public class RequestBodyQuery {
    public static String url;
    public static String requestBody;

    public interface RequestBodyQueryInter {
        void RequestBodyQueryReturner(String str);
    }

    public RequestBodyQuery(String url, String requestBody) {
        RequestBodyQuery.url = url;
        RequestBodyQuery.requestBody = requestBody;
    }


    public static class PostQueryJsoup extends AsyncTask<RequestBodyQueryInter, Void, Void> {
        String json = "";
        RequestBodyQueryInter inter;

        @Override
        protected Void doInBackground(RequestBodyQueryInter... inters) {
            inter = inters[0];
            Document doc = null;

            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla")
                        .header("content-type", "application/json")
                        .header("accept", "application/json")
                        .requestBody(requestBody)
                        .post();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(doc != null)
                json = doc.text();

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            inter.RequestBodyQueryReturner(json);
        }
    }
}
