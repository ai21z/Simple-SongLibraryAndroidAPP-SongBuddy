package gr.uth.songbuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    public void goToViewPlaylists(View view) {

        Intent intent = new Intent(this, PlaylistActivity.class);
        startActivity(intent);

    }

    public void goToViewSongs(View view) {

        Intent intent = new Intent(this, SongActivity.class);
        startActivity(intent);

    }
}
