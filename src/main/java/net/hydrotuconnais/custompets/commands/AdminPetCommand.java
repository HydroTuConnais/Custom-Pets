package net.hydrotuconnais.custompets.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.hydrotuconnais.custompets.entity.ModEntities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class AdminPetCommand {
    public static final Map<UUID, Set<String>> playerPetPermissions = new HashMap<>();
    public static final Set<String> availablePetTypes = new HashSet<>(ModEntities.getAllEntityTypes());
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_PET_TYPES = (context, builder) -> {
        return SharedSuggestionProvider.suggest(availablePetTypes, builder);
    };

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_PET_PERMISSION = (context, builder) -> {
        return SharedSuggestionProvider.suggest(
            playerPetPermissions.values().stream()
                .flatMap(Set::stream)
                .collect(java.util.stream.Collectors.toSet()),
            builder
        );
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("adminpet")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("list")
                        .executes(context -> {
                            if (availablePetTypes.isEmpty()) {
                                context.getSource().sendSuccess(() -> Component.literal(
                                        "Aucun pet n'est actuellement disponible."), false);
                            } else {
                                context.getSource().sendSuccess(() -> Component.literal(
                                        "Pets disponibles : " + String.join(", ", availablePetTypes)), false);
                            }
                            return 1;
                        }))
                .then(Commands.literal("permission")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.literal("add")
                                        .then(Commands.argument("petType", StringArgumentType.word())
                                                .suggests(SUGGEST_PET_TYPES)
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
                                                .suggests(SUGGEST_PET_PERMISSION)
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
                                            String permissionList = permissions.isEmpty() ?
                                                    " Aucune permission" :
                                                    "\n- " + String.join("\n- ", permissions);

                                            context.getSource().sendSuccess(() -> Component.literal(
                                                    "Permissions de " + targetPlayer.getName().getString() + " : " +
                                                            permissionList), false);
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
