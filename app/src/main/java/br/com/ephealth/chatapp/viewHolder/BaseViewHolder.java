package br.com.ephealth.chatapp.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.ephealth.chatapp.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private String key;

    private Context context;
    private CircleImageView circleImageView;
    private CircleImageView circleImageViewOnline;
    private CircleImageView circleImageViewOffline;
    private TextView textView;

    public BaseViewHolder(@NonNull View itemView, @NonNull Context context) {
        super(itemView);

        this.context = context;
        circleImageView = itemView.findViewById(R.id.circleImageViewProfile);
        circleImageViewOnline = itemView.findViewById(R.id.circleImageViewProfileOnline);
        circleImageViewOffline = itemView.findViewById(R.id.circleImageViewProfileOffline);
        textView = itemView.findViewById(R.id.textViewUserName);

    }

    public CircleImageView getCircleImageView() {
        return circleImageView;
    }

    public void setCircleImageView(CircleImageView circleImageView) {
        this.circleImageView = circleImageView;
    }

    public void setTextUserName(String text) {
        textView.setText(text);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public CircleImageView getCircleImageViewOnline() {
        return circleImageViewOnline;
    }

    public void setCircleImageViewOnline(CircleImageView circleImageViewOnline) {
        this.circleImageViewOnline = circleImageViewOnline;
    }

    public CircleImageView getCircleImageViewOffline() {
        return circleImageViewOffline;
    }

    public void setCircleImageViewOffline(CircleImageView circleImageViewOffline) {
        this.circleImageViewOffline = circleImageViewOffline;
    }
}
