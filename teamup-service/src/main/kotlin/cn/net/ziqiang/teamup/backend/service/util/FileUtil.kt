package cn.net.ziqiang.teamup.backend.service.util

import java.text.SimpleDateFormat
import java.util.*

object FileUtil {
    fun getExtensionName(fileName: String): String? {
        if (fileName.contains('.'))
            return fileName.substring(fileName.lastIndexOf(".") + 1)
        return null
    }

    fun getFileNameNoEx(fileName: String): String {
        return fileName.substring(0, fileName.lastIndexOf("."))
    }

    fun getRandomFileName(fileName: String): String {

        val formatter = SimpleDateFormat("yyyyMMddHHmmss")
        val now = formatter.format(Date())

        val randomString = ((Math.random()*9+1)*1000).toInt().toString()

        val ext = getExtensionName(fileName)

        return if (ext != null) {
            "$now$randomString.$ext"
        } else {
            "$now$randomString"
        }
    }


}