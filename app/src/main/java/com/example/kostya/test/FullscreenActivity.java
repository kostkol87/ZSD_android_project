package com.example.kostya.test;

import com.example.kostya.test.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = false;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = false;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;


    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    String getBal;
    TextView textView;
    EditText login;
    EditText password;
    Button enter_btn;
    Button notifications;
    CheckBox rememberMe;
    ProgressBar progressBar;
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        textView = (TextView) findViewById(R.id.result_text);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        enter_btn = (Button) findViewById(R.id.enter_button);
        rememberMe = (CheckBox) findViewById(R.id.rememberCheckBox);
        notifications = (Button) findViewById(R.id.notify_b);
        progressBar = (ProgressBar) findViewById(R.id.pgBar);

        if (rememberMe.isChecked()){
            loadRegIngo();
            loadBalance();
        }

        enter_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                GetBalance gb = new GetBalance();
                gb.execute(login.getText().toString(), password.getText().toString());
                if (rememberMe.isChecked()){
                    saveRegInfo();
                }
            }
        });

        //here need to add notification en/dis button!


        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });


        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.notify_b).setOnTouchListener(mDelayHideTouchListener);
    }
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    SharedPreferences sPref;
    public boolean checkString(String string) {
        if (string == null) return false;
        return string.matches("^-?\\d+$");
    }
    void saveBalance() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        if(checkString(textView.getText().toString())){
            ed.putString("balance", textView.getText().toString());
            ed.commit();
        }
    }

    void saveRegInfo() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("login", login.getText().toString());
        ed.putString("password", password.getText().toString());
        ed.commit();
    }

    void loadBalance(){
        sPref = getPreferences(MODE_PRIVATE);
        String textVietText = sPref.getString("balance", "");
        textView.setText(textVietText);
    }


    void loadRegIngo() {
        sPref = getPreferences(MODE_PRIVATE);
        String loginText = sPref.getString("login", "");
        String passwordText = sPref.getString("password", "");
        login.setText(loginText);
        password.setText(passwordText);
    }

    private class GetBalance extends AsyncTask<String, Void, Void> {
        Editable balance;

        @Override
        protected Void doInBackground(String... params) {
            String balance_str = null;
            try {
                balance_str = HtmlParser.parse(params[0], params[1]);
                balance = new SpannableStringBuilder(balance_str);
            } catch (Exception e) {
                e.printStackTrace();
                balance = new SpannableStringBuilder("0");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(checkString(balance.toString())){
                textView.setText("Баланс: " + balance + " руб.");
            }else {
                textView.setText(balance);
                saveBalance();
            }
            progressBar.setVisibility(View.GONE);
        }
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(10);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
