package com.laolarou.helloworld.adapters;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.laolarou.helloworld.R;
import com.laolarou.helloworld.models.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    private List<Contact> contactList;
    private Context context;

    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contact_list_view, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.textViewName.setText(contact.getName());
        holder.textViewPhone.setText(contact.getPhone());

        holder.baseLayout.setOnClickListener(v -> makePhoneCall(contact.getPhone()));

        // Edit button
        holder.buttonEdit.setOnClickListener(v -> showEditDialog(position));

        // Delete button
        holder.buttonDelete.setOnClickListener(v -> {
            contactList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Contact deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    private void makePhoneCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        // Ensure permission is granted before initiating the call
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Please grant call permission", Toast.LENGTH_SHORT).show();
        }
    }

    // Show edit dialog
    private void showEditDialog(int position) {
        Contact contact = contactList.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_or_edit_contacts, null);
        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextPhone = dialogView.findViewById(R.id.editTextPhone);

        editTextName.setText(contact.getName());
        editTextPhone.setText(contact.getPhone());

        new AlertDialog.Builder(context)
                .setTitle("Edit Contact")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = editTextName.getText().toString().trim();
                    String newPhone = editTextPhone.getText().toString().trim();

                    if (!newName.isEmpty() && !newPhone.isEmpty()) {
                        contact.setName(newName);
                        contact.setPhone(newPhone);
                        notifyItemChanged(position);
                        Toast.makeText(context, "Contact updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}