package net.hydrotuconnais.custompets.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class AdminPetCommand {
    // Map pour stocker les permissions des joueurs
    private static final Map<UUID, Set<String>> playerPetPermissions = new HashMap<>();
    // Liste des pets disponibles
    private static final Set<String> availablePets = new HashSet<>(Arrays.asList("elephant")); // Ajoutez d'autres pets ici

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("adminpets")
                .requires(source -> source.hasPermission(2)) // Niveau 2 = Permission OP
                .then(Commands.literal("list")
                        .executes(context -> {
                            ServerPlayer admin = context.getSource().getPlayerOrException();
                            admin.sendSystemMessage(Component.literal("Pets disponibles : " + String.join(", ", availablePets)));
                            return 1;
                        }))
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("add")
                                .then(Commands.argument("petName", StringArgumentType.word())
                                        .executes(context -> {
                                            ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
                                            String petName = StringArgumentType.getString(context, "petName");

                                            if (!availablePets.contains(petName.toLowerCase())) {
                                                context.getSource().sendFailure(Component.literal("Pet inconnu : " + petName));
                                                return 0;
                                            }

                                            addPetPermission(targetPlayer.getUUID(), petName.toLowerCase());
                                            context.getSource().sendSuccess(
                                                    () -> Component.literal("Permission accordée à " + targetPlayer.getName().getString() +
                                                            " pour le pet : " + petName), true);
                                            return 1;
                                        })))
                        .then(Commands.literal("remove")
                                .then(Commands.argument("petName", StringArgumentType.word())
                                        .executes(context -> {
                                            ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
                                            String petName = StringArgumentType.getString(context, "petName");

                                            removePetPermission(targetPlayer.getUUID(), petName.toLowerCase());
                                            context.getSource().sendSuccess(
                                                    () -> Component.literal("Permission retirée à " + targetPlayer.getName().getString() +
                                                            " pour le pet : " + petName), true);
                                            return 1;
                                        })))
                        .then(Commands.literal("reset")
                                .executes(context -> {
                                    ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
                                    resetPlayerPermissions(targetPlayer.getUUID());
                                    context.getSource().sendSuccess(
                                            () -> Component.literal("Toutes les permissions de pets ont été retirées à " +
                                                    targetPlayer.getName().getString()), true);
                                    return 1;
                                }))
                ));
    }

    private static void addPetPermission(UUID playerUUID, String petName) {
        playerPetPermissions.computeIfAbsent(playerUUID, k -> new HashSet<>()).add(petName);
    }

    private static void removePetPermission(UUID playerUUID, String petName) {
        playerPetPermissions.computeIfPresent(playerUUID, (uuid, pets) -> {
            pets.remove(petName);
            return pets;
        });
    }

    private static void resetPlayerPermissions(UUID playerUUID) {
        playerPetPermissions.remove(playerUUID);
    }

    public static boolean hasPermissionForPet(UUID playerUUID, String petName) {
        return playerPetPermissions.containsKey(playerUUID) &&
                playerPetPermissions.get(playerUUID).contains(petName.toLowerCase());
    }
}
