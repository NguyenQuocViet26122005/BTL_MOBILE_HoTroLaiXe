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
        TextView tvTitle, tvDescription, tvBadge;

        ViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.cardExam);
            tvTitle = v.findViewById(R.id.tvExamTitle);
            tvDescription = v.findViewById(R.id.tvExamDescription);
            tvBadge = v.findViewById(R.id.tvBadge);
        }

        void bind(ExamActivity.ExamItem exam, OnExamClickListener listener) {
            tvTitle.setText(exam.getTitle());
            tvDescription.setText(exam.getDescription());
            tvBadge.setVisibility(exam.isRandom() ? View.VISIBLE : View.GONE);
            if (exam.isRandom()) tvBadge.setText("🎲 Ngẫu nhiên");
            cardView.setOnClickListener(v -> listener.onExamClick(exam));
        }
    }
}
