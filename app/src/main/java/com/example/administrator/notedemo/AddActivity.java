package com.example.administrator.notedemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

import static android.R.attr.data;
import static android.R.attr.handle;
import static android.R.attr.path;
import static android.R.id.list;
import static com.example.administrator.notedemo.ContentDao.insertNote;
import static junit.runner.Version.id;

/**
 * Created by Administrator on 2017/5/3.
 */

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView photo;
    private ImageView imgSave;
    private Note note;
    private EditText edt_title;
    private EditText edt_content;
    private ImageView add_photo;
    private static final int CHOOSE_PHOTO = 2;
    private String mImagePath;
    String imagePath = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recode);
        initViews();
        photo.setOnClickListener(this);
        imgSave.setOnClickListener(this);
    }


    private void initViews() {
        photo = (ImageView) findViewById(R.id.photo);
        add_photo = (ImageView) findViewById(R.id.add_photo);
        imgSave = (ImageView) findViewById(R.id.save);
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_content = (EditText) findViewById(R.id.edt_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo:
                if(ContextCompat.checkSelfPermission(AddActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddActivity.this,new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },1);
                }else {
                    openAlbum();
                }
                break;
            case R.id.save:
                addDate();
                startActivity(new Intent(AddActivity.this, MainActivity.class));
                finish();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    private void addDate() {
        String myTitle = edt_title.getText().toString().trim();
        String myContent = edt_content.getText().toString().trim();
        note = new Note();
        note.setTitle(myTitle);
        note.setContent(myContent);
        note.setImage_url(imagePath);
//        note.setImage_url(getImagePath(uri,null));
        ContentDao.insertNote(note);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOOSE_PHOTO :
                if(resultCode == RESULT_OK){
                    //判断手机系统版本号
                    if(Build.VERSION.SDK_INT >= 19){
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }else {
                        //4.4以下的系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
        }

    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {

        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this, uri)){
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id ;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.document".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是Content类型的Uri，则使用普通的方式处理
            imagePath = getImagePath(uri,null);
        }else  if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }
    private void handleImageBeforeKitKat(Intent data) {
         Uri uri = data.getData();
        mImagePath = getImagePath(uri,null);
        displayImage(mImagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                try {
                    path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath) {
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            add_photo.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"failed to get Image",Toast.LENGTH_SHORT).show();
        }
    }
}