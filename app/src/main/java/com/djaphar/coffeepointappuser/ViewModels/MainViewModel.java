package com.djaphar.coffeepointappuser.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.coffeepointappuser.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.coffeepointappuser.SupportClasses.ApiClasses.Point;
import com.djaphar.coffeepointappuser.SupportClasses.ApiClasses.PointsApi;
import com.djaphar.coffeepointappuser.SupportClasses.ApiClasses.SupervisorModel;
import com.djaphar.coffeepointappuser.SupportClasses.LocalDataClasses.LastBounds;
import com.djaphar.coffeepointappuser.SupportClasses.LocalDataClasses.LocalDataDao;
import com.djaphar.coffeepointappuser.SupportClasses.LocalDataClasses.LocalDataRoom;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {

    private LiveData<LastBounds> lastBoundsLiveData;
    private MutableLiveData<SupervisorModel> supervisorModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Point>> pointsMutableLiveData = new MutableLiveData<>();
    private LocalDataDao dao;
    private PointsApi pointsApi;

    public MainViewModel(@NonNull Application application) {
        super(application);
        LocalDataRoom room = LocalDataRoom.getDatabase(application);
        dao = room.localDataDao();
        lastBoundsLiveData = dao.getLastBoundsLiveData();
        pointsApi = ApiBuilder.getPointsApi();
    }

    public MutableLiveData<ArrayList<Point>> getPoints() {
        return pointsMutableLiveData;
    }

    public MutableLiveData<SupervisorModel> getSupervisor() {
        return supervisorModelMutableLiveData;
    }

    public LiveData<LastBounds> getLastBounds() {
        return lastBoundsLiveData;
    }

    public void setLastScreenBounds(double northLat, double northLong, double southLat, double southLong) {
        if (lastBoundsLiveData.getValue() == null) {
            LocalDataRoom.databaseWriteExecutor.execute(() -> dao.setLastBounds(new LastBounds(northLat, northLong, southLat, southLong)));
        } else {
            LocalDataRoom.databaseWriteExecutor.execute(() -> dao.updateLastBounds(northLat, northLong, southLat, southLong));
        }
    }

    public void requestPointsInBox(LatLngBounds bounds) {
        String box = bounds.southwest.latitude + "," + bounds.southwest.longitude + ","
                + bounds.northeast.latitude + "," + bounds.northeast.longitude;
        Call<ArrayList<Point>> call = pointsApi.requestPointsInBox(box);
        call.enqueue(new Callback<ArrayList<Point>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Point>> call, @NonNull Response<ArrayList<Point>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                pointsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Point>> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestSupervisor(String supervisorId) {
        Call<SupervisorModel> call = pointsApi.requestSupervisor(supervisorId);
        call.enqueue(new Callback<SupervisorModel>() {
            @Override
            public void onResponse(@NonNull Call<SupervisorModel> call, @NonNull Response<SupervisorModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                supervisorModelMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<SupervisorModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
