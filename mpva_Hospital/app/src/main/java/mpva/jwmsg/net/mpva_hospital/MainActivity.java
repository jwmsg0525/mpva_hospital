package mpva.jwmsg.net.mpva_hospital;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String lattitude, longitude, address;
    boolean locationFlag = false;
    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(this,splash_activity.class));


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        findViewById(R.id.webViews).setVisibility(View.INVISIBLE);

        BottomNavigationView navigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(navigationListener);

         onCreateList();

        //navigationListener.onNavigationItemSelected();
        findViewById(R.id.btn_refsh).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {onCreateList();
                    }
                });
        findViewById(R.id.btn_selLoc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {CreateLocationSelect();
            }
        });
        ListView listView = findViewById(R.id.List_Hospital);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView HosName = (TextView) view.findViewById(R.id.hospitalName);
                final TextView HosAddr = (TextView) view.findViewById(R.id.hospitalAddress);
                final TextView HosTel = (TextView) view.findViewById(R.id.TelNum);
                final View v = view;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(HosName.getText())        // 제목 설정
                        .setMessage("Tel : " + HosTel.getText() + "\n주소 :" + HosAddr.getText())        // 메세지 설정
                        .setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("길안내", new DialogInterface.OnClickListener() {
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                if(locationFlag)
                                    intent.setData(Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + address + "&destination=" + HosAddr.getText() + "&travelmode=transit"));
                                else
                                    intent.setData(Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + "&destination=" + HosAddr.getText() + "&travelmode=transit"));
                                try {
                                    v.getContext().startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("전화걸기", new DialogInterface.OnClickListener() {
                            // 취소 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + HosTel.getText()));
                                try {
                                    v.getContext().startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                dialog.cancel();
                            }

                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            WebView myWebView = (WebView) findViewById(R.id.webViews);
            switch (item.getItemId()) {
                case R.id.searchHos:
                    findViewById(R.id.webViews).setVisibility(View.INVISIBLE);
                    findViewById(R.id.List_Hospital).setVisibility(View.VISIBLE);
                    return true;
                case R.id.introduce:
                    findViewById(R.id.List_Hospital).setVisibility(View.INVISIBLE);
                    findViewById(R.id.webViews).setVisibility(View.VISIBLE);
                    myWebView.loadUrl("http://jwmsg.kr/mpva/introduce.html");
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://jwmsg.kr/mpva/introduce.html")));
                    return true;
                case R.id.developers:
                    findViewById(R.id.List_Hospital).setVisibility(View.INVISIBLE);
                    findViewById(R.id.webViews).setVisibility(View.VISIBLE);
                    myWebView.loadUrl("http://jwmsg.kr/mpva/developers.html");
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mpva.go.kr/")));
                    return true;
            }
            return false;
        }


    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       if(id == R.id.mpva_site)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mpva.go.kr/")));
        }

        return super.onOptionsItemSelected(item);
    }




    public boolean locationInit(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            return false;

        } else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            return true;
        }
        return false;
    }

    public boolean getlocation(boolean init)
    {
        if(!init)
            return init;
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            return false;
        }else{
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null)
            {
                double latt = location.getLatitude();
                double longi = location.getLongitude();
                lat = latt;
                lng = longi;
                lattitude = String.valueOf(latt);
                longitude = String.valueOf(longi);
                return true;

            }else{
                return false;
            }

        }

    }

    public String getAddress(Context mContext, double lat, double lng)
    {
        String result = "위치 확인 불가";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> addr;
        try {
            if(geocoder != null)
            {
                addr = geocoder.getFromLocation(lat,lng,1);
                if(addr != null && addr.size() > 0)
                {
                    String currentAddress = addr.get(0).getAddressLine(0).toString();
                    result = currentAddress;
                }
            }
        }catch (IOException e){
            result = "주소를 불러올 수 없습니다.";
        }
        return result;
    }

    public void CreateLocationSelect(){
        List<String>citylist = new ArrayList<String>();
        citylist.add("강원도");
        citylist.add("경기도");
        citylist.add("경상남도");
        citylist.add("경상북도");
        citylist.add("전라남도");
        citylist.add("전라북도");
        citylist.add("충청남도");
        citylist.add("충청북도");
        citylist.add("서울특별시");
        citylist.add("인천광역시");
        citylist.add("대전광역시");
        citylist.add("대구광역시");
        citylist.add("광주광역시");
        citylist.add("울산광역시");
        citylist.add("부산광역시");
        citylist.add("세종특별자치시");
        citylist.add("제주특별자치도");
        ListView listhos = (ListView) findViewById(R.id.List_Hospital);
        final TextView locat = (TextView) findViewById(R.id.CurrentLoc);
        final CharSequence[] Cityarray = citylist.toArray(new String[citylist.size()]);
        final HospitalViewing netService = new HospitalViewing(this,listhos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("지역선택")
                .setItems(Cityarray,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                String selectedText = Cityarray[item].toString();  //Selected item in listview
                                address = ("대한민국 "+selectedText);
                                locat.setText(address);
                                netService.execute(selectedText);
                            }
                        });
        builder.create().show();

    }

    public boolean onCreateList(){

        final TextView locat = (TextView) findViewById(R.id.CurrentLoc);
        ListView listhos = (ListView) findViewById(R.id.List_Hospital);
        final HospitalViewing netService = new HospitalViewing(this,listhos);
        String city  = "서울특별시";
        if(getlocation(locationInit())){
            address = getAddress(this,lat,lng);
            locat.setText(address);
            city = URLEncoder.encode( address.split(" ")[1] );
            locationFlag = true;
            netService.execute(city);
        }else {
            address = "대한민국 서울특별시";
            Toast.makeText(getApplicationContext(), "위치를 확인할 수 없습니다.\n지역을 선택해주세요.", Toast.LENGTH_LONG).show();
            CreateLocationSelect();
            locat.setText(address);
        }

    return true;
    }


}

