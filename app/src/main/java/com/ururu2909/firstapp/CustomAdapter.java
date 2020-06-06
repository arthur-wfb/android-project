package com.ururu2909.firstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends ListAdapter<Contact, CustomAdapter.ContactViewHolder> {
    private OnContactListener mOnContactListener;

    CustomAdapter(OnContactListener mOnContactListener){
        super(DIFF_CALLBACK);
        this.mOnContactListener = mOnContactListener;
    }

    void setData(ArrayList<Contact> contacts){
        submitList(contacts);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_contact, parent, false);
        return new ContactViewHolder(view, mOnContactListener, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, final int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    public interface OnContactListener{
        void onContactClick(String id);
    }

    private static final DiffUtil.ItemCallback<Contact> DIFF_CALLBACK = new DiffUtil.ItemCallback<Contact>() {
        @Override
        public boolean areItemsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getPhoneNumber1().equals(newItem.getPhoneNumber1());
        }
    };

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView contactName;
        private TextView contactNumber;
        private CustomAdapter.OnContactListener onContactListener;
        private CustomAdapter adapter;

        ContactViewHolder(View v, CustomAdapter.OnContactListener onContactListener, CustomAdapter adapter) {
            super(v);
            this.adapter = adapter;
            contactName = v.findViewById(R.id.contactName);
            contactNumber = v.findViewById(R.id.contactPhoneNumber);
            this.onContactListener = onContactListener;
            itemView.setOnClickListener(this);
        }

        void bind(Contact contact){
            contactName.setText(contact.getName());
            contactNumber.setText(contact.getPhoneNumber1());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION){
                onContactListener.onContactClick(adapter.getCurrentList().get(position).getId());
            }
        }
    }
}