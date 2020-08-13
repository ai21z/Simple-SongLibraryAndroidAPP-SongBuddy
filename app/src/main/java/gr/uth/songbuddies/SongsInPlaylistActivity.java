package gr.uth.songbuddies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import gr.uth.songbuddies.model.Song;
import gr.uth.songbuddies.database.SqliteDatabase;
import gr.uth.songbuddies.adapters.SongAdapter;
import gr.uth.songbuddies.adapters.SongsInPlaylistAdapter;

public class SongsInPlaylistActivity extends AppCompatActivity {

    private static final String TAG = SongActivity.class.getSimpleName();

    private SqliteDatabase mDatabase;
    private ArrayList<Song> songsInPlaylist = new ArrayList<>();
    private ArrayList<Song> allSongs = new ArrayList<>();
    private SongsInPlaylistAdapter splAdapter;
    private SongAdapter songAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_in_playlist);

        RecyclerView songView = findViewById(R.id.songs_in_playlist_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        songView.setLayoutManager(linearLayoutManager);
        songView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this);
        allSongs = mDatabase.listSongs();

        //tha parw to intent
        //tha mpei sthn parametro ths listSongsInPlaylist

        Intent intent = getIntent();
        int playlistId = intent.getIntExtra("playlistID", -1);
        //System.out.println(playlistId);

        songsInPlaylist = mDatabase.listSongsInPlaylist(playlistId);

        //Logw tou menu kalyptetai to prwto item opote katevainei ligo poio katw sto layout (done)
//        for(Song s : songsInPlaylist) {
//            System.out.println(s.getName());
//        }

        if(songsInPlaylist.size() > 0){
            songView.setVisibility(View.VISIBLE);
            splAdapter = new SongsInPlaylistAdapter(this, songsInPlaylist);
            songView.setAdapter(splAdapter);
            splAdapter.notifyDataSetChanged();


        } else {
            songView.setVisibility(View.GONE);
            Toast.makeText(this, "No songs!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null){
            mDatabase.close();
        }
    }

}
