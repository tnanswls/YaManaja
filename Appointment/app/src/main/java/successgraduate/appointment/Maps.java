package successgraduate.appointment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleApiClient mGoogleApiClient = null;

    private GoogleMap mMap = null;

    private Marker currentMarker = null;
    Button geoLocationBt;
    Button IntentBt;
    Button clear;
    LocationManager locationManager;
    Button btnClose;

    private FragmentActivity mActivity;
    boolean askPermissionOnceAgain = false;
    private ProgressDialog progressDialog;
    private String errorString = "";
    private Button searchBt;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_maps);
        mActivity = this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toast.makeText(getApplicationContext(), "위치를 찾는 중입니다", Toast.LENGTH_SHORT).show();
//-------------------------------------------------------------------------------------
//현재 위치 찾기
//---------------------------------------------------------------------------------------
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); //위치관리자 객체 얻기
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        //check the network provider is enabled
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) { //위치 업데이트 값
                    //get the latitude
                    double latitude = location.getLatitude(); //위도
                    //get the longitude
                    double longitude = location.getLongitude(); //경도
                    //instantiate the class, LatLng
                    LatLng latLng = new LatLng(latitude, longitude);
                    //Instantiate the class, Geocoder
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1); //지역이름, 읽을 개수
                        String str = addressList.get(0).getLocality() + ",";
                        str += addressList.get(0).getCountryName();
                        Marker addMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(str).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.bluemarker)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }
                @Override
                public void onProviderEnabled(String provider) {

                    Log.d("test", "onProviderEnabled, provider:" + provider);
                }
                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //get the latitude
                    double latitude = location.getLatitude();
                    //get the longitude
                    double longitude = location.getLongitude();
                    //instantiate the class, LatLng
                    LatLng latLng = new LatLng(latitude, longitude);
                    //Instantiate the class, Geocoder
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getLocality() + ",";
                        str += addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.bluemarker)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
        //-----------------------검색쓰
        geoLocationBt = (Button) findViewById(R.id.btSearch);
        geoLocationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchText = (EditText) findViewById(R.id.etLocationEntry);
                searchText.setHint("목적지를 입력하세요.");
                final String search = searchText.getText().toString();
                if (search != null && !search.equals("")) {
                    List<Address> addressList = null;
                    Geocoder geocoder = new Geocoder(Maps.this);

                    try {
                        addressList = geocoder.getFromLocationName(search, 5);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final Address address = addressList.get(0);
                    String str = addressList.get(0).getLocality() + ",";
                    str += addressList.get(0).getCountryName();
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(str).
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.bluemarker)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    //고쓰
                    Button button1 = (Button) findViewById(R.id.button1);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i3 = new Intent(Intent.ACTION_VIEW
                                    ,Uri.parse("geo:"+ address.getLatitude()  +"," + address.getLongitude()));//geo:위도,경도
                            startActivity(i3);
                        }
                    });

                }
            }
        });
        clear = (Button) findViewById(R.id.btClear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
            }
        });

    }
//--------------------------길찾기1

    public String getUrl(String dep_src, String dep_dst, String dest_src, String dest_dst) {
        StringBuilder urlString = new StringBuilder();

        urlString.append("http://maps.google.com/maps/api/directions/json?origin=");
        urlString.append(dep_src);
        urlString.append(",");
        urlString.append(dep_dst);
        urlString.append("&destination="); // to
        urlString.append(dest_src);
        urlString.append(",");
        urlString.append(dest_dst);
        urlString.append("&mode=transit&key=AIzaSyCpCz0wgKOaqLVbluK0xXCSq5Cmo7GkW-0");

        Log.i("urlString", urlString.toString());

        return urlString.toString();
    }
    @Override

    public void onMapReady(GoogleMap googlemap) {
        //  Log.d(TAG, "onMapReady");
        mMap = googlemap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}