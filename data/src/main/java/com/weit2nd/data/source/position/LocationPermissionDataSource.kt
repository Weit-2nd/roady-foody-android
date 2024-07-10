package com.weit2nd.data.source.position

import android.Manifest
import com.gun0912.tedpermission.coroutine.TedPermission
import javax.inject.Inject

class LocationPermissionDataSource @Inject constructor() {
    private val permission =
        TedPermission
            .create()
            .setDeniedTitle("위치 권한 거절")
            .setDeniedMessage("권한 설정을 해주셔야 현재 위치 파악이 가능합니다.")
            .setGotoSettingButtonText("설정")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )

    suspend fun requestLocationPermission(): Boolean = permission.checkGranted()
}
