package com.cronicasaetherium.mod.common.capability;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

/**
 * Provedor de capabilities genérico para BlockEntities do mod
 * 
 * Esta classe simplifica a exposição de capabilities (energia e itens)
 * para as BlockEntities, permitindo que outros mods e sistemas interajam
 * com nossas máquinas de forma padronizada.
 * 
 * Capabilities suportadas:
 * - Forge Energy (IEnergyStorage) - para sistemas de energia
 * - Item Handler (IItemHandler) - para sistemas de automação de itens
 * 
 * A classe permite configurar diferentes capabilities para diferentes lados
 * da máquina, possibilitando controle fino sobre onde energia/itens podem
 * ser inseridos ou extraídos.
 */
public class CapabilityProvider implements ICapabilityProvider<Object, @Nullable Direction> {
    
    private final IEnergyStorage energyStorage;
    private final IItemHandler itemHandler;
    private final DirectionConfig directionConfig;
    
    /**
     * Construtor para máquinas com energia e itens
     * 
     * @param energyStorage Sistema de energia da máquina
     * @param itemHandler Sistema de itens da máquina
     * @param directionConfig Configuração de quais lados permitem o quê
     */
    public CapabilityProvider(IEnergyStorage energyStorage, IItemHandler itemHandler, DirectionConfig directionConfig) {
        this.energyStorage = energyStorage;
        this.itemHandler = itemHandler;
        this.directionConfig = directionConfig != null ? directionConfig : DirectionConfig.ALL_SIDES;
    }
    
    /**
     * Construtor apenas para energia
     */
    public CapabilityProvider(IEnergyStorage energyStorage, DirectionConfig directionConfig) {
        this(energyStorage, null, directionConfig);
    }
    
    /**
     * Construtor apenas para itens
     */
    public CapabilityProvider(IItemHandler itemHandler, DirectionConfig directionConfig) {
        this(null, itemHandler, directionConfig);
    }
    
    @Override
    public @Nullable Object getCapability(Object capability, @Nullable Direction direction) {
        // Capability de energia
        if (capability == Capabilities.EnergyStorage.BLOCK) {
            if (energyStorage != null && directionConfig.allowsEnergy(direction)) {
                return energyStorage;
            }
        }
        
        // Capability de itens
        if (capability == Capabilities.ItemHandler.BLOCK) {
            if (itemHandler != null && directionConfig.allowsItems(direction)) {
                return itemHandler;
            }
        }
        
        return null;
    }
    
    /**
     * Configuração de quais lados da máquina permitem quais capabilities
     */
    public static class DirectionConfig {
        
        // Configurações pré-definidas comuns
        public static final DirectionConfig ALL_SIDES = new DirectionConfig(true, true);
        public static final DirectionConfig NO_ENERGY = new DirectionConfig(false, true);
        public static final DirectionConfig NO_ITEMS = new DirectionConfig(true, false);
        public static final DirectionConfig TOP_BOTTOM_ENERGY = new DirectionConfig() {
            @Override
            public boolean allowsEnergy(@Nullable Direction direction) {
                return direction == Direction.UP || direction == Direction.DOWN;
            }
            
            @Override
            public boolean allowsItems(@Nullable Direction direction) {
                return direction != Direction.UP && direction != Direction.DOWN;
            }
        };
        
        private final boolean defaultEnergy;
        private final boolean defaultItems;
        
        public DirectionConfig(boolean defaultEnergy, boolean defaultItems) {
            this.defaultEnergy = defaultEnergy;
            this.defaultItems = defaultItems;
        }
        
        public DirectionConfig() {
            this(true, true);
        }
        
        /**
         * Verifica se um lado permite energia
         * 
         * @param direction Direção (null = interno)
         * @return true se permite
         */
        public boolean allowsEnergy(@Nullable Direction direction) {
            return defaultEnergy;
        }
        
        /**
         * Verifica se um lado permite itens
         * 
         * @param direction Direção (null = interno)
         * @return true se permite
         */
        public boolean allowsItems(@Nullable Direction direction) {
            return defaultItems;
        }
        
        /**
         * Cria configuração onde energia vem de lados específicos
         */
        public static DirectionConfig energyFrom(Direction... directions) {
            return new DirectionConfig() {
                @Override
                public boolean allowsEnergy(@Nullable Direction direction) {
                    if (direction == null) return true; // Sempre permite interno
                    for (Direction d : directions) {
                        if (d == direction) return true;
                    }
                    return false;
                }
                
                @Override
                public boolean allowsItems(@Nullable Direction direction) {
                    return true; // Itens em todos os lados por padrão
                }
            };
        }
        
        /**
         * Cria configuração onde itens vêm de lados específicos
         */
        public static DirectionConfig itemsFrom(Direction... directions) {
            return new DirectionConfig() {
                @Override
                public boolean allowsEnergy(@Nullable Direction direction) {
                    return true; // Energia em todos os lados por padrão
                }
                
                @Override
                public boolean allowsItems(@Nullable Direction direction) {
                    if (direction == null) return true; // Sempre permite interno
                    for (Direction d : directions) {
                        if (d == direction) return true;
                    }
                    return false;
                }
            };
        }
        
        /**
         * Configuração complexa para máquinas de processamento
         * - Energia: apenas laterais e traseira
         * - Itens entrada: apenas topo
         * - Itens saída: apenas base
         */
        public static DirectionConfig processing() {
            return new DirectionConfig() {
                @Override
                public boolean allowsEnergy(@Nullable Direction direction) {
                    if (direction == null) return true;
                    return direction != Direction.UP && direction != Direction.DOWN;
                }
                
                @Override
                public boolean allowsItems(@Nullable Direction direction) {
                    return direction != null; // Itens em qualquer lado externo
                }
            };
        }
        
        /**
         * Configuração para geradores
         * - Energia: saída em todos os lados
         * - Itens: entrada apenas por cima, saída por baixo
         */
        public static DirectionConfig generator() {
            return new DirectionConfig() {
                @Override
                public boolean allowsEnergy(@Nullable Direction direction) {
                    return true; // Energia em todos os lados
                }
                
                @Override
                public boolean allowsItems(@Nullable Direction direction) {
                    return direction != null; // Itens em qualquer lado externo
                }
            };
        }
    }
}