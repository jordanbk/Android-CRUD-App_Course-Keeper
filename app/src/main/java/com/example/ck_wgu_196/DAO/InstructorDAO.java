package com.example.ck_wgu_196.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ck_wgu_196.Entity.Instructor;

import java.util.List;

@Dao
public interface InstructorDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Instructor instructor);

    @Update
    void update(Instructor instructor);

    @Delete
    void delete(Instructor instructor);

    @Query("SELECT * FROM instructors ORDER BY instructorID ASC")
    List<Instructor> getAllInstructors();


    @Query ("SELECT * FROM instructors WHERE courseID = :courseID ORDER BY instructorName ASC")
    LiveData<List<Instructor>> getAllAssociatedMentors(int courseID);
}
