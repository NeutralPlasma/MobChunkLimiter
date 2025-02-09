package eu.virtusdevelops.mobchunklimiter.plugin.listeners

import eu.virtusdevelops.mobchunklimiter.api.ChunkManager
import eu.virtusdevelops.mobchunklimiter.plugin.MobChunkLimiterPlugin
import org.bukkit.Material
import org.bukkit.entity.Animals
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.ItemStack
import java.util.*


class BreedingListener(private val chunkManager: ChunkManager) : Listener {





    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun onBreed(event: PlayerInteractAtEntityEvent){
        val entity: Entity = event.rightClicked
        if (entity !is Animals || !entity.canBreed()) return

        val player = event.player
        val breedingItem: ItemStack = player.inventory.getItem(event.hand)

        // check if item is a valid breeding item for the entity
        if(!entity.isBreedItem(breedingItem)) return

        if(chunkManager.canSpawnMoreEntities(entity.chunk, entity.type)) return

        player.sendMessage(MobChunkLimiterPlugin.MM.deserialize("<red>V tej regiji je že preveč <gold><lang:entity.minecraft.${entity.type.name.lowercase(
            Locale.getDefault()
        )}><red>!")    )

        event.isCancelled = true
    }
}