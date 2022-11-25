package cn.net.ziqiang.teamup.backend.common.entity

import io.swagger.v3.oas.annotations.media.Schema
import javax.persistence.Column
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.Lob

@Entity(name = "exception_log")
@DiscriminatorValue("0")
class ExceptionLog (
    @Column(name = "eid")
    @Schema(description = "异常ID")
    var eid: String? = null,

    @Column(name = "class_type")
    @Schema(description = "API异常类型")
    var classType: String? = null,

    @Column(name = "class_code")
    @Schema(description = "API异常码")
    var exceptionCode: String? = null,

    @Column(name = "exception_type")
    @Schema(description = "Java异常类型")
    var exceptionType: String? = null,

    @Lob
    @Column(name = "exception_msg")
    @Schema(description = "Java异常信息")
    var exceptionMsg: String? = null,

    @Lob
    @Column(name = "exception_stack")
    @Schema(description = "Java异常栈")
    var exceptionStack: String? = null,

) : RequestLog() {
    override fun toString(): String {
        return "ExceptionLog{" +
        "id=" + id +
        ", exceptionType=" + exceptionType +
        ", exceptionMsg=" + exceptionMsg +
        ", exceptionStack=" + exceptionStack +
        "}"
    }
}
