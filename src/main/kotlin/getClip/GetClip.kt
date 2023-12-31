package getClip

import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.helix.domain.Clip
import java.io.FileInputStream
import java.util.Properties
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.random.Random

class GetClip {
    /**
     * Download clip
     *
     * @param videoId vod id
     * @param startTime time when clip starts in seconds
     * @param endTime time when clip starts in seconds
     * @param uniqueNumber number for unique title
     */
    private fun downloadClip(videoId : Int, startTime : String, endTime : String, uniqueNumber : String) {
        val scriptName = "-"
        val url = "https://www.twitch.tv/videos/$videoId"
        // create process
        val processBuilder = ProcessBuilder("python", scriptName, url, startTime, endTime, uniqueNumber)
        processBuilder.redirectErrorStream(true)
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)

        val process = processBuilder.start()
        // start yt-dlp script
        GetClip::class.java.getResourceAsStream("/yt-dlp.py")!!.copyTo(process.outputStream)
        process.outputStream.close()
        val exitCode = process.waitFor()
        println("Script exited with code $exitCode")
    }

    /**
     * Handle clip
     *
     * @param clip clip object from getClip()
     * @param counter number for unique title
     */
    private fun handleClip (clip : Clip, counter : Int) : Unit {
        // check if clip has no related vod
        if (clip.videoId == "") {
            println(
                "\u001B[31m ERROR, no vod found \u001B[0m" +
                clip.broadcasterName +
                        " Clip: ${clip.url} " +
                        "Views: ${clip.viewCount} " +
                        "Title: ${clip.title} " +
                        "from: ${clip.vodOffset} " +
                        "for: ${clip.duration} "
            )

            return
        }
        val vodId = clip.videoId.toInt()
        val randomNumber = Random.nextInt(5, 10)
        val startTime = clip.vodOffset
        // create filename structured [title][number] only letters
        val fileName = clip.title.replace("[^a-zA-ZäöüÄÖÜß]".toRegex(), " ") + " " +  counter
        println(
            clip.broadcasterName +
                " Clip: ${clip.url} " +
                "Views: ${clip.viewCount} " +
                "Title: ${clip.title} " +
                "from: ${clip.vodOffset} " +
                "for: ${clip.duration} "
        )
            downloadClip(vodId, "$startTime" , "${startTime +randomNumber+60}", fileName)

    }

    /**
     * Get clip
     *  authenticate and start getting clips
     */
    fun getClip() {
        // temp auth Jannik
        val clientId = getConfig("clientId")
        val clientSecret = getConfig("clientSecret")
        // initialize client
        val twitchClient = TwitchClientBuilder.builder()
            .withEnableHelix(true)
            .withClientId(clientId)
            .withClientSecret(clientSecret)
            .build()
        // list of streamers
        val streamer = getConfig("streamerList").split(", ")
        // list of streamer-ID's
        val streamerId: ArrayList<String> = ArrayList()
        // get streamer ID's for clips
        val resultUserList = twitchClient.helix.getUsers(null, null, streamer).execute()
        // store ID's
        for (i in 0 until resultUserList.users.size) {
            streamerId.add(resultUserList.users[i].id)
        }
        // get date (00:00 O'clock)
            // val date = Instant.now().atOffset(ZoneOffset.UTC).with(LocalTime.MIN).toInstant()
        // last 24h
         val date = Instant.now().minus(1, ChronoUnit.DAYS)
        // counting variable for unique name
        var count = 0
        // get all clips
        for (i in 0 until streamerId.size) {
            val clipList =
                twitchClient.helix.getClips(null, streamerId[i], null, null, null, null, 10, date, null, null).execute()
            clipList.`data`.forEach { clip ->
                handleClip(clip, count)
                count++
            }
        }
    }

    /**
     * GetConfig
     * @param key key to get value
     * @return value as String
     */
    private fun getConfig (key : String) : String{
        val properties = Properties()
        val inputStream = GetClip::class.java.getResourceAsStream("/config.properties")
        properties.load(inputStream)
        return properties.getProperty(key)
    }
}