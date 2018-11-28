package com.cafedroid.gpstrackerapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MemberViewHolder>{

    ArrayList<CreateUser> nameList;
    Context mContext;

    public MembersAdapter(ArrayList<CreateUser> nameList, Context mContext) {
        this.nameList = nameList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MemberViewHolder(LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder memberViewHolder, int i) {

        CreateUser MemberObject = nameList.get(i);
        memberViewHolder.MemberTextView.setText(MemberObject.name);

    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder{
        TextView MemberTextView;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            MemberTextView = itemView.findViewById(android.R.id.text1);


        }
    }
}