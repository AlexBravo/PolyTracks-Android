package com.example.mrfir.roughdraft;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

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
    ArrayList<String> imageURL = new ArrayList();
    ArrayList<String> trackNames = new ArrayList();
    String Credentials = "";
    String privateUser;
    UserPrivate tUserPrivate;
    String[] itemname ={

            "College of Science",
            "College of Engineering",
            "College of Letters, Arts and Social Sciences",
            "College of Environmental Design",
            "College of Education and Integrative Studies",
            "Library",
            "Music",
            "Bronco Student Center"
    };

    String[] imgid={
            "https://www.cpp.edu/maps/common/structure_imgs/Bg-8.gif",
            "https://www.cpp.edu/maps/common/structure_imgs/Bg-9.gif",
            "https://www.cpp.edu/maps/common/structure_imgs/Bg-5.gif",
            "https://www.cpp.edu/maps/common/structure_imgs/Bg-7.gif",
            "https://www.cpp.edu/maps/common/structure_imgs/Bg-6.gif",
            "https://www.cpp.edu/maps/common/structure_imgs/Bg-15.gif",
            "https://www.cpp.edu/maps/common/structure_imgs/Bg-24.gif",
            "https://www.cpp.edu/maps/common/structure_imgs/Bg-35.gif"
    };

    String[] PlaylistURI = {
            "729LhyVuS3ZRh9n0fGCe0e",
            "2ENtqwOp8pX9LI5P4DPZoz",
            "7dg0GFmFAGqaTaiBXYUQSv",
            "35X5FarJDWxCwGIWruIAJv",
            "4Fm5pSHlWbkUMdDthDPLa7",
            "3BffdmUlBsqQNBqG8FbhBN",
            "6usOvLJjqW6FiVKrrBXZ5j",
            "35X5FarJDWxCwGIWruIAJv"

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Mustard);
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
                tUserPrivate = userPrivate;

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
                //itemname[0] = playlist.name;




                list=(ListView)findViewById(R.id.list);
                list.setAdapter(adapter);
                Test();


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

    public void Test(){
        //When you click the Playlist information it will pass the name of the playlist and tracks
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

                spotify.getPlaylist(tUserPrivate.id, PlaylistURI[position], new Callback<Playlist>() {
                    @Override
                    public void success(Playlist playlist, Response response) {
                        Intent intent = new Intent(NearbyPlaylist.this, PlaylistDetail.class);
                        //Transfer over data needed for next activity
                        ArrayList<String> test = getTrackURIFromPlaylist(playlist);

                        intent.putExtra("playlistName", playlist.name);
                        intent.putExtra("playlistTrackURI", test);
                        intent.putExtra("Credentials", Credentials);
                        intent.putExtra("PlaylistUri", playlist.id);
                        intent.putExtra("ImageURL", imageURL);
                        intent.putExtra("trackNames", trackNames);




                        //intent.putExtra("playlistImage", playlist.images);


                        //start next activity
                        startActivity(intent);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });




            }
        });
    }


}