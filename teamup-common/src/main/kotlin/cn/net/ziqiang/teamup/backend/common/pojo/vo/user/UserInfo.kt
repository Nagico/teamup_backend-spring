package cn.net.ziqiang.teamup.backend.common.pojo.vo.user

abstract class UserInfo {
    abstract var id: Long?
    abstract var username: String
    abstract var faculty: String?
    abstract var grade: String?
    abstract var avatar: String?
}