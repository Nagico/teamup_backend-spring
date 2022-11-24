package cn.net.ziqiang.teamup.backend.common.utils.sort

import org.springframework.data.domain.Sort

fun handleSort(sort: String = "-id"): Sort {
    return if (sort.startsWith("-")) {
        Sort.by(Sort.Direction.DESC, sort.substring(1))
    } else {
        Sort.by(Sort.Direction.ASC, sort)
    }
}