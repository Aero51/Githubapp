package com.aero51.githubapp.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.aero51.githubapp.utils.Constants;
import com.aero51.githubapp.db.model.Repo;

import static com.aero51.githubapp.utils.Constants.DATABASE_NAME;


@androidx.room.Database(entities = {Repo.class}, version =1)
public abstract class Database extends RoomDatabase {
    private static Database instance;

    public abstract RepoDao getRepoDao();

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();

        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d(Constants.LOG, "RoomDatabase.Callback roomCallback onCreate  ");

        }
    };
}
