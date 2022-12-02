package cn.net.ziqiang.teamup.backend.util.request

import org.springframework.util.StreamUtils
import java.io.*
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper


class CachedBodyHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private val cachedBody: ByteArray

    init {
        val requestInputStream: InputStream = request.inputStream
        cachedBody = StreamUtils.copyToByteArray(requestInputStream)
    }

    override fun getInputStream(): ServletInputStream {
        return CachedBodyServletInputStream(cachedBody)
    }

    override fun getReader(): BufferedReader {
        // Create a reader from cachedContent
        // and return it
        val byteArrayInputStream = ByteArrayInputStream(cachedBody)
        return BufferedReader(InputStreamReader(byteArrayInputStream))
    }
}