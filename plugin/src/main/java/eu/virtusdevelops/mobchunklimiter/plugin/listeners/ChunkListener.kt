package eu.virtusdevelops.mobchunklimiter.plugin.listeners

import eu.virtusdevelops.mobchunklimiter.api.ChunkManager
import org.bukkit.Chunk
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkLoadEvent

class ChunkListener(private val chunkManager: ChunkManager) : Listener {



    @EventHandler(priority = org.bukkit.event.EventPriority.NORMAL, ignoreCancelled = true)
    fun onChunkLoad(event: ChunkLoadEvent) {
        val chunk = event.chunk
        chunkManager.scanChunk(chunk)
    }


}