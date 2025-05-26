package net.hydrotuconnais.custompets.event;

import net.hydrotuconnais.custompets.CustomPets;
import net.hydrotuconnais.custompets.entity.client.ElephantModel;
import net.hydrotuconnais.custompets.entity.custom.ElephantEntity;
import net.hydrotuconnais.custompets.entity.ModEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CustomPets.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ElephantModel.LAYER_LOCATION, ElephantModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ELEPHANT.get(), ElephantEntity.createAttributes().build());
    }
}