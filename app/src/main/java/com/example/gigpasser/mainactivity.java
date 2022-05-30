package com.example.gigpasser;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.ActionBarDrawerToggle;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.core.view.GravityCompat;
        import androidx.drawerlayout.widget.DrawerLayout;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.gigpasser.Adapter.UserAdapter;
        import com.example.gigpasser.Model.User;
        import com.google.android.material.navigation.NavigationView;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.Query;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.List;

public class mainactivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav_view;

   // private CircleImageView nav_profile_image;
    private TextView nav_fullname,nav_email,nav_lefteye,nav_righteye,nav_type;
    private DatabaseReference userRef;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    UserAdapter userAdapter;

    private List<User> userList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mainactivity);
        drawerLayout=findViewById(R.id.drawerLayout);
        toolbar=findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("Gigpasser");

        nav_view=findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(mainactivity.this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(mainactivity.this);
        progressBar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager= new LinearLayoutManager(mainactivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);


        userList =new ArrayList<>();

        userAdapter = new UserAdapter(mainactivity.this, userList);
        recyclerView.setAdapter(userAdapter);

        userRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.child("type").getValue().toString();
                if (type.equals("donor")){

                    readRecipients();
                }else{
                    readDonor();

                    Log.d("userlistlogd",userList.toString());
                    userAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(userAdapter);
                    progressBar.setVisibility(View.VISIBLE);

                    if (userList.isEmpty()){
                        Toast.makeText(mainactivity.this, "No donor", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainactivity.this, "Arey kay hugayaa ye............", Toast.LENGTH_SHORT).show();
            }
        });

      //  nav_profile_image=nav_view.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_email=nav_view.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_fullname=nav_view.getHeaderView(0).findViewById(R.id.nav_user_fullname);
        nav_lefteye=nav_view.getHeaderView(0).findViewById(R.id.nav_user_lefteye);
        nav_righteye=nav_view.getHeaderView(0).findViewById(R.id.nav_user_righteye);

        nav_type=nav_view.getHeaderView(0).findViewById(R.id.nav_user_type);

        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    //DataSnapshot name=snapshot.child("name");
                    String name = snapshot.child("name").getValue().toString();
                    nav_fullname.setText(name);

                    String email = snapshot.child("email").getValue().toString();
                    nav_email.setText(email);

                    String lefteye = snapshot.child("lefteye").getValue().toString();
                    nav_lefteye.setText(lefteye);

                    String righteye = snapshot.child("righteye").getValue().toString();
                    nav_righteye.setText(righteye);

                    String type = snapshot.child("type").getValue().toString();
                    nav_type.setText(type);

                  /*  if (snapshot.hasChild("profile")){
                        String imageUrl = snapshot.child("profile").getValue().toString();
                        Glide.with(getApplicationContext()).load(imageUrl).into(nav_profile_image);
                    }else{
                        nav_profile_image.setImageResource(R.drawable.profile_image);
                    }

                    String profile = snapshot.child("profile").getValue().toString();
                    Glide.with(getApplicationContext()).load(profile).into(nav_profile_image);*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainactivity.this, "Arey kay hugayaa ye............", Toast.LENGTH_SHORT).show();
            }
        });




    }

   private void readDonor() {
       Toast.makeText(this, "readdonor enter kiya", Toast.LENGTH_SHORT).show();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = userRef.orderByChild("type").equalTo("donor");
        Toast.makeText(this, query.toString(), Toast.LENGTH_SHORT).show();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    Toast.makeText(mainactivity.this, user.getName().toString(), Toast.LENGTH_SHORT).show();
                    userList.add(user);
                    Toast.makeText(mainactivity.this, user.toString(), Toast.LENGTH_SHORT).show();
                }

                Log.d("userlistlogd",userList.toString());
                userAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(userAdapter);
                progressBar.setVisibility(View.VISIBLE);

                if (userList.isEmpty()){
                    Toast.makeText(mainactivity.this, "No donor", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

           @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainactivity.this, "Arey kay hugayaa ye donors kaha gaye............", Toast.LENGTH_SHORT).show();
            }
        });
    }

          private void readRecipients() {
        Toast.makeText(this, "readrecipient enter kiya", Toast.LENGTH_SHORT).show();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = userRef.orderByChild("type").equalTo("recipient");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                    Toast.makeText(mainactivity.this,   userList.toString(), Toast.LENGTH_SHORT).show();
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (userList.isEmpty()){
                    Toast.makeText(mainactivity.this, "No recipients", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainactivity.this, "Arey kay hugayaa ye. recipient kaha gaye...........", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch(item.getItemId()){

           /* case R.id.lefteye:
                Intent intent3=new Intent(mainactivity.this,profile.class);
                intent3.putExtra("group","lefteye");
                startActivity(intent3);
                break;


            case R.id.righteye:
                Intent intent4=new Intent(mainactivity.this,profile.class);
                intent4.putExtra("group","righteye");
                startActivity(intent4);
                break;*/

            case R.id.profile:
                Intent intent=new Intent(mainactivity.this,profile.class);
                startActivity(intent);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent2=new Intent(mainactivity.this,loginactivity.class);
                startActivity(intent2);
                break;

        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}