package net.hydrotuconnais.custompets.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.hydrotuconnais.custompets.Config;
import net.hydrotuconnais.custompets.entity.ModEntities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.chat.Component;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PetCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("pets")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    Set<String> permissions = AdminPetCommand.getPlayerPermissions(player.getUUID());
                    if (permissions.isEmpty()) {
                        player.sendSystemMessage(Component.literal("§cVous n'avez accès à aucun pet."));
                    } else {
                        player.sendSystemMessage(Component.literal("§aPets disponibles: §f" + String.join(", ", permissions)));
                    }
                    return 1;
                })
                .then(Commands.literal("kill")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            ServerLevel level = context.getSource().getLevel();
                            killPlayerPets(level, player.getUUID());
                            player.sendSystemMessage(Component.literal("§aTous vos pets ont été supprimés."));
                            return 1;
                        })
                )
                .then(Commands.argument("name", StringArgumentType.word())
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            ServerLevel level = context.getSource().getLevel();
                            String name = StringArgumentType.getString(context, "name").toLowerCase();

                            if (!AdminPetCommand.hasPermission(player.getUUID(), name)) {
                                player.sendSystemMessage(Component.literal("§cVous n'avez pas la permission d'invoquer ce pet !"));
                                return 0;
                            }

                            killPlayerPets(level, player.getUUID());

                            if (name.equals("elephant")) {
                                Animal pet = ModEntities.ELEPHANT.get().create(level);
                                if (pet != null) {
                                    pet.moveTo(player.getX(), player.getY(), player.getZ(), 0, 0);

                                    if (pet instanceof net.minecraft.world.entity.TamableAnimal tamable) {
                                        tamable.setOwnerUUID(player.getUUID());
                                    }

                                    level.addFreshEntity(pet);
                                    player.sendSystemMessage(Component.literal("§aÉléphant invoqué !"));
                                    return 1;
                                }
                            }
                            player.sendSystemMessage(Component.literal("§cPet non reconnu ou indisponible."));
                            return 0;
                        })
                )
        );
    }

    private static void killPlayerPets(ServerLevel level, UUID playerUUID) {
        for (Entity entity : level.getEntities().getAll()) {
            if (entity instanceof TamableAnimal tameable && playerUUID.equals(tameable.getOwnerUUID())) {
                if (entity instanceof LivingEntity living) {
                    living.hurt(level.damageSources().outOfBorder(), Float.MAX_VALUE);
                } else {
                    entity.discard();
                }
                if (Config.debugMode) {
                    System.out.println("[DEBUG] Pet killed: " + entity.getName().getString());
                }
            }
        }
    }
}
