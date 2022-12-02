package cn.net.ziqiang.teamup.backend.util.request

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream


class CachedBodyServletInputStream(cachedBody: ByteArray) : ServletInputStream() {
    private val cachedBodyInputStream: InputStream

    init {
        cachedBodyInputStream = ByteArrayInputStream(cachedBody)
    }

    override fun isFinished(): Boolean {
        try {
            return cachedBodyInputStream.available() == 0
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    override fun isReady(): Boolean {
        return true
    }

    override fun setReadListener(readListener: ReadListener) {
        throw UnsupportedOperationException()
    }

    override fun read(): Int {
        return cachedBodyInputStream.read()
    }
}