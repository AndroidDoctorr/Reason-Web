package com.andrewtorr.reasonweb;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.andrewtorr.reasonweb.Models.Evid;
import com.elevenfifty.reasonweb.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrew on 8/24/2015.
 *
 */

public class SubmitEvidActivity extends ActionBarActivity {
    @Bind(R.id.submit_evid)
    Button submit_evid;
    //TODO: CHANGE THIS TO A PARSE IMAGE VIEW
    @Bind(R.id.evidence_image)
    ImageButton evidence_image;
    @Bind(R.id.evid_title)
    EditText evid_title;
    @Bind(R.id.evid_description)
    EditText evid_description;
    @Bind(R.id.evid_links)
    EditText evid_links;

    private ParseFile imageFile;
    private ParseFile thumbFile;
    private String title;
    private String description;
    private JSONArray links;

    private String TAG = "Evidence Submit";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_evid);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);

        Integer randImage = (int) Math.ceil(3 * Math.random());
        Log.d("Evidence submit: ", randImage.toString());
        switch (randImage) {
            case 1:
                evidence_image.setBackgroundResource(R.mipmap.evidence_placeholder_image1);
                break;
            case 2:
                evidence_image.setBackgroundResource(R.mipmap.evidence_placeholder_image2);
                break;
            case 3:
                evidence_image.setBackgroundResource(R.mipmap.evidence_placeholder_image3);
        }

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.dark_orange));
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "Image captured from camera");
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    evidence_image.setImageBitmap(imageBitmap);

                    try {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] imageData = stream.toByteArray();

                        imageFile = new ParseFile("image.png", imageData);

                        Bitmap thumb = Bitmap.createScaledBitmap(imageBitmap, 1024, 1024, false);

                        ByteArrayOutputStream thStream = new ByteArrayOutputStream();
                        thumb.compress(Bitmap.CompressFormat.PNG, 100, thStream);
                        byte[] image = thStream.toByteArray();

                        thumbFile = new ParseFile(image);

                        stream.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "Image chosen from Disk");
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    evidence_image.setImageBitmap(imageBitmap);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] imageData = stream.toByteArray();

                    imageFile = new ParseFile("image.png", imageData);
                }
                break;
        }
    }

    @OnClick(R.id.evidence_image)
    public void evidenceImage() {
        Log.d(TAG, "Select image");

        //Throw up a dialog to select camera or upload for the Evidence Image

        final Dialog dialog = new Dialog(this, R.style.Dialog_No_Border);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater dialog_inflater = LayoutInflater.from(this);
        View dialog_view = dialog_inflater.inflate(R.layout.camera_dialog, null);

        Button camera_button = (Button) dialog_view.findViewById(R.id.camera_button);
        Button upload_button = (Button) dialog_view.findViewById(R.id.upload_button);

        View.OnClickListener m_clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View p_v) {
                switch (p_v.getId()) {
                    case R.id.camera_button:
                        //Take picture with camera
                        Log.d(TAG, "Camera");

                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);
                        dialog.dismiss();
                        break;
                    case R.id.upload_button:
                        //Upload image from file
                        Log.d(TAG, "Upload");

                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);

                        dialog.dismiss();
                        break;
                    default:
                        dialog.cancel();
                        break;
                }
            }
        };

        camera_button.setOnClickListener(m_clickListener);
        upload_button.setOnClickListener(m_clickListener);

        dialog.setContentView(dialog_view);
        dialog.show();
    }

    @OnClick(R.id.submit_evid)
    public void confirmEvidence() {
        title = evid_title.getText().toString();
        description = evid_description.getText().toString();

        final Dialog dialog = new Dialog(this, R.style.Dialog_No_Border);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater dialog_inflater = LayoutInflater.from(this);
        View dialog_view = dialog_inflater.inflate(R.layout.confirm_evid, null);

        com.parse.ParseImageView confirm_evidence_image = (com.parse.ParseImageView) dialog_view.findViewById(R.id.confirm_evidence_image);
        TextView confirm_evid_title = (TextView) dialog_view.findViewById(R.id.confirm_evid_title);
        TextView confirm_evid_description = (TextView) dialog_view.findViewById(R.id.confirm_evid_description);

        confirm_evid_title.setText(title);
        confirm_evid_description.setText(description);
        confirm_evidence_image.setParseFile(imageFile);

        Button back_button = (Button) dialog_view.findViewById(R.id.back_button);
        Button new_evid_button = (Button) dialog_view.findViewById(R.id.new_evid_button);

        View.OnClickListener m_clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View p_v) {
                switch (p_v.getId()) {
                    case R.id.back_button:
                        Log.d(TAG, "Back to edit");
                        dialog.dismiss();
                        break;
                    case R.id.new_evid_button:
                        Log.d(TAG, "Submit new evidence");

                        Evid newEvid = new Evid();
                        newEvid.setTitle(title);
                        newEvid.setDescription(description);
                        newEvid.setSearchStr(title.toLowerCase() + description.toLowerCase());
                        newEvid.setThumbFile(thumbFile);

                        newEvid.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(SubmitEvidActivity.this, "New Evidence Saved!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    Intent intent = new Intent(SubmitEvidActivity.this, ViewPropActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SubmitEvidActivity.this, "Evidence failed to ave :(", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, e.getMessage());
                                    dialog.dismiss();
                                }
                            }
                        });
                    default:
                        dialog.cancel();
                        break;
                }
            }
        };

        back_button.setOnClickListener(m_clickListener);
        new_evid_button.setOnClickListener(m_clickListener);

        dialog.setContentView(dialog_view);
        dialog.show();
    }

    //Hashtag Stuff:

    private void Linkfiy(String a, TextView textView) {

        Pattern urlPattern = Patterns.WEB_URL;
        Pattern mentionPattern = Pattern.compile("(@[A-Za-z0-9_-]+)");
        Pattern hashtagPattern = Pattern.compile("#(\\w+|\\W+)");

        Matcher o = hashtagPattern.matcher(a);
        Matcher mention = mentionPattern.matcher(a);
        Matcher weblink = urlPattern.matcher(a);

        SpannableString spannableString = new SpannableString(a);

        // --- #hashtag
        while (o.find()) {
            spannableString.setSpan(new NonUnderlinedClickableSpan(o.group(),
                            0), o.start(), o.end(),
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

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public class NonUnderlinedClickableSpan extends ClickableSpan {

        int type;// 0-hashtag , 1- mention, 2- url link
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
                        R.color.orange));
            else if (type == 2)
                ds.setColor(getApplicationContext().getResources().getColor(
                        R.color.red));
            else
                ds.setColor(getApplicationContext().getResources().getColor(
                        R.color.blue));
            ds.setUnderlineText(false);
            // ds.setTypeface(Typeface.DEFAULT_BOLD);
        }

        @Override
        public void onClick(View v) {
            Log.d("click done", "ok " + text);
            if (type == 0) {
                //pass hashtags to activity using intents

            } else if (type == 1) {
                //do for mentions

            } else {
                // Don't link from here, just color them blue

            }
        }
    }
}
