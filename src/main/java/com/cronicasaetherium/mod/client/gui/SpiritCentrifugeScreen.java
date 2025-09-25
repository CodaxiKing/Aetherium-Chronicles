package com.cronicasaetherium.mod.client.gui;

import com.cronicasaetherium.mod.CronicasAetherium;
import com.cronicasaetherium.mod.common.gui.SpiritCentrifugeMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Tela da GUI da Centrífuga Espiritual - Interface Cliente
 * 
 * Esta classe renderiza a interface gráfica da Centrífuga Espiritual no cliente,
 * mostrando slots de entrada, progresso de processamento, consumo de energia FE,
 * e armazenamento de essências separadas.
 * 
 * Características visuais:
 * - Slot de entrada para Bolsas de Espírito 
 * - Barra de progresso animada
 * - Indicador de energia FE
 * - Slots de saída para tipos específicos de essência
 * - Partículas visuais durante o processamento
 * 
 * Exemplo de sinergia tech-magic: máquina tecnológica processando elementos mágicos
 */
@OnlyIn(Dist.CLIENT)
public class SpiritCentrifugeScreen extends AbstractContainerScreen<SpiritCentrifugeMenu> {
    
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        CronicasAetherium.MODID, "textures/gui/spirit_centrifuge.png");
    
    private static final int TEXTURE_WIDTH = 176;
    private static final int TEXTURE_HEIGHT = 166;
    
    // Coordenadas das barras de progresso
    private static final int PROGRESS_BAR_X = 79;
    private static final int PROGRESS_BAR_Y = 35;
    private static final int PROGRESS_BAR_WIDTH = 22;
    private static final int PROGRESS_BAR_HEIGHT = 16;
    
    // Coordenadas da barra de energia
    private static final int ENERGY_BAR_X = 157;
    private static final int ENERGY_BAR_Y = 13;
    private static final int ENERGY_BAR_WIDTH = 12;
    private static final int ENERGY_BAR_HEIGHT = 50;
    
    public SpiritCentrifugeScreen(SpiritCentrifugeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = TEXTURE_WIDTH;
        this.imageHeight = TEXTURE_HEIGHT;
        this.inventoryLabelY = this.imageHeight - 94;
    }
    
    @Override
    protected void init() {
        super.init();
        // Configurações adicionais da tela podem ser feitas aqui
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        
        // Renderiza a textura base da GUI
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        
        // Renderiza barra de progresso
        if (menu.isProcessing()) {
            int progressWidth = menu.getProgressWidth(PROGRESS_BAR_WIDTH);
            guiGraphics.blit(TEXTURE, 
                x + PROGRESS_BAR_X, y + PROGRESS_BAR_Y,
                176, 0, progressWidth, PROGRESS_BAR_HEIGHT);
        }
        
        // Renderiza barra de energia FE
        if (menu.getEnergyStored() > 0) {
            int energyHeight = menu.getEnergyHeight(ENERGY_BAR_HEIGHT);
            guiGraphics.blit(TEXTURE,
                x + ENERGY_BAR_X, y + ENERGY_BAR_Y + (ENERGY_BAR_HEIGHT - energyHeight),
                176, 16 + (ENERGY_BAR_HEIGHT - energyHeight), 
                ENERGY_BAR_WIDTH, energyHeight);
        }
        
        // Efeitos visuais durante processamento
        if (menu.isProcessing()) {
            renderProcessingEffects(guiGraphics, x, y, partialTick);
        }
    }
    
    /**
     * Renderiza efeitos visuais especiais durante o processamento
     * Adiciona partículas e animações para dar feedback visual ao jogador
     */
    private void renderProcessingEffects(GuiGraphics guiGraphics, int guiX, int guiY, float partialTick) {
        // TODO: Implementar partículas visuais
        // - Espirais de energia ao redor do slot de entrada
        // - Brilho pulsante nos slots de saída
        // - Efeitos de separação mágica
        
        // Por ora, apenas um brilho sutil
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.8F + 0.2F * (float)Math.sin(System.currentTimeMillis() * 0.01));
    }
    
    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        
        // Tooltip para barra de energia
        if (isHoveringEnergyBar(mouseX, mouseY)) {
            Component energyTooltip = Component.literal(
                String.format("Energia: %d / %d FE", 
                    menu.getEnergyStored(), 
                    menu.getMaxEnergyStored()));
            guiGraphics.renderTooltip(font, energyTooltip, mouseX, mouseY);
        }
        
        // Tooltip para barra de progresso
        if (isHoveringProgressBar(mouseX, mouseY)) {
            if (menu.isProcessing()) {
                Component progressTooltip = Component.literal(
                    String.format("Progresso: %d%%", menu.getProgressPercent()));
                guiGraphics.renderTooltip(font, progressTooltip, mouseX, mouseY);
            } else {
                guiGraphics.renderTooltip(font, 
                    Component.literal("Adicione uma Bolsa de Espírito para começar"), 
                    mouseX, mouseY);
            }
        }
    }
    
    private boolean isHoveringEnergyBar(int mouseX, int mouseY) {
        int guiX = (width - imageWidth) / 2;
        int guiY = (height - imageHeight) / 2;
        return mouseX >= guiX + ENERGY_BAR_X && mouseX < guiX + ENERGY_BAR_X + ENERGY_BAR_WIDTH &&
               mouseY >= guiY + ENERGY_BAR_Y && mouseY < guiY + ENERGY_BAR_Y + ENERGY_BAR_HEIGHT;
    }
    
    private boolean isHoveringProgressBar(int mouseX, int mouseY) {
        int guiX = (width - imageWidth) / 2;
        int guiY = (height - imageHeight) / 2;
        return mouseX >= guiX + PROGRESS_BAR_X && mouseX < guiX + PROGRESS_BAR_X + PROGRESS_BAR_WIDTH &&
               mouseY >= guiY + PROGRESS_BAR_Y && mouseY < guiY + PROGRESS_BAR_Y + PROGRESS_BAR_HEIGHT;
    }
}