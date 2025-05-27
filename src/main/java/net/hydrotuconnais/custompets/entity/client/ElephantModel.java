package net.hydrotuconnais.custompets.entity.client;

import net.hydrotuconnais.custompets.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydrotuconnais.custompets.CustomPets;
import net.hydrotuconnais.custompets.entity.custom.ElephantEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ElephantModel<T extends ElephantEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CustomPets.MOD_ID, "elephant"), "main");
    private final ModelPart body;
    private final ModelPart head;

    public ElephantModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -3.0F, -1.0F, 8.0F, 7.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, -2.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -2.993F, 8.007F));

        PartDefinition cube_r1 = tail.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(31, 17).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.007F, -0.007F, -0.7854F, 0.0F, 0.0F));

        PartDefinition right_front = body.addOrReplaceChild("right_front", CubeListBuilder.create().texOffs(35, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(35, 0).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(-3.0F, 2.0F, 0.0F));

        PartDefinition right_back = body.addOrReplaceChild("right_back", CubeListBuilder.create().texOffs(0, 34).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 34).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(-3.0F, 1.0F, 7.0F));

        PartDefinition left_back = body.addOrReplaceChild("left_back", CubeListBuilder.create().texOffs(0, 34).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.3F))
                .texOffs(0, 34).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 1.0F, 7.0F));

        PartDefinition left_front = body.addOrReplaceChild("left_front", CubeListBuilder.create().texOffs(35, 0).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.3F))
                .texOffs(35, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 2.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(35, 11).addBox(-2.5F, -3.5532F, -5.667F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.8042F, -2.4945F));

        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(19, 46).addBox(-1.5F, -1.75F, -1.75F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.4393F, 0.5923F, -3.6805F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(35, 11).addBox(-2.5F, 1.775F, -3.0F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.9586F, -1.8359F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 17).addBox(-11.5F, -25.5286F, -7.7122F, 7.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 18.9675F, 9.6425F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r5 = head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(19, 46).addBox(-1.5F, -1.75F, -1.75F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.4393F, 0.5923F, -3.6805F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 46).addBox(-3.75F, -1.0F, -1.0F, 7.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.7255F, -3.6822F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r7 = head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(47, 37).addBox(6.5F, -1.0F, 1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(47, 37).addBox(-0.5F, -1.0F, 1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 3.1958F, -5.5055F, -0.7854F, 0.0F, 0.0F));

        PartDefinition trunk = head.addOrReplaceChild("trunk", CubeListBuilder.create(), PartPose.offset(0.0F, 1.3266F, -5.3542F));

        PartDefinition cube_r8 = trunk.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(31, 27).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 1.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition trunk2 = trunk.addOrReplaceChild("trunk2", CubeListBuilder.create().texOffs(17, 37).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(17, 37).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 3.0824F, 0.3869F));

        PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create(), PartPose.offset(2.7294F, -3.6655F, -0.3503F));

        PartDefinition cube_r9 = left_ear.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(34, 37).addBox(-0.5F, -3.0F, -2.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(34, 37).addBox(-3.5F, -3.0F, -2.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8879F, 0.4228F, 0.5F, 0.0F, 0.0F, -0.3927F));

        PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create(), PartPose.offset(-2.7294F, -3.6655F, -0.3503F));

        PartDefinition cube_r10 = right_ear.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(34, 37).addBox(0.5F, -3.0F, -2.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(34, 37).addBox(-2.5F, -3.0F, -2.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.8879F, 0.4228F, 0.5F, 0.0F, 0.0F, 0.3927F));

        PartDefinition hitbox = partdefinition.addOrReplaceChild("hitbox", CubeListBuilder.create().texOffs(47, 63).addBox(-7.0F, -15.0F, -15.25F, 14.0F, 15.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, 8.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(ElephantEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        if (entity.isSitting()) {
            if (Config.debugMode){
                System.out.println("[RENDER] Elephant is sitting");
            }
            entity.idleAnimationState.startIfStopped((int) ageInTicks);

            this.body.xRot = (float) Math.toRadians(-25);
            this.animate(entity.idleAnimationState, ElephantAnimations.ANIM_ELEPHANT_IDLE, ageInTicks, 1f);
        } else if (limbSwingAmount > 0.1f) {
            if (Config.debugMode) {
                System.out.println("[RENDER] Elephant is not sitting");
            }
            this.animateWalk(ElephantAnimations.ANIM_ELEPHANT_WALK, limbSwing, limbSwingAmount, 2.5f, 2f);
        }
    }

    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -30.0F, 30.0F);

        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return body;
    }
}
