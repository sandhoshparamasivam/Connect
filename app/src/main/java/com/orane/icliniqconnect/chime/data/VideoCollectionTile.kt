package com.orane.icliniqconnect.chime.data

import com.amazonaws.services.chime.sdk.meetings.audiovideo.video.VideoTileState


data class VideoCollectionTile(
    val attendeeName: String,
    val videoTileState: VideoTileState
)
