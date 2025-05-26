package net.hydrotuconnais.custompets.entity.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.hydrotuconnais.custompets.entity.ModEntities;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.Optional;

public class ElephantEntity extends TamableAnimal {
    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(ElephantEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> ANIMATION_TICK =
            SynchedEntityData.defineId(ElephantEntity.class, EntityDataSerializers.INT);

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public ElephantEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));

        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Animal.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (this.isOwnedBy(player)) {
                this.setSitting(!this.isSitting());
                return InteractionResult.SUCCESS;
            } else {
                player.sendSystemMessage(Component.translatable("entity.custompets.notowner"));
                return InteractionResult.FAIL;
            }
        }
        return super.mobInteract(player, hand);
    }

    /*================ Breeding Methods =================*/
    /*
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.ELEPHANT.get().create(pLevel);
    }
    */

    /*================= Animation Methods =================*/
    public void startIdleAnimation() {
        this.entityData.set(ANIMATION_TICK, this.tickCount);
    }

    private void setupAnimationStates() {
        int animTick = this.entityData.get(ANIMATION_TICK);
        if (animTick > 0 && !this.idleAnimationState.isStarted()) {
            this.idleAnimationState.start(animTick);
        }
    }

    /*================== Sitting Methods =================*/
    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        if (sitting) {
            this.startIdleAnimation();
        }
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    /*================= Synced Data =================*/
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION_TICK, 0);
        this.entityData.define(SITTING, false);
    }

    /*================= Utils Methods =================*/
    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isSitting() && this.onGround()) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            return;
        }
        super.travel(travelVector);
    }
}
