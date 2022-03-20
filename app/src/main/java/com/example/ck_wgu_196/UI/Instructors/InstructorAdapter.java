package com.example.ck_wgu_196.UI.Instructors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ck_wgu_196.Entity.Course;
import com.example.ck_wgu_196.Entity.Instructor;
import com.example.ck_wgu_196.Entity.Term;
import com.example.ck_wgu_196.R;
import com.example.ck_wgu_196.UI.TermAdapter;
import com.example.ck_wgu_196.UI.TermDetailActivity;

import java.util.List;


public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.InstructorViewHolder>{

    public class InstructorViewHolder extends RecyclerView.ViewHolder {
        private final TextView instructorItemView;
        public InstructorViewHolder(@NonNull View itemView) {
            super(itemView);
            this.instructorItemView = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view){
                    int position = getAdapterPosition();
                    final Instructor current = mInstructor.get(position);
                    Intent intent = new Intent(context, InstructorDetailActivity.class);
                    intent.putExtra("id", current.getInstructorID());
                    intent.putExtra("name", current.getInstructorName());
                    intent.putExtra("email", current.getInstructorEmail());
                    intent.putExtra("phone", current.getInstructorPhone());
                    intent.putExtra("courseID", current.getCourseID());
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        }
    }
    private List<Instructor> mInstructor;
    private final Context context;
    private final LayoutInflater mInflator;
    public InstructorAdapter(Context context) {
        mInflator = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public InstructorAdapter.InstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflator.inflate(R.layout.term_list_item,parent,false);
        return new InstructorAdapter.InstructorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorViewHolder holder, int position) {
        if(mInstructor!=null){
            Instructor current = mInstructor.get(position);
            String name = current.getInstructorName();
            holder.instructorItemView.setText(name);
        }
        else{
            holder.instructorItemView.setText("No term name");
        }
    }

    @Override
    public int getItemCount() {
        return mInstructor.size();
    }

    public void setInstructors(List<Instructor> instructors){
        mInstructor = instructors;
        notifyDataSetChanged();
    }

}
