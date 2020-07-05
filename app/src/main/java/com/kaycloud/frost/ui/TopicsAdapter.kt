package com.kaycloud.frost.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kaycloud.frost.R
import com.kaycloud.frost.data.model.Topic
import com.kaycloud.frost.data.model.TopicDiff
import com.kaycloud.frost.databinding.OnboardingTopicItemBinding
import com.kaycloud.frost.ui.topic.TopicThumbnailTarget

/**
 * Created by jiangyunkai on 2020/4/13
 */
class TopicsAdapter(context: Context) : ListAdapter<Topic, TopicsViewHolder>(TopicDiff) {

    private val selectedTint = ContextCompat.getColor(context, R.color.topic_tint)
    private val selectedTopLeftCornerRadius =
        context.resources.getDimensionPixelSize(R.dimen.small_component_top_left_radius)
    private val selectedDrawable = context.getDrawable(R.drawable.ic_checkmark)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicsViewHolder {
        val binding = OnboardingTopicItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).apply {
            root.setOnClickListener {
                it.isActivated = !it.isActivated
            }
        }
        return TopicsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopicsViewHolder, position: Int) {
        holder.bind(getItem(position), selectedTint, selectedTopLeftCornerRadius, selectedDrawable)
    }

    override fun onViewRecycled(holder: TopicsViewHolder) {
        holder.itemView.rotation = 0f
    }

}


class TopicsViewHolder(private val binding: OnboardingTopicItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        topic: Topic, @ColorInt selectedTint: Int, @Px selectedTopLeftCornerRadius: Int,
        selectedDrawable: Drawable
    ) {
        binding.run {
            this.topic = topic
            Glide.with(topicImage)
                .asBitmap()
                .load(topic.imageUrl)
                .placeholder(R.drawable.course_image_placeholder)
                .into(
                    TopicThumbnailTarget(
                        topicImage,
                        selectedTint,
                        selectedTopLeftCornerRadius,
                        selectedDrawable
                    )
                )
            executePendingBindings()
        }
    }
}