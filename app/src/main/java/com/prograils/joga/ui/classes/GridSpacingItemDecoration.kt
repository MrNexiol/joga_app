package com.prograils.joga.ui.classes

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
        private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) % 2 == 0){
            outRect.right = space
        } else {
            outRect.left = space
        }
        outRect.bottom = space
    }
}