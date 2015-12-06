package com.andrewtorr.reasonweb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.elevenfifty.reasonweb.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Andrew on 8/24/2015.
 *
 */

public class ViewEvidActivity extends ActionBarActivity {
    @Bind(R.id.submit_evid)
    Button submit_evid;
    @Bind(R.id.evidence_image)
    ImageButton evidence_image;
    @Bind(R.id.evid_title)
    TextView evid_title;
    @Bind(R.id.evid_description)
    TextView evid_description;
    @Bind(R.id.evid_links)
    TextView evid_links;

    private String TAG = "Evidence Submit";

    //TODO: Add a Suggest Edit submission button

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_evid);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.dark_orange));
        }

        Linky(evid_links.getText().toString(), evid_links);

        //TODO: Inflate view with info from Evid object
    }

    //Hashtag Stuff

    private void Linky(String a, TextView textView) {

        Pattern urlPattern = Patterns.WEB_URL;
        Pattern mentionPattern = Pattern.compile("(@[A-Za-z0-9_-]+)");
        Pattern hashtagPattern = Pattern.compile("#(\\w+|\\W+)");

        Matcher o = hashtagPattern.matcher(a);
        Matcher mention = mentionPattern.matcher(a);
        Matcher weblink = urlPattern.matcher(a);


        SpannableString spannableString = new SpannableString(a);
        // --- #ter
        while (o.find()) {
            spannableString.setSpan(new NonUnderlinedClickableSpan(o.group(), 0), o.start(), o.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // --- @mention
        while (mention.find()) {
            spannableString.setSpan(
                    new NonUnderlinedClickableSpan(mention.group(), 1), mention.start(), mention.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        // --- @weblink
        while (weblink.find()) {
            spannableString.setSpan(
                    new NonUnderlinedClickableSpan(weblink.group(), 2), weblink.start(), weblink.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        evid_links.setText(spannableString);
        evid_links.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public class NonUnderlinedClickableSpan extends ClickableSpan {
        int type;// 0-term , 1- mention, 2- url link
        String text;// Keyword or url
        String time;

        public NonUnderlinedClickableSpan(String text, int type) {
            this.text = text;
            this.type = type;
            this.time = time;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            //adding colors
            if (type == 1)
                ds.setColor(getApplicationContext().getResources().getColor(
                        R.color.mention));
            else if (type == 2)
                ds.setColor(getApplicationContext().getResources().getColor(
                        R.color.url));
            else
                ds.setColor(getApplicationContext().getResources().getColor(
                        R.color.term));
            ds.setUnderlineText(false);
            // ds.setTypeface(Typeface.DEFAULT_BOLD);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "ok " + text);
            if (type == 0) {
                //handle term
                Log.d(TAG,"term");
            } else if (type == 1) {
                //handle mention
                Log.d(TAG,"mention -- whatever this ends up being, if anything???");
            } else {
                // pass url to webview activity
                Log.d(TAG,"link");
                Intent intent = new Intent(ViewEvidActivity.this, WebviewActivity.class);
                intent.putExtra("url", text);
                //TODO: start for result so it comes back to this???
                startActivity(intent);
            }
        }
    }
}