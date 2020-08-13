package gr.uth.songbuddies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.util.ArrayList;


import gr.uth.songbuddies.model.PlayList;
import gr.uth.songbuddies.model.Song;


public class SqliteDatabase extends SQLiteOpenHelper {

    private	static final int DATABASE_VERSION =	14;
    private	static final String	DATABASE_NAME = "songDB";

    /** ~~~~~~~~~~~~ CREATE DATABASE TABLES ~~~~~~~~~~~~ */

    //table songs
    private	static final String TABLE_SONGS = "songs";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ARTIST = "artist";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_RATE = "rate";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_LINK = "link";

    private static final String	CREATE_SONGS_TABLE = "CREATE	TABLE " + TABLE_SONGS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_ARTIST + " TEXT,"
            + COLUMN_GENRE + " TEXT,"
            + COLUMN_DURATION + " DOUBLE,"
            + COLUMN_RATE + " INTEGER,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_LINK + " TEXT" +
            ")";




    //table playlists
    private static final String TABLE_PLAYLISTS = "playlists";
    private static final String COLUMN_PID = "pid";
    private static final String COLUMN_PNAME = "pname";
    private static final String COLUMN_IMAGE = "image";

    private static final String CREATE_PLAYLISTS_TABLE = "CREATE TABLE " + TABLE_PLAYLISTS + "("
            + COLUMN_PID + " INTEGER PRIMARY KEY,"
            + COLUMN_PNAME + " TEXT,"
            + COLUMN_IMAGE + " BLOB" +
            ")";



    //table songs_playlists (intersection)
    private static final String TABLE_SONGS_PLAYLISTS = "songs_playlists";
    private static final String COLUMN_S_P_ID = "spid";
    private static final String COLUMN_KEY_SID = "song_id";
    private static final String COLUMN_KEY_PID = "playlist_id";

    private static final String CREATE_SONGS_PLAYLISTS_TABLE = " CREATE TABLE " +
            TABLE_SONGS_PLAYLISTS + "("
            + COLUMN_S_P_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_KEY_SID + " INTEGER,"
            + COLUMN_KEY_PID + " INTEGER, FOREIGN KEY ("
            + COLUMN_KEY_SID + ") REFERENCES " + TABLE_SONGS
            + "(" + COLUMN_ID + ")," + " FOREIGN KEY ("
            + COLUMN_KEY_PID + ") REFERENCES " + TABLE_PLAYLISTS
            + "(" + COLUMN_PID + "))";


    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SONGS_TABLE);
        db.execSQL(CREATE_PLAYLISTS_TABLE);
        db.execSQL(CREATE_SONGS_PLAYLISTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);

        //NOT COMPLETED or INCORRECT
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS_PLAYLISTS);
        onCreate(db);
    }


    /** ~~~~~~~~~~~~ SONGS TABLE METHODS ~~~~~~~~~~~~ */

    public ArrayList<Song> listSongs(){
        String sql = "select * from " + TABLE_SONGS;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Song> storeSongs = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String artist = cursor.getString(2);
                String genre = cursor.getString(3);
                double duration = Double.parseDouble(cursor.getString(4));
                int rate = Integer.parseInt(cursor.getString(5));
                String description = cursor.getString(6);
                String link = cursor.getString(7);

                storeSongs.add(new Song(id, name, artist, genre, duration, rate, description, link));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeSongs;
    }


    public void addSongs(Song song){
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, song.getName());
        values.put(COLUMN_ARTIST, song.getArtist());
        values.put(COLUMN_GENRE, song.getGenre());
        values.put(COLUMN_DURATION, song.getDuration());
        values.put(COLUMN_RATE, song.getRate());
        values.put(COLUMN_DESCRIPTION, song.getDescription());
        values.put(COLUMN_LINK, song.getLink());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_SONGS, null, values);
    }

    public void updateSongs(Song song){
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, song.getName());
        values.put(COLUMN_ARTIST, song.getArtist());
        values.put(COLUMN_GENRE, song.getGenre());
        values.put(COLUMN_DURATION, song.getDuration());
        values.put(COLUMN_RATE, song.getRate());
        values.put(COLUMN_DESCRIPTION, song.getDescription());
        values.put(COLUMN_LINK, song.getLink());

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_SONGS, values, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(song.getId())});
    }

    public Song findSong(int id){
        String query = "Select * FROM "	+ TABLE_SONGS + " WHERE " + COLUMN_ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Song song = null;
        Cursor cursor = db.rawQuery(query,	null);
        if	(cursor.moveToFirst()){
            int songId = Integer.parseInt(cursor.getString(0));
            String songName = cursor.getString(1);
            String songArtist = cursor.getString(2);
            String songGenre = cursor.getString(3);
            double songDuration = Double.parseDouble(cursor.getString(4));
            int songRate = Integer.parseInt(cursor.getString(5));
            String songDescription = cursor.getString(6);
            String songLink = cursor.getString(7);
            song = new Song(songId, songName, songArtist, songGenre, songDuration, songRate, songDescription, songLink);
        }
        cursor.close();
        return song;
    }

    public void deleteSong(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONGS, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});

        //delete also the song from intersection
        db.delete(TABLE_SONGS_PLAYLISTS, COLUMN_KEY_SID + " = ?", new String[] {String.valueOf(id)});
    }




    /** ~~~~~~~~~~~~ PLAYLISTS TABLE METHODS ~~~~~~~~~~~~ */

    public ArrayList<PlayList> listPlaylist(){
        String sql = "select * from " + TABLE_PLAYLISTS;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PlayList> storePlaylists = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);

                byte[] img = cursor.getBlob(2);

                Bitmap theImage = null;
                if(img != null){
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(img);
                    theImage= BitmapFactory.decodeStream(imageStream);
                }

                storePlaylists.add(new PlayList(id, name, theImage));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storePlaylists;
    }


    public void addPlaylist(PlayList playlist){
        ContentValues values = new ContentValues();

        values.put(COLUMN_PNAME, playlist.getName());
        values.put(COLUMN_IMAGE, playlist.getImageAsBlob());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PLAYLISTS, null, values);
    }


    public void updatePlaylist(PlayList pl){
        ContentValues values = new ContentValues();

        values.put(COLUMN_PNAME, pl.getName());
        //values.put(COLUMN_IMAGE, pl.getImage())
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_PLAYLISTS, values, COLUMN_PID + "	= ?", new String[] { String.valueOf(pl.getId())});
    }


    public void deletePlaylist(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYLISTS, COLUMN_PID + "	= ?", new String[] { String.valueOf(id)});

        //DELETE SONGS IN PLAYLIST ON PLAYLIST DELETION
        db.delete(TABLE_SONGS_PLAYLISTS, COLUMN_KEY_PID + " = ?", new String[] {String.valueOf(id)});

    }



    /** ~~~~~~~~~~~~ SONGS-PLAYLISTS TABLE METHODS ~~~~~~~~~~~~ */


    public ArrayList<Song> listSongsInPlaylist(int playlistId){
        String sql = "select " + COLUMN_KEY_SID + " from " + TABLE_SONGS_PLAYLISTS + " WHERE " +
                COLUMN_KEY_PID + " = " + playlistId;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Song> storeSongs = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                storeSongs.add(findSong(id));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeSongs;
    }


    public int addSongToPlaylist(int song_id, int playlist_id ) {

        //Check if SQLiteDatabase is getReadable and not getWritable
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KEY_SID, song_id);
        values.put(COLUMN_KEY_PID, playlist_id);

        long id = db.insert(TABLE_SONGS_PLAYLISTS, null, values);
        return (int) id;
    }

    public void deleteSongFromPlaylist(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONGS_PLAYLISTS, COLUMN_KEY_SID + " = ?", new String[] {id});
    }


}
