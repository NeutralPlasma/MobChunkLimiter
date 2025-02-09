package eu.virtusdevelops.mobchunklimiter.core.providers

import org.bukkit.entity.Mob

class VanillaKillProvider : KillProvider {
    override fun init() {

    }

    override fun killEntity(entity: Mob) {
        entity.health = 0.0
    }
}