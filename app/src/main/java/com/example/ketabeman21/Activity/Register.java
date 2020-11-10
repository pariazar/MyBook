package com.example.ketabeman21.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.ketabeman21.Helper.SessionManager;
import com.example.ketabeman21.Model.DB.SQLiteHandler;
import com.example.ketabeman21.Network.Network;
import com.example.ketabeman21.Network.RequestInterface;
import com.example.ketabeman21.R;
import com.example.ketabeman21.Utils.Constants;
import com.example.ketabeman21.Utils.FilePath;
import com.example.ketabeman21.Utils.ScalingUtilities;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.luozm.captcha.Captcha;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class Register extends Activity {
    private static final String TAG = Register.class.getSimpleName();
    private Button btnLinkToLogin;
    private CircleImageView profile;
    private EditText inputFullName;
    private EditText inputUsername;
    private EditText inputPassword;
    private TextView imNotBot_txt;
    private ProgressDialog pDialog;
    private SessionManager session;
    public static SQLiteHandler db;
    private boolean hasAccess;
    private Network network;

    //for uploading Profile Picture
    private static final int PICK_FILE_REQUEST = 1;
    private String selectedFilePath;
    private String SERVER_URL = Constants.BASE_URL+"/uploadFile.php";
    ImageView ivAttachment;
    Button bUpload;

    ProgressDialog dialog;
    String sdStart ;
    final String myAddress = Constants.BASE_URL+"/files/profilepics/";
    String s,image;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        network = new Network(this);
        imNotBot_txt = findViewById(R.id.imNotbot_txt);
        final Captcha captcha = (Captcha) findViewById(R.id.captCha);
        captcha.setBitmap("https://footage.framepool.com/shotimg/qf/536757588-minnesota-traffic-light-crossroad-road-traffic.jpg");
        captcha.setCaptchaListener(new Captcha.CaptchaListener() {
            @Override
            public String onAccess(long time) {
                hasAccess = true;
                imNotBot_txt.setText("پازل با موفقیت حل شد");
                imNotBot_txt.setTextColor(Color.BLUE);
                // Login button Click Event
                String name = inputFullName.getText().toString().trim();
                String email = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String pic;

                if(!session.getUserPic().equals("noImage")){
                    pic = session.getUserPic();
                }
                else {
                    pic = "noImage";
                }
                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password,pic);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "لطفا موارد خواسته شده را وارد کنید", Toast.LENGTH_LONG)
                            .show();
                }
                return String.valueOf(time);
            }

            @Override
            public String onFailed(int failedCount) {
                hasAccess = false;
                imNotBot_txt.setText("اشتباهی رخ داده لطفا مجددا سعی کنید");
                imNotBot_txt.setTextColor(Color.RED);
                captcha.reset(false);
                return String.valueOf(failedCount);
            }

            @Override
            public String onMaxFailed() {
                hasAccess = false;
                return "حداکثر ورود";
            }
        });
        profile = findViewById(R.id.profile_img);
        inputFullName = (EditText) findViewById(R.id.name);
        inputUsername = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Register.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }



        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Login.class);
                startActivity(i);
                finish();
            }
        });




        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

    }

    private void responseTask(String response) {
        hideDialog();

        try {
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");
            if (!error) {
                // User successfully stored in MySQL
                // Now store the user in sqlite
                String uid = jObj.getString("uid");

                JSONObject user = jObj.getJSONObject("user");
                String name = user.getString("name");
                String email = user.getString("email");
                String created_at = user
                        .getString("created_at");
                String pic22 = user.getString("pic");

                // Inserting row in users table
                db.addUser(name, email,pic22, uid, created_at);

                Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                // Launch login activity
                Intent intent = new Intent(
                        Register.this,
                        Login.class);
                startActivity(intent);
                finish();
            } else {

                // Error occurred in registration. Get the error
                // message
                String errorMsg = jObj.getString("error_msg");
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void registerUser(String name,String username , String password,final  String pic) {
        pDialog.setMessage("Registering ...");
        showDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RequestInterface api = retrofit.create(RequestInterface.class);

        Call<String> call = api.getUserRegister(name,username,pic,password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonResponse = response.body();
                        responseTask(jsonResponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                        Toast.makeText(Register.this, "ارتباط با مشکل مواجه شد!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Login Error: " + t.getMessage());
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }





    public void selectImage(View view) {
        if(network.isOnline()) {
            Dexter.withActivity(Register.this)
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                                showFileChooser();

                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).
                    withErrorListener(new PermissionRequestErrorListener() {
                        @Override
                        public void onError(DexterError error) {
                            Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .onSameThread()
                    .check();
        }
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setTitle("مجوز های لازم");
        builder.setMessage("این برنامه باید اجازه استفاده از این ویژگی را داشته باشد. شما می\u200Cتوانید آن\u200Cها را در تنظیمات app به آن\u200Cها اعطا کنید.");
        builder.setPositiveButton("برو به تنظیمات برنامه", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
        Log.e("lol","1");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    return;
                }

                Log.e("lol","2");

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this,selectedFileUri);
                Log.i(TAG,"Selected File Path:" + selectedFilePath);
                // Toast.makeText(this, selectedFilePath, Toast.LENGTH_SHORT).show();
                if(selectedFilePath != null && !selectedFilePath.equals("")){

                    dialog = ProgressDialog.show(Register.this, "", "Uploading File...", true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //creating new thread to handle Http Operations
                            Log.e("lol","3");
                            Log.e("path", selectedFilePath);
                            uploadFile(decodeFile(selectedFilePath, 900, 900));

                        }
                    }).start();
                }else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //android upload file to server
    public int uploadFile(final String selectedFilePath){
        Log.e("ready2up",selectedFilePath);
        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/"+ fileName);
                            //s = "http://decoder.pariazar.ir/files/profilepics/"+session.getUserPic();
                            //Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                            Picasso.with(getApplicationContext())
                                    .load(myAddress+session.getUserPic())
                                    .into(profile);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("444",e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Register.this,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(Register.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Register.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }

    }
    private String decodeFile(String path,int DESIREDWIDTH, int DESIREDHEIGHT) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
            } else {
                strMyImagePath = rename(strMyImagePath, unscaledBitmap,10);
                unscaledBitmap.recycle();

                return strMyImagePath;
            }

            // Store to tmp file

            strMyImagePath = rename(strMyImagePath, scaledBitmap,75);
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }

    @NonNull
    private String rename(String strMyImagePath, Bitmap scaledBitmap , int size) {
        String extr = Environment.getExternalStorageDirectory().toString();
        File mFolder = new File(extr + "/TMMFOLDER");
        if (!mFolder.exists()) {
            mFolder.mkdir();
        }
        int min = 1;
        int max = 1000000;

        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;
        String gen= Integer.toHexString(i1);
        s = gen+"tmp.png";
        session.setPicture(s);
        File f = new File(mFolder.getAbsolutePath(), s);

        strMyImagePath = f.getAbsolutePath();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, size, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("222",e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("333",e.getMessage());

            e.printStackTrace();
        }

        scaledBitmap.recycle();
        return strMyImagePath;
    }
}
