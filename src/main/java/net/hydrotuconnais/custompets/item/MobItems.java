package net.hydrotuconnais.custompets.item;

import net.hydrotuconnais.custompets.CustomPets;
import net.hydrotuconnais.custompets.entity.ModEntities;
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

    public static final Map<String, RegistryObject<Item>> SPAWN_EGGS = new HashMap<>();

    static {
        registerSpawnEgg("elephant", 0xb6b39e, 0x787671);
    }

    private static void registerSpawnEgg(String name, int primaryColor, int secondaryColor) {
        SPAWN_EGGS.put(name, ITEMS.register(name + "_spawn_egg",
                () -> new ForgeSpawnEggItem(
                        ModEntities.ELEPHANT,
                        primaryColor,
                        secondaryColor,
                        new Item.Properties()
                )));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
