package com.example.alexconstantin.jrs;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    private static Context context;
    private GoogleMap mMap;

    private String TAG = MainActivity.class.getSimpleName();
    private static String languagesUrl = "http://www.jrsdr.robertoderesu.com/api/languages";
    private static String objectiveTypesUrl = "http://www.jrsdr.robertoderesu.com/api/objectiveTypes";
    private static String objectivesUrl = "http://www.jrsdr.robertoderesu.com/api/objectives";
    private Spinner languagesView;
    NavigationView navigationView;
    String[] spinnerArray;
    HashMap<Integer,String> spinnerMap = new HashMap<Integer, String>();
    Integer[] objectiveTypesIdArray;
    String[] objectiveTypesNameArray;
    Menu menu;
    Obiectiv[] objectives;

    GetObjectiveTypes chooseObjectiveType;
    GetLanguages getLanguages;
    GetObjectives getObjectives;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        languagesView = (Spinner) navigationView.getMenu().findItem(R.id.navigation_language).getActionView();
        menu = navigationView.getMenu();

        getLanguages = new GetLanguages();
        chooseObjectiveType = new GetObjectiveTypes();
        getObjectives = new GetObjectives();

        getLanguages.execute();
        chooseObjectiveType.execute();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getObjectives.execute();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker arg0) {
              //  Toast.makeText(MainActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                Integer objectiveId = Integer.parseInt(arg0.getSnippet());
                String objectiveTitle = "";
                String objectiveType = "";

                for (int i = 0; i < objectives.length; i++)
                {
                    if (objectives[i].getId() == objectiveId)
                    {
                        objectiveTitle = objectives[i].getName();
                        objectiveType = objectives[i].getTypeName();
                        break;
                    }
                }

                Intent intent = new Intent(MainActivity.this, ObjectiveActivity.class);
                Bundle b = new Bundle();
                b.putInt("objectiveId", objectiveId);
                b.putString("objectiveTitle", objectiveTitle);
                b.putString("objectiveType", objectiveType);
                intent.putExtras(b);
                startActivity(intent);
                return true;
            }
        });

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(42.5963,26.1743));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(2);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else
            {
                //Request Location Permission
                checkLocationPermission();
            }
        } else
        {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
/*        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(30000);*/
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            Log.println(Log.DEBUG, "onConnected ", "point 0");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.println(Log.DEBUG, "onConnected ", "point 1");

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    class GetLanguages extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(languagesUrl, null);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray contacts = jsonObj.getJSONArray("languages");
                    spinnerArray = new String[contacts.length()];
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String key = c.getString("key");
                        String name = c.getString("name");
                        spinnerMap.put(i, id);
                        spinnerArray[i] = name;
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            languagesView.setAdapter(adapter);

            if (sharedpreferences.getString("languageId", "") == "") {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("languageId", spinnerMap.get(0));
                editor.commit();
                languagesView.setSelection(0);
            } else {
                Integer languageId = Integer.parseInt(sharedpreferences.getString("languageId", ""));
                for (HashMap.Entry<Integer, String> entry : spinnerMap.entrySet()) {
                    if (entry.getValue() == languageId.toString()) {
                        languagesView.setSelection(entry.getKey());
                        break;
                    }
                }
            }
            languagesView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                Boolean first = true;
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    for (HashMap.Entry<Integer, String> entry : spinnerMap.entrySet()) {
                        if (entry.getKey() == position) {
                            //code here
                            String languageId = entry.getValue();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("languageId", languageId);
                            editor.commit();
                            break;
                        }
                    }
                    if (first == true) {
                        first = false;
                    }
                    else {
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }

                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });
        }

    }

    class GetObjectiveTypes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            Integer languageId = 1;
            try
            {
                languageId = Integer.parseInt(sharedpreferences.getString("languageId", ""));
            }
            catch (Exception e)
            {

            }
            Limba request = new Limba();
            request.setLanguageId(languageId);
            String jsonStr = sh.makeServiceCall(objectiveTypesUrl, request);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray objectiveTypes = jsonObj.getJSONArray("objectiveTypes");
                    objectiveTypesIdArray = new Integer[objectiveTypes.length()];
                    objectiveTypesNameArray = new String[objectiveTypes.length()];

                    for (int i = 0; i < objectiveTypes.length(); i++) {
                        JSONObject c = objectiveTypes.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");
                        objectiveTypesIdArray[i] = Integer.parseInt(id);
                        objectiveTypesNameArray[i] = name;
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            for (int i = 0; i < objectiveTypesIdArray.length; i++) {
                CheckBox checkBox = new CheckBox(context);
                checkBox.setChecked(true);
                checkBox.setId(objectiveTypesIdArray[i]);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        CheckBox myCheckBox = (CheckBox)buttonView;
                        for (int i = 0; i < objectives.length; i++) {
                            if (objectives[i].getTypeId() == myCheckBox.getId()) {
                                objectives[i].getMarker().setVisible(isChecked);
                            }
                        }
                    }
                });

                menu.add(Menu.NONE, 1, Menu.NONE, objectiveTypesNameArray[i]).setActionView(checkBox);
            }
        }

    }

    class GetObjectives extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            Integer languageId = 1;
            try
            {
                languageId = Integer.parseInt(sharedpreferences.getString("languageId", ""));
            }
            catch (Exception e)
            {

            }
            RequestObiective request = new RequestObiective();
            request.setxLeftTop(0.0);
            request.setyLeftTop(0.0);
            request.setxRightBottom(1000.0);
            request.setyRightBottom(1000.0);
            request.setLanguageId(languageId);

            List<Integer> res = new ArrayList<Integer>();;
            for (int i = 0; i < objectiveTypesIdArray.length; i++)
            {
                res.add(objectiveTypesIdArray[i]);
            }
            request.setFilteredTypes(res);

            String jsonStr = sh.makeServiceCall(objectivesUrl, request);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray objectivesArray = jsonObj.getJSONArray("objectives");
                    objectives = new Obiectiv[objectivesArray.length()];
                    // looping through All Contacts
                    for (int i = 0; i < objectivesArray.length(); i++) {
                        JSONObject c = objectivesArray.getJSONObject(i);
                        Obiectiv obj = new Obiectiv();

                        String id = c.getString("id");
                        String name = c.getString("name");

                        JSONObject location = c.getJSONObject("location");
                        double coordx=location.getDouble("x");
                        double coordy=location.getDouble("y");

                        JSONObject type = c.getJSONObject("type");
                        String typeId=type.getString("id");
                        String typeName=type.getString("name");

                        obj.setId(Integer.parseInt(id));
                        obj.setName(name);
                        obj.setTypeId(Integer.parseInt(typeId));
                        obj.setTypeName(typeName);
                        obj.setX(coordx);
                        obj.setY(coordy);
                        objectives[i] = obj;
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            for (int i = 0; i < objectives.length; i++) {
                objectives[i].setMarker(createMarker(objectives[i].getX(), objectives[i].getY(), objectives[i].getName(), String.valueOf(objectives[i].getId())));
            }
        }

    }

    public Marker createMarker(double latitude,double longitude, String title, String id)
    {
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(title);
        markerOptions.snippet(id);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        Marker newMarker = mMap.addMarker(markerOptions);
        return newMarker;
    }

    //
    @Override
    public void onLocationChanged(Location location)
    {

        mLastLocation = location;
        if (mCurrLocationMarker != null)
        {
            mCurrLocationMarker.remove();
        }

/*        LatLng latLng = new LatLng(44.431819,26.101456);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11))*/;

    }

    private void checkLocationPermission()
    {

        Log.println(Log.DEBUG, "checkLocationP ", "point 0");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {

        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)
                    {

                        if (mGoogleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else
                {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    //--
}
