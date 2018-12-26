package com.cafedroid.gpstrackerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyNavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    FirebaseAuth auth;
    GoogleMap mMap;
    GoogleApiClient client;
    LocationRequest request;
    LatLng latlng;
    DatabaseReference reference, latReference, lonReference, newReference;
    StorageReference storageReference;
    FirebaseUser user;
    String current_user_name, current_user_email, current_user_imageUrl;
    View header;
    TextView name_textView, email_textView;
    CircleImageView profile_image;
    Marker m;
    ArrayList<String> keyList;
    HashMap<String, Marker> markerHashMap;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_navigation_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        auth = FirebaseAuth.getInstance();
        markerHashMap = new HashMap<>();
        user = auth.getCurrentUser();




        final ValueEventListener markerEvent = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double latitude = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
                Double longitude = Double.parseDouble(dataSnapshot.child("lng").getValue().toString());
                Marker thisMarker = markerHashMap.get(dataSnapshot.getKey());

                if (markerHashMap.get(dataSnapshot.getKey()) == null) {
                    markerHashMap.put(dataSnapshot.getKey(), thisMarker=mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))));
                    thisMarker.setTitle(dataSnapshot.child("name").getValue().toString());
                } else {
                    thisMarker.setPosition(new LatLng(latitude, longitude));
                }
                markerHashMap.get(dataSnapshot.getKey()).setPosition(new LatLng(latitude, longitude));

//                markerHashMap.get(dataSnapshot.getKey()).position(new LatLng(latitude, longitude));
//                mMap.addMarker(markerHashMap.get(dataSnapshot.getKey()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        newReference = reference.child(user.getUid()).child("circleMembers");
        keyList = new ArrayList<>();
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (final DataSnapshot childss : dataSnapshot.getChildren()) {
                        markerHashMap.put(childss.getKey(), null);
                        Log.e("TAG", "onDataChange: " + markerHashMap.get(childss.getKey()));
                        reference.child(Objects.requireNonNull(childss.getKey())).addValueEventListener(markerEvent);


//                                            MarkerOptions options1 = new MarkerOptions();
//                                            options1.position(latlng);
//                                            options1.title(dataSnapshot1.child(childss.getKey()).child("name").getValue().toString());
//
//
//                                            final Marker m1 = mMap.addMarker(options1);
//                                            markerList.add(m1);
//
//
//                                            m1.setPosition(new LatLng(latitude, longitude));


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        for (int i = 0; i < markerHashMap.size(); i++) {
//
//        }


        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent myIntent = new Intent(MyNavigationDrawer.this, LoginActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        });
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);

        name_textView = header.findViewById(R.id.titleText);
        email_textView = header.findViewById(R.id.emailText);
        profile_image = header.findViewById(R.id.imageView);

        storageReference= FirebaseStorage.getInstance().getReference().child("user images/"+user.getUid()+".jpg");
        Log.e("result","hello" + storageReference);
       // Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/gps-tracker-87d5e.appspot.com/o/user%20images%2F4VKrEJ6iV5WzI5Cy4Nbpcw7bklO2.jpg?alt=media&token=ff1f1c35-cce6-4d5c-892c-8558c3640a4b").into(profile_image);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageUri=uri;
                Glide.with(MyNavigationDrawer.this).load(uri).into(profile_image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("TAG", "onFailure: "+storageReference.toString() );
                Toast.makeText(MyNavigationDrawer.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                current_user_name = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                current_user_email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);


                Log.e("image", "onDataChange: " + current_user_imageUrl);
                name_textView.setText(current_user_name);
                email_textView.setText(current_user_email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_signOut) {
            auth.signOut();

        }
//        else if (id == R.id.nav_inviteMembers) {
//
//        }
        else if (id == R.id.nav_joinCircle) {
            Intent i = new Intent(MyNavigationDrawer.this, JoinCircleActivity.class);
            startActivity(i);

        }
//        else if (id == R.id.nav_joinedCircle) {
//
//
//        }
        else if (id == R.id.nav_myCircle) {
            Intent i = new Intent(MyNavigationDrawer.this, MyCircleActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_shareLocation) {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, "My location is : " + "https://www.google.com/maps/@" + latlng.latitude + "," + latlng.longitude + ",17z");
            startActivity(Intent.createChooser(i, "Share Using:"));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        client.connect();


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        request = new LocationRequest().create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(3000);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);

        Location currentLoc=LocationServices.FusedLocationApi.getLastLocation(client);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLoc.getLatitude(),currentLoc.getLongitude()),15.0f));
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location == null) {
            Toast.makeText(getApplicationContext(), "Cannot get location", Toast.LENGTH_SHORT).show();
        } else {

            latReference = reference.child(user.getUid()).child("lat");
            latReference.setValue(location.getLatitude());
            lonReference = reference.child(user.getUid()).child("lng");
            lonReference.setValue(location.getLongitude());
            latlng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions options = new MarkerOptions();
            options.position(latlng);
            options.title("My Location");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            if (m != null) {
                m.remove();
            }
//            m = mMap.addMarker(options);
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
//
//            m.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }


    }


}

