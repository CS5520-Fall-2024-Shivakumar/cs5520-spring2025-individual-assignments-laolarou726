package com.laolarou.helloworld.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laolarou.helloworld.R;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    LinearLayout baseLayout;
    TextView textViewName, textViewPhone;
    Button buttonEdit, buttonDelete;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        baseLayout = itemView.findViewById(R.id.baseLayout);
        textViewName = itemView.findViewById(R.id.textViewName);
        textViewPhone = itemView.findViewById(R.id.textViewPhone);
        buttonEdit = itemView.findViewById(R.id.buttonEdit);
        buttonDelete = itemView.findViewById(R.id.buttonDelete);
    }
}