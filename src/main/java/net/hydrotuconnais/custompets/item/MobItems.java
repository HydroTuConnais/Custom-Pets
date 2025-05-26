package net.hydrotuconnais.custompets.item;

import net.hydrotuconnais.custompets.CustomPets;
import net.hydrotuconnais.custompets.entity.ModEntities;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MobItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CustomPets.MOD_ID);

    public static final RegistryObject<Item> ELEPHANT_SPAWN_EGG = ITEMS.register("elephant_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ELEPHANT, 0xb6b39e, 0x787671, new Item.Properties()));

    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }
}
