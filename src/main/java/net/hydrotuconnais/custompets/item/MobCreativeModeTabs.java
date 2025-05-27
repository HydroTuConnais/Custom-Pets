package net.hydrotuconnais.custompets.item;

import net.hydrotuconnais.custompets.CustomPets;
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
                    .icon(() -> new ItemStack(MobItems.SPAWN_EGGS.get("elephant").get()))
                    .title(Component.translatable("item.custompets.elephant_spawn_egg"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(MobItems.SPAWN_EGGS.get("elephant").get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
