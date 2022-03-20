package com.example.ck_wgu_196.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.ck_wgu_196.DAO.AssessmentDAO;
import com.example.ck_wgu_196.DAO.CourseDAO;
import com.example.ck_wgu_196.DAO.InstructorDAO;
import com.example.ck_wgu_196.DAO.TermDAO;
import com.example.ck_wgu_196.Entity.Assessment;
import com.example.ck_wgu_196.Entity.Course;
import com.example.ck_wgu_196.Entity.Instructor;
import com.example.ck_wgu_196.Entity.Term;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.SimpleFormatter;

@Database(entities={Term.class, Course.class, Assessment.class, Instructor.class}, version=5, exportSchema = false)
public abstract class DatabaseBuilder extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract TermDAO termDAO();

    public abstract CourseDAO courseDAO();

    public abstract AssessmentDAO assessmentDAO();

    public abstract InstructorDAO instructorDAO();

    private static volatile DatabaseBuilder INSTANCE;

    static DatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseBuilder.class, "myDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static DatabaseBuilder.Callback populateDatabaseCallback = new DatabaseBuilder.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                TermDAO termDao = INSTANCE.termDAO();
                CourseDAO courseDao = INSTANCE.courseDAO();
                AssessmentDAO assessmentDao = INSTANCE.assessmentDAO();
                InstructorDAO instructorDao = INSTANCE.instructorDAO();

            });
        };

    };
}

