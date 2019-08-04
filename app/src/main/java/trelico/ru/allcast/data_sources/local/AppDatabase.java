package trelico.ru.allcast.data_sources.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import trelico.ru.allcast.data_sources.local.yandex.TTSDatabaseEntityDAO;

@Database(entities = {TTSDatabaseEntityDAO.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract TTSDatabaseEntityDAO ttsDatabaseEntityDao();
}
