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

class ReloadCommand : AbstractCommand {
    private lateinit var plugin: MobChunkLimiterPlugin
    override fun registerCommand(plugin: MobChunkLimiterPlugin, annotationParser: AnnotationParser<Source>) {
        annotationParser.parse(this)
        this.plugin = plugin
    }


    @Command("mobchunklimiter reload")
    @Permission("mobchunklimiter.command.reload")
    @CommandDescription("Reloads the configuration")
    fun reloadCommand(ctx: Source){
        val start = System.currentTimeMillis()
        plugin.reloadConfig()
        plugin.getChunkManager().initConfiguration()
        var end = System.currentTimeMillis() - start;
        ctx.source().sendMessage(MobChunkLimiterPlugin.MM.deserialize("<green>Config reloaded took: <gold>" + end + "<green>ms"))
    }
}