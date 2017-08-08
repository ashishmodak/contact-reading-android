package com.example.contacts.contactsdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.contacts.contactsdemo.adapters.CountryCodeListAdapter;
import com.example.contacts.contactsdemo.utilities.RecycleViewItemClickListners;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType.UNKNOWN;

public class ContactDemo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, RecycleViewItemClickListners.OnItemClickListener {
    private static final int REQUEST_READ_PHONE_PERMISSION = 99;
    Spinner mCountryCode;
    RelativeLayout loadingView, countryselectiondemo, demoNoContent;
    LinearLayout demo1ParentContainer;
    private RecyclerView recyclerView;
    private ContactsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Button readBtn;
    ArrayList<ContactObj> contactList = new ArrayList<>();

    CountryCodeListAdapter ccList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_demo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCountryCode = (Spinner) findViewById(R.id.phone_cc);
        loadingView = (RelativeLayout)findViewById(R.id.loadingView);
        countryselectiondemo = (RelativeLayout)findViewById(R.id.countryselectiondemo);
        readBtn= (Button)findViewById(R.id.readBtn);
        readBtn.setOnClickListener(this);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView)findViewById(R.id.contactListRV);
        recyclerView.setLayoutManager(mLayoutManager);
        demoNoContent = (RelativeLayout)findViewById(R.id.demoNoContent);
        demo1ParentContainer = (LinearLayout)findViewById(R.id.demo1ParentContainer);

        mAdapter = new ContactsAdapter(this, new ArrayList<ContactObj>());
        recyclerView.setAdapter(mAdapter);

        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    mAdapter.setAnimationsLocked(true);
                }
            }
        });
        RecycleViewItemClickListners.addTo(recyclerView).setOnItemClickListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.contact_demo_in_memory) {
            showContactSyncDemoWithoutPersistentDatabase();
//        } else if (id == R.id.nav_gallery) {
//            showContactSyncDemoWithPersistentDatabase();
//        } else if (id == R.id.nav_slideshow) {
        } else {
            showCountrySelectionDemo();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showContactSyncDemoWithoutPersistentDatabase() {
        countryselectiondemo.setVisibility(View.GONE);
        demo1ParentContainer.setVisibility(View.VISIBLE);
    }
    private void showContactSyncDemoWithPersistentDatabase() {
        countryselectiondemo.setVisibility(View.GONE);
    }
    private void showCountrySelectionDemo() {
        countryselectiondemo.setVisibility(View.VISIBLE);
        demo1ParentContainer.setVisibility(View.GONE);
        CountryLoader cnLoader = new CountryLoader(this);
        cnLoader.execute((Void)null);
    }



    @Override
    public void onClick(View v) {
        if(v==readBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.demo);
            builder.setMessage(R.string.permissiont_str);
            builder.setPositiveButton(R.string.grant, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    checkRequiredPermission();
                }
            });
            builder.setNegativeButton(R.string.deny, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do nothing
                }
            });
            builder.show();
        }
    }

    public void checkRequiredPermission() {
        List<String> permissionsRequiredList = new ArrayList<>();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            permissionsRequiredList.add(Manifest.permission.READ_CONTACTS);
        }
        if(permissionsRequiredList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    permissionsRequiredList.toArray(new String[] {}),
                    REQUEST_READ_PHONE_PERMISSION);
        } else {
            readContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {

                }
            }
            break;
        }
    }


    private void readContacts() {
        ContactLoader contactLoader = new ContactLoader(this);
        contactLoader.execute((Void)null);
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        Intent eventDetailIntent = new Intent(ContactDemo.this, ContactDetatils.class);
        final ContactObj cnObj;
        try {
            cnObj = contactList.get(position);
        } catch (Exception exp) {
            return;
        }
        eventDetailIntent.putExtra("cnObj", cnObj);
        startActivity(eventDetailIntent);

    }

    /*
     * Contact loader
     */
    class ContactLoader extends AsyncTask<Void, Void, Void> {
        Context ctx;
        PhoneNumberUtil util;
        public ContactLoader(Context ctx) {
            this.ctx = ctx;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            contactList.clear(); //Clear contact list;
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Cursor cursor = null;
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String usersCountryISOCode = manager.getNetworkCountryIso();


            if(TextUtils.isEmpty(usersCountryISOCode)) { //If user don't have sim card in a mobile. Assign default conuntry code.
                usersCountryISOCode = "IN";
            }

            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            cursor = ctx.getContentResolver().query(uri, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            if (cursor == null || cursor.getCount() <=0)
                return null; // No contacts fount
            cursor.moveToFirst();
            do {
                String accountType,id,contactId,phoneNumber,contactName,version,photoUri, e164Number,interNationalNumber,email;
                int hasPhoneNumber;
                accountType = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.ACCOUNT_TYPE_AND_DATA_SET));
                id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER));
                photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                try {
                    version = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA_VERSION));
                } catch (Exception e) {
                    version = "0";
                }
                PhoneNumberUtil.PhoneNumberType phoneNumberType;
                try {
                    Phonenumber.PhoneNumber phonenUmberObj = phoneUtil.parse(phoneNumber, usersCountryISOCode.toUpperCase());
                    phoneNumberType = phoneUtil.getNumberType(phonenUmberObj);
                    e164Number = phoneUtil.format(phonenUmberObj, PhoneNumberUtil.PhoneNumberFormat.E164);
                    interNationalNumber = phoneUtil.format(phonenUmberObj, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                } catch (Exception e) {
                    e164Number = "NA";
                    interNationalNumber = "NA";
                    phoneNumberType = UNKNOWN;

                }
                ContactObj cntObj = new ContactObj();
                cntObj.setAccountType(accountType);
                cntObj.setId(id);
                cntObj.setDisplayName(contactName);
                cntObj.setHasPhoneNumber(hasPhoneNumber);
                cntObj.setStoredNumber(phoneNumber);
                cntObj.setE164Number(e164Number);
                cntObj.setInternationalNumber(interNationalNumber);
                cntObj.setPhotoURI(photoUri);
                cntObj.setContactId(Long.parseLong(contactId));
                cntObj.setVersion(version);
                cntObj.setPhoneNumberType(phoneNumberType);


                Cursor cursor1 = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{contactId}, null);
                while (cursor1.moveToNext()) {
                    String emailAddr = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    cntObj.addEmail(emailAddr);
                }
                if(cursor1 != null) {
                    cursor1.close();
                }

                contactList.add(cntObj);
//
                Log.d("Debug", accountType + " "+ contactId +" " + id + " " +phoneNumber + " "+contactName + " "+hasPhoneNumber
                        + " "+photoUri + " "+version + " " + e164Number +  " " + interNationalNumber );

            } while (cursor.moveToNext());

            return  null;
        }

        @Override
        protected void onPostExecute(final Void param) {
            mAdapter.update(contactList);
            recyclerView.setVisibility(View.VISIBLE);
            demoNoContent.setVisibility(View.GONE);
        }
    }


    /*
     * Country Loader Async Task
     */
    class CountryLoader extends AsyncTask<Void, Void, Void> {
        Context ctx;
        PhoneNumberUtil util;
        public CountryLoader(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            loadingView.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
        ccList = new CountryCodeListAdapter(this.ctx,
                android.R.layout.simple_list_item_1,
                android.R.layout.simple_spinner_dropdown_item);
        util = PhoneNumberUtil.getInstance();
        Set<String> ccSet = getSupportedRegions(util);
        for (String cc : ccSet) {
            ccList.add(cc);

        }
        ccList.sort(new Comparator<CountryCodeListAdapter.CountryCode>() {
            public int compare(CountryCodeListAdapter.CountryCode lhs, CountryCodeListAdapter.CountryCode rhs) {
                return lhs.regionName.compareTo(rhs.regionName);
            }
        });
        return null;
        }

        @Override
        protected void onPostExecute(final Void param) {
            mCountryCode.setAdapter(ccList);
            mCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ccList.setSelected(position);
                    CountryCodeListAdapter.CountryCode obj = ccList.getItem(position);
                    Toast.makeText(ContactDemo.this,"You selected: Country="+obj.regionName + " Region Code=" + obj.regionCode + " Region Name="+ obj.countryCode,Toast.LENGTH_LONG ).show();
                }

                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            Phonenumber.PhoneNumber myNum = getMyNumber(this.ctx);
            if (myNum != null) {
                CountryCodeListAdapter.CountryCode cc = new CountryCodeListAdapter.CountryCode();
                cc.regionCode = util.getRegionCodeForNumber(myNum);
                if (cc.regionCode == null)
                    cc.regionCode = util.getRegionCodeForCountryCode(myNum.getCountryCode());
                mCountryCode.setSelection(ccList.getPositionForId(cc));
                //mPhone.setText(String.valueOf(myNum.getNationalNumber()));
            }
            else {
                final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                final String regionCode = tm.getSimCountryIso().toUpperCase(Locale.US);
                CountryCodeListAdapter.CountryCode cc = new CountryCodeListAdapter.CountryCode();
                cc.regionCode = regionCode;
                cc.countryCode = util.getCountryCodeForRegion(regionCode);
                mCountryCode.setSelection(ccList.getPositionForId(cc));
            }
            loadingView.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("unchecked")
    private Set<String> getSupportedRegions(PhoneNumberUtil util) {
        try {
            return (Set<String>) util.getClass()
                    .getMethod("getSupportedRegions")
                    .invoke(util);
        }
        catch (NoSuchMethodException e) {
            try {
                return (Set<String>) util.getClass()
                        .getMethod("getSupportedCountries")
                        .invoke(util);
            } catch (Exception helpme) {
                // ignored
            }
        }
        catch (Exception e) {
            // ignored
        }
        return new HashSet<>();
    }

    @SuppressLint("HardwareIds")
    public Phonenumber.PhoneNumber getMyNumber(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String regionCode = tm.getSimCountryIso().toUpperCase(Locale.US);
            return PhoneNumberUtil.getInstance().parse(tm.getLine1Number(), regionCode);
        }
        catch (Exception e) {
            return null;
        }
    }
}
