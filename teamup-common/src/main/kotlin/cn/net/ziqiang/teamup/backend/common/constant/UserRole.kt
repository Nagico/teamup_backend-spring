package cn.net.ziqiang.teamup.backend.common.constant

enum class UserRole(val string: String, val weight: Int) {
    None("", 0),
    User("User", 1),
    Manager("Manager", 2),
    Admin("Admin", 3),

    ;
}