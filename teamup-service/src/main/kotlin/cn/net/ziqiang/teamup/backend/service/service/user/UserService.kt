package cn.net.ziqiang.teamup.backend.service.service.user

import cn.net.ziqiang.teamup.backend.common.pojo.dto.user.UpdateUserProfileDto
import cn.net.ziqiang.teamup.backend.common.pojo.entity.User
import cn.net.ziqiang.teamup.backend.common.pojo.vo.user.UserInfoVO
import cn.net.ziqiang.teamup.backend.common.pojo.vo.user.UserProfileVO
import org.springframework.web.multipart.MultipartFile

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
     * 更新用户头像
     *
     * @param id 用户id
     * @param avatar 头像文件
     * @return
     */
    fun updateUserAvatar(id: Long, avatar: MultipartFile): UserProfileVO

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
}