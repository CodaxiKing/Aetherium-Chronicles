package com.cronicasaetherium.mod.common.capability;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Implementação customizada de inventário para as máquinas do mod
 * 
 * Esta classe extends ItemStackHandler do NeoForge para fornecer funcionalidades
 * específicas das nossas máquinas, incluindo validação de slots e callbacks.
 * 
 * Funcionalidades:
 * - Validação automática de slots (entrada, saída, combustível)
 * - Callbacks quando itens são modificados
 * - Proteção de slots de saída
 * - Configuração flexível por tipo de máquina
 */
public class ModItemHandler extends ItemStackHandler {
    
    private final Runnable onContentsChanged;
    private final SlotValidator slotValidator;
    
    /**
     * Construtor básico
     * 
     * @param size Número de slots
     * @param onContentsChanged Callback executado quando inventário muda
     */
    public ModItemHandler(int size, Runnable onContentsChanged) {
        this(size, onContentsChanged, null);
    }
    
    /**
     * Construtor com validação de slots
     * 
     * @param size Número de slots
     * @param onContentsChanged Callback executado quando inventário muda
     * @param slotValidator Validador de slots customizado
     */
    public ModItemHandler(int size, Runnable onContentsChanged, SlotValidator slotValidator) {
        super(size);
        this.onContentsChanged = onContentsChanged;
        this.slotValidator = slotValidator;
    }
    
    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        if (onContentsChanged != null) {
            onContentsChanged.run();
        }
    }
    
    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slotValidator != null) {
            return slotValidator.isItemValid(slot, stack);
        }
        return super.isItemValid(slot, stack);
    }
    
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slotValidator != null && !slotValidator.canInsert(slot, stack)) {
            return stack; // Bloqueia inserção em slots protegidos
        }
        return super.insertItem(slot, stack, simulate);
    }
    
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slotValidator != null && !slotValidator.canExtract(slot)) {
            return ItemStack.EMPTY; // Bloqueia extração de slots protegidos
        }
        return super.extractItem(slot, amount, simulate);
    }
    
    /**
     * Cria um inventário padrão para máquinas de processamento
     * Slot 0: Entrada, Slot 1: Saída principal, Slot 2: Subproduto
     * 
     * @param onContentsChanged Callback de mudança
     * @param inputValidator Validador para slot de entrada
     * @return Inventário configurado
     */
    public static ModItemHandler createProcessingInventory(Runnable onContentsChanged, 
                                                         ItemValidator inputValidator) {
        return new ModItemHandler(3, onContentsChanged, new SlotValidator() {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == 0) { // Slot de entrada
                    return inputValidator.isValid(stack);
                }
                return false; // Slots de saída não aceitam inserção manual
            }
            
            @Override
            public boolean canInsert(int slot, ItemStack stack) {
                return slot == 0; // Apenas slot de entrada aceita inserção
            }
            
            @Override
            public boolean canExtract(int slot) {
                return slot != 0; // Apenas slots de saída permitem extração automática
            }
        });
    }
    
    /**
     * Cria um inventário para geradores (combustível + resultado)
     * Slot 0: Combustível, Slot 1: Auxiliar (água, etc.)
     * 
     * @param onContentsChanged Callback de mudança
     * @param fuelValidator Validador para combustível
     * @param auxiliaryValidator Validador para slot auxiliar
     * @return Inventário configurado
     */
    public static ModItemHandler createGeneratorInventory(Runnable onContentsChanged,
                                                        ItemValidator fuelValidator,
                                                        ItemValidator auxiliaryValidator) {
        return new ModItemHandler(2, onContentsChanged, new SlotValidator() {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == 0) {
                    return fuelValidator.isValid(stack);
                } else if (slot == 1) {
                    return auxiliaryValidator != null ? auxiliaryValidator.isValid(stack) : true;
                }
                return false;
            }
            
            @Override
            public boolean canInsert(int slot, ItemStack stack) {
                return isItemValid(slot, stack); // Permite inserção se item é válido
            }
            
            @Override
            public boolean canExtract(int slot) {
                return slot == 1; // Permite extração apenas do slot auxiliar (buckets vazios)
            }
        });
    }
    
    /**
     * Cria um inventário genérico com slots de entrada e saída
     * 
     * @param inputSlots Número de slots de entrada
     * @param outputSlots Número de slots de saída
     * @param onContentsChanged Callback de mudança
     * @param inputValidator Validador para slots de entrada
     * @return Inventário configurado
     */
    public static ModItemHandler createGenericInventory(int inputSlots, int outputSlots,
                                                      Runnable onContentsChanged,
                                                      ItemValidator inputValidator) {
        int totalSlots = inputSlots + outputSlots;
        return new ModItemHandler(totalSlots, onContentsChanged, new SlotValidator() {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot < inputSlots) { // Slots de entrada
                    return inputValidator.isValid(stack);
                }
                return false; // Slots de saída não aceitam inserção manual
            }
            
            @Override
            public boolean canInsert(int slot, ItemStack stack) {
                return slot < inputSlots && isItemValid(slot, stack);
            }
            
            @Override
            public boolean canExtract(int slot) {
                return slot >= inputSlots; // Permite extração apenas dos slots de saída
            }
        });
    }
    
    /**
     * Interface para validação de slots
     */
    public interface SlotValidator {
        boolean isItemValid(int slot, ItemStack stack);
        boolean canInsert(int slot, ItemStack stack);
        boolean canExtract(int slot);
    }
    
    /**
     * Interface para validação de itens
     */
    public interface ItemValidator {
        boolean isValid(ItemStack stack);
    }
}