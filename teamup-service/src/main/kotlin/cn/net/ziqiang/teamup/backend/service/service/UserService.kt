package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.constant.status.UserStatus
import cn.net.ziqiang.teamup.backend.common.pojo.auth.TokenBean
import cn.net.ziqiang.teamup.backend.common.pojo.entity.User
import cn.net.ziqiang.teamup.backend.common.pojo.vo.user.*
import org.springframework.web.multipart.MultipartFile

interface UserService {
    /**
     * 通过id获取用户，没有则抛出异常
     * @param id
     * @return
     */
    fun getUserById(id: Long, useCache: Boolean = true): User

    /**
     * 通过id获取用户，没有则返回Null
     * @param id
     * @return
     */
    fun getUserByIdOrNull(id: Long, useCache: Boolean = true): User?

    /**
     * 通过id获取用户简要信息
     *
     * @param id
     * @return
     */
    fun getUserInfoById(id: Long): User

    /**
     * 通过id获取用户详细资料
     *
     * @param id
     * @return
     */
    fun getUserProfileById(id: Long): User

    /**
     * 更新用户信息
     * @param id
     * @param dto
     */
    fun updateUser(id: Long, dto: User): User

    /**
     * 更新用户头像
     *
     * @param id 用户id
     * @param avatar 头像文件
     * @return
     */
    fun updateUserAvatar(id: Long, avatar: MultipartFile): User

    /**
     * 是否是可以正常使用的用户
     * @param user
     */
    fun checkNormalUserOrThrow(user: User)

    /**
     * 是否是为激活的用户
     * @param user
     */
    fun checkActiveUserOrThrow(user: User)

    /**
     * 检查用户名数量
     *
     * @param username 待检测用户名
     * @return
     */
    fun countByUsername(username: String?): Int

    /**
     * 检查手机号数量
     *
     * @param phone 待检测手机号
     * @return
     */
    fun countByPhone(phone: String?): Int

    /**
     * 用户名密码注册
     *
     * @return
     */
    fun register(registerDto: UserDto): TokenBean

    /**
     * 重置密码
     *
     * @param resetPasswordDto
     */
    fun resetPassword(resetPasswordDto: UserDto)

    /**
     * 修改密码
     *
     * @param changePasswordDto
     */
    fun changePassword(userId: Long, changePasswordDto: UserDto)

    /**
     * 修改手机号
     *
     * @param userId
     * @param changePhoneDto
     */
    fun changePhone(userId: Long, changePhoneDto: UserDto)

    /**
     * ws登录
     *
     * @param userId
     * @return
     */
    fun messageLogin(userId: Long) : User

    /**
     * ws登出
     *
     * @param user
     */
    fun messageLogout(user: User)

    /**
     * 获取用户状态
     *
     * @param userId
     * @return
     */
    fun getUserStatus(userId: Long): UserStatus
}