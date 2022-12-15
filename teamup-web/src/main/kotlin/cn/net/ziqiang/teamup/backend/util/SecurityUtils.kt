package cn.net.ziqiang.teamup.backend.util

import org.springframework.util.DigestUtils
import java.util.*

object SecurityUtils {
    fun encryptPassword(password: String): String {
        // 加密过程
        // 1. 使用MD5算法
        // 2. 使用随机的盐值
        // 3. 循环5次
        // 4. 盐的处理方式为：盐 + 原密码 + 盐 + 原密码 + 盐
        // 注意：因为使用了随机盐，盐值必须被记录下来，本次的返回结果使用$分隔盐与密文
        val salt: String = UUID.randomUUID().toString().replace("-", "")
        var encodedPassword = password
        for (i in 0..4) {
            encodedPassword = DigestUtils.md5DigestAsHex(
                (salt + encodedPassword + salt + encodedPassword + salt).toByteArray()
            )
        }
        return salt + encodedPassword
    }

    fun matches(password: String, encodedPassword: String): Boolean {
        if (encodedPassword.isBlank()) {
            return true
        }
        val salt = encodedPassword.substring(0, 32)
        var newPassword = password
        for (i in 0..4) {
            newPassword = DigestUtils.md5DigestAsHex(
                (salt + newPassword + salt + newPassword + salt).toByteArray()
            )
        }
        newPassword = salt + newPassword
        return newPassword == encodedPassword
    }
}