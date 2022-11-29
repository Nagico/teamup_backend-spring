package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Tag

interface TagService {
    fun getOrCreateTag(content: String): Tag

    fun getOrCreateTags(contents: List<String>): List<Tag>
}