package eu.virtusdevelops.mobchunklimiter.plugin.commands

import eu.virtusdevelops.mobchunklimiter.plugin.MobChunkLimiterPlugin
import org.incendo.cloud.CommandManager
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.paper.util.sender.Source

class CommandRegistry(
    private val plugin: MobChunkLimiterPlugin,
    private val manager: CommandManager<Source>
) {
    private val annotationParser: AnnotationParser<Source> = AnnotationParser(manager, Source::class.java)


    private val COMMANDS: List<AbstractCommand> = listOf(
        HelpCommand(),
        ScanCommand(),
        FixCommand(),
        ReloadCommand()
    )


    init {
        registerCommands()
    }


    private fun registerCommands() {
        COMMANDS.forEach {
                command -> command.registerCommand(plugin, annotationParser)
        }
    }
}