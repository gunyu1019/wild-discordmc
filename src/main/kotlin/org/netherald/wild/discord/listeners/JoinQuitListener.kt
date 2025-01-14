package org.netherald.wild.discord.listeners

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.netherald.wild.discord.WildDiscord
import org.netherald.wild.discord.utils.FormatModule

class JoinQuitListener(private val plugin: WildDiscord): Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (!plugin.config.getBoolean("accessEnable")) return
        val formatModule = FormatModule()
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        val format: String = plugin.config.getString("joinFormat")?:
        "**<player>**님이 게임에 들어왔습니다. 현재 플레이어 수: <online>/<max>명"

        if (plugin.config.getBoolean("joinEmbed")) {
            val title: String? = plugin.config.getString("joinEmbedTitle")
            val description: String = formatModule.replaceAccessFormat(event, format, false)
            val color: Int = plugin.config.getInt("joinEmbedColor")
            val builder = EmbedBuilder().setDescription(description)
                .setColor(color)
                .setAuthor(null, null, "https://crafatar.com/avatars/${event.player.uniqueId}?size=64&overlay=true")

            if (!(title == null || title == "")) {
                builder.setTitle(title)
            }

            val embed: MessageEmbed = builder.build()
            channel?.sendMessage(embed)?.queue()
        } else {
            channel?.sendMessage(formatModule.replaceAccessFormat(event, format, false))?.queue()
        }
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        if (!plugin.config.getBoolean("accessEnable")) return
        val formatModule = FormatModule()
        val channel = WildDiscord.jda?.getTextChannelById(plugin.config.getString("channelId")!!)
        val format: String = plugin.config.getString("leaveFormat")?:
        "**<player>**님이 게임에서 나갔습니다. 현재 플레이어 수: <online>/<max>명"

        if (plugin.config.getBoolean("leaveEmbed")) {
            val title: String? = plugin.config.getString("leaveEmbedTitle")
            val description: String = formatModule.replaceAccessFormat(event, format, true)
            val color: Int = plugin.config.getInt("leaveEmbedColor")
            val builder = EmbedBuilder().setDescription(description)
                .setColor(color)
                .setAuthor(null, null, "https://crafatar.com/avatars/${event.player.uniqueId}?size=64&overlay=true")

            if (!(title == null || title == "")) {
                builder.setTitle(title)
            }

            val embed: MessageEmbed = builder.build()
            channel?.sendMessage(embed)?.queue()
        } else {
            channel?.sendMessage(formatModule.replaceAccessFormat(event, format, true))?.queue()
        }
    }
}