package br.com.ephealth.chatapp.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import br.com.ephealth.chatapp.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolder extends BaseViewHolder {
    private TextView textViewMessage;
    private CircleImageView circleImageView;

    public ChatViewHolder(@NonNull View itemView, @NonNull Context context) {
        super(itemView, context);

        textViewMessage = itemView.findViewById(R.id.textViewShowMessage);
        circleImageView = itemView.findViewById(R.id.circleImageViewProfile);
    }

    public TextView getTextViewMessage() {
        return textViewMessage;
    }

    public void setTextViewMessage(String text) {
        textViewMessage.setText(text);
    }

    @Override
    public CircleImageView getCircleImageView() {
        return circleImageView;
    }

    @Override
    public void setCircleImageView(CircleImageView circleImageView) {
        this.circleImageView = circleImageView;
    }
}
