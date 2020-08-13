package gr.uth.songbuddies.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gr.uth.songbuddies.R;
import gr.uth.songbuddies.model.PlayList;
import gr.uth.songbuddies.model.Song;
import gr.uth.songbuddies.database.SqliteDatabase;
import gr.uth.songbuddies.dataHolders.SongViewHolder;

public class SongAdapter extends RecyclerView.Adapter<SongViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Song> listSongs;
    private ArrayList<Song> mArrayList;
    private ArrayList<PlayList> listPlaylists;

    private SqliteDatabase mDatabase;

    public SongAdapter(Context context, ArrayList<Song> listSongs) {
        this.context = context;
        this.listSongs = listSongs;
        this.mArrayList=listSongs;
        mDatabase = new SqliteDatabase(context);
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        final Song song = listSongs.get(position);

        holder.name.setText(song.getName());
        holder.artist.setText(song.getArtist());
        holder.genre.setText(song.getGenre());
        holder.duration.setText(String.valueOf(song.getDuration()));
        holder.rate.setText(String.valueOf(song.getRate()));
        holder.description.setText(song.getDescription());
        holder.link.setText(song.getLink());


        holder.editSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(song);
            }
        });

        holder.deleteSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database

                mDatabase.deleteSong(song.getId());

                //refresh the activity page.
                ((Activity)context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });

        holder.addSongToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSongToPlaylistDialog(song);
            }
        });

    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    listSongs = mArrayList;
                } else {

                    ArrayList<Song> filteredList = new ArrayList<>();

                    for (Song s : mArrayList) {

                        if ( (s.getName().toLowerCase().contains(charString)) || (s.getGenre().toLowerCase().contains(charString)) ) {

                            filteredList.add(s);
                        }
                    }

                    listSongs = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listSongs;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listSongs = (ArrayList<Song>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return listSongs.size();
    }

    private void addSongToPlaylistDialog(final Song song){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_song_to_playlist, null);
        listPlaylists = mDatabase.listPlaylist();

        final LinearLayout LL = (LinearLayout)subView.findViewById(R.id.ll_btns);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Add song to playlist");
        builder.setView(subView);


        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });

        final AlertDialog alert = builder.create();
        alert.show();

        //rows of playlists
        for(int i=0; i<listPlaylists.size(); i++){
            final PlayList pl = listPlaylists.get(i);
            Button btn;
            btn = new Button(context);
            btn.setText(pl.getName());
            LL.addView(btn);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //handler code
                    mDatabase.addSongToPlaylist(song.getId(), pl.getId());
                    alert.cancel();
                    Toast.makeText(context, "Added to playlist: " + pl.getName(), Toast.LENGTH_LONG).show();
                }
            });
        }
        alert.setView(subView);

    }

    private void editTaskDialog(final Song song){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_song_layout, null);

        final EditText nameField = subView.findViewById(R.id.enter_name);
        final EditText artistField = subView.findViewById(R.id.enter_artist);
        final EditText genreField = subView.findViewById(R.id.enter_genre);
        final EditText durationField = subView.findViewById(R.id.enter_duration);
        final EditText rateField = subView.findViewById(R.id.enter_rate);
        final EditText descriptionField = subView.findViewById(R.id.enter_description);
        final EditText linkField = subView.findViewById(R.id.enter_link);

        if(song != null){
            nameField.setText(song.getName());
            artistField.setText(song.getArtist());
            genreField.setText(song.getGenre());
            durationField.setText(String.valueOf(song.getDuration()));
            rateField.setText(String.valueOf(song.getRate()));
            descriptionField.setText(song.getDescription());
            linkField.setText(song.getLink());

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit song");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("EDIT SONG", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String artist = artistField.getText().toString();
                final String genre = genreField.getText().toString();
                final Double duration = Double.parseDouble(durationField.getText().toString());
                final int rate = Integer.parseInt(rateField.getText().toString());
                final String description = descriptionField.getText().toString();
                final String link = linkField.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(artist) || TextUtils.isEmpty(genre)){
                    Toast.makeText(context, "Name, artist and genre are mandatory", Toast.LENGTH_LONG).show();
                } else if (rate <1 || rate >5 ){
                    Toast.makeText(context, "Rate must be between 1-5", Toast.LENGTH_LONG).show();
                } else if (duration >0.60 && duration < 1.00 ||
                        duration > 1.60 && duration < 2.00 ||
                        duration > 2.60 && duration < 3.00 ||
                        duration > 3.60 && duration < 4.00 ||
                        duration > 4.60 && duration <5.00 ||
                        duration > 5.60 && duration < 6.00 ||
                        duration > 6.60 && duration < 7.00 ||
                        duration > 7.60 && duration < 8.00 ||
                        duration > 8.60 && duration < 9.00 ||
                        duration > 9.60 && duration < 10.00) {
                    Toast.makeText(context, "Duration must follow the clock rules (1:00-10:00)", Toast.LENGTH_LONG).show();

                }
                else{
                    mDatabase.updateSongs(new Song(song.getId(), name, artist, genre, duration, rate, description, link));
                    //refresh actvity
                    ((Activity)context).finish();
                    context.startActivity(((Activity)context).getIntent());
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }


}
