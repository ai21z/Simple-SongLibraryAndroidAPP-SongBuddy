package gr.uth.songbuddies.dataHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import gr.uth.songbuddies.R;

public class PlaylistViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public ImageView avatar;
    public ImageView deletePlaylist;
    public ImageView editPlaylist;

    public CardView cardView;

    public PlaylistViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.playlist_name);


        avatar = itemView.findViewById(R.id.playlist_avatar);

        deletePlaylist = itemView.findViewById(R.id.delete_playlist);
        editPlaylist = itemView.findViewById(R.id.edit_playlist);

        cardView = itemView.findViewById(R.id.playlistCard);
    }

}
