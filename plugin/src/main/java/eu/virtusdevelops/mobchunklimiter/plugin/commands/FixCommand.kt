package eu.virtusdevelops.mobchunklimiter.plugin.commands

import eu.virtusdevelops.mobchunklimiter.api.MobChunkLimiterAPI
import eu.virtusdevelops.mobchunklimiter.core.MobAmount
import eu.virtusdevelops.mobchunklimiter.plugin.MobChunkLimiterPlugin
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.paper.util.sender.Source

class FixCommand : AbstractCommand {
    private lateinit var plugin: MobChunkLimiterPlugin
    override fun registerCommand(plugin: MobChunkLimiterPlugin, annotationParser: AnnotationParser<Source>) {
        annotationParser.parse(this)
        this.plugin = plugin
    }


    @Command("mobchunklimiter fix")
    @Permission("mobchunklimiter.command.fix")
    @CommandDescription("Scans the chunk and fixes mobs")
    fun scanCommand(ctx: Source){
        if(ctx.source() !is Player){
            ctx.source().sendMessage("This command can only be executed by a player!")
            return
        }

        val player = ctx.source() as Player
        val chunk = player.chunk


        // send info
        plugin.getChunkManager().scanChunk(chunk)

        player.sendMessage(MobChunkLimiterPlugin.MM.deserialize(
            "<green>Okay scanned and updated chunk"
        ))

    }
}