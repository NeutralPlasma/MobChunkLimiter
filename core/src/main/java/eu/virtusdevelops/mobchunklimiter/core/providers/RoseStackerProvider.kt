package eu.virtusdevelops.mobchunklimiter.core.providers

import dev.rosewood.rosestacker.RoseStacker
import dev.rosewood.rosestacker.api.RoseStackerAPI
import org.bukkit.entity.Entity
import org.bukkit.entity.Mob

class RoseStackerProvider : KillProvider {

    private lateinit var roseStacker: RoseStackerAPI
    private var initialized = false

    override fun init() {
        if(initialized) return
        roseStacker = RoseStackerAPI.getInstance()
        initialized = true
    }

    override fun killEntity(entity: Mob) {
        val stacked = roseStacker.getStackedEntity(entity)
        if(stacked != null){
            stacked.killEntireStack()
        }else{
            entity.health = 0.0
        }
    }
}