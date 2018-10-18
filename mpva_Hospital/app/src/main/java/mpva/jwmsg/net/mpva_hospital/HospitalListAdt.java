package mpva.jwmsg.net.mpva_hospital;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HospitalListAdt extends BaseAdapter {

    Context context;
    String jsondata = "";
    JSONArray jary = null;
    LayoutInflater Lif;

    public HospitalListAdt(Context appcontext, String jsondata)
    {
        this.jsondata = jsondata;
        this.context = appcontext;
        try {
            this.jary = new JSONArray(jsondata);
        } catch (JSONException e) {
            JSONObject job = new JSONObject();
            try {
                job.put("Name","일시적인 오류로 서비스 이용이 불가합니다.");
                job.put("AddrDetail","Error");
            } catch (JSONException e1) {

            }
        }
        Lif = (LayoutInflater.from(appcontext));
    }

    @Override
    public int getCount() {
        return jary.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        final Context context = viewGroup.getContext();

        view = Lif.inflate(R.layout.hospital_list_item, null);
        TextView HosName = (TextView) view.findViewById(R.id.hospitalName);
        TextView HosAddr = (TextView) view.findViewById(R.id.hospitalAddress);
        TextView HosTel = (TextView) view.findViewById(R.id.TelNum);
        JSONObject job = null;
        try {
            job = (JSONObject)jary.get(i);
            HosName.setText(job.getString("Name"));
            HosAddr.setText(job.getString("AddrDetail"));
            HosTel.setText(job.getString("Tel"));
        } catch (JSONException e) {
            HosName.setText("일시적인 오류로 서비스 이용이 불가합니다.");
            HosAddr.setText("Error");
            HosTel.setText("-");
            return view;
        }
//        HosName.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(context, pos + "번째 제목 선택", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );




        return view;
    }


}
