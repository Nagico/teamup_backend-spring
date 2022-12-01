package cn.net.ziqiang.teamup.backend.service.bussiness

import cn.net.ziqiang.teamup.backend.service.annotation.DocId
import cn.net.ziqiang.teamup.backend.service.annotation.EsClass
import cn.net.ziqiang.teamup.backend.service.annotation.EsField
import cn.net.ziqiang.teamup.backend.service.constant.EsDataType


@EsClass
class UserDoc (
    @DocId
    var id: Long? = null,

    @EsField(type = EsDataType.KEYWORD)
    var name: String? = null,

    @EsField(type = EsDataType.INTEGER)
    var age: Int? = null,

    @EsField(type = EsDataType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    var dec: String? = null,

    @EsField(type = EsDataType.KEYWORD)
    var sku: String? = null,

    @EsField(type = EsDataType.DOUBLE)
    var price: Double? = null,
) {
    override fun toString(): String {
        return "UserDoc(id=$id, name=$name, age=$age, dec=$dec, sku=$sku, price=$price)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserDoc) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (age != other.age) return false
        if (dec != other.dec) return false
        if (sku != other.sku) return false
        if (price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (age ?: 0)
        result = 31 * result + (dec?.hashCode() ?: 0)
        result = 31 * result + (sku?.hashCode() ?: 0)
        result = 31 * result + (price?.hashCode() ?: 0)
        return result
    }
}

