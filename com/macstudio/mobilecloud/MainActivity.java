package com.macstudio.mobilecloud;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.macstudio.mobilecloud.FilesActivity;
import com.macstudio.mobilecloud.dbHelper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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

public class MainActivity
extends AppCompatActivity {
    public TextView app_heading = null;
    public dbHelper db = null;
    public EditText email_up = null;
    public TextView heading = null;
    public Button loginbtn = null;
    private AppBarConfiguration mAppBarConfiguration;
    private String mode = "";
    public EditText pass_up = null;
    public EditText passconf_up = null;
    public EditText passwordText = null;
    public Button signup_btn = null;
    public TextView signup_link = null;
    public Animation slideout = null;
    public ImageView top_img = null;
    public String username = null;
    public EditText usernameText = null;
    public EditText username_up = null;

    @Override
    public void onBackPressed() {
        if (this.mode.equals("up_in")) {
            this.heading.animate().alpha(0.0f);
            this.username_up.animate().alpha(0.0f);
            this.pass_up.animate().alpha(0.0f);
            this.passconf_up.animate().alpha(0.0f);
            this.email_up.animate().alpha(0.0f);
            this.signup_btn.animate().alpha(0.0f);
            this.username_up.setText((CharSequence)"");
            this.email_up.setText((CharSequence)"");
            this.pass_up.setText((CharSequence)"");
            this.passconf_up.setText((CharSequence)"");
            this.username_up.setVisibility(8);
            this.pass_up.setVisibility(8);
            this.passconf_up.setVisibility(8);
            this.email_up.setVisibility(8);
            this.signup_btn.setVisibility(8);
            this.usernameText.setVisibility(0);
            this.passwordText.setVisibility(0);
            this.loginbtn.setVisibility(0);
            this.signup_link.setVisibility(0);
            this.heading.setText((CharSequence)this.getResources().getString(2131755082));
            this.heading.animate().alpha(1.0f).setDuration(300L);
            this.usernameText.animate().alpha(1.0f).setDuration(300L);
            this.passwordText.animate().alpha(1.0f).setDuration(300L);
            this.signup_link.animate().alpha(1.0f).setDuration(300L);
            this.loginbtn.animate().alpha(1.0f).setDuration(300L);
            this.mode = "not";
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle object) {
        Intent intent;
        super.onCreate((Bundle)object);
        this.getWindow().setFlags(512, 512);
        this.setContentView(2131427356);
        this.setSupportActionBar((Toolbar)((Object)this.findViewById(2131230954)));
        object = (CoordinatorLayout)this.findViewById(2131230752);
        object.addView(this.getLayoutInflater().inflate(2131427359, (ViewGroup)object, false));
        if (this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 100);
        }
        this.db = new dbHelper((Context)this);
        if (this.db.checkUser().booleanValue()) {
            object = this.db.getCurrentUser();
            intent = new Intent((Context)this, FilesActivity.class);
            intent.putExtra("currentUser", (String)object);
            this.startActivity(intent);
            this.finish();
            this.overridePendingTransition(2130771984, 2130771985);
        }
        this.usernameText = (EditText)this.findViewById(2131230791);
        this.passwordText = (EditText)this.findViewById(2131230793);
        this.loginbtn = (Button)this.findViewById(2131230759);
        this.loginbtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View object) {
                object = MainActivity.this;
                object.username = object.usernameText.getText().toString();
                String string2 = MainActivity.this.passwordText.getText().toString();
                if (!MainActivity.this.username.equals("") && !string2.equals("")) {
                    object = MainActivity.this;
                    object.username = object.username.toLowerCase();
                    new AsyncLogin().execute((Object[])new String[]{MainActivity.this.username, string2, "signin"});
                    return;
                }
                Toast.makeText((Context)MainActivity.this, (CharSequence)"Please fill all the fields!", (int)1).show();
            }
        });
        this.app_heading = (TextView)this.findViewById(2131230935);
        this.heading = (TextView)this.findViewById(2131230938);
        this.signup_link = (TextView)this.findViewById(2131230934);
        this.username_up = (EditText)this.findViewById(2131230792);
        this.email_up = (EditText)this.findViewById(2131230794);
        this.pass_up = (EditText)this.findViewById(2131230795);
        this.passconf_up = (EditText)this.findViewById(2131230796);
        this.signup_btn = (Button)this.findViewById(2131230760);
        this.top_img = (ImageView)this.findViewById(2131230824);
        this.username_up.setVisibility(8);
        this.pass_up.setVisibility(8);
        this.passconf_up.setVisibility(8);
        this.email_up.setVisibility(8);
        this.signup_btn.setVisibility(8);
        intent = Typeface.createFromAsset((AssetManager)this.getAssets(), (String)"fonts/inkfree.ttf");
        object = Typeface.createFromAsset((AssetManager)this.getAssets(), (String)"fonts/nunito.ttf");
        Typeface typeface = Typeface.createFromAsset((AssetManager)this.getAssets(), (String)"fonts/prestige.otf");
        this.app_heading.setTypeface((Typeface)intent);
        this.heading.setTypeface(typeface);
        this.username_up.setTypeface((Typeface)object);
        this.email_up.setTypeface((Typeface)object);
        this.pass_up.setTypeface((Typeface)object);
        this.passconf_up.setTypeface((Typeface)object);
        this.usernameText.setTypeface((Typeface)object);
        this.passwordText.setTypeface((Typeface)object);
        this.signup_btn.setTypeface((Typeface)object);
        this.signup_link.setTypeface((Typeface)object);
        this.loginbtn.setTypeface((Typeface)object);
        object = AnimationUtils.loadAnimation((Context)this, (int)2130771988);
        this.top_img.startAnimation((Animation)object);
        this.app_heading.animate().alpha(1.0f).setDuration(1000L).setStartDelay(500L);
        this.heading.animate().alpha(1.0f).setDuration(1000L).setStartDelay(500L);
        this.usernameText.animate().alpha(1.0f).setDuration(1000L).setStartDelay(500L);
        this.passwordText.animate().alpha(1.0f).setDuration(1000L).setStartDelay(500L);
        this.signup_link.animate().alpha(1.0f).setDuration(1000L).setStartDelay(500L);
        this.loginbtn.animate().alpha(1.0f).setDuration(1000L).setStartDelay(500L);
        this.signup_link.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                MainActivity.this.heading.animate().alpha(0.0f);
                MainActivity.this.usernameText.animate().alpha(0.0f);
                MainActivity.this.passwordText.animate().alpha(0.0f);
                MainActivity.this.signup_link.animate().alpha(0.0f);
                MainActivity.this.loginbtn.animate().alpha(0.0f);
                MainActivity.this.usernameText.setText((CharSequence)"");
                MainActivity.this.passwordText.setText((CharSequence)"");
                MainActivity.this.usernameText.setVisibility(8);
                MainActivity.this.passwordText.setVisibility(8);
                MainActivity.this.loginbtn.setVisibility(8);
                MainActivity.this.signup_link.setVisibility(8);
                MainActivity.this.heading.setText((CharSequence)MainActivity.this.getResources().getString(2131755090));
                MainActivity.this.username_up.setVisibility(0);
                MainActivity.this.pass_up.setVisibility(0);
                MainActivity.this.passconf_up.setVisibility(0);
                MainActivity.this.email_up.setVisibility(0);
                MainActivity.this.signup_btn.setVisibility(0);
                MainActivity.this.heading.animate().alpha(1.0f);
                MainActivity.this.username_up.animate().alpha(1.0f);
                MainActivity.this.pass_up.animate().alpha(1.0f);
                MainActivity.this.passconf_up.animate().alpha(1.0f);
                MainActivity.this.email_up.animate().alpha(1.0f);
                MainActivity.this.signup_btn.animate().alpha(1.0f);
                if (MainActivity.this.signup_btn.getVisibility() != 0) return;
                MainActivity.this.mode = "up_in";
            }
        });
        this.signup_btn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View object) {
                String string2 = MainActivity.this.username_up.getText().toString();
                object = MainActivity.this.email_up.getText().toString();
                String string3 = MainActivity.this.pass_up.getText().toString();
                String string4 = MainActivity.this.passconf_up.getText().toString();
                if (!string2.equals("") && !((String)object).equals("") && !string3.equals("") && string4.equals("")) {
                    if (!string3.equals(string4)) {
                        Toast.makeText((Context)MainActivity.this, (CharSequence)"Unequal passwords!", (int)1).show();
                        MainActivity.this.pass_up.setText((CharSequence)"");
                        MainActivity.this.passconf_up.setText((CharSequence)"");
                        return;
                    }
                    string4 = string2.toLowerCase();
                    MainActivity.this.db.addUser(string4, string3, (String)object);
                    new AsyncSignup().execute((Object[])new String[]{string4, object, string3});
                    return;
                }
                Toast.makeText((Context)MainActivity.this, (CharSequence)"Please fill all the fields!", (int)1).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        this.getMenuInflater().inflate(2131492866, menu2);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (NavigationUI.navigateUp(Navigation.findNavController(this, 2131230851), this.mAppBarConfiguration)) return true;
        if (super.onSupportNavigateUp()) return true;
        return false;
    }

    private class AsyncLogin
    extends AsyncTask<String, String, String> {
        HttpURLConnection httpURLConnection;
        URL url = null;

        private AsyncLogin() {
        }

        protected String doInBackground(String ... object) {
            int n;
            int n2 = 0;
            int n3 = n = 0;
            int n4 = n2;
            try {
                n3 = n;
                n4 = n2;
                Object object2 = new URL("http://13.234.78.165/~coderx/cloudMobile/service.php");
                n3 = n;
                n4 = n2;
                this.url = object2;
                n3 = n;
                n4 = n2;
                this.httpURLConnection = (HttpURLConnection)this.url.openConnection();
                n3 = n;
                n4 = n2;
                this.httpURLConnection.setReadTimeout(15000);
                n3 = n;
                n4 = n2;
                this.httpURLConnection.setConnectTimeout(10000);
                n3 = n;
                n4 = n2;
                this.httpURLConnection.setRequestMethod("POST");
                n3 = n;
                n4 = n2;
                this.httpURLConnection.setDoOutput(true);
                n3 = n;
                n4 = n2;
                this.httpURLConnection.setDoInput(true);
                n3 = n;
                n4 = n2;
                object2 = this.httpURLConnection.getOutputStream();
                n3 = n;
                n4 = n2;
                n3 = n;
                n4 = n2;
                n3 = n;
                n4 = n2;
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter((OutputStream)object2, "UTF-8");
                n3 = n;
                n4 = n2;
                Appendable appendable = new BufferedWriter(outputStreamWriter);
                n3 = n;
                n4 = n2;
                n3 = n;
                n4 = n2;
                outputStreamWriter = new Uri.Builder();
                n3 = n;
                n4 = n2;
                object = outputStreamWriter.appendQueryParameter("username", object[0]).appendQueryParameter("password", object[1]).appendQueryParameter("action", object[2]).build().getEncodedQuery();
                n3 = n;
                n4 = n2;
                ((Writer)appendable).write((String)object);
                n3 = n;
                n4 = n2;
                ((BufferedWriter)appendable).flush();
                n3 = n;
                n4 = n2;
                ((BufferedWriter)appendable).close();
                n3 = n;
                n4 = n2;
                ((OutputStream)object2).close();
                n3 = n;
                n4 = n2;
                this.httpURLConnection.connect();
                n3 = n;
                n4 = n2;
                n3 = n = this.httpURLConnection.getResponseCode();
                n4 = n;
                n3 = n;
                n4 = n;
                object2 = new StringBuilder();
                n3 = n;
                n4 = n;
                ((StringBuilder)object2).append("h ");
                n3 = n;
                n4 = n;
                ((StringBuilder)object2).append(this.httpURLConnection.getResponseCode());
                n3 = n;
                n4 = n;
                Log.d((String)"info", (String)((StringBuilder)object2).toString());
                if (n != 200) {
                    n3 = n;
                    n4 = n;
                    n3 = n;
                    n4 = n;
                    object2 = new StringBuilder();
                    n3 = n;
                    n4 = n;
                    ((StringBuilder)object2).append(n);
                    n3 = n;
                    n4 = n;
                    ((StringBuilder)object2).append(" ");
                    n3 = n;
                    n4 = n;
                    ((StringBuilder)object2).append((String)object);
                    n3 = n;
                    n4 = n;
                    ((StringBuilder)object2).append(" unsuccesful!");
                    n3 = n;
                    n4 = n;
                    return ((StringBuilder)object2).toString();
                }
                n3 = n;
                n4 = n;
                n3 = n;
                n4 = n;
                n3 = n;
                n4 = n;
                object2 = new InputStreamReader(this.httpURLConnection.getInputStream());
                n3 = n;
                n4 = n;
                object = new BufferedReader((Reader)object2);
                n3 = n;
                n4 = n;
                n3 = n;
                n4 = n;
                appendable = new StringBuilder();
                do {
                    n3 = n;
                    n4 = n;
                    object2 = ((BufferedReader)object).readLine();
                    if (object2 == null) {
                        n3 = n;
                        n4 = n;
                        return ((StringBuilder)appendable).toString();
                    }
                    n3 = n;
                    n4 = n;
                    ((StringBuilder)appendable).append((String)object2);
                } while (true);
            }
            catch (Exception exception) {
                exception.printStackTrace();
                n4 = n3;
            }
            catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(n4);
            stringBuilder.append(" Unsuccesful!");
            return stringBuilder.toString();
        }

        protected void onPostExecute(String object) {
            if (((String)object).equalsIgnoreCase("successful")) {
                MainActivity.this.db.loginCurrent(MainActivity.this.username);
                MainActivity.this.app_heading.animate().alpha(0.0f);
                MainActivity.this.heading.animate().alpha(0.0f);
                MainActivity.this.usernameText.animate().alpha(0.0f);
                MainActivity.this.passwordText.animate().alpha(0.0f);
                MainActivity.this.signup_link.animate().alpha(0.0f);
                MainActivity.this.loginbtn.animate().alpha(0.0f);
                object = MainActivity.this;
                ((MainActivity)object).slideout = AnimationUtils.loadAnimation((Context)object.getApplicationContext(), (int)2130771989);
                MainActivity.this.top_img.startAnimation(MainActivity.this.slideout);
                new Handler().postDelayed(new Runnable(){

                    @Override
                    public void run() {
                        Intent intent = new Intent((Context)MainActivity.this, FilesActivity.class);
                        intent.putExtra("currentUser", MainActivity.this.username);
                        MainActivity.this.startActivity(intent);
                        MainActivity.this.finish();
                        MainActivity.this.overridePendingTransition(2130771984, 2130771985);
                    }
                }, 500L);
                return;
            }
            if (!((String)object).equalsIgnoreCase("unsuccessful")) return;
            Toast.makeText((Context)MainActivity.this, (CharSequence)"Invalid email or password", (int)1).show();
        }

    }

    private class AsyncSignup
    extends AsyncTask<String, String, String> {
        HttpURLConnection httpURLConnection;
        ProgressDialog pdLoading;
        URL url;

        private AsyncSignup() {
            this.pdLoading = new ProgressDialog((Context)MainActivity.this);
            this.url = null;
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
                object2 = this.httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter((OutputStream)object2, "UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                outputStreamWriter = new Uri.Builder();
                bufferedWriter.write(outputStreamWriter.appendQueryParameter("action", "signup").appendQueryParameter("username", object[0]).appendQueryParameter("email", object[1]).appendQueryParameter("password", object[2]).build().getEncodedQuery());
                bufferedWriter.flush();
                bufferedWriter.close();
                ((OutputStream)object2).close();
                this.httpURLConnection.connect();
                int n = this.httpURLConnection.getResponseCode();
                object = new StringBuilder();
                ((StringBuilder)object).append("h ");
                ((StringBuilder)object).append(n);
                Log.d((String)"info", (String)((StringBuilder)object).toString());
                return "Unsuccesful!";
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
                return "Unsuccesful!";
            }
            catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }
            return "Unsuccesful!";
        }

        protected void onPostExecute(String string2) {
            this.pdLoading.dismiss();
            Toast.makeText((Context)MainActivity.this.getApplicationContext(), (CharSequence)"Registration Successful! Please login.", (int)0).show();
            MainActivity.this.usernameText.setVisibility(0);
            MainActivity.this.passwordText.setVisibility(0);
            MainActivity.this.loginbtn.setVisibility(0);
            MainActivity.this.signup_link.setVisibility(0);
            MainActivity.this.heading.setText((CharSequence)MainActivity.this.getResources().getString(2131755082));
            MainActivity.this.username_up.setVisibility(8);
            MainActivity.this.pass_up.setVisibility(8);
            MainActivity.this.passconf_up.setVisibility(8);
            MainActivity.this.email_up.setVisibility(8);
            MainActivity.this.signup_btn.setVisibility(8);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.pdLoading.setMessage((CharSequence)"\tLoading...");
            this.pdLoading.setCancelable(false);
            this.pdLoading.show();
        }
    }

}

