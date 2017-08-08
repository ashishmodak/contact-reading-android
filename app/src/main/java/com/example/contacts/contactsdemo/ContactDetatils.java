package com.example.contacts.contactsdemo;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactDetatils extends AppCompatActivity {

    private Toolbar toolbar;
    TextView contactNameTxtView, extraInfoTxtView, emailsTxtView;
    CircleImageView contactImageVIew;
    private ContactObj cnObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detatils);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Contact details");
        contactNameTxtView = (TextView) findViewById(R.id.contactNameTxtView);
        extraInfoTxtView = (TextView) findViewById(R.id.extraInfoTxtView);
        emailsTxtView = (TextView) findViewById(R.id.emailsTxtView);
        contactImageVIew = (CircleImageView)findViewById(R.id.contactImage);

        cnObj = (ContactObj) getIntent().getSerializableExtra("cnObj");

        contactNameTxtView.setText(cnObj.getDisplayName());

        if(cnObj.getPhotoURI() != null) {
            Glide.with(this).load(getContactPhotoUri(cnObj.getContactId())).placeholder(R.drawable.ic_user).crossFade().into(contactImageVIew);
        } else {
            Glide.with(this).load(getContactPhotoUri(cnObj.getContactId())).placeholder(R.drawable.ic_user).crossFade().into(contactImageVIew);
        }
        String emailStr = "";
        if(!cnObj.getEmailList().isEmpty()) {
            for(String email : cnObj.getEmailList()) {
                emailStr = email + "\n";
            }
            emailsTxtView.setText(emailStr);
        }
        String extraInfo = "Account type: "  + cnObj.getAccountType() +"\n\n" +
                "Stored number: "  + cnObj.getStoredNumber() +"\n\n" +
                "E164 number: "  + cnObj.getE164Number() +"\n\n" +
                "International number: "  + cnObj.getInternationalNumber() +"\n\n" +
                "Phone number type: "  + cnObj.getPhoneNumberType().toString() +"\n\n" +
                "Version: "  + cnObj.getVersion() +"\n\n" +
                "Id: "  + cnObj.getId() +"\n\n" +
                "Contact id: "  + cnObj.getContactId() +"\n\n" +
                "Photo uri: "  + cnObj.getPhotoURI() +"\n\n" +
                "has phone number: "  + (cnObj.getHasPhoneNumber() == 1 ? "YES":"NO") +"";
        extraInfoTxtView.setText(extraInfo);
    }

    public Uri getContactPhotoUri(long contactId) {
        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        photoUri = Uri.withAppendedPath(photoUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        return photoUri;
    }
}
