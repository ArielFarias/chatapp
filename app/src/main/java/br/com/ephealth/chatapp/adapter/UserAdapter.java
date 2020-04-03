package br.com.ephealth.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.com.ephealth.chatapp.R;
import br.com.ephealth.chatapp.db.model.User;
import br.com.ephealth.chatapp.viewHolder.UserViewHolder;

public class UserAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<User> list;
    private int selectedItemPosition;
    private OnClickItemListener listener;

    public UserAdapter(Context context, OnClickItemListener listener) {
        this.list = new ArrayList<>();
        this.context = context;
        this.listener = listener;
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

        if (userItem.getImageURL().equals("default")) {
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
}
