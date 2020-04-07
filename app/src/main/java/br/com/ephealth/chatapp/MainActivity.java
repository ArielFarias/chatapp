package br.com.ephealth.chatapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.ephealth.chatapp.db.IFirebase;
import br.com.ephealth.chatapp.db.IUser;
import br.com.ephealth.chatapp.db.model.User;
import br.com.ephealth.chatapp.fragments.ChatsFragment;
import br.com.ephealth.chatapp.fragments.ProfileFragment;
import br.com.ephealth.chatapp.fragments.UsersFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.circleImageViewProfile)
    CircleImageView circleImageViewProfile;
    @BindView(R.id.textViewUserName)
    TextView textViewUserName;
    @BindView(R.id.toolbar)
    MaterialToolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        context = getApplicationContext();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference(IFirebase.USERS).child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                textViewUserName.setText(user.getUsername());
                if (user.getImageURL().equals(IFirebase.DEFAULT)) {
                    circleImageViewProfile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(circleImageViewProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

        viewPagerAdapter.addFragment(new ChatsFragment(), getString(R.string.chats));
        viewPagerAdapter.addFragment(new UsersFragment(), getString(R.string.users));
        viewPagerAdapter.addFragment(new ProfileFragment(), getString(R.string.profile));

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return false;
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference(IFirebase.USERS).child(firebaseUser.getUid());

        HashMap<String, Object> map = new HashMap<>();
        map.put(IFirebase.STATUS, status);

        reference.updateChildren(map);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status(IUser.ONLINE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        status(IUser.OFFLINE);
    }
}
