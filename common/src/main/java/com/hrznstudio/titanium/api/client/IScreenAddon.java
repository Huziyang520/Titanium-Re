// Titanium Lite I Screen Addon

package com.hrznstudio.titanium.api.client;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;

public interface IScreenAddon extends GuiEventListener {

    void drawBackgroundLayer(GuiGraphicsExtractor guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks);

    void drawForegroundLayer(GuiGraphicsExtractor guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks);

    default List<Component> getTooltipLines() {
        return Collections.emptyList();
    }

    default void init(int screenX, int screenY) {}

    default boolean isBackground() {
        return false;
    }

    @Override
    default void mouseMoved(double xPos, double yPos) {}

    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    default boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return mouseClicked(event.x(), event.y(), event.button());
    }

    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    default boolean mouseReleased(MouseButtonEvent event) {
        return mouseReleased(event.x(), event.y(), event.button());
    }

    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    @Override
    default boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        return mouseDragged(event.x(), event.y(), event.button(), dragX, dragY);
    }

    @Override
    default boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return false;
    }

    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    default boolean keyPressed(KeyEvent event) {
        return keyPressed(event.key(), event.scancode(), event.modifiers());
    }

    default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    default boolean keyReleased(KeyEvent event) {
        return keyReleased(event.key(), event.scancode(), event.modifiers());
    }

    default boolean charTyped(char codePoint, int modifiers) {
        return false;
    }

    @Override
    default boolean charTyped(CharacterEvent event) {
        return event.codepoint() <= Character.MAX_VALUE && charTyped((char) event.codepoint(), 0);
    }

    @Override
    boolean isFocused();

    @Override
    void setFocused(boolean focus);

    @Override
    default boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }
}
