package cn.net.ziqiang.teamup.backend.service.service.impl

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Tag
import cn.net.ziqiang.teamup.backend.dao.repository.TagRepository
import cn.net.ziqiang.teamup.backend.service.service.TagService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TagServiceImpl : TagService {
    @Autowired
    private lateinit var tagRepository: TagRepository

    override fun getOrCreateTag(content: String): Tag {
        return tagRepository.findByContent(content) ?: tagRepository.save(Tag(content = content))
    }

    override fun getOrCreateTags(contents: List<String>): List<Tag> {
        return tagRepository.findAllByContentIn(contents).toMutableList().apply {
            val newTags = contents.filter { it -> !this.map { it.content }.contains(it) }.map { Tag(content = it) }
            addAll(tagRepository.saveAll(newTags))
        }
    }
}