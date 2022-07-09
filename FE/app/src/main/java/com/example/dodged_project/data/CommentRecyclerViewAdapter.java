package com.example.dodged_project.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodged_project.R;

import java.util.ArrayList;
import java.util.Objects;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Comment> commentsArrayList;

    public CommentRecyclerViewAdapter(ArrayList<Comment> commentsArrayList) {
        this.commentsArrayList = commentsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = commentsArrayList.get(position);
        holder.commentPoster.setText(comment.getPoster());
        holder.commentDate.setText(comment.getDate());
        holder.commentActualComment.setText(comment.getComment());

    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(commentsArrayList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView commentPoster;
        private TextView commentDate;
        private TextView commentActualComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentPoster = itemView.findViewById(R.id.comment_poster_textview);
            commentDate = itemView.findViewById(R.id.comment_date_textview);
            commentActualComment = itemView.findViewById(R.id.comment_row_comment_textview);
        }
    }
}
