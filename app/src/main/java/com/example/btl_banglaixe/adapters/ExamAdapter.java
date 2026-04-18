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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(exams.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle, tvDescription, tvBadge, tvStatus;

        ViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.cardExam);
            tvTitle = v.findViewById(R.id.tvExamTitle);
            tvDescription = v.findViewById(R.id.tvExamDescription);
            tvBadge = v.findViewById(R.id.tvBadge);
            tvStatus = v.findViewById(R.id.tvStatus);
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
            
            if (exam.getStatus() != null && !exam.getStatus().isEmpty()) {
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setText(exam.getStatus());
                if (exam.getStatus().contains("Đã đỗ")) {
                    tvStatus.setTextColor(itemView.getContext().getResources().getColor(com.example.btl_banglaixe.R.color.accent_green));
                } else {
                    tvStatus.setTextColor(itemView.getContext().getResources().getColor(com.example.btl_banglaixe.R.color.error));
                }
            } else {
                tvStatus.setVisibility(View.GONE);
            }
            
            cardView.setOnClickListener(v -> listener.onExamClick(exam));
        }
    }
}
