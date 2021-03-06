package com.example.ck_wgu_196.UI.Courses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ck_wgu_196.Entity.Course;
import com.example.ck_wgu_196.R;
import com.example.ck_wgu_196.UI.TermDetailActivity;

import java.io.Serializable;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {


    class CourseViewHolder extends RecyclerView.ViewHolder{

        private final TextView courseItemView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            this.courseItemView = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view){
                    int position = getAdapterPosition();
                    final Course current = mCourses.get(position);
                    Intent intent = new Intent(context, CourseDetailActivity.class);
                    intent.putExtra("courseID", current.getCourseID());
                    intent.putExtra("title", current.getCourseTitle());
                    intent.putExtra("status", current.getCourseStatus());
                    intent.putExtra("start", current.getCourseStart());
                    intent.putExtra("end", current.getCourseEnd());
                    intent.putExtra("notes", current.getCourseNotes());
                    intent.putExtra("termID", current.getTermID_FK());
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        }
    }
    private List<Course> mCourses;
    private final Context context;
    private final LayoutInflater mInflator;
    public CourseAdapter(Context context) {
        mInflator = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflator.inflate(R.layout.term_list_item,parent,false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        if(mCourses != null) {
            final Course current = mCourses.get(position);
            holder.courseItemView.setText(current.getCourseTitle());
        } else {
            holder.courseItemView.setText("No Course name");
        }
    }

    @Override
    public int getItemCount() {
        if(mCourses != null)
            return mCourses.size();
        else return 0;
    }

    public void setCourses(List<Course> courses){
        mCourses = courses;
    }


}
