package net.hydrotuconnais.custompets;

import com.mojang.logging.LogUtils;
import net.hydrotuconnais.custompets.commands.PetCommand;
import net.hydrotuconnais.custompets.entity.ModEntities;
import net.hydrotuconnais.custompets.entity.client.ElephantRenderer;
import net.hydrotuconnais.custompets.item.MobCreativeModeTabs;
import net.hydrotuconnais.custompets.item.MobItems;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CustomPets.MOD_ID)
public class CustomPets {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "custompets";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public CustomPets(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        MobItems.register(modEventBus);

        ModEntities.register(modEventBus);

        MobCreativeModeTabs.register(modEventBus);


        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTab() == MobCreativeModeTabs.HYDROTUCONNAIS_ITEMS_TAB.get()) {
            event.accept(MobItems.ELEPHANT_SPAWN_EGG.get());
        }
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @SubscribeEvent
    public void onRegisterCommands(net.minecraftforge.event.RegisterCommandsEvent event) {
        PetCommand.register(event.getDispatcher());
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.ELEPHANT.get(), ElephantRenderer::new);
        }
    }
}
