package eu.virtusdevelops.mobchunklimiter.plugin.commands

import eu.virtusdevelops.mobchunklimiter.plugin.MobChunkLimiterPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.paper.util.sender.Source

interface AbstractCommand {
    fun registerCommand(
        plugin: MobChunkLimiterPlugin,
        annotationParser: AnnotationParser<Source>
    )
}