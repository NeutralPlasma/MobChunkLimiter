package eu.virtusdevelops.mobchunklimiter.plugin

import eu.virtusdevelops.mobchunklimiter.api.ChunkManager
import eu.virtusdevelops.mobchunklimiter.api.MobChunkLimiterAPI
import eu.virtusdevelops.mobchunklimiter.core.ChunkManagerImpl
import eu.virtusdevelops.mobchunklimiter.core.FileLogger
import eu.virtusdevelops.mobchunklimiter.core.providers.KillProvider
import eu.virtusdevelops.mobchunklimiter.core.providers.RoseStackerProvider
import eu.virtusdevelops.mobchunklimiter.core.providers.VanillaKillProvider
import eu.virtusdevelops.mobchunklimiter.plugin.commands.CommandRegistry
import eu.virtusdevelops.mobchunklimiter.plugin.listeners.BreedingListener
import eu.virtusdevelops.mobchunklimiter.plugin.listeners.ChunkListener
import eu.virtusdevelops.mobchunklimiter.plugin.listeners.MobListener
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.minecraft.extras.AudienceProvider
import org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler
import org.incendo.cloud.minecraft.extras.MinecraftHelp
import org.incendo.cloud.minecraft.extras.caption.ComponentCaptionFormatter
import org.incendo.cloud.paper.PaperCommandManager
import org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper
import org.incendo.cloud.paper.util.sender.Source
import org.incendo.cloud.setting.ManagerSetting
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import kotlin.io.path.pathString

class MobChunkLimiterPlugin : JavaPlugin(), MobChunkLimiterAPI  {

    companion object {
        val MM: MiniMessage = MiniMessage.miniMessage()
    }

    private lateinit var chunkManager: ChunkManager
    private lateinit var minecraftHelp: MinecraftHelp<Source>
    private lateinit var fileLogger: FileLogger
    private lateinit var killProvider: KillProvider



    override fun onEnable() {
        saveDefaultConfig()
        setupKillProvider()

        MobChunkLimiterAPI.load(this)

        val timestamp = SimpleDateFormat("yyyy-MM-dd").format(Date())

        fileLogger = FileLogger(Paths.get(dataFolder.absolutePath, "$timestamp.log").pathString)

        chunkManager = ChunkManagerImpl(this, fileLogger, killProvider)
        chunkManager.initConfiguration()


        server.pluginManager.registerEvents(ChunkListener(chunkManager), this)
        server.pluginManager.registerEvents(MobListener(chunkManager), this)
        server.pluginManager.registerEvents(BreedingListener(chunkManager), this)

        registerCommands()
    }


    override fun getChunkManager(): ChunkManager {
        return chunkManager
    }

    override fun getPlugin(): JavaPlugin {
        return this
    }


    private fun registerCommands(){

        val manager : PaperCommandManager<Source> = PaperCommandManager
            .builder(PaperSimpleSenderMapper.simpleSenderMapper())
            .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
            .buildOnEnable(this)

        manager.settings().set(ManagerSetting.ALLOW_UNSAFE_REGISTRATION, true)

        manager.exceptionController().clearHandlers()


        val audienceProvider = AudienceProvider<Source> { source: Source -> source.source()  }

        registerHelpCommand(audienceProvider, manager)


        MinecraftExceptionHandler.create(audienceProvider)
            .defaultHandlers()
            .decorator { component: Component? ->
                Component.text()
                    .append(Component.text("[", NamedTextColor.DARK_GRAY))
                    .append(Component.text("MobChunkLimiter", NamedTextColor.GOLD))
                    .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                    .append(component!!)
                    .build()
            }
            .registerTo(manager)

        manager.captionRegistry().registerProvider(MinecraftHelp.defaultCaptionsProvider())

        CommandRegistry(this, manager)
    }

    private fun registerHelpCommand(audienceProvider: AudienceProvider<Source>, commandManager: PaperCommandManager<Source>){
        minecraftHelp = MinecraftHelp.builder<Source>()
            .commandManager(commandManager)
            .audienceProvider(audienceProvider)
            .commandPrefix("/mobchunklimiter help")
            .messageProvider(MinecraftHelp.captionMessageProvider(
                commandManager.captionRegistry(),
                ComponentCaptionFormatter.miniMessage()
            ))
            .build()
    }


    private fun setupKillProvider() {
        // load from config
        var type = config.getString("killer-provider")

        if(type == null){
            type = "vanilla"
            config.set("killer-provider", type)
            saveConfig()
        }

        when(type.lowercase()){
            "vanilla" -> {
                killProvider = VanillaKillProvider()
            }
            "rosestacker" -> {
                killProvider = RoseStackerProvider()
            }
            else -> {
                killProvider = VanillaKillProvider()
            }
        }

        killProvider.init()

    }

    fun getMinecraftHelp(): MinecraftHelp<Source> {
        return minecraftHelp
    }

    fun getFileLogger(): FileLogger {
        return fileLogger
    }
}