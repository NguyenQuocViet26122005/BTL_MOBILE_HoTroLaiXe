package com.example.btl_banglaixe.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_banglaixe.ExamActivity;
import com.example.btl_banglaixe.R;

import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {

    private List<ExamActivity.ExamItem> exams;
    private OnExamClickListener listener;

    public interface OnExamClickListener {
        void onExamClick(ExamActivity.ExamItem exam);
    }

    public ExamAdapter(List<ExamActivity.ExamItem> exams, OnExamClickListener listener) {
        this.exams = exams;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_exam, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExamActivity.ExamItem exam = exams.get(position);
        holder.bind(exam, listener);
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle, tvDescription, tvBadge;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardExam);
            tvTitle = itemView.findViewById(R.id.tvExamTitle);
            tvDescription = itemView.findViewById(R.id.tvExamDescription);
            tvBadge = itemView.findViewById(R.id.tvBadge);
        }

        void bind(ExamActivity.ExamItem exam, OnExamClickListener listener) {
            tvTitle.setText(exam.getTitle());
            tvDescription.setText(exam.getDescription());
            
            if (exam.isRandom()) {
                tvBadge.setVisibility(View.VISIBLE);
                tvBadge.setText("🎲 Ngẫu nhiên");
            } else {
                tvBadge.setVisibility(View.GONE);
            }
            
            cardView.setOnClickListener(v -> listener.onExamClick(exam));
        }
    }
}
