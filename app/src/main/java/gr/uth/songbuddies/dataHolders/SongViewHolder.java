package gr.uth.songbuddies.dataHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import gr.uth.songbuddies.R;

public class SongViewHolder extends RecyclerView.ViewHolder {

    public TextView name, artist, genre, duration, rate, description, link;
    public ImageView editSong, addSongToPlaylist, deleteSong;

    public SongViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.song_name);
        artist = itemView.findViewById(R.id.song_artist);
        genre = itemView.findViewById(R.id.song_genre);
        duration = itemView.findViewById(R.id.song_duration);
        rate = itemView.findViewById(R.id.song_rate);
        description = itemView.findViewById(R.id.song_description);
        link = itemView.findViewById(R.id.song_link);

        deleteSong = itemView.findViewById(R.id.delete_song);
        editSong = itemView.findViewById(R.id.edit_song);
        addSongToPlaylist = itemView.findViewById(R.id.add_song_to_playlist);
    }

}