package gr.uth.songbuddies.dataHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import gr.uth.songbuddies.R;

public class SongsInPlaylistViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView artist;
    public TextView songDuration;
    public TextView totalDuration;
    public TextView link;
    public ImageView deleteSongFromPlaylist;

    public SongsInPlaylistViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.songInPlaylist_name);
        artist = itemView.findViewById(R.id.songInPlaylist_artist);
        songDuration = itemView.findViewById(R.id.songInPlaylist_song_duration);
        totalDuration = itemView.findViewById(R.id.songInPlaylist_total_duration);
        link = itemView.findViewById(R.id.songInPlaylist_link);


        deleteSongFromPlaylist = itemView.findViewById(R.id.delete_song_from_playlist);
    }
}
