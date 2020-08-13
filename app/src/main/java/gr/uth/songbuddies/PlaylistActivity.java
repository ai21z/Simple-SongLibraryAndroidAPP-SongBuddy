package gr.uth.songbuddies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import gr.uth.songbuddies.model.PlayList;
import gr.uth.songbuddies.database.SqliteDatabase;
import gr.uth.songbuddies.adapters.PlaylistAdapter;

public class PlaylistActivity extends AppCompatActivity {

    private SqliteDatabase mDatabase;
    private ArrayList<PlayList> allPlaylists = new ArrayList<>();
    private PlaylistAdapter plAdapter;
    final int PICK_PLAYLIST_IMG = 100;
    private View lastAddPlaylistView;
    Uri add_playlist_img_uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        RecyclerView playlistView = (RecyclerView)findViewById(R.id.recycler_list_playlist);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        playlistView.setLayoutManager(linearLayoutManager);
        mDatabase = new SqliteDatabase(this);
        allPlaylists = mDatabase.listPlaylist();
        if(allPlaylists.size() > 0){
            playlistView.setVisibility(View.VISIBLE);
            plAdapter = new PlaylistAdapter(this, allPlaylists);
            playlistView.setAdapter(plAdapter);
            plAdapter.notifyDataSetChanged();

        } else {
            playlistView.setVisibility(View.GONE);
            Toast.makeText(this, "Add a playlist dude!", Toast.LENGTH_LONG).show();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog();
            }
        });



//        PlayList pl = new PlayList("Rock");
//        mDatabase.addPlaylist(pl);
//
//
//        mDatabase.addSongToPlaylist(1, 1);

    }


    private void addTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View subView = inflater.inflate(R.layout.add_playlist_layout, null);
        lastAddPlaylistView = subView;

        final EditText nameField = (EditText) subView.findViewById(R.id.enter_playlist_name);
        final Button selectImageBtn = (Button) subView.findViewById(R.id.btn_select_image);


        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new PLAYLIST");
        builder.setView(subView);
        builder.create();

        final Context mContext = this;

        builder.setPositiveButton("ADD PLAYLIST", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                Bitmap newBitmap = null;

                if(add_playlist_img_uri != null){
                    try {
                        newBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), add_playlist_img_uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(PlaylistActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    PlayList newPlaylist = new PlayList(name, newBitmap);
                    mDatabase.addPlaylist(newPlaylist);

                    finish();
                    startActivity(getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PlaylistActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }



    //To image gellery repo tou kinitou
    private void OpenGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_PLAYLIST_IMG);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView img = lastAddPlaylistView.findViewById(R.id.enter_playlist_img);
        if(resultCode == RESULT_OK && requestCode == PICK_PLAYLIST_IMG){
            add_playlist_img_uri = data.getData();
            Log.d("imgPickResult", data.getData().toString());

            img.setImageURI(add_playlist_img_uri);
        }
        else if (resultCode == RESULT_CANCELED && requestCode == PICK_PLAYLIST_IMG){
            Log.d("imgPickResult", "no image selected");
            img.setImageURI(null);
        }
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

        getMenuInflater().inflate(R.menu.menu_playlists, menu);

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
                if (plAdapter !=null)
                    plAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

}
