package com.cdp.mymap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1; // Código de solicitud para permisos de ubicación
    EditText txtLatitud, txtLongitud; // Campos de texto para mostrar latitud y longitud
    GoogleMap mMap; // Mapa de Google

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enlaza los campos de texto para latitud y longitud del diseño XML
        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);

        // Obtiene el fragmento del mapa de Google y establece un callback para cuando el mapa esté listo
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Solicita permisos de ubicación
        solicitarPermisosUbicacion();
    }

    // Método para solicitar permisos de ubicación
    private void solicitarPermisosUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Si los permisos no están concedidos, se solicitan
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            // Si los permisos ya están concedidos, inicia la localización
            iniciarLocalizacion();
        }
    }

    // Método para solicitar permiso de ubicación en segundo plano (para Android 10 o superior)
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void solicitarPermisoUbicacionEnSegundoPlano() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Solicita el permiso de ubicación en segundo plano si aún no está concedido
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_BACKGROUND_LOCATION }, REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    // Maneja el resultado de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show();
                iniciarLocalizacion();

                // Solicita el permiso de ubicación en segundo plano si la versión de Android es 10 o superior
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    solicitarPermisoUbicacionEnSegundoPlano();
                }
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Inicia la localización (aquí se puede implementar la lógica necesaria)
    private void iniciarLocalizacion() {
        Toast.makeText(this, "Iniciando localización", Toast.LENGTH_SHORT).show();
    }

    // Método llamado cuando el mapa está listo para usarse
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Configura el mapa para responder a toques (click) y toques prolongados (long click)
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        // Establece una ubicación inicial en el mapa y mueve la cámara allí
        LatLng mexico = new LatLng(4.6482975, -74.107807);
        mMap.addMarker(new MarkerOptions().position(mexico).title("Bogota"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico));
    }

    // Maneja los toques en el mapa
    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        // Actualiza los campos de texto con la latitud y longitud del toque
        txtLatitud.setText(String.valueOf(latLng.latitude));
        txtLongitud.setText(String.valueOf(latLng.longitude));

        // Limpia el mapa, agrega un marcador en la nueva posición y mueve la cámara allí
        mMap.clear();
        LatLng mexico = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(mexico).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico));
    }

    // Maneja los toques prolongados en el mapa
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        // Igual que en el toque simple, actualiza latitud y longitud, limpia el mapa, coloca un marcador y mueve la cámara
        txtLatitud.setText(String.valueOf(latLng.latitude));
        txtLongitud.setText(String.valueOf(latLng.longitude));

        mMap.clear();
        LatLng mexico = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(mexico).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico));
    }
}
