package cn.net.ziqiang.teamup.backend.common.constant

import javax.persistence.AttributeConverter

enum class UserRole(val string: String, val weight: Int) {
    None("", 0),
    User("user", 1),
    Manager("manager", 2),
    Admin("admin", 3),

    ;

    companion object {
        fun all(): Array<UserRole> {
            return arrayOf(User, Manager, Admin)
        }
    }
}