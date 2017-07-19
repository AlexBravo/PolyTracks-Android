package com.example.mrfir.roughdraft;

/**
 * Created by mrfir on 6/29/2017.
 */

import android.app.Activity;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CustomListAdapter extends ArrayAdapter<String> {

    private  Activity context;
    private String[] itemname;
    private  String[] imgid;
    private  Image[] images = new Image[100];


    public CustomListAdapter(Activity context, String[] itemname, String[] imgid) {
        // We need to clear our R.layout.mylist somehow
        super(context, R.layout.mylist, itemname);
        // TODO Auto-generated constructor stub


        if(this.itemname ==  null){
            this.context=context;
            this.itemname=itemname;
            this.imgid=imgid;
        }
        else {
            Log.d("STRING", "STRING");
        }







    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();


        View rowView = inflater.inflate(R.layout.mylist, null,true);





        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        if(imgid.length > 0){
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            Picasso.with(context)
                    .load(imgid[position])
                    .into(imageView);
        }

        // TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);


        txtTitle.setText(itemname[position]);
        // TODO HERE IS WHERE THE PICASSO CODE WILL GO TO PUT INTO THE RIGHT LOCATION FOR THE ALBUM IMAGE
        //imageView.setImageResource(imgid[position]);
        // extratxt.setText("Description "+itemname[position]);
        return rowView;

    };
}