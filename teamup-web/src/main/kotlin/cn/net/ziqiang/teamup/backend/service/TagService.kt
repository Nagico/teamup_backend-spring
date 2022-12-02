package cn.net.ziqiang.teamup.backend.service

import cn.net.ziqiang.teamup.backend.pojo.entity.Tag

interface TagService {
    /**
     * 创建或获取Tag
     *
     * @param content
     * @return
     */
    fun getOrCreateTag(content: String): Tag

    /**
     * 创建或获取Tag列表
     *
     * @param contents
     * @return
     */
    fun getOrCreateTags(contents: List<String>): List<Tag>
}