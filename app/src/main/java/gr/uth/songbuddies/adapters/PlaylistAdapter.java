package gr.uth.songbuddies.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gr.uth.songbuddies.R;
import gr.uth.songbuddies.SongsInPlaylistActivity;
import gr.uth.songbuddies.dataHolders.PlaylistViewHolder;
import gr.uth.songbuddies.model.PlayList;
import gr.uth.songbuddies.model.Song;
import gr.uth.songbuddies.database.SqliteDatabase;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistViewHolder> implements Filterable {


    private Context context;
    private ArrayList<PlayList> listPlaylist;
    private ArrayList<PlayList> mArrayList;

    private SongsInPlaylistAdapter songsInPlaylistAdapter;
    private ArrayList<Song> songsInPlaylist;

    private SqliteDatabase mDatabase;
    private View lastEditPlaylistView = null;
    final int PICK_PLAYLIST_IMG = 100;

    public PlaylistAdapter(Context context, ArrayList<PlayList> listPlaylist) {
        this.context = context;
        this.listPlaylist = listPlaylist;

        this.mArrayList=listPlaylist;
        mDatabase = new SqliteDatabase(context);
    }


    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_list_item, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {
        final PlayList pl = listPlaylist.get(position);


        holder.name.setText(pl.getName());
        holder.avatar.setImageBitmap(pl.getImage());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SongsInPlaylistActivity.class);
                intent.putExtra("playlistID", pl.getId());
                context.startActivity(intent);
                //aaaa
            }
        });

        holder.editPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(pl);
            }
        });

        holder.deletePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database

                mDatabase.deletePlaylist(pl.getId());

                //refresh the activity page.
                ((Activity)context).finish();
                context.startActivity(((Activity) context).getIntent());
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

                    listPlaylist = mArrayList;
                } else {

                    ArrayList<PlayList> filteredList = new ArrayList<>();

                    for (PlayList pl : mArrayList) {

                        if (pl.getName().toLowerCase().contains(charString)) {

                            filteredList.add(pl);
                        }
                    }

                    listPlaylist = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listPlaylist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listPlaylist = (ArrayList<PlayList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return listPlaylist.size();
    }


    private void editTaskDialog(final PlayList pl){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_playlist_layout, null);
        lastEditPlaylistView = subView;

        final EditText nameField = subView.findViewById(R.id.enter_playlist_name);
        final ImageView imageField = subView.findViewById(R.id.enter_playlist_img);
        final Button selectImageBtn = subView.findViewById(R.id.btn_select_image);
        imageField.setVisibility(View.GONE);
        selectImageBtn.setVisibility(View.GONE);

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OpenGallery();
            }
        });

        if(pl != null){
            nameField.setText(pl.getName());
        }
        imageField.setImageBitmap(pl.getImage());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Playlist");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("EDIT PLAYLIST", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();



                if(TextUtils.isEmpty(name)){
                    Toast.makeText(context, "Check your name input values", Toast.LENGTH_LONG).show();
                }
                else{
                    // TODO: Update image
                    mDatabase.updatePlaylist(new PlayList(pl.getId(), name, pl.getImage()));
                    //refresh the activity
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
