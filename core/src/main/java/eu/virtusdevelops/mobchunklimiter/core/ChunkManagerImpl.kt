package eu.virtusdevelops.mobchunklimiter.core

import eu.virtusdevelops.mobchunklimiter.api.ChunkManager
import eu.virtusdevelops.mobchunklimiter.core.providers.KillProvider
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

class ChunkManagerImpl(private val plugin: JavaPlugin,
                       private val fileLogger: FileLogger,
                        private val killProvider: KillProvider) : ChunkManager {

    private var inited = false

    private var maxMobLimits : MutableMap<EntityType, Int> = mutableMapOf()
    private var globalMobLimit : Int = 0
    private var chunks: MutableList<Chunk> = mutableListOf()

    private var checkerTask : BukkitTask? = null

    override fun initConfiguration() {
        if(checkerTask != null && !checkerTask!!.isCancelled){
            checkerTask!!.cancel()
        }
        inited = false
        maxMobLimits.clear()
        // load from config.yml
        globalMobLimit = plugin.config.getInt("global-mob-limit")

        val config = plugin.getConfig().getConfigurationSection("mob-limits")

        // scan all possible mob types
        EntityType.entries.forEach { type ->
            if(config != null && config.contains(type.name)) {
                maxMobLimits[type] = config.getInt(type.name)
            } else {
                maxMobLimits[type] = globalMobLimit
            }
        }
        inited = true
        processDelayed()



        checkerTask = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            Bukkit.getWorlds().forEach { world ->
                world.loadedChunks.forEach { chunk ->
                    scanChunk(chunk)
                }
            }
        }, 20L, 1000L)




    }


    private fun processDelayed(){
        for(chunk in chunks){
            scanChunk(chunk)
        }
        chunks.clear()
    }


    override fun scanChunk(chunk: Chunk) {

        // Return early if the chunk has no entities
        if (chunk.entities.isEmpty()) return

        // Initialize only if necessary
        if (!inited) {
            chunks.add(chunk)
            return
        }

        // Use a mutable map to count mobs of each type in the chunk
        val mobCountByType = mutableMapOf<EntityType, MobAmount>()
        var totalMobCount = 0
        var removedMobs = 0

        // Count the total mobs and group them by type
        chunk.entities.forEach { entity ->
            if (entity is Mob) {
                val mobType = entity.type // Assuming a property identifying the type of mob
                if (mobCountByType[mobType] == null) {
                    mobCountByType[mobType] = MobAmount(mobType, 1, getLimit(mobType))
                }else{
                    mobCountByType[mobType]!!.amount += 1
                }
                totalMobCount++
            }
        }

        // Check if we need to remove mobs (global or per-type limits exceeded)
        if (totalMobCount > globalMobLimit || mobCountByType.any { it.value.getRatio() > 1 }) {
            //val toRemove = mutableListOf<Mob>() // List of mobs to remove




            // Use a while loop for mob removal based on type ratio and global limit
            while (totalMobCount > globalMobLimit || mobCountByType.values.any { it.getRatio() > 1 }) {
                // Re-sort the excess list by ratio (descending priority)
                val sortedExcessTypes = mobCountByType.entries
                    .sortedByDescending { it.value.getRatio() } // Highest ratio processed first


                val type = sortedExcessTypes.first().key
                val mobAmount = sortedExcessTypes.first().value

                val mobsOfType = chunk.entities.filterIsInstance<Mob>().filter { it.type == type }

                if (mobsOfType.isNotEmpty()) {
                    // Remove one mob of this type
                    val mobToRemove = mobsOfType.first()
                    fileLogger.logMobKill(mobToRemove, System.currentTimeMillis())
                    /*val stacked = roseStackerAPI.getStackedEntity(mobToRemove)
                    if(stacked != null){
                        stacked.killEntireStack()
                    }else{
                        mobToRemove.health = 0.0
                    }*/
                    killProvider.killEntity(mobToRemove)

                    removedMobs++
                    //toRemove.add(mobToRemove)
                    mobAmount.amount-- // Reduce recorded excess for this type
                    totalMobCount--
                }

            }

            // Remove the excess mobs (set their health to 0.0 or use your removal logic)
            //toRemove.forEach { it.health = 0.0 }

            // Log the removal results for debugging
            fileLogger.logInfo("Removed ${removedMobs} excess mobs from the chunk to meet global and per-type limits. Total mobs now: $totalMobCount")
        }
    }


    override fun setLimit(entity: EntityType, limit: Int) {
        if(limit < 0) throw IllegalArgumentException("Limit must be greater than 0")
        maxMobLimits[entity] = limit
    }

    override fun getLimit(entity: EntityType): Int {
        if(maxMobLimits.containsKey(entity)) return maxMobLimits[entity]!!
        return globalMobLimit;
    }

    override fun setDefaultLimit(limit: Int) {
        if(limit < 0) throw IllegalArgumentException("Limit must be greater than 0")
        globalMobLimit = limit
    }

    override fun getDefaultLimit(): Int {
        return globalMobLimit
    }

    override fun canSpawnMoreEntities(chunk: Chunk, entity: EntityType): Boolean {

        val totalMobCount = chunk.entities.count { it is Mob }
        val mobCountByType = chunk.entities.count { it is Mob && it.type == entity }

        return totalMobCount < globalMobLimit && mobCountByType < getLimit(entity)
    }

    override fun isInited(): Boolean {
        return inited
    }
}