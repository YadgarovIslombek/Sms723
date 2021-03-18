package com.aid.sms723.ui.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aid.sms723.FileManager;
import com.aid.sms723.model.Number;
import com.aid.sms723.adapter.NumberAdapter;
import com.aid.sms723.R;
import com.aid.sms723.model.UserData;
import com.aid.sms723.ui.fragments.HomeFragment;
import com.aid.sms723.ui.fragments.KabinetFragment;
import com.aid.sms723.util.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG = "MainActivity";
    RecyclerView recyclerView;
    int defaultLimit =7;
    private static final int FILE_EXPLORER_CODE = 10;
    ArrayList<Number> numberList ;
    NumberAdapter adapter;
    Context context;
    Number number;
    TextView textView;
    Button button;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        centerToolbarTitle(toolbar);




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final LinearLayout holder = findViewById(R.id.holder);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerSlide (View drawerView,float slideOffset) {
                float scaleFactor = 7f;
                float slideX = drawerView.getWidth() * slideOffset;

                holder.setTranslationX(slideX);
                holder.setScaleX(1 - (slideOffset / scaleFactor));
                holder.setScaleY(1 - (slideOffset / scaleFactor));

                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);// will remove all possible our aactivity's window bounds
        }
        drawer.addDrawerListener(toggle);
        drawer.setScrimColor(Color.TRANSPARENT);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        View view = navigationView.getHeaderView(0);
        TextView nav_user = view.findViewById(R.id.nav_header_textView);
        TextView nav_userEmail = view.findViewById(R.id.nav_header_email);

        LinearLayout nav_footer = findViewById(R.id.footer_text);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                assert userData !=null;
                nav_user.setText(userData.getUsername().toString() + "");
                nav_userEmail.setText(userData.getMail().toString() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        nav_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), LoginRegister.class));
//                finish();
//                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                // Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();
            }
        });


        displaySelectedScreen(R.id.nav_home);

    }
    private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
//            case R.id.nav_history:
//                fragment = new History();
//                break;
            case R.id.nav_kabinet:
                fragment = new KabinetFragment();
                break;
//            case R.id.nav_personal:
//                fragment = new ProfileFragment();
//                break;
//            case R.id.nav_operator:
//                fragment = new TestFragment();
//                break;
//            case R.id.nav_aloqa:
//                fragment = new ComunicateFragment();
//                break;
//
//            case R.id.nav_offers:
//                fragment = new OffrersFragment();
//                break;
//
//            case R.id.nav_my_order:
//                fragment = new MyOrderFragment();
//                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;

    }


    public boolean TekshiruvPermission(){
        if(Build.VERSION.SDK_INT >= 23){
            //==================Avval ruxsat berilgan bolsa ifni ichiga kiradi Start ==========-//////////
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                Log.v(TAG,"Ruxsat berilgan uje");

                return true;
            }
            //==================Avval ruxsat berilgan bolsa ifni ichiga kiradi End ==========-//////////

            // ==================================================================================================================//

            //==================Agarda user app ga 1 kirib bekor qilsa Start ==========-//////////
            else{
                Log.d(TAG,"Bekor qilindi");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                return false;
            }
            //==================Agarda user app ga 1 kirib bekor qilsa End ==========-//////////
        }else{
            Log.v(TAG,"Ruxsat berildi");

            return true;
        }
    }
    //=========================================External storage ochilik faylni tanladi Start=================================//
//    private class EventChooseFile implements View.OnClickListener {
//
//        @Override
//        public void onClick(View view) {
//
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("text/*");
//            try {
//                startActivityForResult(intent, FILE_EXPLORER_CODE);
//            } catch (android.content.ActivityNotFoundException e) {
//                e.getMessage();
//            }
//        }
//    }
    //=========================================External storage ochilik faylni tanladi End =================================//

    //=========================================External storage Faylni ochib oqimiz Start=================================//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode ==RESULT_OK && requestCode == FILE_EXPLORER_CODE){
//            if(data != null && data.getData() != null){
//                FileManager fileManager = new FileManager();
//                try {
//                    ArrayList<String> numberModellist = fileManager.ReadNumbers(this,data.getData());
//                    if(numberModellist.size() == 0){
//                        Utils.showNeutralAlertDialog(this,"Xatolik",".txt fayl ichidagi malumot o'qishda xatolik");
//                        //recyler viewni obnovit qilibarish garak yani tozalab!!!!!
//                        recyclerView.removeAllViewsInLayout();
//                    }else {
//                        numberList = fileManager.createFiles(numberModellist, numberModellist.size());
//                        Log.d("SizListSize", String.valueOf(numberModellist.size()));
//                        adapter = new NumberAdapter(numberList,this);
//                        recyclerView.setAdapter(adapter);
//
//                    }
////                    Log.d("ReadNumberMetod", String.valueOf(numberModellist));
////                    Log.d("CreateFilesNumberMetod", String.valueOf(numberList));
////                    Log.d("ListSize", String.valueOf(numberModellist.size()));
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case 2:
//                //==================Agarda user app ga 1 kirib ruxsat barsa Start ==========-//////////
//                Log.d(TAG,"Ichki xotira");
//                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
//                    Log.v(TAG,"Ruxsat  "+permissions[0]+ " bo " + grantResults[0]);
//                    new EventChooseFile();
//                }
//                //==================Agarda user app ga 1 kirib ruxsat barsa End ==========-//////////
//                else{
//
//                }
//                break;
//        }
//    }
static void centerToolbarTitle(@NonNull final Toolbar toolbar) {
    final CharSequence title = toolbar.getTitle();
    final ArrayList<View> outViews = new ArrayList<>(1);
    toolbar.findViewsWithText(outViews, title, View.FIND_VIEWS_WITH_TEXT);
    if (!outViews.isEmpty()) {
        final TextView titleView = (TextView) outViews.get(0);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextColor(Color.parseColor("#FFFFFF"));
        final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) titleView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        toolbar.requestLayout();
        //also you can use titleView for changing font: titleView.setTypeface(Typeface);
    }
}
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else{
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Chiqish")
                    .setMessage("Dasturdan chiqmoqchimisiz?")
                    .setPositiveButton("ha", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("yo'q", null).show();}
    }
}