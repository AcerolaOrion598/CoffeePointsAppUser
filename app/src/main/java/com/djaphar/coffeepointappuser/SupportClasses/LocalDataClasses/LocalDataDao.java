package com.djaphar.coffeepointappuser.SupportClasses.LocalDataClasses;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface LocalDataDao {

    @Insert
    void setLastBounds(LastBounds bounds);

    @Query("SELECT * FROM last_bounds_table")
    LiveData<LastBounds> getLastBoundsLiveData();

    @Query("UPDATE last_bounds_table SET north_lat = (:northLat), north_long = (:northLong), south_lat = (:southLat), south_long = (:southLong)")
    void updateLastBounds(double northLat, double northLong, double southLat, double southLong);
}
