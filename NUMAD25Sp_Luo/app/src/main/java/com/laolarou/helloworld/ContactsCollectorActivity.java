package com.laolarou.helloworld;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.laolarou.helloworld.adapters.ContactAdapter;
import com.laolarou.helloworld.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsCollectorActivity extends AppCompatActivity {
    private static final String KEY_CONTACTS = "contacts";
    private List<Contact> contactList;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contacts_collector);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contact_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.contactListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactList = new ArrayList<>();
        contactAdapter = new ContactAdapter(this, contactList);
        recyclerView.setAdapter(contactAdapter);

        // Restore data if available
        if (savedInstanceState != null) {
            ArrayList<Contact> savedContacts = savedInstanceState.getParcelableArrayList(KEY_CONTACTS);
            if (savedContacts != null) {
                contactList.addAll(savedContacts);
                contactAdapter.notifyDataSetChanged();
            }
        }
    }

    public void createNewContact(View v) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_or_edit_contacts, null);

        // Get EditText references
        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextPhone = dialogView.findViewById(R.id.editTextPhone);

        // Create and show dialog
        new AlertDialog.Builder(this)
                .setTitle("Enter Details")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    String name = editTextName.getText().toString().trim();
                    String phone = editTextPhone.getText().toString().trim();

                    if (!name.isEmpty() && !phone.isEmpty()) {
                        Contact contact = new Contact(name, phone);
                        contactList.add(contact);
                        contactAdapter.notifyItemInserted(contactList.size() - 1);

                        Snackbar bar = Snackbar.make(findViewById(R.id.contact_main), "Name: " + name + ", Phone: " + phone, Snackbar.LENGTH_LONG);
                        bar.setAction("Undo", v1 -> {
                            int index = contactList.indexOf(contact);
                            contactList.remove(contact);
                            contactAdapter.notifyItemRemoved(index);
                        });

                        bar.show();
                    } else {
                        Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_CONTACTS, new ArrayList<>(contactList));
    }

    // Restore the contact list
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Contact> savedContacts = savedInstanceState.getParcelableArrayList(KEY_CONTACTS);
        if (savedContacts != null) {
            contactList.clear();
            contactList.addAll(savedContacts);
            contactAdapter.notifyDataSetChanged();
        }
    }
}