package com.edward.navigation.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~      http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~
 ~ https://github.com/miguelhincapie/CustomBottomSheetBehavior
 */

/**
 * This class only cares about hide or unhide the FAB because the anchor behavior is something
 * already in FAB.
 *
 * 某个view监听另一个view的状态变化，例如大小、位置、显示状态等
 * layoutDependsOn和onDependentViewChanged
 *某个view监听CoordinatorLayout里的滑动状态
 * onStartNestedScroll和onNestedPreScroll
 */
public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    /**
     * One of the point used to set hide() or show() in FAB
     */
    private float offset;
    /**
     * The FAB should be hidden when it reach {@link #offset} or when {@link BottomSheetBehaviorGoogleMapsLike}
     * is visually lower than {@link BottomSheetBehaviorGoogleMapsLike#getPeekHeight()}.
     * We got a reference to the object to allow change dynamically PeekHeight in BottomSheet and
     * got updated here.
     */
    private WeakReference<BottomSheetBehaviorGoogleMapsLike> mBottomSheetBehaviorRef;

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
        offset = 0;
        mBottomSheetBehaviorRef = null;
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
//         Ensure we react to vertical scrolling
     //pre:   return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
        //ed:
        return  nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof NestedScrollView) {
            try {
                BottomSheetBehaviorGoogleMapsLike.from(dependency);
                return true;
            }
            catch (IllegalArgumentException e){}
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        /**
         * Because we are not moving it, we always return false in this method.
         */

        if (offset == 0)
            setOffsetValue(parent);

        if (mBottomSheetBehaviorRef == null)
            getBottomSheetBehavior(parent);

       // int DyFix = getDyBetweenChildAndDependency(child, dependency);
        int DyFix = 0;//ed
        Log.i("id"+child.getId()+"father","childgety =" +child.getY() + " dependency.getY()="+dependency.getY() + " offset=" + offset);
        if ((dependency.getY()-(int)offset + DyFix) < offset)
            child.hide();//控制按钮到最顶部的时候消失不见
        else if ((dependency.getY()-(int)offset + DyFix) >= offset) {

            /**
             * We are calculating every time point in Y where BottomSheet get {@link BottomSheetBehaviorGoogleMapsLike#STATE_COLLAPSED}.
             * If PeekHeight change dynamically we can reflect the behavior asap.
             */
            if (mBottomSheetBehaviorRef == null || mBottomSheetBehaviorRef.get() == null)
                getBottomSheetBehavior(parent);
            int collapsedY = dependency.getHeight() - mBottomSheetBehaviorRef.get().getPeekHeight();
            Log.i("father","childgety =" +child.getY() + " DyFix="+DyFix + " collapsedY=" + collapsedY);
       /*     if ((child.getY() + DyFix) > collapsedY)
                child.hide();
            else {
                child.show();

            }*/
            if ((dependency.getY()-(int)offset + DyFix) > collapsedY)
                child.hide();
            else {
                Log.i("father","show1!!!" + child.getY() );
                child.setY(dependency.getY()-(int)offset);
                Log.i("father","show2!!!" + child.getY() );
                child.invalidate();
                Log.i("father","show3!!!" + child.getY() );
                child.show();
                Log.i("father","show4!!!" + child.getY() );

            }
             //child.show();
        }

        return false;
    }

    /**
     * In some <bold>WEIRD</bold> cases, mostly when you perform a little scroll but a fast one
     * the {@link #onDependentViewChanged(CoordinatorLayout, FloatingActionButton, View)} DOESN'T
     * reflect the real Y position of child mean the dependency get a better APROXIMATION of the real
     * Y. This was causing that FAB some times doesn't get unhidden.
     * @param child the FAB
     * @param dependency NestedScrollView instance
     * @return Dy betweens those 2 elements in Y, minus child's height/2
     */
    private int getDyBetweenChildAndDependency(@NonNull FloatingActionButton child, @NonNull View dependency) {
        if (dependency.getY() == 0 || dependency.getY() < offset)
            return 0;

        if ( (dependency.getY() - child.getY()) > child.getHeight() )
            return Math.max(0, (int) ((dependency.getY() - (child.getHeight()/2)) - child.getY()) );
        else
            return 0;
    }

    /**
     * Define one of the point in where the FAB should be hide when it reachs that point.
     * @param coordinatorLayout container of BottomSheet and AppBarLayout
     */
    private void setOffsetValue(CoordinatorLayout coordinatorLayout) {

        for (int i = 0; i < coordinatorLayout.getChildCount(); i++) {
            View child = coordinatorLayout.getChildAt(i);

            if (child instanceof AppBarLayout) {

                if (child.getTag() != null &&
                        child.getTag().toString().contentEquals("modal-appbar") ) {
                    offset = child.getY()+child.getHeight();
                    break;
                }
            }
        }
    }

    /**
     * Look into the CoordiantorLayout for the {@link BottomSheetBehaviorGoogleMapsLike}
     * @param coordinatorLayout with app:layout_behavior= {@link BottomSheetBehaviorGoogleMapsLike}
     */
    private void getBottomSheetBehavior(@NonNull CoordinatorLayout coordinatorLayout) {

        for (int i = 0; i < coordinatorLayout.getChildCount(); i++) {
            View child = coordinatorLayout.getChildAt(i);

            if (child instanceof NestedScrollView) {

                try {
                    BottomSheetBehaviorGoogleMapsLike temp = BottomSheetBehaviorGoogleMapsLike.from(child);
                    mBottomSheetBehaviorRef = new WeakReference<>(temp);
                    break;
                }
                catch (IllegalArgumentException e){}
            }
        }
    }
}