package com.example.englishappforkid.presentation.permissions

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes

@Composable
fun permissionRequest(navController: NavHostController) {
    // State để theo dõi tình trạng quyền
    val permissionDenied = remember { mutableStateOf(false) }

    // Khởi tạo launcher yêu cầu quyền
    val requestPermissionsLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            // Kiểm tra quyền sau khi yêu cầu
            if (permissions[Manifest.permission.READ_CALENDAR] == true &&
                permissions[Manifest.permission.WRITE_CALENDAR] == true
            ) {
                // Quyền đã được cấp, điều hướng tới màn hình chính
                navController.navigate("main_screen")
            } else {
                // Quyền bị từ chối, thay đổi state để hiển thị thông báo
                permissionDenied.value = true
            }
        }

    // Kiểm tra quyền hiện tại
    val readPermission =
        ContextCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.READ_CALENDAR,
        )
    val writePermission =
        ContextCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.WRITE_CALENDAR,
        )

    // Nếu quyền chưa được cấp, yêu cầu người dùng cấp quyền
    if (readPermission != PackageManager.PERMISSION_GRANTED || writePermission != PackageManager.PERMISSION_GRANTED) {
        requestPermissionsLauncher.launch(
            arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR),
        )
    } else {
        // Nếu quyền đã được cấp, chuyển sang màn hình chính
        navController.navigate(ScreenRoutes.MAIN_SCREEN)
    }

    // Hiển thị thông báo nếu quyền bị từ chối
    if (permissionDenied.value) {
        Text(
            text = "Permission Denied! Cannot access Calendar.",
            modifier = Modifier.padding(16.dp),
        )
    }
}
