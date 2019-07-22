package com.workday.vizzy

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest

object Md5Util {
    fun calculateMD5(updateFile: File): String {
        val inputStream = FileInputStream(updateFile)
        return calculateMD5(inputStream)
    }

    fun calculateMD5(inputStream: InputStream): String {
        val digest = MessageDigest.getInstance("MD5")
        val buffer = ByteArray(8192)
        var read: Int
        try {
            while (true) {
                read = inputStream.read(buffer)
                if (read <= 0) {
                    break;
                }
                digest.update(buffer, 0, read)
            }
            val md5sum = digest.digest()
            val bigInt = BigInteger(1, md5sum)
            var output = bigInt.toString(16)
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0')
            return output
        } catch (e: IOException) {
            throw RuntimeException("Unable to process file for MD5", e)
        } finally {
            inputStream.close()
        }
    }
}
