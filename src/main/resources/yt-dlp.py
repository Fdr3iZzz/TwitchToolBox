import yt_dlp
import sys

URLS = sys.argv[1]
start_time : str = yt_dlp.utils.parse_duration(sys.argv[2])
end_time : str = yt_dlp.utils.parse_duration(sys.argv[3])
name = sys.argv[4]

print(f'URL: {URLS}')
print(f'Start Time: {start_time}, End Time: {end_time}')

ydl_opts = {'download_ranges': yt_dlp.utils.download_range_func([], [[start_time, end_time]]),
              'outtmpl': f'Z:/desktop/Clips/{name}.%(ext)s'
             # 'outtmpl': f'C:/Users/Franz3/Desktop/Clips/{name}.%(ext)s'
             # ,'verbose': True
            }

with yt_dlp.YoutubeDL(ydl_opts) as ydl:
    ydl.download(URLS)