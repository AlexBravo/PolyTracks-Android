package com.example.mrfir.roughdraft;

        import android.content.Context;
        import android.content.Intent;
        import android.location.Location;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.util.Log;
        import android.view.View;
        import android.support.design.widget.NavigationView;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuItem;

        import com.google.android.gms.common.api.Response;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;

        import kaaes.spotify.webapi.android.SpotifyApi;
        import kaaes.spotify.webapi.android.SpotifyCallback;
        import kaaes.spotify.webapi.android.SpotifyError;
        import kaaes.spotify.webapi.android.SpotifyService;
        import kaaes.spotify.webapi.android.models.Album;
        import kaaes.spotify.webapi.android.models.Pager;
        import kaaes.spotify.webapi.android.models.SavedTrack;
        import kaaes.spotify.webapi.android.models.UserPrivate;
        import retrofit.Callback;
        import retrofit.RetrofitError;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {


    private GPSTracker gpsTracker;
    private Location mLocation;
    private double longitude,latitude;
    private GoogleMap mMap;
    private FloatingActionButton backToCurrentLocationFAB;
    static final int LOGIN_REQUEST = 1;
    static final String EXTRA_TOKEN = "EXTRA_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        backToCurrentLocationFAB = (FloatingActionButton) findViewById(R.id.backToCurrentLocationFAB);
        backToCurrentLocationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetCurrentLocation();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.Intromap);
        mapFragment.getMapAsync(this);


        gpsTracker = new GPSTracker(getApplicationContext());
        mLocation = gpsTracker.getLocation();
        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();



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
        //getMenuInflater().inflate(R.menu.activity_main, menu);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_camera) {
            //TODO:WHERE THE INTENT IS CONTRIBUTIONS ON NAV NEEDS TO BE CLICKED (CAN CHANGE TO DIALOG BOX FOR ACTUAL USE)
            Intent intent = new Intent(this,LoginActivity.class);
            startActivityForResult(intent,LOGIN_REQUEST);

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this,NearbyPlaylist.class);
            startActivityForResult(intent,LOGIN_REQUEST);
        } else if (id == R.id.nav_slideshow) {


        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }
    public void onMapReady(GoogleMap googleMap){

        mMap = googleMap;
        LatLng currentLocation = new LatLng(latitude, longitude);
        MarkerOptions currentMarket = new MarkerOptions().position(currentLocation).title("Current Location");
        mMap.addMarker(currentMarket);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.setMinZoomPreference(18.0f);
        mMap.setMaxZoomPreference(19.0f);



    }

    public void SetCurrentLocation(){

        gpsTracker = new GPSTracker(getApplicationContext());
        mLocation = gpsTracker.getLocation();
        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();
        LatLng mCurrentLocation = new LatLng(latitude, longitude);
        MarkerOptions currentMarket = new MarkerOptions().position(mCurrentLocation).title("Current Location");
        mMap.clear();
        mMap.addMarker(currentMarket);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(mCurrentLocation));
        //CredentialsHandler currentLocation = null;
        //currentLocation.setLocation(this ,(long)longitude, (long)latitude);

        //Test code to see how to use the objects

//        spotify.getAlbum("76290XdXVF9rPzGdNRWdCh", new Callback<Album>() {
//
//            public void success(Album album, Response response) {
//                Log.d("Album success", album.name);
//            }
//
//            @Override
//            public void success(Album album, retrofit.client.Response response) {
//                Log.d("Album success", album.name);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.d("Album failure", error.toString());
//            }
//        });



    }


}