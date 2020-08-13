package gr.uth.songbuddies.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import gr.uth.songbuddies.R;
import gr.uth.songbuddies.model.Song;
import gr.uth.songbuddies.database.SqliteDatabase;
import gr.uth.songbuddies.dataHolders.SongsInPlaylistViewHolder;

import java.util.ArrayList;

public class SongsInPlaylistAdapter extends RecyclerView.Adapter<SongsInPlaylistViewHolder> {

    private Context context;
    private ArrayList<Song> songsInPlaylist;
    private ArrayList<Song> mArrayList;


    private SqliteDatabase mDatabase;
    private CardView cdView;
    public SongsInPlaylistAdapter(Context context, ArrayList<Song> songsInPlaylist) {
        this.context = context;
        this.songsInPlaylist = songsInPlaylist;
        this.mArrayList = songsInPlaylist;
        this.mDatabase = new SqliteDatabase(context);
    }



    @Override
    public SongsInPlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_in_playlist_item, parent, false);
        return new SongsInPlaylistViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SongsInPlaylistViewHolder holder, int position) {

        final Song song = songsInPlaylist.get(position);

        //WARNING: Sophisticated algorithm on the way...
        double stotal = 0;
        int stotal2 = 0;
        double mod = 0.0;
        double result = 0.0;
        for(Song s: songsInPlaylist){

            stotal = result + s.getDuration();

            stotal2 = (int) stotal;
            if((stotal - stotal2) > .60) {
                mod = (stotal - stotal2) - .60;
                stotal2 = stotal2 + 1;
                result = mod + (double) stotal2;
            } else {
                result = stotal;
            }
        }

       //System.out.println(stotal);
        double sTotal = Double.parseDouble(String.format("%.2f", result));

        holder.name.setText(song.getName());
        holder.artist.setText(song.getArtist());
        holder.songDuration.setText(String.valueOf(song.getDuration()));
        holder.totalDuration.setText(String.valueOf(sTotal));
        holder.link.setText(song.getLink());


        holder.deleteSongFromPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database
                mDatabase.deleteSongFromPlaylist(String.valueOf(song.getId()));

                //refresh the activity page.
                ((Activity)context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });

    }

    @Override
    public int getItemCount() {
        return songsInPlaylist.size();
    }

}

