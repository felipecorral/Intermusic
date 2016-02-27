package es.leitmotiv.a14felipecm.intermusic;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;

public class Mapa_Interactivo extends FragmentActivity implements OnMapReadyCallback {


    // Google Maps variables
    private GoogleMap mMap;
    private String[] localizacions;
    private ArrayAdapter<String> adaptador;
    private ArrayList<LatLng> posiciones = new ArrayList<LatLng>();
    private String[] latitudes;
    private String[] longitudes;
    final LatLng paris = new LatLng(48.8534100,2.3488000);
    int ciudad_focalizada =0;
    Spinner spciudades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa__interactivo);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gmaps);
        mapFragment.getMapAsync(this);

        localizacions = getResources().getStringArray(R.array.Ciudades);
        latitudes = getResources().getStringArray(R.array.lat_ciudades);
        longitudes = getResources().getStringArray(R.array.lon_ciudades);
        for (int i=0; i<localizacions.length;i++){
            //posiciones.add(new LatLng(44.3,3.25));
            posiciones.add(new LatLng(Float.parseFloat(latitudes[i]),Float.parseFloat(longitudes[i])));
        }
        //Spinner de ciudades
        spciudades = (Spinner) findViewById(R.id.spciudades_maps);

        spciudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ciudad_focalizada = position;
                cambiarMapa();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // Method for the map once ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int m=0; m<posiciones.size();m++){
            mMap.addMarker(new MarkerOptions()
                    .position(posiciones.get(m))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_favorite))
                    .title(localizacions[m]));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posiciones.get(ciudad_focalizada), 3));

    }

    public void siguienteCiudad(View v){
        if(ciudad_focalizada>=posiciones.size()){
            ciudad_focalizada =0;
        }else{
            ciudad_focalizada ++;
        }
        cambiarMapa();
    }

    public void anteriorCiudad(View v){
        if(ciudad_focalizada==0){
            ciudad_focalizada =posiciones.size();
        }else{
            ciudad_focalizada --;
        }
        cambiarMapa();
    }




    public void cambiarMapa(){


        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomOut(), 10000, null);

        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        setTitle(localizacions[ciudad_focalizada]);
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(1), 12000, null);
        // Construct a CameraPosition focusing on posiciones[ciudad_localizada] and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(posiciones.get(ciudad_focalizada))      // asigna las coordenadas de la posicion seleccionada
                .zoom(10)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),10000,null);
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(paris,16));
    }
    
}
