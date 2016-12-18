package id.co.dzaky.apsi_jek;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class User extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener {
    GoogleMap mMap;
    MapView mapview;
    PlacesAutocompleteTextView placesAutocomplete,placesAutocomplete2;
    GoogleApiClient googleApiClient;
    LatLng awal,tujuan;
    TextView textView;
    Button button;
    String distance,duration,harga;
    OrderModel orderModel;

    public User() {
        awal=null;
        tujuan=null;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        ActiveAndroid.initialize(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.user, container, false);
        mapview = (MapView) rootView.findViewById(R.id.mapview);
        mapview.onCreate(savedInstanceState);
        mapview.onResume();

        placesAutocomplete = (PlacesAutocompleteTextView) rootView.findViewById(R.id.places_autocomplete);
        placesAutocomplete2 = (PlacesAutocompleteTextView) rootView.findViewById(R.id.places_autocomplete2);
        textView = (TextView) rootView.findViewById(R.id.textView);

        placesAutocomplete.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(final Place place) {
                        placesAutocomplete.getDetailsFor(place, new DetailsCallback() {
                            @Override
                            public void onSuccess(final PlaceDetails details) {
                                awal=new LatLng(details.geometry.location.lat, details.geometry.location.lng);
                                mMap.addMarker(new MarkerOptions().position(awal));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(awal));
                            }
                            @Override
                            public void onFailure(final Throwable failure) {
                                Log.d("test", "failure " + failure);
                            }
                        });
                    }
                });

        placesAutocomplete2.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(final Place place) {
                        placesAutocomplete2.getDetailsFor(place, new DetailsCallback() {
                            @Override
                            public void onSuccess(final PlaceDetails details) {
                                tujuan=new LatLng(details.geometry.location.lat, details.geometry.location.lng);
                                mMap.addMarker(new MarkerOptions().position(tujuan));
                                mapsetted();
                            }
                            @Override
                            public void onFailure(final Throwable failure) {
                                Log.d("test", "failure " + failure);
                            }
                        });
                    }
                });
        button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder pilihan = new AlertDialog.Builder(getActivity());
                pilihan.setMessage("Yakin untuk memesan?");
                pilihan.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar snackbar = Snackbar.make(rootView, "Terpesan, silahkan tunggu!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        orderModel = new OrderModel();
                        orderModel.awal=placesAutocomplete.getText().toString();
                        orderModel.tujuan=placesAutocomplete2.getText().toString();
                        orderModel.jarak=distance;
                        orderModel.durasi=duration;
                        orderModel.harga=harga;
                        orderModel.save();
                    }
                });
                pilihan.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert = pilihan.create();
                alert.show();
            }
        });
        mapview.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void getCurrentLocation() {
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            placesAutocomplete.setCurrentLocation(location);
            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public void mapsetted(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(awal);
        builder.include(tujuan);
        GoogleDirection.withServerKey(getResources().getString(R.string.googlegeneralkey))
                .from(awal)
                .to(tujuan)
                .avoid(AvoidType.TOLLS)
                .avoid(AvoidType.FERRIES)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if(direction.isOK()) {
                            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                            distance = direction.getRouteList().get(0).getLegList().get(0).getDistance().getText();
                            duration = direction.getRouteList().get(0).getLegList().get(0).getDuration().getText();
                            harga = "Rp "+String.valueOf(Double.parseDouble(distance.replace(" km",""))*3000);
                            textView.setText("Jarak: "+distance+"\nWaktu: "+duration+"\n Harga: "+harga);
                            mMap.addPolyline(DirectionConverter.createPolyline(getActivity(), directionPositionList, 5, Color.BLUE));
                        } else {
                            Log.d("mapse", rawBody);
                            // Do something
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Log.d("mapse", t.toString());
                    }
                });
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
        mMap.moveCamera(cu);
    }
}
