package com.example.hash.pictureupload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class  MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE=1;
    private static final String SERVER_ADDRESS = "https://compartmental-servi.000webhos";

    ImageView imageToUpload,downloadedImage;
    Button bUpload,bDownload;
    EditText uploadImageName,downloadImageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageToUpload =(ImageView)findViewById(R.id.imageToUpload);
        downloadedImage =(ImageView)findViewById(R.id.downloadedImage);

        bUpload = (Button)findViewById(R.id.bUpload);
        bDownload = (Button)findViewById(R.id.bDownload);

        uploadImageName = (EditText)findViewById(R.id.etimageUpload);
        downloadImageName = (EditText)findViewById(R.id.etimageDownload);

        imageToUpload.setOnClickListener(this);
        bUpload.setOnClickListener(this);
        bDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageToUpload:
                Intent gallryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallryIntent ,RESULT_LOAD_IMAGE);
                break;
            case R.id.bUpload:
                Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
                new UploadImage(image,uploadImageName.getText().toString()).execute();

                break;
            case R.id.bDownload:

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!= null){
            Uri selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
        }
    }


    private class UploadImage extends AsyncTask<Void,Void,Void>{

        Bitmap image;
        String name;

        public  UploadImage(Bitmap image,String name){
            this.image = image;
            this.name = name;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

            ArrayList<NameValuePair> dataTosend = new ArrayList<>();
            dataTosend.add(new BasicNameValuePair("image",encodedImage));
            dataTosend.add(new BasicNameValuePair("name",name));

            HttpParams httpRequestParms = getHttpRequestParms();
            HttpClient client = new DefaultHttpClient(httpRequestParms);
            HttpPost post = new HttpPost(SERVER_ADDRESS+ "SavePicture.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataTosend));
                client.execute(post);
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
        }


    }


    private HttpParams getHttpRequestParms(){
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams,1000*30);
        HttpConnectionParams.setSoTimeout(httpRequestParams,1000*30);
        return httpRequestParams;
    }


}
