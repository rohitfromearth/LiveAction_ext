package com.ext.liveaction_ext;// QuestionAnswerAdapter.java
import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ext.liveaction_ext.QandA;
import com.ext.liveaction_ext.R;

import java.util.List;

public class QuestionAnswerAdapter extends RecyclerView.Adapter<QuestionAnswerAdapter.ViewHolder> {

    private List<QandA> itemList;
    private int expandedPosition = -1;
    public QuestionAnswerAdapter(List<QandA> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.faq_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QandA item = itemList.get(position);
        holder.questionTextView.setText(item.getQuestion());
        holder.answerTextView.setText(item.getAnswer());

        holder.questionTextView.setOnClickListener(v -> {
            if (holder.answerTextView.getVisibility() == View.VISIBLE) {
                collapse(holder.answerTextView);
            } else {
                expand(holder.answerTextView);
            }
        });
        final boolean isExpanded = position == expandedPosition;
        holder.answerTextView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);

        holder.questionTextView.setOnClickListener(v -> {
            if (expandedPosition >= 0) {
                int prevExpandedPosition = expandedPosition;
                notifyItemChanged(prevExpandedPosition);
            }
            expandedPosition = isExpanded ? -1 : position;
            notifyItemChanged(position);
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        TextView answerTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            answerTextView = itemView.findViewById(R.id.answerTextView);
        }
    }

    private void expand(View view) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getMeasuredHeight();
        view.getLayoutParams().height = 0;
        view.setVisibility(View.VISIBLE);
        ValueAnimator animator = ValueAnimator.ofInt(0, targetHeight);
        animator.addUpdateListener(animation -> {
            view.getLayoutParams().height = (int) animation.getAnimatedValue();
            view.requestLayout();
        });
        animator.setDuration(200);
        animator.start();
    }

    private void collapse(View view) {
        final int initialHeight = view.getMeasuredHeight();
        ValueAnimator animator = ValueAnimator.ofInt(initialHeight, 0);
        animator.addUpdateListener(animation -> {
            view.getLayoutParams().height = (int) animation.getAnimatedValue();
            view.requestLayout();
        });
        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        animator.setDuration(200);
        animator.start();
    }
}
