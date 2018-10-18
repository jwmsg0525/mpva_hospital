package mpva.jwmsg.net.mpva_hospital;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HospitalViewing extends AsyncTask<String,String,String>{
    Context context;
    ListView listView;
    String jsonData = "[]";
    public HospitalViewing(Context context, ListView listView){
        this.context = context;
        this.listView = listView;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... pararms) {
        String result = onHTTPREQ(pararms[0]);
        jsonData = result;
        publishProgress(pararms);
        return result;
    }
    @Override
    protected void onProgressUpdate(String... values) {

    }

    @Override
    protected void onPostExecute(String s) {
        HospitalListAdt hadt = new HospitalListAdt(context,s);
        listView.setAdapter(hadt);
    }

    public String onHTTPREQ(String param){
        String result = "error";
        try {

            URL url = new URL("http://jwmsg.kr/mpva/API.jsp?mode=1&param="+param);
            HttpURLConnection cnn = (HttpURLConnection) url.openConnection();
            cnn.setRequestMethod("GET");
            cnn.setRequestProperty("User-Agent", "mpva-app");
            cnn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            cnn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");
            if (cnn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return "ERROR";

            BufferedReader reader = new BufferedReader(new InputStreamReader(cnn.getInputStream(), "UTF-8"));
            String page = "";
            String tmp;
            while ((tmp = reader.readLine()) != null){
                page += tmp;
            }
            result = page;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getJsonData() {
        return jsonData;
    }
}
