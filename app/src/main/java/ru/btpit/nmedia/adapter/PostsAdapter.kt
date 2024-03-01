package ru.btpit.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.btpit.nmedia.R
import ru.btpit.nmedia.databinding.PostCardLayoutBinding
import ru.btpit.nmedia.dto.Post

typealias OnLikeListener = (post: Post) -> Unit
typealias onShareListener = (post: Post) -> Unit

class PostsAdapter(private val onLikeListener: OnLikeListener, private val onShareListener: onShareListener) : RecyclerView.Adapter<PostViewHolder>() {
    var list = emptyList<Post>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onShareListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = list[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = list.size
}

class PostViewHolder(
    private val binding: PostCardLayoutBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: onShareListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            likeCount.text = formatCount(post.likes)
            shareCount.text = formatCount(post.shares)
            viewsCount.text = formatCount(post.viewes + 1)
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.setImageResource(
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
            )
            like.setOnClickListener {
                onLikeListener(post)
            }
            share.setOnClickListener {
                onShareListener(post)
            }
        }
    }
}
fun formatCount(num: Int): String {
    return when {
        num >= 1_000_000 -> {
            val millionPart = num / 1_000_000
            val remainder = num % 1_000_000
            if (remainder == 0) {
                "$millionPart"
            }
            else { "$millionPart.${(remainder / 1_00000)}M"
            }
        }
        num   > 999 -> {
            val numString =
                if (num % 1_000 == 0) "${num / 1_000}K" else "${num / 1_000}.${(num % 1_000) / 100}K"
            numString
        }
        num >= 10000 -> "${num / 10000}K"
        else -> num.toString()
    }
}