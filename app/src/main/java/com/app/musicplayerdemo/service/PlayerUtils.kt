package com.app.musicplayerdemo.service

import android.content.Context
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.app.musicplayerdemo.R
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

object PlayerUtils {


    @UnstableApi
    enum class NotificationPlayerCustomCommandButton(
        val action: String,
        val icon: Int,
        val displayName: String
    ) {
        REWIND(
            action = "REWIND_15",
            icon = R.drawable.ic_home,
            displayName = "Rewind",
        ),
        FORWARD(
            action = "FAST_FWD_15",
            icon = R.drawable.ic_profile,
            displayName = "Forward",
        ),
        NEXT(
            action = "NEXT",
            icon = R.drawable.ic_launcher_background,
            displayName = "Next",
        ),
        PREVIOUS(
            action = "PREVIOUS",
            icon = R.drawable.ic_upgrade,
            displayName = "Previous",
        );

        val sessionCommand: SessionCommand
            get() = SessionCommand(action, Bundle.EMPTY)

        val commandButton: CommandButton
            get() = CommandButton.Builder()
                .setDisplayName(displayName)
                .setIconResId(icon)
                .setSessionCommand(sessionCommand)
                .build()
    }

    @OptIn(UnstableApi::class)
    class HandleMediaSessionCallback : MediaSession.Callback {

        val sessionCommands =
            NotificationPlayerCustomCommandButton.entries
                .map { it.sessionCommand }

        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo
        ): MediaSession.ConnectionResult {
            val sessionCommands = MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                .addSessionCommands(sessionCommands)
                .build()

            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setAvailableSessionCommands(sessionCommands).build()
        }

        override fun onCustomCommand(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            customCommand: SessionCommand,
            args: Bundle
        ): ListenableFuture<SessionResult> {
            if (customCommand.customAction == NotificationPlayerCustomCommandButton.FORWARD.action
            ) {
                // Do custom logic here
                Log.d(
                    TAG,
                    "onCustomCommand: ${NotificationPlayerCustomCommandButton.FORWARD.action}"
                )
                return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
            }
            return super.onCustomCommand(session, controller, customCommand, args)
        }
    }

    @OptIn(UnstableApi::class)
    class CustomMediaNotificationProvider(
        context: Context
    ) : DefaultMediaNotificationProvider(context) {
        
        override fun addNotificationActions(
            mediaSession: MediaSession,
            mediaButtons: ImmutableList<CommandButton>,
            builder: NotificationCompat.Builder,
            actionFactory: MediaNotification.ActionFactory
        ): IntArray {
            val playPauseButton = mediaButtons.firstOrNull {
                it.playerCommand == Player.COMMAND_PLAY_PAUSE
            }

            val notificationButtons =
                if (playPauseButton != null) {
                    ImmutableList.builder<CommandButton>().apply {
                        add(NotificationPlayerCustomCommandButton.REWIND.commandButton)
                        add(playPauseButton)
                        add(NotificationPlayerCustomCommandButton.FORWARD.commandButton)
                        add(NotificationPlayerCustomCommandButton.NEXT.commandButton)
                    }.build()
                } else {
                    mediaButtons
                }

            return super.addNotificationActions(
                mediaSession,
                notificationButtons,
                builder,
                actionFactory
            )
        }
    }
}