package br.com.ephealth.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.ephealth.chatapp.R;
import br.com.ephealth.chatapp.db.IFirebase;
import br.com.ephealth.chatapp.db.IUser;
import br.com.ephealth.chatapp.db.model.Chat;
import br.com.ephealth.chatapp.db.model.User;
import br.com.ephealth.chatapp.viewHolder.UserViewHolder;

public class UserAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<User> list;
    private int selectedItemPosition;
    private OnClickItemListener listener;
    private boolean isChat;
    String theLastMessage;

    public UserAdapter(Context context, OnClickItemListener listener, boolean isChat) {
        this.list = new ArrayList<>();
        this.context = context;
        this.listener = listener;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.base_item, parent, false);

        UserViewHolder userViewHolder = new UserViewHolder(view, context);
        userViewHolder.itemView.setOnClickListener(view1 -> {
            if (listener != null) {
                setSelectedItemPosition(userViewHolder.getAdapterPosition());
                User item = list.get(getSelectedItemPosition());
                listener.setOnClick(item.getId());
            }
        });

        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserViewHolder viewHolder = (UserViewHolder) holder;

        User userItem = (User) getList().get(position);

        viewHolder.setKey(userItem.getId());
        viewHolder.setTextUserName(userItem.getUsername());

        if (isChat) {
            viewHolder.getTextViewLastMessage().setVisibility(View.VISIBLE);
            lastMessage(userItem.getId(), viewHolder.getTextViewLastMessage());
            if (userItem.getStatus().equals(IUser.ONLINE)) {
                viewHolder.getCircleImageViewOnline().setVisibility(View.VISIBLE);
                viewHolder.getCircleImageViewOffline().setVisibility(View.GONE);
            } else {
                viewHolder.getCircleImageViewOnline().setVisibility(View.GONE);
                viewHolder.getCircleImageViewOffline().setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.getTextViewLastMessage().setVisibility(View.GONE);

        }

        if (userItem.getImageURL().equals(IFirebase.DEFAULT)) {
            viewHolder.getCircleImageView().setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(userItem.getImageURL()).into(viewHolder.getCircleImageView());
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
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

    private void add(User modelItem) {
        list.add(modelItem);
        notifyItemInserted(list.size() - 1);
    }

    public void addAll(List<User> newItens) {
        for (User modelItem : newItens)
            add(modelItem);
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public void update(int position, User item) {
        list.set(position, item);
        notifyItemChanged(position);
    }

    public interface OnClickItemListener {
        void setOnClick(String key);
    }

    private void lastMessage(String userId, TextView textViewLastMessage) {
        theLastMessage = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(IFirebase.CHATS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(firebaseUser.getUid())) {
                        theLastMessage = chat.getMessage();
                    }
                }

                if ("default".equals(theLastMessage)) {
                    textViewLastMessage.setText(R.string.noMessages);
                } else {
                    textViewLastMessage.setText(theLastMessage);
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
