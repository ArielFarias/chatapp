package br.com.ephealth.chatapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.ephealth.chatapp.GeneralUtils;
import br.com.ephealth.chatapp.MessageActivity;
import br.com.ephealth.chatapp.R;
import br.com.ephealth.chatapp.adapter.UserAdapter;
import br.com.ephealth.chatapp.db.IFirebase;
import br.com.ephealth.chatapp.db.IUser;
import br.com.ephealth.chatapp.db.model.User;
import es.dmoral.toasty.Toasty;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private Context context;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private UserAdapter.OnClickItemListener listener;
    private EditText editTextSearchUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager((getContext())));

        mUsers = new ArrayList<>();

        readUsers();

        editTextSearchUsers = view.findViewById(R.id.editTextSearchUsers);
        editTextSearchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userAdapter.clear();
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchUsers(String s) {

        s = GeneralUtils.normalizedName(s);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference(IFirebase.USERS).orderByChild(IUser.NORMALIZED_NAME)
                .startAt(s)
                .endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert firebaseUser != null;
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        mUsers.add(user);
                    }
                }

                userAdapter = new UserAdapter(getContext(), listener, false);
                userAdapter.addAll(mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void readUsers() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (editTextSearchUsers.getText().toString().isEmpty()) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);

                        assert user != null;
                        assert firebaseUser != null;
                        if (!user.getId().equals(firebaseUser.getUid())) {
                            mUsers.add(user);
                        }
                    }

                    setListener();
                    userAdapter = new UserAdapter(getContext(), listener, false);
                    userAdapter.addAll(mUsers);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
