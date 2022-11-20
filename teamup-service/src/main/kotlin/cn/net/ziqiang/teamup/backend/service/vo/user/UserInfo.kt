package cn.net.ziqiang.teamup.backend.service.vo.user

abstract class UserInfo {
    abstract var id: Long?
    abstract var username: String
    abstract var faculty: String?
    abstract var avatar: String?
}