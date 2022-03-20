package com.example.ck_wgu_196.UI.Assessments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ck_wgu_196.Entity.Assessment;
import com.example.ck_wgu_196.Entity.Term;
import com.example.ck_wgu_196.R;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder>{

    class AssessmentViewHolder extends RecyclerView.ViewHolder{
        private final TextView assessmentItemView;

        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.assessmentItemView = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view){
                    int position = getAdapterPosition();
                    final Assessment current = mAssessments.get(position);
                    Intent intent = new Intent(context, AssessmentDetailActivity.class);
                    intent.putExtra("assessmentID", current.getAssessmentID());
                    intent.putExtra("title", current.getAssessmentTitle());
                    intent.putExtra("start", current.getAssessmentStartDate());
                    intent.putExtra("end", current.getAssessmentEndDate());
                    intent.putExtra("courseID_FK", current.getCourseID_FK());
                    intent.putExtra("kind", current.getAssessmentKind());
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        }
    }
    private List<Assessment> mAssessments;
    private final Context context;
    private final LayoutInflater mInflator;
    public AssessmentAdapter(Context context){
        mInflator = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public AssessmentAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflator.inflate(R.layout.term_list_item,parent,false);
        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        if(mAssessments != null) {
            Assessment current = mAssessments.get(position);
            String name = current.getAssessmentTitle();
            holder.assessmentItemView.setText(name);
        } else {
            holder.assessmentItemView.setText("No Assessment name");
        }
    }

    @Override
    public int getItemCount() {
        if(mAssessments != null)
            return mAssessments.size();
        else return 0;
    }

    public void setAssessments(List<Assessment> assessments){
        mAssessments = assessments;
        notifyDataSetChanged();
    }


}
