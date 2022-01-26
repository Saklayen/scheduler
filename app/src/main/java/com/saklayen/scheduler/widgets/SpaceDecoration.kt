package com.saklayen.scheduler.widgets

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import com.saklayen.scheduler.utils.isRtl

/** ItemDecoration that adds space around items. */
class SpaceDecoration(
    private val start: Int = 0,
    private val top: Int = 0,
    private val end: Int = 0,
    private val bottom: Int = 0
) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        val isRtl = parent.isRtl()
        outRect.set(
            if (isRtl) end else start,
            top,
            if (isRtl) start else end,
            bottom
        )
    }
}
