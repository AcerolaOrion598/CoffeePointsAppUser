package com.djaphar.coffeepointappuser.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.coffeepointappuser.R;
import com.djaphar.coffeepointappuser.SupportClasses.Adapters.MapPointProductsRecyclerViewAdapter;
import com.djaphar.coffeepointappuser.SupportClasses.ApiClasses.AuthModel;
import com.djaphar.coffeepointappuser.SupportClasses.ApiClasses.Point;
import com.djaphar.coffeepointappuser.SupportClasses.ApiClasses.Product;
import com.djaphar.coffeepointappuser.SupportClasses.ApiClasses.ReviewModel;
import com.djaphar.coffeepointappuser.SupportClasses.ApiClasses.SupervisorModel;
import com.djaphar.coffeepointappuser.SupportClasses.OtherClasses.MapPointsChangeChecker;
import com.djaphar.coffeepointappuser.SupportClasses.OtherClasses.PermissionDriver;
import com.djaphar.coffeepointappuser.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.coffeepointappuser.ViewModels.MainViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraIdleListener, View.OnTouchListener {

    private MainViewModel mainViewModel;
    private MapPointsChangeChecker mapPointsChangeChecker;
    private GoogleMap gMap;
    private SupportMapFragment supportMapFragment;
    private String[] locationPerms = new String[2];
    private ArrayList<Marker> markers = new ArrayList<>(), tempMarkers = new ArrayList<>();
    private Point focusedMarkerInfo;
    private Resources resources;
    private ConstraintLayout pointInfoWindow, reviewWindow;
    private String statusTrueText, statusFalseText;
    private TextView pointName, pointOwner, pointActive, pointRatingTv, supervisorRatingTv;
    private Button sendReviewsBtn;
    private ImageView pointRatingIv, supervisorRatingIv;
    private RecyclerView mapPointProductsRecyclerView;
    private ArrayList<ImageButton> supStars = new ArrayList<>(), courStars = new ArrayList<>();
    private Integer supReview, courReview;
    private HashMap<String, String> authHeaderMap = new HashMap<>();
    private SupervisorModel focusedPointSupervisor;
    private int markerSize, whoMoved, statusTrueColor, statusFalseColor;
    private float infoWindowCorrectionY, infoWindowStartMotionY, infoWindowEndMotionY,
            reviewWindowStartMotionY, reviewWindowCorrectionY, reviewWindowEndMotionY;
    private boolean alreadyFocused = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        mapPointsChangeChecker = new MapPointsChangeChecker(new Handler(), this);
        locationPerms[0] = Manifest.permission.ACCESS_COARSE_LOCATION;
        locationPerms[1] = Manifest.permission.ACCESS_FINE_LOCATION;
        resources = getResources();
        markerSize = (int) resources.getDimension(R.dimen.marker_size);
        statusTrueText = getString(R.string.point_status_true);
        statusFalseText = getString(R.string.point_status_false);
        statusTrueColor = resources.getColor(R.color.colorGreen60);
        statusFalseColor = resources.getColor(R.color.colorRed60);
        pointInfoWindow = findViewById(R.id.point_info_window);
        reviewWindow = findViewById(R.id.review_window);
        pointName = findViewById(R.id.point_name);
        pointOwner = findViewById(R.id.point_owner);
        pointActive = findViewById(R.id.point_active);
        pointRatingTv = findViewById(R.id.point_rating_tv);
        pointRatingIv = findViewById(R.id.point_rating_iv);
        supervisorRatingTv = findViewById(R.id.supervisor_rating_tv);
        supervisorRatingIv = findViewById(R.id.supervisor_rating_iv);
        mapPointProductsRecyclerView = findViewById(R.id.map_point_products_recycler_view);
        sendReviewsBtn = findViewById(R.id.send_reviews_btn);
        Button showReviewWindowBtn = findViewById(R.id.show_review_window_btn);
        Button reviewCancelBtn = findViewById(R.id.review_cancel_btn);
        ImageButton supStarOneBtn = findViewById(R.id.sup_star_one_btn);
        ImageButton supStarTwoBtn = findViewById(R.id.sup_star_two_btn);
        ImageButton supStarThreeBtn = findViewById(R.id.sup_star_three_btn);
        ImageButton supStarFourBtn = findViewById(R.id.sup_star_four_btn);
        ImageButton supStarFiveBtn = findViewById(R.id.sup_star_five_btn);
        ImageButton courStarOneBtn = findViewById(R.id.cour_star_one_btn);
        ImageButton courStarTwoBtn = findViewById(R.id.cour_star_two_btn);
        ImageButton courStarThreeBtn = findViewById(R.id.cour_star_three_btn);
        ImageButton courStarFourBtn = findViewById(R.id.cour_star_four_btn);
        ImageButton courStarFiveBtn = findViewById(R.id.cour_star_five_btn);
        supStars.add(supStarOneBtn);
        supStars.add(supStarTwoBtn);
        supStars.add(supStarThreeBtn);
        supStars.add(supStarFourBtn);
        supStars.add(supStarFiveBtn);
        courStars.add(courStarOneBtn);
        courStars.add(courStarTwoBtn);
        courStars.add(courStarThreeBtn);
        courStars.add(courStarFourBtn);
        courStars.add(courStarFiveBtn);

        mainViewModel.requestUser(new AuthModel(""));

        mainViewModel.getUser().observe(this, user -> {
            if (user == null) {
                return;
            }
            authHeaderMap.put(getString(R.string.authorization_header), user.getToken());
        });

        mainViewModel.getSupervisor().observe(this, supervisor -> {
            if (supervisor == null) {
                return;
            }

            focusedPointSupervisor = supervisor;

            String name = supervisor.getName();
            if (name == null || name.equals("")) {
                name = getString(R.string.some_string_is_null_text);
            }
            pointOwner.setText(name);

            Float rating = supervisor.getAvgRating();
            if (rating != null) {
                supervisorRatingIv.setVisibility(View.VISIBLE);
                supervisorRatingTv.setText(String.format(Locale.US, "%.2f", rating));
            }

        });

        pointInfoWindow.setOnTouchListener(this);
        reviewWindow.setOnTouchListener(this);

        if (supportMapFragment == null) {
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);
            if (supportMapFragment != null) {
                supportMapFragment.getMapAsync(this);
            }
        }

        showReviewWindowBtn.setOnClickListener(lView -> {
            for (ImageButton star : supStars) {
                star.setImageDrawable(resources.getDrawable(R.drawable.ic_star_48dp));
            }

            for (ImageButton star : courStars) {
                star.setImageDrawable(resources.getDrawable(R.drawable.ic_star_48dp));
            }

            supReview = null;
            courReview = null;
            sendReviewsBtn.setEnabled(false);

            ViewDriver.hideView(pointInfoWindow, R.anim.bottom_view_hide_animation, this);
            ViewDriver.showView(reviewWindow, R.anim.top_view_show_animation, this);
        });
        reviewCancelBtn.setOnClickListener(lView -> {
            ViewDriver.hideView(reviewWindow, R.anim.top_view_hide_animation, this);
            ViewDriver.showView(pointInfoWindow, R.anim.bottom_view_show_animation, this);
        });

        supStarOneBtn.setOnClickListener(lView -> setCheckedStars(lView, supStars));
        supStarTwoBtn.setOnClickListener(lView -> setCheckedStars(lView, supStars));
        supStarThreeBtn.setOnClickListener(lView -> setCheckedStars(lView, supStars));
        supStarFourBtn.setOnClickListener(lView -> setCheckedStars(lView, supStars));
        supStarFiveBtn.setOnClickListener(lView -> setCheckedStars(lView, supStars));
        courStarOneBtn.setOnClickListener(lView -> setCheckedStars(lView, courStars));
        courStarTwoBtn.setOnClickListener(lView -> setCheckedStars(lView, courStars));
        courStarThreeBtn.setOnClickListener(lView -> setCheckedStars(lView, courStars));
        courStarFourBtn.setOnClickListener(lView -> setCheckedStars(lView, courStars));
        courStarFiveBtn.setOnClickListener(lView -> setCheckedStars(lView, courStars));

        sendReviewsBtn.setOnClickListener(lView -> {
            mainViewModel.requestSetSupervisorReview(focusedPointSupervisor.get_id(), authHeaderMap,
                    new ReviewModel(supReview), gMap.getProjection().getVisibleRegion().latLngBounds);
            mainViewModel.requestSetCourierReview(focusedMarkerInfo.get_id(), authHeaderMap,
                    new ReviewModel(courReview), gMap.getProjection().getVisibleRegion().latLngBounds);
            ViewDriver.hideView(reviewWindow, R.anim.top_view_hide_animation, this);
            removeFocusFromMarker();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapPointsChangeChecker.startMapPointsChangeCheck();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapPointsChangeChecker.stopMapPointsChangeCheck();
        if (mainViewModel == null || gMap == null) {
            return;
        }
        LatLngBounds bounds = gMap.getProjection().getVisibleRegion().latLngBounds;
        mainViewModel.setLastScreenBounds(bounds.northeast.latitude, bounds.northeast.longitude, bounds.southwest.latitude, bounds.southwest.longitude);
    }

    @Override
    public void onBackPressed() {
        if (pointInfoWindow.getVisibility() == View.VISIBLE) {
            ViewDriver.hideView(pointInfoWindow, R.anim.bottom_view_hide_animation, this);
            removeFocusFromMarker();
            return;
        }

        if (reviewWindow.getVisibility() == View.VISIBLE) {
            ViewDriver.hideView(reviewWindow, R.anim.top_view_hide_animation, this);
            ViewDriver.showView(pointInfoWindow, R.anim.bottom_view_show_animation, this);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        mainViewModel.getLastBounds().observe(this, lastBounds -> {
            if (alreadyFocused || lastBounds == null || gMap == null) {
                return;
            }
            LatLngBounds bounds = new LatLngBounds(new LatLng(lastBounds.getSouthLat(), lastBounds.getSouthLong()),
                    new LatLng(lastBounds.getNorthLat(), lastBounds.getNorthLong()));
            gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
            requestPointsInBox();
        });

        if (PermissionDriver.hasPerms(locationPerms, this)) {
            getDeviceLocation();
            gMap.setMyLocationEnabled(true);
        } else {
            PermissionDriver.requestPerms(this, locationPerms);
        }

        gMap.setOnCameraMoveStartedListener(this);
        gMap.setOnCameraIdleListener(this);

        mainViewModel.getPoints().observe(this, points -> {
            drawMarkers(points);
            removeMarkers();
            rewriteMarkerList();
            gMap.setOnMarkerClickListener(marker -> {
                showPointInfo(marker);
                focusedMarkerInfo = (Point) marker.getTag();
                return false;
            });
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        getDeviceLocation();
        gMap.setMyLocationEnabled(true);
    }

    private void setCheckedStars(View view, ArrayList<ImageButton> stars) {
        for (ImageButton star : stars) {
            star.setImageDrawable(resources.getDrawable(R.drawable.ic_star_48dp));
        }
        int i = 0;
        for (ImageButton star : stars) {
            i++;
            star.setImageDrawable(resources.getDrawable(R.drawable.ic_star_checked_48dp));
            if (star == view) {
                break;
            }
        }
        if (stars == supStars) {
            supReview = i;
        } else {
            courReview = i;
        }
        if (supReview == null || courReview == null) {
            return;
        }
        sendReviewsBtn.setEnabled(true);
    }

    private void getDeviceLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, locationPerms[0]) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, locationPerms[1]) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
            if (!alreadyFocused) {
                alreadyFocused = true;
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15.0f));
                requestPointsInBox();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) { }

        @Override
        public void onProviderEnabled(String s) { }

        @Override
        public void onProviderDisabled(String s) { }
    };

    private void drawMarkers(ArrayList<Point> points) {
        for (Point point : points) {
            Marker marker = gMap.addMarker(setMarkerOptions(point));
            marker.setTag(point);
            tempMarkers.add(marker);
        }
    }

    private void removeMarkers() {
        for (Marker marker : markers) {
            marker.remove();
        }
    }

    private void rewriteMarkerList() {
        markers.clear();
        markers.addAll(tempMarkers.subList(0, tempMarkers.size()));
        tempMarkers.clear();
    }

    private void showPointInfo(Marker marker) {
        Point point = (Point) marker.getTag();
        if (point == null) {
            return;
        }

        pointRatingIv.setVisibility(View.GONE);
        supervisorRatingIv.setVisibility(View.GONE);
        pointRatingTv.setText("");
        supervisorRatingTv.setText("");
        pointOwner.setText("");
        mainViewModel.requestSupervisor(point.getSupervisor());

        if (point.getCurrentlyNotHere()) {
            ViewDriver.setStatusTvOptions(pointActive, statusTrueText, statusTrueColor);
        } else {
            ViewDriver.setStatusTvOptions(pointActive, statusFalseText, statusFalseColor);
        }

        String name = point.getName();
        if (name == null || name.equals("")) {
            name = getString(R.string.point_name_null);
        }
        pointName.setText(name);

        Float rating = point.getAvgRating();
        if (rating != null) {
            pointRatingIv.setVisibility(View.VISIBLE);
            pointRatingTv.setText(String.format(Locale.US, "%.2f", rating));
        }

        ArrayList<Product> products = point.getProductList();
        if (products.size() > 5) {
            mapPointProductsRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) resources.getDimension(R.dimen.map_point_products_recycler_view_max_height)));
        } else {
            mapPointProductsRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        MapPointProductsRecyclerViewAdapter adapter = new MapPointProductsRecyclerViewAdapter(products, getString(R.string.point_product_null));
        mapPointProductsRecyclerView.setAdapter(adapter);
        mapPointProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ViewDriver.hideView(reviewWindow, R.anim.top_view_hide_animation, this);
        ViewDriver.showView(pointInfoWindow, R.anim.bottom_view_show_animation, this);

        equalizeMarkers(0.4f);
        marker.setAlpha(1.0f);
    }

    private MarkerOptions setMarkerOptions(Point point) {
        Bitmap customIcon;
        if (point.getCurrentlyNotHere()) {
            customIcon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.green_marker), markerSize, markerSize, false);
        } else {
            customIcon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.red_marker), markerSize, markerSize, false);
        }

        float alphaValue;
        if (focusedMarkerInfo == null) {
            alphaValue = 0.87f;
        } else if (focusedMarker(point)) {
            alphaValue = 1.0f;
        } else {
            alphaValue = 0.4f;
        }

        String hint = point.getHint();
        if (hint == null) {
            hint = "";
        }

        return new MarkerOptions()
                .position(new LatLng(point.getCoordinates().getLat(), point.getCoordinates().getLng()))
                .alpha(alphaValue)
                .title(hint)
                .icon(BitmapDescriptorFactory.fromBitmap(customIcon));
    }

    private boolean focusedMarker(Point point) {
        LatLng focusedLatLng = new LatLng(focusedMarkerInfo.getCoordinates().getLat(), focusedMarkerInfo.getCoordinates().getLng());
        LatLng currentLatLng = new LatLng(point.getCoordinates().getLat(), point.getCoordinates().getLng());
        return focusedLatLng.latitude == currentLatLng.latitude && focusedLatLng.longitude == currentLatLng.longitude;
    }

    private void removeFocusFromMarker() {
        focusedMarkerInfo = null;
        equalizeMarkers(0.87f);
    }

    private void equalizeMarkers(float opacity) {
        for (Marker marker : markers) {
            marker.setAlpha(opacity);
        }
    }

    public void requestPointsInBox() {
        if (gMap == null) {
            return;
        }
        mainViewModel.requestPointsInBox(gMap.getProjection().getVisibleRegion().latLngBounds);
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        whoMoved = reason;
        if (whoMoved == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            removeFocusFromMarker();
            ViewDriver.hideView(reviewWindow, R.anim.top_view_hide_animation, this);
            ViewDriver.hideView(pointInfoWindow, R.anim.bottom_view_hide_animation, this);
        }
    }

    @Override
    public void onCameraIdle() {
        if (whoMoved == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            requestPointsInBox();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (view == pointInfoWindow) {
            return handleInfoWindowMotion(view, motionEvent);
        }

        if (view == reviewWindow) {
            return handleReviewWindowMotion(view, motionEvent);
        }

        return false;
    }

    private boolean handleInfoWindowMotion(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                infoWindowStartMotionY = motionEvent.getRawY();
                infoWindowCorrectionY = view.getY() - infoWindowStartMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                infoWindowEndMotionY = motionEvent.getRawY();
                if (infoWindowStartMotionY > infoWindowEndMotionY) {
                    break;
                }
                view.setY(infoWindowEndMotionY + infoWindowCorrectionY);
                break;
            case MotionEvent.ACTION_UP:
                if (infoWindowEndMotionY != 0 && infoWindowEndMotionY - infoWindowStartMotionY > 300) {
                    setAnimationForSwipedViewHide(view, infoWindowStartMotionY, infoWindowCorrectionY);
                    removeFocusFromMarker();
                    break;
                }
                view.animate().y(infoWindowCorrectionY + infoWindowStartMotionY).setDuration(200);
                break;
        }
        return false;
    }

    private boolean handleReviewWindowMotion(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                reviewWindowStartMotionY = motionEvent.getRawY();
                reviewWindowCorrectionY = view.getY() - reviewWindowStartMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                reviewWindowEndMotionY = motionEvent.getRawY();
                if (reviewWindowStartMotionY < reviewWindowEndMotionY) {
                    break;
                }
                view.setY(reviewWindowEndMotionY + reviewWindowCorrectionY);
                break;
            case MotionEvent.ACTION_UP:
                if (reviewWindowEndMotionY != 0 && reviewWindowStartMotionY - reviewWindowEndMotionY > 300) {
                    setAnimationForSwipedViewHide(view, reviewWindowStartMotionY, reviewWindowCorrectionY);
                    break;
                }
                view.animate().y(reviewWindowCorrectionY + reviewWindowStartMotionY).setDuration(200);
                break;
        }
        return false;
    }

    private void setAnimationForSwipedViewHide(View view, float start, float tempY) {
        Animation animation = null;

        if (view == pointInfoWindow) {
            animation = ViewDriver.hideView(view, R.anim.bottom_view_hide_animation, this);
        }

        if (view == reviewWindow) {
            animation = ViewDriver.hideView(view, R.anim.top_view_hide_animation, this);
            ViewDriver.showView(pointInfoWindow, R.anim.bottom_view_show_animation, this);
        }

        if (animation == null) {
            return;
        }

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setY(tempY + start);
            }

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }
}
