package com.macstudio.mobilecloud;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.TextViewCompat;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.zxing.integration.android.IntentIntegrator;
import com.macstudio.mobilecloud.MainActivity;
import com.macstudio.mobilecloud.dbHelper;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class FilesActivity
extends AppCompatActivity {
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public TextView app_head = null;
    public ImageView bg_img1 = null;
    public ImageView bg_img2 = null;
    public String currentUser = null;
    public dbHelper db = null;
    public TableLayout ll = null;
    private AppBarConfiguration mAppBarConfiguration;
    public String mode = "";
    public ProgressDialog pdLoading = null;
    public Button qrcode_btn = null;
    public Button senddata_btn = null;
    public Animation slidein = null;
    public Animation slideout = null;
    public Toolbar toolbar3 = null;
    public String uploadFilePath = null;
    public ValueCallback<Uri[]> uploadMessage;
    public TextView url_link = null;
    public WebView webView = null;

    public static void copyFile(File object, File arrby) throws IOException {
        object = new FileInputStream((File)object);
        FileOutputStream fileOutputStream = new FileOutputStream((File)arrby);
        arrby = new byte[1024];
        do {
            int n;
            if ((n = ((FileInputStream)object).read(arrby)) <= 0) {
                ((FileInputStream)object).close();
                fileOutputStream.close();
                return;
            }
            fileOutputStream.write(arrby, 0, n);
        } while (true);
    }

    private void startQRScanner() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setPrompt("Scan a QR code");
        intentIntegrator.initiateScan();
    }

    public void callchecktimer() {
        final Handler handler = new Handler();
        new Timer().schedule(new TimerTask(){

            @Override
            public void run() {
                handler.post(new Runnable(){

                    @Override
                    public void run() {
                        try {
                            Log.d((String)"info", (String)"callchecktimer ");
                            Asynccheck asynccheck = new Asynccheck();
                            asynccheck.execute((Object[])new String[]{"trans_check"});
                            return;
                        }
                        catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                });
            }

        }, 0L, 15000L);
    }

    public String getFilePath(Context context, Uri object) {
        Context context2 = null;
        try {
            context2 = context = context.getContentResolver().query(object, new String[]{"_data"}, null, null, null);
            int n = context.getColumnIndexOrThrow("_data");
            context2 = context;
            context.moveToFirst();
            context2 = context;
            object = context.getString(n);
            if (context == null) return object;
        }
        catch (Throwable throwable) {
            if (context2 == null) throw throwable;
            context2.close();
            throw throwable;
        }
        context.close();
        return object;
    }

    @Override
    public void onActivityResult(int n, int n2, Intent intent) {
        String string2;
        Context context = IntentIntegrator.parseActivityResult(n, n2, intent);
        if (context != null) {
            if (context.getContents() == null) {
                Toast.makeText((Context)this, (CharSequence)"Cancelled", (int)1).show();
            } else {
                new AsyncQRsignin().execute((Object[])new String[]{context.getContents()});
            }
        } else {
            super.onActivityResult(n, n2, intent);
        }
        if (Build.VERSION.SDK_INT < 23) {
            Toast.makeText((Context)this.getApplicationContext(), (CharSequence)"Failed to Upload Image", (int)1).show();
            return;
        }
        if (n != 100) return;
        if (this.uploadMessage == null) {
            return;
        }
        context = WebChromeClient.FileChooserParams.parseResult((int)n2, (Intent)intent);
        this.uploadFilePath = string2 = this.getFilePath(this.getApplicationContext(), context[0]);
        context = this.getApplicationContext();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("file path : ");
        stringBuilder.append(string2);
        Toast.makeText((Context)context, (CharSequence)stringBuilder.toString(), (int)1).show();
        this.uploadMessage.onReceiveValue((Object)WebChromeClient.FileChooserParams.parseResult((int)n2, (Intent)intent));
        this.uploadMessage = null;
    }

    @Override
    public void onBackPressed() {
        if (this.mode.equals("webview")) {
            this.webView.setVisibility(8);
            this.qrcode_btn.setVisibility(0);
            this.senddata_btn.setVisibility(0);
            this.url_link.setVisibility(0);
            this.mode = "not";
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle object) {
        super.onCreate((Bundle)object);
        this.getWindow().setFlags(512, 512);
        this.currentUser = this.getIntent().getStringExtra("currentUser");
        if (this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 100);
        }
        this.setContentView(2131427356);
        this.setSupportActionBar((Toolbar)((Object)this.findViewById(2131230954)));
        object = (CoordinatorLayout)this.findViewById(2131230752);
        object.addView(this.getLayoutInflater().inflate(2131427358, (ViewGroup)object, false));
        Object object2 = Typeface.createFromAsset((AssetManager)this.getAssets(), (String)"fonts/inkfree.ttf");
        object = Typeface.createFromAsset((AssetManager)this.getAssets(), (String)"fonts/nunito.ttf");
        this.app_head = (TextView)this.findViewById(2131230933);
        this.app_head.setTypeface((Typeface)object2);
        object2 = new StringBuilder();
        ((StringBuilder)object2).append(Environment.getExternalStorageDirectory());
        ((StringBuilder)object2).append("/cmfiles/");
        object2 = new File(((StringBuilder)object2).toString());
        this.ll = (TableLayout)this.findViewById(2131230922);
        Object object3 = new TableRow((Context)this);
        object3.setLayoutParams((ViewGroup.LayoutParams)new TableRow.LayoutParams(-2, -2));
        object3.setPadding(0, 0, 0, 20);
        Object object4 = new TextView((Context)this);
        object4.setText((CharSequence)" File Name ");
        object4.setTextSize(20.0f);
        object4.setTypeface(null, 1);
        object4.setPadding(30, 0, 30, 0);
        object4.setTextColor(Color.parseColor((String)"#000000"));
        TextViewCompat.setTextAppearance((TextView)object4, 2131821048);
        File[] arrfile = new TextView((Context)this);
        arrfile.setText((CharSequence)" Size ");
        arrfile.setTextSize(20.0f);
        arrfile.setTypeface(null, 1);
        arrfile.setPadding(30, 0, 30, 0);
        arrfile.setTextColor(Color.parseColor((String)"#000000"));
        TextViewCompat.setTextAppearance((TextView)arrfile, 2131821048);
        Object object5 = new TextView((Context)this);
        object5.setText((CharSequence)" Type ");
        object5.setTextSize(20.0f);
        object5.setTypeface(null, 1);
        object5.setPadding(30, 0, 30, 0);
        object5.setTextColor(Color.parseColor((String)"#000000"));
        TextViewCompat.setTextAppearance((TextView)object5, 2131821048);
        TextView textView = new TextView((Context)this);
        textView.setText((CharSequence)" remove ");
        textView.setTextSize(20.0f);
        textView.setTypeface(null, 1);
        textView.setTextColor(Color.parseColor((String)"#000000"));
        textView.setPadding(30, 0, 30, 0);
        TextViewCompat.setTextAppearance(textView, 2131821048);
        object3.addView((View)object4, 0);
        object3.addView((View)arrfile, 1);
        object3.addView((View)object5, 2);
        object3.addView((View)textView, 3);
        this.ll.addView((View)object3, 0);
        object3 = new AlertDialog.Builder((Context)this);
        if (!((File)object2).exists()) {
            ((File)object2).mkdir();
        }
        if (((File)object2).exists()) {
            arrfile = ((File)object2).listFiles();
            object2 = new JSONObject();
            object5 = new JSONArray();
            if (arrfile.length != 0) {
                int n = 0;
                while (n < arrfile.length) {
                    object4 = new JSONArray();
                    textView = new TableRow((Context)this);
                    TextView textView2 = new TextView((Context)this);
                    textView2.setText((CharSequence)arrfile[n].getName());
                    textView2.setTextSize(16.0f);
                    textView2.setGravity(1);
                    textView2.setPadding(30, 0, 30, 0);
                    String string2 = arrfile[n].getName();
                    ((ArrayList)object4).add(arrfile[n].getName());
                    TextView textView3 = new TextView((Context)this);
                    textView3.setText((CharSequence)String.valueOf(arrfile[n].length() / 1024L));
                    textView3.setTextSize(16.0f);
                    textView3.setGravity(1);
                    textView3.setPadding(30, 0, 30, 0);
                    ((ArrayList)object4).add(String.valueOf(arrfile[n].length() / 1024L));
                    TextView textView4 = new TextView((Context)this);
                    int n2 = arrfile[n].getName().indexOf(".");
                    String string3 = arrfile[n].getName();
                    textView4.setText((CharSequence)string3.substring(++n2));
                    textView4.setTextSize(16.0f);
                    textView4.setGravity(1);
                    textView4.setPadding(30, 0, 30, 0);
                    ((ArrayList)object4).add(arrfile[n].getName().substring(n2));
                    string3 = new TextView((Context)this);
                    string3.setText((CharSequence)"");
                    string3.setGravity(1);
                    string3.setPadding(30, 0, 30, 0);
                    string3.setCompoundDrawablesWithIntrinsicBounds(2131165288, 0, 0, 0);
                    string3.setOnClickListener(new View.OnClickListener((AlertDialog.Builder)object3, string2){
                        final /* synthetic */ AlertDialog.Builder val$alertDialog;
                        final /* synthetic */ String val$filenm;
                        {
                            this.val$alertDialog = builder;
                            this.val$filenm = string2;
                        }

                        public void onClick(View object) {
                            AlertDialog.Builder builder = this.val$alertDialog.setIcon(17301543).setTitle("Are you sure?");
                            object = new StringBuilder();
                            ((StringBuilder)object).append("You want to delete ");
                            ((StringBuilder)object).append(this.val$filenm);
                            ((StringBuilder)object).append(" file?");
                            builder.setMessage(((StringBuilder)object).toString()).setPositiveButton("Yes", new DialogInterface.OnClickListener(){

                                public void onClick(DialogInterface object, int n) {
                                    object = new StringBuilder();
                                    ((StringBuilder)object).append(Environment.getExternalStorageDirectory());
                                    ((StringBuilder)object).append("/cmfiles/");
                                    ((StringBuilder)object).append(val$filenm);
                                    new File(((StringBuilder)object).toString()).delete();
                                    new AsyncDelfile().execute((Object[])new String[]{val$filenm});
                                    FilesActivity.this.recreate();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener(){

                                public void onClick(DialogInterface dialogInterface, int n) {
                                }
                            }).show();
                        }

                    });
                    textView.addView((View)textView2, 0);
                    textView.addView((View)textView3, 1);
                    textView.addView((View)textView4, 2);
                    textView.addView((View)string3, 3);
                    textView.setLayoutParams((ViewGroup.LayoutParams)new TableRow.LayoutParams(600, 100));
                    textView.setPadding(0, 0, 0, 10);
                    textView3 = this.ll;
                    textView3.addView((View)textView, ++n);
                    ((ArrayList)object5).add(object4);
                }
            }
            try {
                ((HashMap)object2).put(this.currentUser, object5);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            new AsyncGetfiles().execute((Object[])new String[]{((JSONObject)object2).toString(), "createjson"});
            this.callchecktimer();
        }
        this.qrcode_btn = (Button)this.findViewById(2131230761);
        this.qrcode_btn.setTypeface((Typeface)object);
        this.qrcode_btn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                if (FilesActivity.this.checkSelfPermission("android.permission.CAMERA") != 0) {
                    FilesActivity.this.requestPermissions(new String[]{"android.permission.CAMERA"}, 100);
                }
                FilesActivity.this.startQRScanner();
            }
        });
        this.pdLoading = new ProgressDialog((Context)this);
        this.senddata_btn = (Button)this.findViewById(2131230760);
        this.senddata_btn.setTypeface((Typeface)object);
        this.senddata_btn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View object) {
                Log.d((String)"info sd ", (String)"senddata btn pressed.");
                FilesActivity.this.pdLoading.setMessage((CharSequence)"\tLoading...");
                FilesActivity.this.pdLoading.setCancelable(false);
                FilesActivity.this.pdLoading.show();
                object = FilesActivity.this;
                object.mode = "webview";
                object = (ConstraintLayout)((Object)object.findViewById(2131230722));
                FilesActivity filesActivity = FilesActivity.this;
                filesActivity.webView = new WebView(filesActivity.getApplicationContext());
                filesActivity = new DisplayMetrics();
                FilesActivity.this.getWindowManager().getDefaultDisplay().getMetrics((DisplayMetrics)filesActivity);
                int n = ((DisplayMetrics)filesActivity).heightPixels;
                filesActivity = new LinearLayout.LayoutParams(((DisplayMetrics)filesActivity).widthPixels, n);
                FilesActivity.this.webView.setLayoutParams((ViewGroup.LayoutParams)filesActivity);
                object.addView((View)FilesActivity.this.webView);
                FilesActivity.this.webView.getSettings().setJavaScriptEnabled(true);
                FilesActivity.this.webView.getSettings().setAllowFileAccess(true);
                FilesActivity.this.webView.getSettings().setAllowContentAccess(true);
                FilesActivity.this.webView.setWebViewClient(new WebViewClient(){

                    public void onPageFinished(WebView webView, String charSequence) {
                        webView = FilesActivity.this.webView;
                        charSequence = new StringBuilder();
                        ((StringBuilder)charSequence).append("javascript:(function(){document.getElementById('currun').value = '");
                        ((StringBuilder)charSequence).append(FilesActivity.this.currentUser);
                        ((StringBuilder)charSequence).append("';})()");
                        webView.loadUrl(((StringBuilder)charSequence).toString());
                        FilesActivity.this.pdLoading.dismiss();
                    }
                });
                FilesActivity.this.webView.setWebChromeClient(new WebChromeClient(){

                    public boolean onJsAlert(WebView object, String object2, String string2, JsResult jsResult) {
                        if (string2.equals("success")) {
                            object = new File(FilesActivity.this.uploadFilePath).getName();
                            object2 = new StringBuilder();
                            ((StringBuilder)object2).append(Environment.getExternalStorageDirectory());
                            ((StringBuilder)object2).append("/cmfiles/");
                            ((StringBuilder)object2).append((String)object);
                            String string3 = ((StringBuilder)object2).toString();
                            if (!FilesActivity.this.uploadFilePath.equals(string3)) {
                                try {
                                    object = new File(FilesActivity.this.uploadFilePath);
                                    object2 = new File(string3);
                                    FilesActivity.copyFile((File)object, (File)object2);
                                }
                                catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            }
                        }
                        Toast.makeText((Context)FilesActivity.this.getApplicationContext(), (CharSequence)string2, (int)1).show();
                        jsResult.confirm();
                        FilesActivity.this.recreate();
                        FilesActivity.this.webView.setVisibility(8);
                        FilesActivity.this.url_link.setVisibility(0);
                        FilesActivity.this.qrcode_btn.setVisibility(0);
                        FilesActivity.this.senddata_btn.setVisibility(0);
                        FilesActivity.this.mode = "not";
                        return true;
                    }

                    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                        if (FilesActivity.this.uploadMessage != null) {
                            FilesActivity.this.uploadMessage.onReceiveValue(null);
                            FilesActivity.this.uploadMessage = null;
                        }
                        FilesActivity.this.uploadMessage = valueCallback;
                        webView = fileChooserParams.createIntent();
                        try {
                            FilesActivity.this.startActivityForResult((Intent)webView, 100);
                            return true;
                        }
                        catch (ActivityNotFoundException activityNotFoundException) {
                            FilesActivity.this.uploadMessage = null;
                            return false;
                        }
                    }
                });
                FilesActivity.this.webView.loadUrl("http://13.234.78.165/~coderx/cloudMobile/get22.html");
                FilesActivity.this.qrcode_btn.setVisibility(8);
                FilesActivity.this.senddata_btn.setVisibility(8);
                FilesActivity.this.url_link.setVisibility(8);
            }

        });
        this.toolbar3 = (Toolbar)((Object)this.findViewById(2131230955));
        this.setSupportActionBar(this.toolbar3);
        this.getSupportActionBar().setTitle("");
        this.bg_img1 = (ImageView)this.findViewById(2131230825);
        this.bg_img2 = (ImageView)this.findViewById(2131230826);
        this.url_link = (TextView)this.findViewById(2131230936);
        this.slidein = AnimationUtils.loadAnimation((Context)this, (int)2130771986);
        this.bg_img1.startAnimation(this.slidein);
        this.bg_img2.startAnimation(this.slidein);
        this.app_head.animate().alpha(1.0f).setStartDelay(500L);
        this.toolbar3.animate().alpha(1.0f).setStartDelay(500L);
        this.url_link.animate().alpha(1.0f).setStartDelay(500L);
        this.senddata_btn.animate().alpha(1.0f).setStartDelay(500L);
        this.qrcode_btn.animate().alpha(1.0f).setStartDelay(500L);
        this.ll.animate().alpha(1.0f).setStartDelay(1000L);
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        this.getMenuInflater().inflate(2131492865, menu2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 2131230838) {
            return super.onOptionsItemSelected(menuItem);
        }
        this.db = new dbHelper(this.getApplicationContext());
        this.db.logoutCurrent(this.currentUser);
        this.app_head.animate().alpha(0.0f);
        this.toolbar3.animate().alpha(0.0f);
        this.senddata_btn.animate().alpha(0.0f);
        this.qrcode_btn.animate().alpha(0.0f);
        this.url_link.animate().alpha(0.0f);
        this.ll.animate().alpha(0.0f);
        this.slideout = AnimationUtils.loadAnimation((Context)this, (int)2130771987);
        this.bg_img1.startAnimation(this.slideout);
        this.bg_img2.startAnimation(this.slideout);
        Toast.makeText((Context)this.getApplicationContext(), (CharSequence)"Logged out Successsfully!", (int)1).show();
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent intent = new Intent((Context)FilesActivity.this, MainActivity.class);
                FilesActivity.this.startActivity(intent);
                FilesActivity.this.finish();
                FilesActivity.this.overridePendingTransition(2130771984, 2130771985);
            }
        }, 500L);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int n, String[] arrstring, int[] arrn) {
        super.onRequestPermissionsResult(n, arrstring, arrn);
        if (n != 100) return;
        if (arrn[0] == 0) {
            Toast.makeText((Context)this, (CharSequence)"permission granted", (int)1).show();
            return;
        }
        Toast.makeText((Context)this, (CharSequence)"permission denied", (int)1).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (NavigationUI.navigateUp(Navigation.findNavController(this, 2131230851), this.mAppBarConfiguration)) return true;
        if (super.onSupportNavigateUp()) return true;
        return false;
    }

    public void trans_action(String string2, String string3) {
        int n = string2.hashCode();
        if (n != 3739) {
            if (n != 3089570) return;
            if (!string2.equals("down")) return;
            n = 1;
        } else {
            if (!string2.equals("up")) return;
            n = 0;
        }
        if (n == 0) {
            Looper.prepare();
            new Asyncdownfile().execute((Object[])new String[]{string3});
            return;
        }
        if (n != 1) {
            return;
        }
        Log.d((String)"info", (String)"reached to trans_action as down");
    }

    private class AsyncDelfile
    extends AsyncTask<String, String, String> {
        HttpURLConnection httpURLConnection;
        URL url = null;

        private AsyncDelfile() {
        }

        protected String doInBackground(String ... arrstring) {
            try {
                Object object = new URL("http://13.234.78.165/~coderx/cloudMobile/service.php");
                this.url = object;
                this.httpURLConnection = (HttpURLConnection)this.url.openConnection();
                this.httpURLConnection.setReadTimeout(15000);
                this.httpURLConnection.setConnectTimeout(10000);
                this.httpURLConnection.setRequestMethod("POST");
                this.httpURLConnection.setDoOutput(true);
                this.httpURLConnection.setDoInput(true);
                object = this.httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter((OutputStream)object, "UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                outputStreamWriter = new Uri.Builder();
                bufferedWriter.write(outputStreamWriter.appendQueryParameter("file_name", arrstring[0]).appendQueryParameter("username", FilesActivity.this.currentUser).appendQueryParameter("action", "delete_file").build().getEncodedQuery());
                bufferedWriter.flush();
                bufferedWriter.close();
                ((OutputStream)object).close();
                this.httpURLConnection.connect();
                this.httpURLConnection.getResponseCode();
                return "1";
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
                return "1";
            }
            catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }
            return "1";
        }
    }

    private class AsyncGetfiles
    extends AsyncTask<String, String, String> {
        HttpURLConnection httpURLConnection;
        URL url = null;

        private AsyncGetfiles() {
        }

        protected String doInBackground(String ... arrstring) {
            try {
                Object object = new URL("http://13.234.78.165/~coderx/cloudMobile/service.php");
                this.url = object;
                this.httpURLConnection = (HttpURLConnection)this.url.openConnection();
                this.httpURLConnection.setReadTimeout(15000);
                this.httpURLConnection.setConnectTimeout(10000);
                this.httpURLConnection.setRequestMethod("POST");
                this.httpURLConnection.setDoOutput(true);
                this.httpURLConnection.setDoInput(true);
                OutputStream outputStream = this.httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                object = new BufferedWriter(outputStreamWriter);
                outputStreamWriter = new Uri.Builder();
                ((Writer)object).write(outputStreamWriter.appendQueryParameter("jsondata", arrstring[0]).appendQueryParameter("username", FilesActivity.this.currentUser).appendQueryParameter("action", arrstring[1]).build().getEncodedQuery());
                ((BufferedWriter)object).flush();
                ((BufferedWriter)object).close();
                outputStream.close();
                this.httpURLConnection.connect();
                this.httpURLConnection.getResponseCode();
                return "1";
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
                return "1";
            }
            catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }
            return "1";
        }
    }

    private class AsyncQRsignin
    extends AsyncTask<String, String, String> {
        HttpURLConnection httpURLConnection2;
        URL url = null;

        private AsyncQRsignin() {
        }

        protected String doInBackground(String ... object) {
            try {
                Object object2 = new URL("http://13.234.78.165/~coderx/cloudMobile/service.php");
                this.url = object2;
                this.httpURLConnection2 = (HttpURLConnection)this.url.openConnection();
                this.httpURLConnection2.setReadTimeout(3000);
                this.httpURLConnection2.setConnectTimeout(3000);
                this.httpURLConnection2.setRequestMethod("POST");
                this.httpURLConnection2.setDoOutput(true);
                this.httpURLConnection2.setDoInput(true);
                object2 = this.httpURLConnection2.getOutputStream();
                Appendable appendable = new OutputStreamWriter((OutputStream)object2, "UTF-8");
                Object object3 = new BufferedWriter((Writer)appendable);
                appendable = new Uri.Builder();
                object = appendable.appendQueryParameter("qrcode", object[0]).appendQueryParameter("username", "moin5").appendQueryParameter("action", "qr_signin").build().getEncodedQuery();
                ((Writer)object3).write((String)object);
                ((BufferedWriter)object3).flush();
                ((BufferedWriter)object3).close();
                ((OutputStream)object2).close();
                this.httpURLConnection2.connect();
                int n = this.httpURLConnection2.getResponseCode();
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("h ");
                ((StringBuilder)object2).append(n);
                ((StringBuilder)object2).append(" ");
                ((StringBuilder)object2).append((String)object);
                Log.d((String)"info", (String)((StringBuilder)object2).toString());
                if (n != 200) return "0";
                object3 = new InputStreamReader(this.httpURLConnection2.getInputStream());
                object2 = new BufferedReader((Reader)object3);
                appendable = new StringBuilder();
                do {
                    if ((object3 = ((BufferedReader)object2).readLine()) == null) {
                        ((StringBuilder)appendable).toString();
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("h ");
                        ((StringBuilder)object2).append(n);
                        ((StringBuilder)object2).append(" ");
                        ((StringBuilder)object2).append((String)object);
                        Log.d((String)"info", (String)((StringBuilder)object2).toString());
                        return "1";
                    }
                    ((StringBuilder)appendable).append((String)object3);
                } while (true);
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return "1";
            }
        }
    }

    private class Asynccheck
    extends AsyncTask<String, String, String> {
        HttpURLConnection httpURLConnection;
        URL url = null;

        private Asynccheck() {
        }

        protected String doInBackground(String ... object) {
            try {
                Object object2 = new URL("http://13.234.78.165/~coderx/cloudMobile/service.php");
                this.url = object2;
                this.httpURLConnection = (HttpURLConnection)this.url.openConnection();
                this.httpURLConnection.setReadTimeout(15000);
                this.httpURLConnection.setConnectTimeout(10000);
                this.httpURLConnection.setRequestMethod("POST");
                this.httpURLConnection.setDoOutput(true);
                this.httpURLConnection.setDoInput(true);
                Object object3 = this.httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter((OutputStream)object3, "UTF-8");
                object2 = new BufferedWriter(outputStreamWriter);
                outputStreamWriter = new Uri.Builder();
                ((Writer)object2).write(outputStreamWriter.appendQueryParameter("username", FilesActivity.this.currentUser).appendQueryParameter("action", object[0]).build().getEncodedQuery());
                ((BufferedWriter)object2).flush();
                ((BufferedWriter)object2).close();
                ((OutputStream)object3).close();
                this.httpURLConnection.connect();
                int n = this.httpURLConnection.getResponseCode();
                object = new StringBuilder();
                ((StringBuilder)object).append("h ");
                ((StringBuilder)object).append(this.httpURLConnection.getResponseCode());
                Log.d((String)"info", (String)((StringBuilder)object).toString());
                if (n != 200) return "0";
                object2 = new InputStreamReader(this.httpURLConnection.getInputStream());
                object = new BufferedReader((Reader)object2);
                object3 = new StringBuilder();
                while ((object2 = ((BufferedReader)object).readLine()) != null) {
                    ((StringBuilder)object3).append((String)object2);
                }
                object = ((StringBuilder)object3).toString();
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("checkresp");
                ((StringBuilder)object2).append((String)object);
                Log.d((String)"info", (String)((StringBuilder)object2).toString());
                object2 = new JSONParser();
                object3 = (JSONObject)((JSONParser)object2).parse((String)object);
                object2 = (String)((HashMap)object3).get("action");
                object = new StringBuilder();
                ((StringBuilder)object).append("action ");
                ((StringBuilder)object).append((String)object2);
                Log.d((String)"info", (String)((StringBuilder)object).toString());
                boolean bl = ((String)object2).equals("up");
                if (bl) {
                    object2 = (String)((HashMap)object3).get("filename");
                    object = new StringBuilder();
                    ((StringBuilder)object).append("file ");
                    ((StringBuilder)object).append((String)object2);
                    Log.d((String)"info", (String)((StringBuilder)object).toString());
                    FilesActivity.this.trans_action("up", (String)object2);
                    return "1";
                }
                if (!((String)object2).equals("down")) return "1";
                object2 = (String)((HashMap)object3).get("filename");
                object = new StringBuilder();
                ((StringBuilder)object).append("file ");
                ((StringBuilder)object).append((String)object2);
                Log.d((String)"info", (String)((StringBuilder)object).toString());
                FilesActivity.this.trans_action("down", (String)object2);
                return "1";
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return "1";
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
                return "1";
            }
            catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }
            return "1";
        }
    }

    private class Asyncdownfile
    extends AsyncTask<String, String, String> {
        String fileName;
        String folder;
        HttpURLConnection httpURLConnection;
        HttpURLConnection httpURLConnection2;
        URL url = null;

        private Asyncdownfile() {
        }

        protected String doInBackground(String ... object) {
            try {
                Runnable runnable = new StringBuilder();
                ((StringBuilder)((Object)runnable)).append("http://13.234.78.165/~coderx/cloudMobile/");
                ((StringBuilder)((Object)runnable)).append(FilesActivity.this.currentUser);
                ((StringBuilder)((Object)runnable)).append("/assets/");
                ((StringBuilder)((Object)runnable)).append(object[0]);
                Runnable runnable2 = new URL(((StringBuilder)((Object)runnable)).toString());
                this.url = runnable2;
                this.httpURLConnection = (HttpURLConnection)this.url.openConnection();
                this.httpURLConnection.connect();
                this.httpURLConnection.getContentLength();
                runnable = new BufferedInputStream(this.url.openStream(), 8192);
                this.fileName = object[0];
                object = new StringBuilder();
                ((StringBuilder)object).append(Environment.getExternalStorageDirectory());
                ((StringBuilder)object).append("/cmfiles/");
                this.folder = ((StringBuilder)object).toString();
                runnable2 = new StringBuilder();
                ((StringBuilder)((Object)runnable2)).append(this.folder);
                ((StringBuilder)((Object)runnable2)).append(this.fileName);
                object = new FileOutputStream(((StringBuilder)((Object)runnable2)).toString());
                runnable2 = new byte[1024];
                do {
                    int n;
                    if ((n = ((InputStream)((Object)runnable)).read((byte[])runnable2)) == -1) {
                        ((OutputStream)object).flush();
                        ((OutputStream)object).close();
                        ((InputStream)((Object)runnable)).close();
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Downloaded at: ");
                        ((StringBuilder)object).append(this.folder);
                        ((StringBuilder)object).append(this.fileName);
                        object = ((StringBuilder)object).toString();
                        runnable = new StringBuilder();
                        ((StringBuilder)((Object)runnable)).append("post msg ");
                        ((StringBuilder)((Object)runnable)).append((String)object);
                        Log.d((String)"info", (String)((StringBuilder)((Object)runnable)).toString());
                        runnable = FilesActivity.this;
                        runnable2 = new Runnable((String)object){
                            final /* synthetic */ String val$result;
                            {
                                this.val$result = string2;
                            }

                            @Override
                            public void run() {
                                Toast.makeText((Context)FilesActivity.this.getApplicationContext(), (CharSequence)this.val$result, (int)1).show();
                            }
                        };
                        runnable.runOnUiThread(runnable2);
                        object = new StringBuilder();
                        ((StringBuilder)object).append("upload success ");
                        ((StringBuilder)object).append(this.fileName);
                        Log.d((String)"info", (String)((StringBuilder)object).toString());
                        this.url = object = new URL("http://13.234.78.165/~coderx/cloudMobile/service.php");
                        this.httpURLConnection2 = (HttpURLConnection)this.url.openConnection();
                        this.httpURLConnection2.setReadTimeout(3000);
                        this.httpURLConnection2.setConnectTimeout(3000);
                        this.httpURLConnection2.setRequestMethod("POST");
                        this.httpURLConnection2.setDoOutput(true);
                        this.httpURLConnection2.setDoInput(true);
                        runnable = this.httpURLConnection2.getOutputStream();
                        runnable2 = new OutputStreamWriter((OutputStream)((Object)runnable), "UTF-8");
                        object = new BufferedWriter((Writer)((Object)runnable2));
                        runnable2 = new Uri.Builder();
                        runnable2 = runnable2.appendQueryParameter("filename", this.fileName).appendQueryParameter("action", "upload_success").build().getEncodedQuery();
                        ((Writer)object).write((String)((Object)runnable2));
                        ((BufferedWriter)object).flush();
                        ((BufferedWriter)object).close();
                        ((OutputStream)((Object)runnable)).close();
                        this.httpURLConnection2.connect();
                        n = this.httpURLConnection2.getResponseCode();
                        object = new StringBuilder();
                        ((StringBuilder)object).append("h ");
                        ((StringBuilder)object).append(n);
                        ((StringBuilder)object).append(" ");
                        ((StringBuilder)object).append((String)((Object)runnable2));
                        Log.d((String)"info", (String)((StringBuilder)object).toString());
                        object = FilesActivity.this;
                        runnable = new Runnable(){

                            @Override
                            public void run() {
                                FilesActivity.this.recreate();
                            }
                        };
                        object.runOnUiThread(runnable);
                        return "Something went wrong";
                    }
                    ((OutputStream)object).write((byte[])runnable2, 0, n);
                } while (true);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            return "Something went wrong";
        }

        protected void onPreExecute() {
            FilesActivity.this.runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Toast.makeText((Context)FilesActivity.this.getApplicationContext(), (CharSequence)"Downloading new files...", (int)1).show();
                }
            });
        }

    }

}

