package cn.net.ziqiang.teamup.backend.constant.type

enum class EsDataType(val type: String) {
    TEXT("text"),
    KEYWORD("keyword"),
    FLOAT("float"),
    LONG("long"),
    INTEGER("integer"),
    SHORT("short"),
    DOUBLE("double"),
    HALF_FLOAT("half_float"),
    SCALED_FLOAT("scaled_float"),
    BYTE("byte"),

    DATE("date"),

    BOOLEAN("boolean"),
    RANGE("rang"),
    BINARY("binary"),
    ARRAY("array"),
    OBJECT("object"),
    NESTED("nested"),
    GEO_POINT("geo_point"),
    GEO_SHAPE("geo_shape"),
    IP("ip"),
    COMPLETION("completion"),
    TOKEN_COUNT("token_count"),
    ATTACHMENT("attachment"),
    PERCOLATOR("percolator");
}

