package eu.virtusdevelops.mobchunklimiter.plugin.listeners

import eu.virtusdevelops.mobchunklimiter.api.ChunkManager
import org.bukkit.entity.Mob
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent

class MobListener(private val chunkManager: ChunkManager) : Listener {


    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun onMobSpawn(event: EntitySpawnEvent) {
        val entity = event.entity
        if(entity !is Mob) return

        if(chunkManager.canSpawnMoreEntities(event.location.chunk, entity.type)) return

        // cancel if cant
        event.isCancelled = true
    }
}