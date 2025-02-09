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

class ScanCommand : AbstractCommand {
    private lateinit var plugin: MobChunkLimiterPlugin
    override fun registerCommand(plugin: MobChunkLimiterPlugin, annotationParser: AnnotationParser<Source>) {
        annotationParser.parse(this)
        this.plugin = plugin
    }


    @Command("mobchunklimiter scan")
    @Permission("mobchunklimiter.command.scan")
    @CommandDescription("Scans the chunk for mobs and prints mob information")
    fun scanCommand(ctx: Source){
        if(ctx.source() !is Player){
            ctx.source().sendMessage("This command can only be executed by a player!")
            return
        }

        val player = ctx.source() as Player
        val chunk = player.chunk

        val mobCountByType = mutableMapOf<EntityType, MobAmount>()
        var totalMobCount = 0
        chunk.entities.forEach { entity ->
            if (entity is Mob) {
                val mobType = entity.type // Assuming a property identifying the type of mob
                if (mobCountByType[mobType] == null) {
                    mobCountByType[mobType] = MobAmount(mobType, 1, plugin.getChunkManager().getLimit(mobType))
                }else{
                    mobCountByType[mobType]!!.amount += 1
                }
                totalMobCount++
            }
        }


        // send info
        mobCountByType.forEach { (type, mob) ->
            player.sendMessage(MobChunkLimiterPlugin.MM.deserialize(
                "<yellow>$type <green>-> <gold>${mob.amount} <green>(<gold>${mob.maxAmount}<green>/<gold>${mob.getRatio()}<green>)"
                //"<green>Mob type <yellow>$type<green>: <gold>$count<green>, limit: <gold>${plugin.getChunkManager().getLimit(type)}"
            ))
        }
        player.sendMessage(MobChunkLimiterPlugin.MM.deserialize(
            "<green>Total mob count: <yellow>$totalMobCount <green>limit: <gold>${plugin.getChunkManager().getDefaultLimit()}"
        ))

    }
}