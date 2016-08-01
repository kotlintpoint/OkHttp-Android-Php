package com.example.ankitsodha.okhttpdemoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ankitsodha.okhttpdemoapp.R;
import com.example.ankitsodha.okhttpdemoapp.activity.MainActivity;
import com.example.ankitsodha.okhttpdemoapp.model.Contact;

import java.util.ArrayList;

/**
 * Created by Admin on 7/26/2016.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    ArrayList<Contact> contactArrayList;
    MainActivity mainActivity;

    public ContactAdapter(ArrayList<Contact> contactArrayList, Context context) {
        this.contactArrayList=contactArrayList;
        mainActivity=(MainActivity)context;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        ContactViewHolder viewHolder=new ContactViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        final Contact contact=contactArrayList.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvNumber.setText(contact.getNumber());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.setContactData(contact);
            }
        });
        holder.ibCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.makeCall(contact.getNumber());
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvNumber;
        ImageButton ibCall;
        View view;

        public ContactViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvNumber=(TextView)itemView.findViewById(R.id.tvNumber);
            ibCall=(ImageButton)itemView.findViewById(R.id.ibCall);
        }
    }
}
