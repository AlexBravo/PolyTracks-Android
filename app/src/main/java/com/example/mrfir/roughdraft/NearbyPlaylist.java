package com.example.mrfir.roughdraft;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NearbyPlaylist extends AppCompatActivity implements Serializable {
    SpotifyService spotify;
    ListView list;
    CustomListAdapter adapter;
    ArrayList<String> PlaylistTrackURI;
    String Credentials = "";
    String[] itemname ={
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War"
    };

    Integer[] imgid={
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4,
            R.drawable.pic5,
            R.drawable.pic6,
            R.drawable.pic7,
            R.drawable.pic8,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.Mustard);
        setContentView(R.layout.activity_nearby_playlist);

        //Instantiate spotify crap, and get access token
        SpotifyApi api = new SpotifyApi();
        Credentials = CredentialsHandler.getToken(this);
        api.setAccessToken(CredentialsHandler.getToken(this));
        spotify = api.getService();

        spotify.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                retrievePlaylist(userPrivate, "");
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        adapter=new CustomListAdapter(this, itemname, imgid);



    }

    private void retrievePlaylist(UserPrivate userPrivate, String tURI) {
        spotify.getPlaylist(userPrivate.id, "50hbYFK5UvH74zrJ36DMrh" , new Callback<Playlist>() {
            /**
             * Successful HTTP response.
             *
             * @param playlist
             * @param response
             */
            @Override
            public void success(final Playlist playlist, Response response) {
                ArrayList<String> PlaylistTrackURI;
                itemname[0] = playlist.name;




                list=(ListView)findViewById(R.id.list);
                list.setAdapter(adapter);

                //When you click the Playlist information it will pass the name of the playlist and tracks
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub
                        String Slecteditem= itemname[+position];
                        Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NearbyPlaylist.this, PlaylistDetail.class);

                        //Transfer over data needed for next activity
                        intent.putExtra("playlistName", playlist.name);
                        intent.putExtra("playlistTrackURI", getTrackURIFromPlaylist(playlist));
                        intent.putExtra("Credentials", Credentials);



                        //intent.putExtra("playlistImage", playlist.images);


                        //start next activity
                        startActivity(intent);


                    }
                });
            }

            /**
             * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
             * exception.
             *
             * @param error
             */
            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    public ArrayList<String> getTrackURIFromPlaylist(Playlist playlist){
        ArrayList<String> PlaylistTrackURI = new ArrayList();


        for(int i = 0 ; i < playlist.tracks.items.size(); i++){
            PlaylistTrackURI.add(playlist.tracks.items.get(i).track.id) ;
        }
        return PlaylistTrackURI;


    }


}