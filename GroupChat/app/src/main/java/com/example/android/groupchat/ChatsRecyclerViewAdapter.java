package com.example.android.groupchat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.groupchat.dao.Message;
import com.example.android.groupchat.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohail on 1/19/16.
 */
public class ChatsRecyclerViewAdapter extends RecyclerView.Adapter<ChatsRecyclerViewAdapter.GenericViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Message> data = new ArrayList<>();
    private Context context;

    private static final int LEFT_VIEW = 0;
    private static final int RIGHT_VIEW = 1;

    public ChatsRecyclerViewAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void insertData(Message newData) {
        int currentSize = data.size();
        data.add(newData);
        notifyItemRangeInserted(currentSize, 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getUser().equals(Utils.currentUser)) return RIGHT_VIEW;
        else return LEFT_VIEW;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case RIGHT_VIEW:
                return new MyViewHolderRight(
                        layoutInflater.inflate(R.layout.list_item_message_right, parent, false));

            case LEFT_VIEW:
                return new MyViewHolderLeft(
                        layoutInflater.inflate(R.layout.list_item_message_left, parent, false));

            default: return null;
        }
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        if (data.isEmpty()) return;

        Message message = data.get(position);

        if (message.getUser().equals(Utils.currentUser)) {
            MyViewHolderRight viewHolder = (MyViewHolderRight) holder;
            viewHolder.userName.setText(message.getUser());
            viewHolder.message.setText(message.getMessage());

        } else {
            MyViewHolderLeft viewHolder = (MyViewHolderLeft) holder;
            viewHolder.userName.setText(message.getUser());
            viewHolder.message.setText(message.getMessage());
        }
    }

    public static class GenericViewHolder extends RecyclerView.ViewHolder {

        public GenericViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class MyViewHolderLeft extends GenericViewHolder {
        TextView userName;
        TextView message;

        public MyViewHolderLeft(View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.user_left);
            message = (TextView) itemView.findViewById(R.id.message_left);
        }
    }

    public static class MyViewHolderRight extends GenericViewHolder {
        TextView userName;
        TextView message;

        public MyViewHolderRight(View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.user_right);
            message = (TextView) itemView.findViewById(R.id.message_right);
        }
    }
}
