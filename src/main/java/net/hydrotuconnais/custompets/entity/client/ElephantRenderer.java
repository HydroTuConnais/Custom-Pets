package net.hydrotuconnais.custompets.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydrotuconnais.custompets.CustomPets;
import net.hydrotuconnais.custompets.entity.custom.ElephantEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ElephantRenderer extends MobRenderer<ElephantEntity, ElephantModel<ElephantEntity>> {
    public ElephantRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ElephantModel<>(pContext.bakeLayer(ElephantModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(ElephantEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(CustomPets.MOD_ID, "textures/entity/elephant/elephant.png");
    }
}
