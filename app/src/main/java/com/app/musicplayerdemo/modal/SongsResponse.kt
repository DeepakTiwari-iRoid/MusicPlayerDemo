package com.app.musicplayerdemo.modal


import com.google.gson.annotations.SerializedName

data class SongsResponse(
    @SerializedName("data")
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("player_type")
        val playerType: Int = 0,
        @SerializedName("content")
        val content: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("short_description")
        val shortDescription: String = "",
        @SerializedName("male_teacher")
        val maleTeacher: MaleTeacher = MaleTeacher(),
        @SerializedName("female_teacher")
        val femaleTeacher: FemaleTeacher = FemaleTeacher(),
        @SerializedName("primary_narrator")
        val primaryNarrator: Int = 0,
        @SerializedName("author")
        val author: String = "",
        @SerializedName("translated")
        val translated: Boolean = false,
        @SerializedName("artist")
        val artist: Any? = null,
        @SerializedName("type")
        val type: String = "",
        @SerializedName("main_category")
        val mainCategory: List<MainCategory> = listOf(),
        @SerializedName("sub_category")
        val subCategory: List<SubCategory> = listOf(),
        @SerializedName("tag")
        val tag: List<Tag> = listOf(),
        @SerializedName("lyrics")
        val lyrics: String = "",
        @SerializedName("sources")
        val sources: String = "",
        @SerializedName("more_info")
        val moreInfo: String = "",
        @SerializedName("content_lock")
        val contentLock: Boolean = false,
        @SerializedName("thumbnail_picture")
        val thumbnailPicture: String = "",
        @SerializedName("bg_picture")
        val bgPicture: Any? = null,
        @SerializedName("background_sound_file")
        val backgroundSoundFile: List<BackgroundSoundFile> = listOf(),
        @SerializedName("male_narrator_file")
        val maleNarratorFile: List<MaleNarratorFile> = listOf(),
        @SerializedName("female_narrator_file")
        val femaleNarratorFile: List<Any> = listOf(),
        @SerializedName("background_music_file")
        val backgroundMusicFile: List<BackgroundMusicFile> = listOf(),
        @SerializedName("music_file")
        val musicFile: Any? = null,
        @SerializedName("video_file")
        val videoFile: Any? = null,
        @SerializedName("is_favorite")
        val isFavorite: Boolean = false
    ) {

        data class MaleTeacher(
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("image")
            val image: String = "",
            @SerializedName("track")
            val track: Int = 0
        )

        data class FemaleTeacher(
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("image")
            val image: String = "",
            @SerializedName("track")
            val track: Int = 0
        )

        data class MainCategory(
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("name")
            val name: String = ""
        )

        data class SubCategory(
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("name")
            val name: String = ""
        )

        data class Tag(
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("name")
            val name: String = ""
        )

        data class BackgroundSoundFile(
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("file")
            val file: String = "",
            @SerializedName("is_primary_file")
            val isPrimaryFile: Boolean = false,
            @SerializedName("duration")
            val duration: Any? = null
        )

        data class MaleNarratorFile(
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("time_id")
            val timeId: Int = 0,
            @SerializedName("time_length")
            val timeLength: String = "",
            @SerializedName("file")
            val file: String = "",
            @SerializedName("is_primary_file")
            val isPrimaryFile: Boolean = false,
            @SerializedName("duration")
            val duration: Any? = null
        )

        data class BackgroundMusicFile(
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("file")
            val file: String = "",
            @SerializedName("is_primary_file")
            val isPrimaryFile: Boolean = false,
            @SerializedName("duration")
            val duration: Any? = null
        )
    }
}