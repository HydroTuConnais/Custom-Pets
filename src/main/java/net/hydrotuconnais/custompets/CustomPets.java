package net.hydrotuconnais.custompets;

import com.mojang.logging.LogUtils;
import net.hydrotuconnais.custompets.commands.AdminPetsCommand;
import net.hydrotuconnais.custompets.commands.PetsCommand;
import net.hydrotuconnais.custompets.entity.ModEntities;
import net.hydrotuconnais.custompets.entity.client.ElephantRenderer;
import net.hydrotuconnais.custompets.item.MobCreativeModeTabs;
import net.hydrotuconnais.custompets.item.MobItems;
import net.hydrotuconnais.custompets.network.NetworkHandler;
import net.hydrotuconnais.custompets.utils.FileControl;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import net.minecraftforge.fml.ModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CustomPets.MOD_ID)
public class CustomPets {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "custompets";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CustomPets() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Enregistrer la config en premier
        //context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // Puis les entités
        ModEntities.register(modEventBus);

        // Puis les items
        MobItems.register(modEventBus);

        // Puis les tabs créatifs
        MobCreativeModeTabs.register(modEventBus);

        NetworkHandler.register();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    private void addCreative(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(MobCreativeModeTabs.HYDROTUCONNAIS_ITEMS_TAB.getKey())) {
            MobItems.SPAWN_EGGS.forEach((key, value) -> event.accept(value.get()));
        }
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        System.out.println("Server starting...");
        FileControl.loadPermissions();
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        System.out.println("Server stoping...");
        FileControl.savePermissions();
    }

    @SubscribeEvent
    public void onRegisterCommands(net.minecraftforge.event.RegisterCommandsEvent event) {
        PetsCommand.register(event.getDispatcher());

        AdminPetsCommand.register(event.getDispatcher());
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.ELEPHANT.get(), ElephantRenderer::new);
        }
    }
}
