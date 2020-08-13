package gr.uth.songbuddies.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PlayList {

    private int id;
    private String name;
    private Bitmap img;
    private double totalLength;

    public PlayList(int id, String name, Bitmap img) {
        this.id = id;
        this.name = name;
        this.img = img;
    }


    public PlayList(String name, Bitmap img, double totalLength) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.totalLength = totalLength;
    }


    public PlayList(int id, String name, byte[] img) {
        this.id = id;
        this.name = name;
        this.setImageFromBlob(img);
    }


    public PlayList(String name) {
        this.name = name;
    }

    public PlayList(String name, Bitmap img) {
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public PlayList() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage(){
        return this.img;
    }

    public byte[] getImageAsBlob(){

        if (this.img!=null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            this.img.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            return stream.toByteArray();
        }
        return null;
    }

    public void setImageFromBlob(byte[] blob){
        ByteArrayInputStream imageStream = new ByteArrayInputStream(blob);
        this.img = BitmapFactory.decodeStream(imageStream);
    }
    //public setImg(Bitmap bmp){this.img =}



}
