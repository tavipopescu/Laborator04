package ro.pub.cs.systems.eim.lab03.contactsmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private EditText jobTitleEditText;
    private EditText companyEditText;
    private EditText websiteEditText;
    private EditText imEditText;

    private Button showHideAdditionalFieldsButton;
    private Button saveButton;
    private Button cancelButton;
    private LinearLayout additionalFields;

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.show_hide_button) {
                if (additionalFields.getVisibility() == View.INVISIBLE) {
                    showHideAdditionalFieldsButton.setText(getResources().getString(R.string.hide_additional_fields));
                    additionalFields.setVisibility(View.VISIBLE);
                } else {
                    showHideAdditionalFieldsButton.setText(getResources().getString(R.string.show_additional_fields));
                    additionalFields.setVisibility(View.INVISIBLE);
                }
            } else if (v.getId() == R.id.save_button) {
                String name = nameEditText.getText().toString();
                String phoneNumber = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String jobTitle = jobTitleEditText.getText().toString();
                String company = companyEditText.getText().toString();
                String website = websiteEditText.getText().toString();
                String im = imEditText.getText().toString();

                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber);
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
                intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
                ArrayList<ContentValues> contactData = new ArrayList<>();
                ContentValues websiteRow = new ContentValues();
                websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                contactData.add(websiteRow);
                ContentValues imRow = new ContentValues();
                imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                contactData.add(imRow);
                intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
                startActivityForResult(intent, Constants.REQUEST_CODE);
            } else if (v.getId() == R.id.cancel_button) {
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
            }
        }
    }

    private ButtonListener buttonListener = new ButtonListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);

        nameEditText = findViewById(R.id.name_text);
        phoneEditText = findViewById(R.id.number_text);
        emailEditText = findViewById(R.id.email_text);
        addressEditText = findViewById(R.id.address_text);
        jobTitleEditText = findViewById(R.id.job_title_text);
        companyEditText = findViewById(R.id.company_text);
        websiteEditText = findViewById(R.id.website_text);
        imEditText = findViewById(R.id.im_text);
        showHideAdditionalFieldsButton = findViewById(R.id.show_hide_button);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);
        additionalFields = findViewById(R.id.additional_fields);

        showHideAdditionalFieldsButton.setOnClickListener(buttonListener);
        saveButton.setOnClickListener(buttonListener);
        cancelButton.setOnClickListener(buttonListener);

        Intent intent = getIntent();

        if (intent != null) {
            String phoneNumber = intent.getStringExtra("ro.pub.cs.systems.eim.lab03.contactsmanager.PHONE_NUMBER_KEY");
            if (phoneNumber != null) {
                phoneEditText.setText(phoneNumber);
            } else {
                Toast.makeText(this, getResources().getString(R.string.number_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE) {
            setResult(resultCode, new Intent());
            finish();
        }
    }
}