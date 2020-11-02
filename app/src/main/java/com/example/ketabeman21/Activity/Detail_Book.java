package com.example.ketabeman21.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.Status;
import com.example.ketabeman21.Adapter.DataAdapter4Book;
import com.example.ketabeman21.Helper.SharedPreferencesHelper;
import com.example.ketabeman21.Model.Book;
import com.example.ketabeman21.Model.DB.DatabaseHelperOfflineBook;
import com.example.ketabeman21.R;
import com.example.ketabeman21.Utils.Utils;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.scottyab.aescrypt.AESCrypt;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.ConstantCallSite;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import info.hoang8f.widget.FButton;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import me.grantland.widget.AutofitHelper;
import me.grantland.widget.AutofitTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.ketabeman21.Utils.Constants;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@SuppressLint("NewApi")
public class Detail_Book extends AppCompatActivity {
    //Download managements

    private int REQUEST_WRITE_PERMISSION_CODE = 1;
    private TextView ketab,detail_txt,summery;
    private AutofitTextView fullname,author,publisher,year,edition,pages;
    private ImageView imageView;
    private KenBurnsView back;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private SimpleRatingBar simpleRatingBar;
    private ExpandableTextView expTv1;
    private Button buttonStart;
    private ProgressBar progressBar;
    // private SmileRating smileRating;
    // private RecyclerView mRecyclerView;
    private ArrayList<Book> mArrayList;
    private DataAdapter4Book mAdapter;
    private TextView nameTextview;
    private View view;
    private int BLUR_PRECENTAGE = 50;
    private Handler handler;
    private final int ANIM_DURATION = 5000;
    String num;
    //for download process
    TextView writer;
    private TextToSpeech texttalk;
    Book b;
    int feedBack;
    boolean flag_EZYNAV = false;
    private Dialog d;
    TextView textViewProgressOne, textViewProgressTwo;
    ProgressBar progressBarOne, progressBarTwo;
    int downloadIdOne, downloadIdTwo;
    private Button buttonOne, buttonCancelOne, buttonTwo, buttonCancelTwo;
    private LinearLayout linearLayout;
    private FButton buybook;
    private TextView buybook_tv;
    //include comment mg
    private EditText input;
    TextView txtview_rateCount;
    private WebView webView;
    private File file;
    private DatabaseHelperOfflineBook offlineBookDB;
    private File input_file;
    private String up;
    private String site;
    private SharedPreferencesHelper sharedPreferencesHelper;


    private TextView title;
    private ExpandableTextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        b = (Book) getIntent().getSerializableExtra("ibook");
        //title = findViewById(R.id.bookTitle);
        description = (ExpandableTextView) findViewById(R.id.expand_text_view);
        imageView = findViewById(R.id.cover);
        fullname = findViewById(R.id.fullname);
        ketab = findViewById(R.id.ketab);
        detail_txt = findViewById(R.id.detail_txt);
        back =  findViewById(R.id.back);
        author = findViewById(R.id.author);
        publisher = findViewById(R.id.publisher);
        year = findViewById(R.id.year);
        edition = findViewById(R.id.edition);
        pages = findViewById(R.id.pages);
        summery = findViewById(R.id.summery);

        textViewProgressOne = (TextView) findViewById(R.id.textViewProgressOne);
        buttonOne = (Button) findViewById(R.id.buttonOne);
        textViewProgressOne = (TextView) findViewById(R.id.textViewProgressOne);
        progressBarOne = (ProgressBar) findViewById(R.id.progressBarOne);

        author.setText("نویسنده : "+b.getAuthor());
        publisher.setText("ناشر : "+b.getPublisher());
        year.setText("سال انتشار : "+b.getYear());
        edition.setText("نسخه : "+String.valueOf(b.getEdition()));
        pages.setText("تعداد صفحات : "+String.valueOf(b.getPage()));


        collapsingToolbarLayout =  findViewById(R.id.collapsing_toolbar);
        appBarLayout =  findViewById(R.id.app_bar_layout);

       /* title.setText(b.getFullName());
        title.setTypeface(MainActivity.my_font);*/
        description.setText(b.getDescription());
        fullname.setText(b.getFullName());
        fullname.setTypeface(MainActivity.my_font);
        summery.setTypeface(MainActivity.my_font);

        ketab.setTypeface(MainActivity.my_font);
        detail_txt.setTypeface(MainActivity.my_font);
        AutofitHelper.create(fullname);

        Picasso.with(this)
                .load(R.drawable.empty)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(getApplicationContext())
                                .load(Constants.Image_Directory + b.getCover()) // image url goes here
                                .placeholder(imageView.getDrawable())
                                .into(imageView);
                    }

                    @Override
                    public void onError() {

                    }
                });


        Picasso.with(this)
                .load(Constants.Image_Directory + b.getCover())
                .fit()
                .centerCrop()
                .transform((new BlurTransformation(this,BLUR_PRECENTAGE,1)))
                .into(back);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(b.getFullName());
                    fullname.setText(" ");
                    back.setVisibility(View.INVISIBLE);
                    //title.setVisibility(View.INVISIBLE);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    fullname.setText(b.getFullName());
                    back.setVisibility(View.VISIBLE);
                    //title.setVisibility(View.VISIBLE);

                    isShow = false;
                }
            }
        });

 /*
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(Detail_Book.this);
        //db = new SQLiteHandler(getApplicationContext());
        /*if(offlineBookDB.check_book_ex(b.getPage())){
            makeDBInvisible(View.GONE, View.VISIBLE);

        }else {
            makeDBInvisible(View.VISIBLE, View.GONE);
        }*/

        //will show comments
        //getUserInfo(null,null,4, String.valueOf(b.getId()),null,null);
        //startChart();
        //startAndBindDownloadService();
        //rate();
        //loadJSON();

        /*Picasso.with(getBaseContext())
                .load(b.getProfilePhoto())
                .into(imageOfUserUp);*/
        //getUserInfo(b.getEmail(),3,null,null,null);
        // loadComments();


        /*if(Integer.parseInt(String.valueOf(b.getBookPrice()))!=0){
            makeDBInvisible(View.GONE, View.VISIBLE);
        }*/
    }
    public void StartDownload(View view) {
        Dexter.withActivity(Detail_Book.this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            startDownload(/*b.getBookURL()*/"http://bayanbox.ir/download/6663019729830843802/20110213120514-629-protected.pdf");
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
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Detail_Book.this);
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

    private void startDownload(String url) {
        if (Status.RUNNING == PRDownloader.getStatus(downloadIdOne)) {
            PRDownloader.pause(downloadIdOne);
            return;
        }

        buttonOne.setEnabled(false);
        progressBarOne.setIndeterminate(true);
        progressBarOne.getIndeterminateDrawable().setColorFilter(
                Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);

        if (Status.PAUSED == PRDownloader.getStatus(downloadIdOne)) {
            PRDownloader.resume(downloadIdOne);
            return;
        }
        file = new File (Environment.getExternalStorageDirectory(),"Ketabeman/Books");

        downloadIdOne = PRDownloader.download(url, file.getPath(), b.getName()+".pdf")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        progressBarOne.setIndeterminate(false);
                        buttonOne.setEnabled(true);
                        buttonOne.setText("pause");
                        buttonCancelOne.setEnabled(true);
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                        buttonOne.setText("ادامه");
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        buttonOne.setText("شروع");
                        buttonCancelOne.setEnabled(false);
                        progressBarOne.setProgress(0);
                        textViewProgressOne.setText("");
                        downloadIdOne = 0;
                        progressBarOne.setIndeterminate(false);
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        double progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                        progressBarOne.setProgress((int) progressPercent);
                        textViewProgressOne.setText(Utils.getProgressDisplayLine(progress.currentBytes, progress.totalBytes));
                        progressBarOne.setIndeterminate(false);
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        try {
                            //new SaveData(Detail_Book.this).execute(b.getCover());
                        }
                        catch (Exception e){
                            Toast.makeText(Detail_Book.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(Error error) {
                        textViewProgressOne.setText("");
                        progressBarOne.setProgress(0);
                        downloadIdOne = 0;
                        buttonCancelOne.setEnabled(false);
                        progressBarOne.setIndeterminate(false);
                        buttonOne.setEnabled(true);
                        Log.e("Error",error.toString());
                    }
                });
    }



    class SaveData extends AsyncTask<String,Integer,String> {
        String strFolderName;
        private ProgressDialog dialog;

        public SaveData(Detail_Book activity) {
            dialog = new ProgressDialog(getApplicationContext());
        }
        @Override
        protected void onPreExecute() {
            dialog.setMessage("در حال آماده سازی فایل PDF");
            dialog.show();
            SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(Detail_Book.this);
            try {
              /*  up = GenerateSerialNumber.createTransactionID(sharedPreferencesHelper.getString(Constants.USERNAME));
                ManipulateData.makeReadyPDF(Detail_Book.this,getBaseContext(),b.getName(),Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "DecoderStore/Books/" + b.getName()+".pdf", up,sharedPreferencesHelper.getString(Constants.USERNAME));*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... aurl) {
            int count;
            try {
                URL url = new URL((String) aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                String targetFileName=b.getBookId()+".png";//Change name and subname
                int lenghtOfFile = conexion.getContentLength();
                String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "DecoderStore/Books/covers/";
                File folder = new File(PATH);
                if(!folder.exists()){
                    folder.mkdir();//If there is no folder it will be created.
                }
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(PATH+targetFileName);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress ((int)(total*100/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {}

            return null;
        }
        protected void onProgressUpdate(Integer... progress) {

        }
        protected void onPostExecute(String result) {
            //blackBox = new BlackBox(Detail.this);
           /* try{
                offlineBookDB.insertdata(cvs(b.getBookID()),b.getName(),b.getAuthor(),cvs(b.getEdition()),cvs(b.getPageNumber()),cvs(b.getISBN()),cvs(b.getAuthorType()),b.getDescription(),b.getBookURL(),cvs(b.getJeld()),b.getBookCoverPicture(),String.valueOf( b.getBookPrice()),cvs(b.getPublishYear()),cvs(b.getWaitingList()), AESCrypt.encrypt(blackBox.dhcp2FromJNI(),up),cvs(b.getActionType()),String.valueOf(b.getActionTime()));
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }*/
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "DecoderStore/Books/" + b.getName()+".pdf");
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
          //  open_pdf_in_PDFReader(file,true);
        }
    }
}