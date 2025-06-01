package net.hydrotuconnais.custompets.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.hydrotuconnais.custompets.Config;
import net.hydrotuconnais.custompets.CustomPets;
import net.hydrotuconnais.custompets.commands.AdminPetsCommand;
import net.hydrotuconnais.custompets.entity.ModEntities;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class PetsScreen extends Screen {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(CustomPets.MOD_ID, "textures/gui/pets_menu.png");
    private final int backgroundWidth = 256;
    private final int backgroundHeight = 256;
    private final int iconSize = 32;
    private final int caseSize = 42;
    private final int iconPadding = 16;
    private final int padding = 32;
    private final int iconMarginBottom = 8;

    // Scrolling variables
    private int scrollOffset = 0;
    private int maxScroll = 0;

    // Scrollbar variables
    private boolean draggingScrollbar = false;
    private int scrollbarDragStartY = 0;
    private int scrollbarStartOffset = 0;

    public PetsScreen() {
        super(Component.translatable("screen.custompets.pets_menu"));
    }

    private List<String> allEntities;

    @Override
    protected void init() {
        super.init();
        allEntities = ModEntities.getAllEntityTypes();

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        this.addRenderableWidget(Button.builder(
                Component.literal("Fermer"),
                b -> this.onClose()
        ).bounds(x + 20, y + backgroundHeight - 30, 80, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);

        int startX = (width - backgroundWidth) / 2;
        int startY = (height - backgroundHeight) / 2;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        graphics.blit(BACKGROUND, startX, startY, 0, 0, backgroundWidth, backgroundHeight);

        if (allEntities == null) return;

        int iconsPerRow = (backgroundWidth - 2 * padding) / (iconSize + iconPadding);
        int rowsVisible = (backgroundHeight - 60) / (iconSize + iconPadding + iconMarginBottom);
        int totalRows = (int) Math.ceil(allEntities.size() / (float) iconsPerRow);
        maxScroll = Math.max(0, totalRows - rowsVisible);

        if (maxScroll > 0) {
            int barHeight = Math.max(20, (rowsVisible * (backgroundHeight - 60)) / totalRows);
            int barY = ((backgroundHeight - 60 - barHeight) * scrollOffset) / maxScroll;
            int barX = (width - backgroundWidth) / 2 + backgroundWidth - 12;
            int barTop = (height - backgroundHeight) / 2 + 30;

            graphics.fill(barX, barTop, barX + 8, barTop + backgroundHeight - 60, 0xFF222222);
            graphics.fill(barX, barTop + barY, barX + 8, barTop + barY + barHeight, 0xFF8888FF);
        }

        int x0 = startX + padding;
        int y0 = startY + 30;

        for (int i = 0; i < allEntities.size(); i++) {
            String entity = allEntities.get(i);
            int row = i / iconsPerRow;
            if (row < scrollOffset || row >= scrollOffset + rowsVisible) continue;
            int col = i % iconsPerRow;

            int x = x0 + col * (iconSize + iconPadding);
            int y = y0 + (row - scrollOffset) * (iconSize + iconPadding + iconMarginBottom);

            boolean hasPerm = AdminPetsCommand.hasPermission(minecraft.player.getUUID(), entity);
            boolean isAdmin = minecraft.player.hasPermissions(Config.ADMIN_PERMISSION_LEVEL);
            boolean hovered = mouseX >= x && mouseX <= x + iconSize && mouseY >= y && mouseY <= y + iconSize;

            ResourceLocation texture = new ResourceLocation(CustomPets.MOD_ID, "textures/gui/" + entity + ".png");
            ResourceLocation caseTexture = new ResourceLocation(CustomPets.MOD_ID, "textures/gui/pets_menu_case2.png");

            float alpha;
            if (!hasPerm) {
                alpha = hovered ? 0.4f : 0.1f;
            } else {
                alpha = hovered ? 1.0f : 0.85f;
            }

            String entityName = entity.substring(0, 1).toUpperCase() + entity.substring(1).toLowerCase();

            RenderSystem.setShaderColor(1f, 1f, 1f, alpha);
            graphics.blit(caseTexture, x, y, 0, 0, caseSize, caseSize, caseSize, caseSize);
            graphics.blit(texture, x + 5, y + 5, 0, 0, iconSize, iconSize, iconSize, iconSize);

            graphics.drawString(font, entityName, x + iconSize / 2 - font.width(entityName) / 2 + 5, y + iconSize + 10, 0xFFFFFF);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

            if (hovered && hasPerm) {
                graphics.renderTooltip(font, Component.literal(hasPerm ? entityName : (isAdmin ? entityName + "(Admin)" : entityName)), mouseX, mouseY);
            } else if (!hasPerm && hovered) {
                graphics.renderTooltip(font, Component.translatable("screen.custompets.no_permission"), mouseX, mouseY);
            }
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // --- Début drag scrollbar ---
        int barX = (width - backgroundWidth) / 2 + backgroundWidth - 12;
        int barTop = (height - backgroundHeight) / 2 + 30;
        int iconsPerRow = (backgroundWidth - 2 * padding) / (iconSize + iconPadding);
        int rowsVisible = (backgroundHeight - 60) / (iconSize + iconPadding + iconMarginBottom);

        int x0 = (width - backgroundWidth) / 2 + padding;
        int y0 = (height - backgroundHeight) / 2 + 30;

        int barHeight = Math.max(20, (maxScroll == 0 ? 0 : ((backgroundHeight - 60) * (backgroundHeight - 60)) / ((maxScroll + 1) * (iconSize + iconPadding + iconMarginBottom))));
        int barY = (maxScroll == 0 ? 0 : ((backgroundHeight - 60 - barHeight) * scrollOffset) / maxScroll);

        if (maxScroll > 0 && mouseX >= barX && mouseX <= barX + 8 && mouseY >= barTop + barY && mouseY <= barTop + barY + barHeight) {
            draggingScrollbar = true;
            scrollbarDragStartY = (int) mouseY;
            scrollbarStartOffset = scrollOffset;
            return true;
        }

        for (int i = 0; i < allEntities.size(); i++) {
            String entity = allEntities.get(i);
            int row = i / iconsPerRow;
            if (row < scrollOffset || row >= scrollOffset + rowsVisible) continue;
            int col = i % iconsPerRow;
            int x = x0 + col * (iconSize + iconPadding);
            int y = y0 + (row - scrollOffset) * (iconSize + iconPadding + iconMarginBottom);

            boolean hasPerm = AdminPetsCommand.hasPermission(minecraft.player.getUUID(), entity);
            if (hasPerm && mouseX >= x && mouseX <= x + iconSize && mouseY >= y && mouseY <= y + iconSize) {
                minecraft.player.connection.sendCommand("pets spawn " + entity);
                this.onClose();
                return true;
            }
        }


        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingScrollbar = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingScrollbar && maxScroll > 0) {
            int barHeight = Math.max(20, (backgroundHeight - 60) * (backgroundHeight - 60) / ((maxScroll + 1) * (iconSize + iconPadding + iconMarginBottom)));
            int barTrack = backgroundHeight - 60 - barHeight;
            int barTop = (height - backgroundHeight) / 2 + 30;
            int deltaY = (int) mouseY - scrollbarDragStartY;
            int newOffset = scrollbarStartOffset + Math.round((float) deltaY * maxScroll / (float) barTrack);
            scrollOffset = Math.max(0, Math.min(maxScroll, newOffset));
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }


        @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (maxScroll > 0) {
            scrollOffset -= delta > 0 ? 1 : -1;
            scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
}
