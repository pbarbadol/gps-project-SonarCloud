package com.unex.asee.ga02.beergo.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.unex.asee.ga02.beergo.databinding.CommentItemListBinding
import com.unex.asee.ga02.beergo.model.Comment

class CommentsAdapter(
    private var comments: List<Comment>,
    private val onClick: (comment: Comment) -> Unit,
    private val onLongClick: (comment: Comment) -> Unit
) : RecyclerView.Adapter<CommentsAdapter.ShowCommentsHolder>() {
    class ShowCommentsHolder(
        private val binding: CommentItemListBinding,
        private val onClick: (comment: Comment) -> Unit,
        private val onLongClick: (comment: Comment) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(comment: Comment, totalItems: Int){

            with(binding){
                userName.text = comment.userName
                commentText.text = comment.comment
                clItem.setOnClickListener{
                    onClick(comment)
                }
                clItem.setOnLongClickListener{
                    onLongClick(comment)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowCommentsHolder {
        val binding =
            CommentItemListBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
        return ShowCommentsHolder(binding, onClick, onLongClick)
    }

    override fun getItemCount() = comments.size

    override fun onBindViewHolder(holder: ShowCommentsHolder, position: Int) {
        holder.bind(comments[position], comments.size)
    }

    fun updateData(newComments: List<Comment>) {
        comments = newComments
        notifyDataSetChanged()
    }

}