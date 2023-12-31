### What it does:
Currently just downloads top 10 clips of past 24h of specified streamers (clips are 1 min + some seconds)

### Issues:
- has out of bounce problem where it tries to get clip beond the vod length and therefor fails.
- prints out fail if no vod available
- (probably) fails if no clips available
- (probably) fails if no streamer available
- filepath not dynamic as it's in the python code
- no out of the box experience

### Requirements:
- [twitch authentication](https://dev.twitch.tv/console/apps/create) (put in config)
- twitch streamers you want (put in config)
- [yt-dlp](https://github.com/yt-dlp/yt-dlp/releases) (needs to be added to path)
- [FFMPEG](https://ffmpeg.org/) (needs to be added to path)
- [JDK 21](https://adoptium.net/de/temurin/archive/)
- change filepath in python code
