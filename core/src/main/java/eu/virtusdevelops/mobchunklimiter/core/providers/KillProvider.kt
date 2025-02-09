package eu.virtusdevelops.mobchunklimiter.core.providers

import org.bukkit.entity.Entity
import org.bukkit.entity.Mob

interface KillProvider {


    fun init()

    fun killEntity(entity: Mob)
}