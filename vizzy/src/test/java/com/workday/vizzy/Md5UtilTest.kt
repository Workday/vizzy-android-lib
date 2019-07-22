package com.workday.vizzy

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class Md5UtilTest {

    @Test
    fun `a file should have an md5 consistent with the one from a Desktop`() {
        val assetsManager = ApplicationProvider.getApplicationContext<Context>().assets
        val md5result = Md5Util.calculateMD5(assetsManager.open("MainActivity.png"))
        assertEquals("ec3938596ecd493b9612f760745436df", md5result)
    }

    @Test
    fun `Identical files with different timestamp return the same md5`() {
        val assetsManager = ApplicationProvider.getApplicationContext<Context>().assets
        val md5result = Md5Util.calculateMD5(assetsManager.open("MainActivity.png"))
        val md5result2 = Md5Util.calculateMD5(assetsManager.open("MainActivity2.png"))
        assertEquals(md5result, md5result2)
    }
}
