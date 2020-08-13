package gr.uth.songbuddies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import gr.uth.songbuddies.model.Song;
import gr.uth.songbuddies.database.SqliteDatabase;
import gr.uth.songbuddies.adapters.SongAdapter;

public class SongActivity extends AppCompatActivity {



    private SqliteDatabase mDatabase;
    private ArrayList<Song> allSongs = new ArrayList<>();
    private SongAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        RecyclerView songView = findViewById(R.id.recycler_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        songView.setLayoutManager(linearLayoutManager);
        songView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this);
        allSongs = mDatabase.listSongs();

        if(allSongs.size() > 0){
            songView.setVisibility(View.VISIBLE);
            sAdapter = new SongAdapter(this, allSongs);
            songView.setAdapter(sAdapter);
            sAdapter.notifyDataSetChanged();

        } else {
            songView.setVisibility(View.GONE);
            Toast.makeText(this, "There is no spoon! Add a song dude!", Toast.LENGTH_LONG).show();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog();
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SongActivity.this, PlaylistActivity.class);
                startActivity(intent);
            }
        });



//        PlayList pl = new PlayList("Rock");
//        mDatabase.addPlaylist(pl);
//
//
//        mDatabase.addSongToPlaylist(1, 1);
    }

    private void addTaskDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_song_layout, null);

        final EditText nameField = subView.findViewById(R.id.enter_name);
        final EditText artistField = subView.findViewById(R.id.enter_artist);
        final EditText genreField = subView.findViewById(R.id.enter_genre);
        final EditText durationField = subView.findViewById(R.id.enter_duration);
        final EditText rateField = subView.findViewById(R.id.enter_rate);
        final EditText descriptionField = subView.findViewById(R.id.enter_description);
        final EditText linkField = subView.findViewById(R.id.enter_link);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new SONG");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("ADD SONG", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String artist = artistField.getText().toString();
                final String genre = genreField.getText().toString();
                final double duration = Double.parseDouble(durationField.getText().toString());
                final int rate = Integer.parseInt(rateField.getText().toString());
                final String description = descriptionField.getText().toString();
                final String link = linkField.getText().toString();


                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(artist) || TextUtils.isEmpty(genre)){
                    Toast.makeText(SongActivity.this, "Name, artist and genre are mandatory", Toast.LENGTH_LONG).show();
                }

                if (rate <1 || rate >5){
                    Toast.makeText(SongActivity.this, "Rate must be between 1-5", Toast.LENGTH_LONG).show();
                } else if (duration >0.60 && duration < 1.00 ||
                        duration > 1.60 && duration < 2.00 ||
                        duration > 2.60 && duration < 3.00 ||
                        duration > 3.60 && duration < 4.00 ||
                        duration > 4.60 && duration <5.00 ||
                        duration > 5.60 && duration < 6.00 ||
                        duration > 6.60 && duration < 7.00 ||
                        duration > 7.60 && duration < 8.00 ||
                        duration > 8.60 && duration < 9.00 ||
                        duration > 9.60 && duration < 10.00)
                {
                    Toast.makeText(SongActivity.this, "Duration must follow the clock rules (1:00-10:00)", Toast.LENGTH_LONG).show();

                }
                else {
                    Song newSong = new Song(name, artist, genre, duration, rate, description, link);
                    mDatabase.addSongs(newSong);

                    finish();
                    startActivity(getIntent());
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SongActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null){
            mDatabase.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_songs, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (sAdapter !=null)
                    sAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
