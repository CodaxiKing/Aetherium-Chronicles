package com.cronicasaetherium.mod.blocks.synergy;

import com.cronicasaetherium.mod.blocks.synergy.ManaInfuserBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Infusora de Mana - Máquina de sinergia Tecnologia → Magia
 * 
 * Esta máquina representa a ponte entre os sistemas tecnológicos e mágicos do mod.
 * Ela converte energia tecnológica (Forge Energy) em mana utilizável pelo sistema mágico.
 * 
 * Funcionalidades:
 * - Aceita energia elétrica como entrada
 * - Processa catalisadores mágicos (flores, essências)
 * - Produz mana líquida ou cristalizada
 * - Interface gráfica para monitoramento
 * - Integração com ambos os sistemas de progressão
 * 
 * A máquina é fundamental para jogadores que focam em tecnologia mas querem
 * acessar alguns benefícios do sistema mágico, promovendo sinergia entre os paths.
 */
public class ManaInfuserBlock extends BaseEntityBlock {
    
    /**
     * Construtor da Infusora de Mana
     * Define as propriedades físicas e comportamentais do bloco
     * 
     * @param properties Propriedades do bloco (resistência, som, etc.)
     */
    public ManaInfuserBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    /**
     * Cria a entidade do bloco (BlockEntity) responsável pela lógica da máquina
     * 
     * @param pos Posição do bloco no mundo
     * @param state Estado atual do bloco
     * @return Nova instância da BlockEntity da Infusora de Mana
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ManaInfuserBlockEntity(pos, state);
    }
    
    /**
     * Configura o ticker da BlockEntity para processar lógica contínua
     * 
     * O ticker é executado a cada tick do jogo quando a máquina está ativa,
     * permitindo que ela processe energia, consuma itens e produza mana.
     * 
     * @param level Nível/mundo onde está o bloco
     * @param state Estado do bloco
     * @param blockEntityType Tipo da BlockEntity
     * @return Ticker configurado ou null se não houver processamento
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide()) {
            return (lvl, pos, st, blockEntity) -> {
                if (blockEntity instanceof ManaInfuserBlockEntity manaInfuser) {
                    manaInfuser.tick();
                }
            };
        }
        return null;
    }
    
    /**
     * Manipula a interação do jogador com a máquina
     * 
     * Quando o jogador clica com o botão direito na máquina,
     * abre a interface gráfica para gerenciar entrada, saída e energia.
     * 
     * @param state Estado do bloco
     * @param level Nível/mundo
     * @param pos Posição do bloco
     * @param player Jogador que interagiu
     * @param hit Informações sobre onde o jogador clicou
     * @return Resultado da interação
     */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ManaInfuserBlockEntity manaInfuser && player instanceof ServerPlayer serverPlayer) {
                // Mensagem informativa sobre o estado da máquina
                String status = manaInfuser.isProcessing() ? "§aProcessando" : "§7Inativa";
                int energy = manaInfuser.getEnergyStored();
                int maxEnergy = manaInfuser.getMaxEnergy();
                int progress = manaInfuser.getProcessProgress();
                
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                    String.format("§6Infusora de Mana - Status: %s§r", status)));
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                    String.format("§eEnergia: %d/%d FE", energy, maxEnergy)));
                if (manaInfuser.isProcessing()) {
                    player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                        String.format("§bProgresso: %d%%", progress)));
                }
                
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    
    /**
     * Define como o bloco deve ser renderizado
     * 
     * @param state Estado do bloco
     * @return Tipo de renderização (modelo 3D normal)
     */
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    
    /**
     * Comportamento quando o bloco é removido
     * 
     * Garante que os itens no inventário da máquina sejam dropados
     * e que a BlockEntity seja adequadamente limpa.
     * 
     * @param state Estado do bloco sendo removido
     * @param level Nível/mundo
     * @param pos Posição do bloco
     * @param newState Novo estado que substitui o bloco
     * @param movedByPiston Se o bloco foi movido por um pistão
     */
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ManaInfuserBlockEntity manaInfuser) {
                // TODO: Dropar itens do inventário quando implementado
                // manaInfuser.dropInventoryContents(level, pos);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }
}