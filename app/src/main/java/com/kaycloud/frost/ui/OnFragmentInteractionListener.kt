package com.kaycloud.frost.ui

import com.kaycloud.frost.data.entity.WallhavenItemEntity

/**
 * author: kaycloud
 * Created_at: 2019-11-11
 *
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 *
 *
 * See the Android Training lesson
 * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
 * for more information.
 */
interface OnFragmentInteractionListener {
    fun onFragmentInteraction(item: WallhavenItemEntity)
}