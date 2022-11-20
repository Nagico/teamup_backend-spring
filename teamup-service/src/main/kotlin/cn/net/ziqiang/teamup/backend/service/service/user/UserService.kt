package cn.net.ziqiang.teamup.backend.service.service.user

import cn.net.ziqiang.teamup.backend.common.dto.user.UpdateUserProfileDto
import cn.net.ziqiang.teamup.backend.common.entity.User
import cn.net.ziqiang.teamup.backend.service.vo.user.UserInfoVO
import cn.net.ziqiang.teamup.backend.service.vo.user.UserProfileVO

interface UserService {
    /**
     * 通过id获取用户，没有则抛出异常
     * @param id
     * @return
     */
    fun getUserById(id: Long): User

    /**
     * 通过id获取用户，没有则返回Null
     * @param id
     * @return
     */
    fun getUserByIdOrNull(id: Long): User?

    /**
     * 通过id获取用户简要信息
     *
     * @param id
     * @return
     */
    fun getUserInfoById(id: Long): UserInfoVO

    /**
     * 通过id获取用户详细资料
     *
     * @param id
     * @return
     */
    fun getUserProfileById(id: Long): UserProfileVO

    /**
     * 更新用户信息
     * @param id
     * @param dto
     */
    fun updateUser(id: Long, dto: UpdateUserProfileDto): UserProfileVO

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

    fun countByUsername(username: String?): Int
}