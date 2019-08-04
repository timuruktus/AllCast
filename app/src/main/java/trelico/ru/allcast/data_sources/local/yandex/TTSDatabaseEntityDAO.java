package trelico.ru.allcast.data_sources.local.yandex;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface TTSDatabaseEntityDAO{

    @Query("SELECT * FROM TTSDatabaseEntity")
    Single<List<TTSDatabaseEntity>> getAll();

    @Query("SELECT * FROM TTSDatabaseEntity WHERE hash = :hash")
    Single<TTSDatabaseEntity> getByHash(String hash);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TTSDatabaseEntity ttsResponse);

    @Update
    void update(TTSDatabaseEntity ttsResponse);

    @Delete
    void delete(TTSDatabaseEntity ttsResponse);

}
