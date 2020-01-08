package com.kaycloud.frost.module.audio

/**
 * Created by jiangyunkai on 2019/11/16
 * 权限请求回调
 */
interface PermissionCallback {

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
}

//override fun onRequestPermissionsResult(requestCode: Int,
//                                        permissions: Array<String>, grantResults: IntArray) {
//    when (requestCode) {
//        MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
//            // If request is cancelled, the result arrays are empty.
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                // permission was granted, yay! Do the
//                // contacts-related task you need to do.
//            } else {
//                // permission denied, boo! Disable the
//                // functionality that depends on this permission.
//            }
//            return
//        }
//
//        // Add other 'when' lines to check for other
//        // permissions this app might request.
//        else -> {
//            // Ignore all other requests.
//        }
//    }
//}
