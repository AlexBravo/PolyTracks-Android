package com.example.mrfir.roughdraft;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.io.Serializable;
import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PlaylistDetail extends AppCompatActivity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback {
    String[] TrackURI;
    CustomListAdapter adapter;
    SpotifyService spotify;
    private Player mPlayer;
    String Credentials;
    private static final String REDIRECT_URI = "RoughDraft://callback";
    int tPosition = 0;
    boolean isPlaying = false;
    String playlistURI;

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;


    Integer[] imgid={
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4,
            R.drawable.pic5,
            R.drawable.pic6,
            R.drawable.pic7,
            R.drawable.pic8,
            R.drawable.pic9,
            R.drawable.pic10,
            R.drawable.pic11,
            R.drawable.pic12,
            R.drawable.pic13,
            R.drawable.pic14,
            R.drawable.pic15,

    };
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.Mustard);
        setContentView(R.layout.activity_playlist_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView list;

        //Retrieves playlist when passed through
        String playlistName = (String) getIntent().getSerializableExtra("playlistName");

        Credentials = (String) getIntent().getSerializableExtra("Credentials");
        ArrayList<String > playlistTrackURI = (ArrayList<String>) getIntent().getSerializableExtra("playlistTrackURI");
        TrackURI = new String[playlistTrackURI.size()];
        TrackURI = playlistTrackURI.toArray(TrackURI);


        //Grab playlist URI
        playlistURI = (String) getIntent().getSerializableExtra("PlaylistUri");

        //Instantiate spotify crap, and get access token
        SpotifyApi api = new SpotifyApi();

        api.setAccessToken(CredentialsHandler.getToken(this));
        spotify = api.getService();

        getSupportActionBar().setTitle(playlistName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CreatePlayer();

        adapter =new CustomListAdapter(this, TrackURI, imgid);
        listView=(ListView)findViewById(R.id.list2);
        listView.setAdapter(adapter);
        Config playerConfig = new Config(this, Credentials, "1245711054");

        Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(SpotifyPlayer spotifyPlayer) {
                mPlayer = spotifyPlayer;
                mPlayer.addConnectionStateCallback(PlaylistDetail.this);
                mPlayer.addNotificationCallback(PlaylistDetail.this);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
            }
        });


        //When you click the Playlist information it will pass the name of the playlist and tracks
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                tPosition = position;
                String Slecteditem= TrackURI[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(PlaylistDetail.this, PlaylistDetail.class);
                mPlayer.playUri(null, "spotify:playlist:"+ playlistURI, 0, 0);




            }
        });
        //Play button to play currently selected playlist
        FloatingActionButton playFab = (FloatingActionButton) findViewById(R.id.PlayButton);
        //Setting OnClick Listener to The Floating Action Button
        playFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying == false) {
                    isPlaying = true;
                    String Slecteditem = TrackURI[tPosition];
                    mPlayer.playUri(null, "spotify:track:" + Slecteditem, 0, 0);
                }
                else {
                    isPlaying = false;
                    mPlayer.pause(new Player.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplicationContext(),
                                    "Pausing",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Error error) {

                        }
                    });
                }


            }
        });

        //Next button to play currently selected playlist
        FloatingActionButton nextFab = (FloatingActionButton) findViewById(R.id.NextButton);
        //Setting OnClick Listener to The Floating Action Button
        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.skipToNext(new Player.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),
                                "Next Track",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Error error) {

                    }
                });
            }
        });

        //Play button to play currently selected playlist
        FloatingActionButton prevFab = (FloatingActionButton) findViewById(R.id.PrevButton);
        //Setting OnClick Listener to The Floating Action Button
        prevFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mPlayer.skipToPrevious(new Player.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),
                        "Previous Track",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Error error) {

                    }
                });
            }
        });



    }

    public void CreatePlayer(){

    }


    @Override
    public void onLoggedIn() {

    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Error error) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {

    }

    @Override
    public void onPlaybackError(Error error) {

    }




}
