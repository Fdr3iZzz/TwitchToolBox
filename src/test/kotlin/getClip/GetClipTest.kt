package getClip

import org.junit.jupiter.api.Test
import getClip.GetClip

import org.junit.jupiter.api.Assertions.*

class GetClipTest {

    @Test
    fun downloadClips() {
        //videoId : Int, startTime : String, endTime : String
        val startTime = "500"
        val endTime = "530"
        val idList = listOf<Int>(2013509635, 2013333621)
        idList.forEach { clipId -> GetClip().downloadClips(clipId, startTime, endTime, "Test") }

    }

    @Test
    fun handleClip() {
    }

    @Test
    fun getClip() {
        GetClip().getClip()
    }
}