package com.example.mrfir.roughdraft;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Playlist;
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
    LatLng currentLocation;

    LatLng bScience = new LatLng(34.058795, -117.824443);
    LatLng bEngineering = new LatLng(34.058795, -117.824443);
    LatLng bLetArtSoc = new LatLng(34.057894, -117.824147);
    LatLng bEnvDes = new LatLng(34.057223, -117.827178);
    LatLng bEduInt = new LatLng(34.059582, -117.821413);
    LatLng bLib = new LatLng(34.057512, -117.821598);
    LatLng bMusic = new LatLng(34.056875, -117.822330);
    LatLng bBSC = new LatLng(34.056614, -117.821269);


    SpotifyService spotify;
    ListView list;
    CustomListAdapter adapter;
    ArrayList<String> PlaylistTrackURI;
    String Credentials = "";
    String privateUser;
    UserPrivate tUserPrivate;
    ArrayList<String> imageURL = new ArrayList();
    ArrayList<String> trackNames = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        setTheme(R.style.Mustard);
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


        //Instantiate spotify crap, and get access token
        SpotifyApi api = new SpotifyApi();
        Credentials = CredentialsHandler.getToken(this);
        api.setAccessToken(CredentialsHandler.getToken(this));
        spotify = api.getService();

        spotify.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, retrofit.client.Response response) {
                tUserPrivate = userPrivate;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });




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
        currentLocation = new LatLng(latitude, longitude);


        reloadMarkers();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

        mMap.setMinZoomPreference(16.0f);
        mMap.setMaxZoomPreference(19.0f);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng test = marker.getPosition();
                //Using position get Value from arraylist
                if(test.equals(bScience)){
                    retrievePlaylist("729LhyVuS3ZRh9n0fGCe0e");
                    return true;
                }else if (test.equals(bMusic)){
                    retrievePlaylist("2ENtqwOp8pX9LI5P4DPZoz");
                    return true;
                }else if (test.equals(bBSC)){
                    retrievePlaylist("7dg0GFmFAGqaTaiBXYUQSv");
                    return true;
                }else if (test.equals(bEduInt)){
                    retrievePlaylist("35X5FarJDWxCwGIWruIAJv");
                    return true;
                } else if(test.equals(bEngineering)){
                    retrievePlaylist("4Fm5pSHlWbkUMdDthDPLa7");
                    return true;
                }else if (test.equals(bLib)){
                    retrievePlaylist("3BffdmUlBsqQNBqG8FbhBN");
                    return true;
                }else if (test.equals(bLetArtSoc)){
                    retrievePlaylist("6usOvLJjqW6FiVKrrBXZ5j");
                    return true;
                }else if (test.equals(bEnvDes)){
                    retrievePlaylist("35X5FarJDWxCwGIWruIAJv");
                    return true;
                }
                return false;
            }
        });


    }

    public void SetCurrentLocation(){

        gpsTracker = new GPSTracker(getApplicationContext());
        mLocation = gpsTracker.getLocation();
        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();
        LatLng mCurrentLocation = new LatLng(latitude, longitude);
        MarkerOptions currentMarket = new MarkerOptions().position(mCurrentLocation).title("Current Location");
        mMap.clear();
        reloadMarkers();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(mCurrentLocation));





    }

    public void reloadMarkers(){


        MarkerOptions currentMarket = new MarkerOptions().position(currentLocation).title("Current Location");
        MarkerOptions scienceBuilding = new MarkerOptions().position(bScience).title("Science Building");
        MarkerOptions engineeringBuilding = new MarkerOptions().position(bEngineering).title("Engineering Building");
        MarkerOptions letArtSocBuilding = new MarkerOptions().position(bLetArtSoc).title("Letters, Art, Social Science Building");
        MarkerOptions envDesignBuilding = new MarkerOptions().position(bEnvDes).title("Environmental Design Building ");
        MarkerOptions eduIntBuilding = new MarkerOptions().position(bEduInt).title("Education and Integrative Building");
        MarkerOptions libBuilding = new MarkerOptions().position(bLib).title("Library Building");
        MarkerOptions musicBuilding = new MarkerOptions().position(bMusic).title("Music Building");
        MarkerOptions bscBuilding = new MarkerOptions().position(bBSC).title("BSC Building");





        mMap.addMarker(currentMarket);
        mMap.addMarker(scienceBuilding);
        mMap.addMarker(engineeringBuilding);
        mMap.addMarker(letArtSocBuilding);
        mMap.addMarker(envDesignBuilding);
        mMap.addMarker(eduIntBuilding);
        mMap.addMarker(libBuilding);
        mMap.addMarker(musicBuilding);
        mMap.addMarker(bscBuilding);


    }

    public ArrayList<String> getTrackURIFromPlaylist(Playlist playlist){
        ArrayList<String> PlaylistTrackURI = new ArrayList();


        for(int i = 0 ; i < playlist.tracks.items.size(); i++){
            PlaylistTrackURI.add(playlist.tracks.items.get(i).track.id) ;
        }
        return PlaylistTrackURI;


    }
    public void retrievePlaylist(final String playlistURI){
        spotify.getPlaylist(tUserPrivate.id, playlistURI, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, retrofit.client.Response response) {
                Test(playlist);
                Intent intent = new Intent(MainActivity.this, PlaylistDetail.class);


                //Transfer over data needed for next activity
                intent.putExtra("playlistName", playlist.name);
                intent.putExtra("playlistTrackURI", getTrackURIFromPlaylist(playlist));
                intent.putExtra("Credentials", Credentials);
                intent.putExtra("PlaylistUri", playlistURI);
                intent.putExtra("ImageURL", imageURL);
                intent.putExtra("trackNames", trackNames);


                //start next activity
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public ArrayList<String> Test(Playlist playlist){
        ArrayList<String> PlaylistTrackURI = new ArrayList();
        if( imageURL.size() > 0 || trackNames.size() > 0){
            imageURL.clear();
            trackNames.clear();
        }




        for(int i = 0 ; i < playlist.tracks.items.size(); i++){
            PlaylistTrackURI.add(playlist.tracks.items.get(i).track.id) ;

            imageURL.add(playlist.tracks.items.get(i).track.album.images.get(0).url);
            trackNames.add(playlist.tracks.items.get(i).track.name);
            Log.d("test", "test");
        }
        return PlaylistTrackURI;


    }





}