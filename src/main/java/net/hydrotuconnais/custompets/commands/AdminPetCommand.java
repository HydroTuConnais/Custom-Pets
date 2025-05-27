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
    private static final Map<UUID, Set<String>> playerPetPermissions = new HashMap<>();
    private static final Set<String> availablePets = new HashSet<>(Arrays.asList("elephant"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("adminpet")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("permission")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.literal("add")
                                        .then(Commands.argument("petType", StringArgumentType.word())
                                                .executes(context -> {
                                                    ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
                                                    String petType = StringArgumentType.getString(context, "petType");
                                                    addPetPermission(targetPlayer.getUUID(), petType);
                                                    context.getSource().sendSuccess(() -> Component.literal(
                                                            "Permission ajoutée pour " + targetPlayer.getName().getString() +
                                                                    " : " + petType), true);
                                                    return 1;
                                                })))
                                .then(Commands.literal("remove")
                                        .then(Commands.argument("petType", StringArgumentType.word())
                                                .executes(context -> {
                                                    ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
                                                    String petType = StringArgumentType.getString(context, "petType");
                                                    removePetPermission(targetPlayer.getUUID(), petType);
                                                    context.getSource().sendSuccess(() -> Component.literal(
                                                            "Permission retirée pour " + targetPlayer.getName().getString() +
                                                                    " : " + petType), true);
                                                    return 1;
                                                })))
                                .then(Commands.literal("list")
                                        .executes(context -> {
                                            ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
                                            Set<String> permissions = getPlayerPermissions(targetPlayer.getUUID());
                                            context.getSource().sendSuccess(() -> Component.literal(
                                                    "Permissions de " + targetPlayer.getName().getString() + " : " +
                                                            String.join(", ", permissions)), false);
                                            return 1;
                                        }))
                                .then(Commands.literal("clear")
                                        .executes(context -> {
                                            ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
                                            clearPermissions(targetPlayer.getUUID());
                                            context.getSource().sendSuccess(() -> Component.literal(
                                                    "Permissions effacées pour " + targetPlayer.getName().getString()), true);
                                            return 1;
                                        }))
                        )));
    }


    public static void addPetPermission(UUID playerUUID, String petType) {
        playerPetPermissions.computeIfAbsent(playerUUID, k -> new HashSet<>()).add(petType.toLowerCase());
    }

    public static void removePetPermission(UUID playerUUID, String petType) {
        playerPetPermissions.computeIfAbsent(playerUUID, k -> new HashSet<>()).remove(petType.toLowerCase());
    }

    public static boolean hasPermission(UUID playerUUID, String petType) {
        return playerPetPermissions.containsKey(playerUUID) &&
                playerPetPermissions.get(playerUUID).contains(petType.toLowerCase());
    }

    public static Set<String> getPlayerPermissions(UUID playerUUID) {
        return playerPetPermissions.getOrDefault(playerUUID, new HashSet<>());
    }

    public static void clearPermissions(UUID playerUUID) {
        playerPetPermissions.remove(playerUUID);
    }
}
