package eu.virtusdevelops.advancedcrops.plugin.commands

import eu.virtusdevelops.advancedcrops.plugin.AdvancedCrops
import org.incendo.cloud.annotations.*
import org.incendo.cloud.paper.util.sender.Source
import javax.annotation.Nullable

class HelpCommand : AbstractCommand {
    private lateinit var plugin: AdvancedCrops
    override fun registerCommand(plugin: AdvancedCrops, annotationParser: AnnotationParser<Source>) {
        annotationParser.parse(this)
        this.plugin = plugin
    }


    @Permission("advancedcrops.help")
    @Command("crops help [query]")
    @CommandDescription("Shows this menu")
    fun reloadCommand(
        ctx: Source,
        @Argument("query") @Nullable query: Array<String>?
    ){
        plugin.getMinecraftHelp().queryCommands(if (query != null) java.lang.String.join(" ", *query) else "", ctx)
    }

}