package net.hydrotuconnais.custompets.entity;

import net.hydrotuconnais.custompets.CustomPets;
import net.hydrotuconnais.custompets.entity.custom.ElephantEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CustomPets.MOD_ID);

    public static final RegistryObject<EntityType<ElephantEntity>> ELEPHANT =
            ENTITY_TYPES.register("elephant",
                    () -> EntityType.Builder.of(ElephantEntity::new, MobCategory.CREATURE)
                            .sized(1.0f, 1.0f)
                            .build(CustomPets.MOD_ID + ":elephant"));

    public static List<String> getAllEntityTypes() {
        return List.of("elephant", "lion", "tiger", "bear", "wolf", "fox", "cat", "dog", "rabbit", "panda", "giraffe", "zebra", "monkey", "kangaroo", "hippo", "rhino", "leopard", "crocodile", "eagle", "owl");
        /*return ENTITY_TYPES.getEntries().stream()
                .map(registryObject -> registryObject.getId().getPath())
                .toList();
         */
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
