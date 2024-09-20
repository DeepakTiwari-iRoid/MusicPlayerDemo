package com.app.musicplayerdemo.utils

import com.app.musicplayerdemo.modal.SongsResponse
import com.google.gson.Gson

object TempData {

    val musicItem = """{
  "data": {
    "id": 1,
    "player_type": 1,
    "content": "M001",
    "title": "Content 1",
    "short_description": "Description",
    "male_teacher": {
      "id": 1,
      "name": "Teacher 1",
      "image": "https://dev.iroidsolutions.com/kavana-meditation-backend/public/storage/teacher/xhGZ0J0fxYTBaGPCWoGziNxi01jewcnZoQnvjUir.png",
      "track": 8
    },
    "female_teacher": {
      "id": 2,
      "name": "Tushar",
      "image": "https://dev.iroidsolutions.com/kavana-meditation-backend/public/storage/teacher/pGt28TYaMeSZJshRLaoQxlBaE7BgXMQkBDxYZtBL.png",
      "track": 13
    },
    "primary_narrator": 1,
    "author": "Author 1",
    "translated": true,
    "artist": null,
    "type": "Type 1",
    "main_category": [
      {
        "id": 1,
        "name": "01 - For Beginners"
      },
      {
        "id": 2,
        "name": "02 - Daily Living"
      }
    ],
    "sub_category": [
      {
        "id": 1,
        "name": "03 - Introduction: The Art of Mindfulness"
      }
    ],
    "tag": [
      {
        "id": 1,
        "name": "Tag 1"
      }
    ],
    "lyrics": "Lyrics",
    "sources": "Source",
    "more_info": "More Info",
    "content_lock": false,
    "thumbnail_picture": "https://dev.iroidsolutions.com/kavana-meditation-backend/public/storage/content/thumbnail_picture/KDcjRQsIMa1XGdNACBl5OlzOitSjRLi15usLtlKP.png",
    "bg_picture": null,
    "background_sound_file": [
      {
        "id": 1,
        "name": "Background Sound 1",
        "file": "https://dev.iroidsolutions.com/kavana-meditation-backend/public/storage/content/background_sound_file/88mCcUghSVi9tv8QKKTHphioHUMofK2jUoSLA9Oc.mp3",
        "is_primary_file": true,
        "duration": null
      },
      {
        "id": 2,
        "name": "Background Sound 2",
        "file": "https://dev.iroidsolutions.com/kavana-meditation-backend/public/storage/content/background_sound_file/zz9UUwqdi07dvfGBR7NKM5fbqLM5LBbcGv0Sj4NP.mp3",
        "is_primary_file": false,
        "duration": null
      }
    ],
    "male_narrator_file": [
      {
        "id": 1,
        "time_id": 1,
        "time_length": "3 Minutes",
        "file": "https://dev.iroidsolutions.com/kavana-meditation-backend/public/storage/content/narrator_file/ludB0bGtZMDS519ZWQs880ZVAMCcIIrsBXN8YA9x.mp3",
        "is_primary_file": true,
        "duration": null
      },
      {
        "id": 2,
        "time_id": 3,
        "time_length": "8 Minutes",
        "file": "https://dev.iroidsolutions.com/kavana-meditation-backend/public/storage/content/narrator_file/GDyli9F6DMudsVn4ZaFQ253VG8O5FrtPziMmTbhW.mp3",
        "is_primary_file": false,
        "duration": null
      }
    ],
    "female_narrator_file": [],
    "background_music_file": [
      {
        "id": 1,
        "name": "Background Music 1",
        "file": "https://dev.iroidsolutions.com/kavana-meditation-backend/public/storage/content/background_music_file/z187m05zoNUhypGOeldqF6Jan33hK3wGCIgQCCdb.mp3",
        "is_primary_file": true,
        "duration": null
      },
      {
        "id": 2,
        "name": "Background Music 2",
        "file": "https://dev.iroidsolutions.com/kavana-meditation-backend/public/storage/content/background_music_file/P42TKpEDH0r84acEDVLxasdwcgnw7ufMv8gPidqS.mp3",
        "is_primary_file": false,
        "duration": null
      },
      {
        "id": 3,
        "name": "Background Music 3",
        "file": "https://dev.iroidsolutions.com/kavana-meditation-backend/public/storage/content/background_music_file/5nD16McQBto6mgYLTovm6wcGzX969dKB405WClLH.mp3",
        "is_primary_file": false,
        "duration": null
      }
    ],
    "music_file": null,
    "video_file": null,
    "is_favorite": false
  }
}"""

    val songsResponse = Gson().fromJson<SongsResponse>(musicItem, SongsResponse::class.java)
}