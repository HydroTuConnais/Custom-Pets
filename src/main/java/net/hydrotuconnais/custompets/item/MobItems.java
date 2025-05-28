package net.hydrotuconnais.custompets.item;

import net.hydrotuconnais.custompets.CustomPets;
import net.hydrotuconnais.custompets.entity.ModEntities;
import net.hydrotuconnais.custompets.entity.custom.ElephantEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class MobItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CustomPets.MOD_ID);

    public static final Map<RegistryObject<EntityType<ElephantEntity>>, RegistryObject<ForgeSpawnEggItem>> SPAWN_EGGS = new HashMap<>();

    static {
        registerSpawnEgg(ModEntities.ELEPHANT, 0xb6b39e, 0x787671);
    }

    private static void registerSpawnEgg(RegistryObject<EntityType<ElephantEntity>> mob, int primaryColor, int secondaryColor) {
        String name = mob.getId().getPath();
        RegistryObject<ForgeSpawnEggItem> spawnEgg = ITEMS.register(name + "_spawn_egg",
                () -> new ForgeSpawnEggItem(
                        mob,
                        primaryColor,
                        secondaryColor,
                        new Item.Properties()
                ));
        SPAWN_EGGS.put(mob, spawnEgg);
    }


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
