package br.com.ephealth.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import br.com.ephealth.chatapp.R;
import br.com.ephealth.chatapp.db.IFirebase;
import br.com.ephealth.chatapp.db.model.Chat;
import br.com.ephealth.chatapp.viewHolder.ChatViewHolder;

public class MessageAdapter extends RecyclerView.Adapter {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    FirebaseUser firebaseUser;
    private Context context;
    private List<Chat> list;
    private int selectedItemPosition;
    private MessageAdapter.OnClickItemListener listener;
    private String imageURL;

    public MessageAdapter(Context context, MessageAdapter.OnClickItemListener listener, String imageURL) {
        this.list = new ArrayList<>();
        this.context = context;
        this.listener = listener;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.chat_item_right, parent, false);
        } else {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.chat_item_left, parent, false);
        }

        ChatViewHolder chatViewHolder = new ChatViewHolder(view, context);
        chatViewHolder.itemView.setOnClickListener(view1 -> {
            if (listener != null) {
                setSelectedItemPosition(chatViewHolder.getAdapterPosition());
                Chat item = list.get(getSelectedItemPosition());
                listener.setOnClick(item.getId());
            }
        });

        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatViewHolder viewHolder = (ChatViewHolder) holder;

        Chat chatItem = (Chat) getList().get(position);

        viewHolder.setKey(chatItem.getId());
        viewHolder.setTextViewMessage(chatItem.getMessage());

        if (imageURL.equals(IFirebase.DEFAULT)) {
            viewHolder.getCircleImageView().setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(imageURL).into(viewHolder.getCircleImageView());
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public List<Chat> getList() {
        return list;
    }

    public void setList(List<Chat> list) {
        this.list = list;
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    private void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
    }

    public void clear() {
        this.list = new ArrayList<>();
    }

    private void add(Chat modelItem) {
        list.add(modelItem);
        notifyItemInserted(list.size() - 1);
    }

    public void addAll(List<Chat> newItens) {
        for (Chat modelItem : newItens)
            add(modelItem);
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public void update(int position, Chat item) {
        list.set(position, item);
        notifyItemChanged(position);
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else return MSG_TYPE_LEFT;
    }

    public interface OnClickItemListener {
        void setOnClick(String key);
    }
}
