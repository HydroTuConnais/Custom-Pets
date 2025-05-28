package net.hydrotuconnais.custompets.entity.custom;

import net.hydrotuconnais.custompets.Config;
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

    private static final EntityDataAccessor<Integer> IDLE =
            SynchedEntityData.defineId(ElephantEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> INVINCIBLE =
            SynchedEntityData.defineId(ElephantEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public ElephantEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 8.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FollowOwnerGoal(this, 1.0, 10.0f, 2.0f, false));

        this.goalSelector.addGoal(2, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Animal.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        switch (hand) {
            case MAIN_HAND:
                if (this.isOwnedBy(player) && !player.level().isClientSide) {
                    this.setSitting(!this.isSitting());
                    return InteractionResult.SUCCESS;
                }
                break;
            default:
                return InteractionResult.PASS;
        }
        return super.mobInteract(player, hand);
    }

    /*================ Breeding Methods =================*/
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.ELEPHANT.get().create(pLevel);
    }

    /*================= Invicible Methods =================*/

    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) {
        if (this.isInvincible()) {
            return false;
        }
        return super.hurt(source, amount);
    }

    public void setInvincible(boolean invincible) {
        this.entityData.set(INVINCIBLE, invincible);
    }

    public boolean isInvincible() {
        return this.entityData.get(INVINCIBLE);
    }

    /*================= Animation Methods =================*/
    private void setupAnimationStates() {
        int animTick = this.entityData.get(IDLE);
        if (animTick > 0 && !this.idleAnimationState.isStarted()) {
            this.idleAnimationState.start(animTick);
        }
    }

    /*================== Sitting Methods =================*/
    public boolean isSitting() { return entityData.get(SITTING); }
    public void setSitting(boolean sit) {
        if (Config.debugMode){
            System.out.println("[DEBUG] setSitting = " + sit + " | side = " + (level().isClientSide ? "CLIENT" : "SERVER"));
        }
        entityData.set(SITTING, sit);
    }

    /*================= Synced Data =================*/
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IDLE, 0);
        this.entityData.define(SITTING, false);
        this.entityData.define(INVINCIBLE, false);
    }

    /*================= Owner Methods =================*/

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Invincible", this.isInvincible());
        tag.putBoolean("Sitting", this.isSitting());
        if (this.getOwnerUUID() != null) {
            tag.putUUID("OwnerUUID", this.getOwnerUUID());
        }
    }
    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setInvincible(tag.getBoolean("Invincible"));
        this.setSitting(tag.getBoolean("Sitting"));
        if (tag.hasUUID("OwnerUUID")) {
            this.setOwnerUUID(tag.getUUID("OwnerUUID"));
        }
    }

    /*================= Utils Methods =================*/
    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()) {
            if (Config.debugMode) {
                System.out.println("[CLIENT] Elephant " + this.getId() + " sitting=" + this.isSitting());
            }
            this.setupAnimationStates();
        } else {
            if (Config.debugMode) {
                System.out.println("[SERVER] Elephant " + this.getId() + " sitting=" + this.isSitting());
            }
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
