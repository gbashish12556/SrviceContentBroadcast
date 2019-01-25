package com.test.ashish.androidpractice;

import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ContentProviderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final String TAG = "ContentProviderDemo";
    private TextView textViewQueryResult;
    private EditText contactNameEditText;
    private boolean firstTimeLoad = true;
    Button addContact,updateContact,deleteContact;
    private String[] mColumnProjection= new String[]{
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.CONTACT_STATUS,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
    };

    private String mSelectionClause = ContactsContract.Contacts.DISPLAY_NAME+" = ?";
    private String[] mSelectionArgument = new String[]{"Ashish"};
    private String mOrderBy = ContactsContract.Contacts.DISPLAY_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider);
        loadContact();
        textViewQueryResult = findViewById(R.id.queryResult);
        addContact = findViewById(R.id.addContact);
        updateContact = findViewById(R.id.updateContact);
        deleteContact = findViewById(R.id.deleteContact);
        contactNameEditText = findViewById(R.id.contactName);
        addContact.setOnClickListener(this);
        updateContact.setOnClickListener(this);
        deleteContact.setOnClickListener(this);

    }

    public void loadContact(){
        if(firstTimeLoad) {
            firstTimeLoad = false;
            getSupportLoaderManager().initLoader(1, null, this);
        }else{
            getSupportLoaderManager().restartLoader(1, null, this);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        if(i == 1) {
            return new CursorLoader(this,ContactsContract.Contacts.CONTENT_URI,mColumnProjection,mSelectionClause,mSelectionArgument,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if(cursor != null && cursor.getCount()>0){
            StringBuilder stringBuilder = new StringBuilder("");
            while (cursor.moveToNext()){
                stringBuilder.append(cursor.getString(0)+", "+ cursor.getString(1)+", "+cursor.getString(2));
            }
            textViewQueryResult.setText(stringBuilder.toString());
        }else{
            textViewQueryResult.setText("NO CONTACT FOUND");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.addContact:
                addContact();
            break;
            case R.id.updateContact:
                updateContact();
            break;
            case R.id.deleteContact:
                deleteContact();
            break ;
        }
    }

    public void addContact(){
        String contactName = contactNameEditText.getText().toString();
        if(!contactName.equalsIgnoreCase("")){
            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.NAME,contactName);
            startActivity(intent);
            loadContact();
        }
    }
    public void updateContact(){
        String[] updateValue = contactNameEditText.getText().toString().split(",");
        ContentProviderResult result = null;
        if(updateValue.length == 2){
            String targetString = updateValue[0];
            String newString = updateValue[1];
            String where = ContactsContract.RawContacts._ID +" = ? ";
            String[] params = new String[]{targetString};
            ContentResolver contentResolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY, newString);
            contentResolver.update(ContactsContract.RawContacts.CONTENT_URI,contentValues,where,params);
        }
    }
    public void deleteContact(){
        String contactName = contactNameEditText.getText().toString();
        if(!contactName.equalsIgnoreCase("")){
            String whereClause =  ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY +" = "+contactName;
            getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI,whereClause,null);
            loadContact();
        }

    }
}
