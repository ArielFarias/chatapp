package br.com.ephealth.chatapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.ephealth.chatapp.BaseActivity;
import br.com.ephealth.chatapp.MessageActivity;
import br.com.ephealth.chatapp.R;
import br.com.ephealth.chatapp.adapter.UserAdapter;
import br.com.ephealth.chatapp.db.IChatList;
import br.com.ephealth.chatapp.db.IFirebase;
import br.com.ephealth.chatapp.db.model.ChatList;
import br.com.ephealth.chatapp.db.model.User;
import es.dmoral.toasty.Toasty;

public class ChatsFragment extends Fragment {


    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private RecyclerView recyclerView;
    private Context context;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private UserAdapter.OnClickItemListener listener;
    private List<ChatList> userList;

    private UserAdapter.OnClickItemListener setListener() {
        return this.listener = key -> {
            if (key != null && !key.isEmpty()) {
                User item = userAdapter.getList().get(userAdapter.getSelectedItemPosition());
                userAdapter.update(userAdapter.getSelectedItemPosition(), item);

                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userId", key);
                context.startActivity(intent);

            } else {
                Toasty.error(context, getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewChats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager((getContext())));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference(IChatList.CHATLIST).child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatList chatList = snapshot.getValue(ChatList.class);
                    userList.add(chatList);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void chatList() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference(IFirebase.USERS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    for (ChatList chatList : userList) {
                        if (user.getId().equals(chatList.getId()))
                            mUsers.add(user);
                    }
                }
                setListener();
                userAdapter = new UserAdapter(BaseActivity.getGlobalContext(), listener, true);
                userAdapter.addAll(mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
