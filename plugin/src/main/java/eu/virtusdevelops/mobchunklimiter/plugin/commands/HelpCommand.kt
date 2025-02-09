package eu.virtusdevelops.mobchunklimiter.plugin.commands

import eu.virtusdevelops.mobchunklimiter.plugin.MobChunkLimiterPlugin
import org.incendo.cloud.annotations.*
import org.incendo.cloud.paper.util.sender.Source
import javax.annotation.Nullable

class HelpCommand : AbstractCommand {
    private lateinit var plugin: MobChunkLimiterPlugin
    override fun registerCommand(plugin: MobChunkLimiterPlugin, annotationParser: AnnotationParser<Source>) {
        annotationParser.parse(this)
        this.plugin = plugin
    }


    @Permission("mobchunklimiter.command.help")
    @Command("mobchunklimiter help [query]")
    @CommandDescription("Shows this menu")
    fun reloadCommand(
        ctx: Source,
        @Argument("query") @Nullable query: Array<String>?
    ){
        plugin.getMinecraftHelp().queryCommands(if (query != null) java.lang.String.join(" ", *query) else "", ctx)
    }

}