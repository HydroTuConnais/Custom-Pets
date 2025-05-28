package net.hydrotuconnais.custompets.item;

import net.hydrotuconnais.custompets.CustomPets;
import net.hydrotuconnais.custompets.entity.ModEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MobCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CustomPets.MOD_ID);

    public static final RegistryObject<CreativeModeTab> HYDROTUCONNAIS_ITEMS_TAB = CREATIVE_MODE_TABS.register("hydrotuconnais_items_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(net.minecraft.world.item.Items.BONE))
                    .title(Component.translatable(CustomPets.MOD_ID))
                    .displayItems((itemDisplayParameters, output) -> {
                        MobItems.SPAWN_EGGS.forEach((key, value) -> output.accept(value.get()));
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
