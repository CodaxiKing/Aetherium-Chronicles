# Overview

Crônicas de Aetherium is a comprehensive Minecraft mod for version 1.21.1 built with NeoForge that implements three main progression systems: creatures/exploration, technology/automation, and magic/mysticism. The mod draws inspiration from OreSpawn's expansive nature while integrating modern and deep systems similar to Create, Applied Energistics 2, and Mekanism. It features a tiered progression system where players can choose to focus on technological automation with steam power and mechanical machines, mystical magic with soul harvesting and arcane rituals, or explore a hybrid approach that synergizes both paths.

# User Preferences

Preferred communication style: Simple, everyday language.

# System Architecture

## Core Framework
The mod is built on NeoForge 21.1.57 for Minecraft 1.21.1 using Java 21. The project follows a modular architecture with clear separation between technology, magic, and exploration systems. The package structure follows standard mod conventions with separate directories for common, client, and data generation code.

## Progression System Design
The mod implements a tiered progression system (Tier 1 and Tier 2) where each tier introduces new materials, machines, and capabilities. The architecture supports both independent progression paths (pure technology or pure magic) and hybrid approaches that require components from both systems. This creates meaningful player choice while encouraging experimentation with different playstyles.

## Energy and Resource Management
The technology path uses Forge Energy (FE) as its power system, with steam engines serving as the primary early-game power source. The magic path operates on a custom essence system where Spirit Essence serves as the magical "fuel" obtained through soul harvesting mechanics. A key architectural decision is the tech-magic bridge system, where certain advanced machines can convert between FE and magical essence.

## Block Entity Architecture
The mod implements sophisticated block entities with full capability support for energy, item handling, and fluid management. All machines feature custom GUIs with progress bars, upgrade slots, and configuration options. The block entity system supports automation through item ducts and energy conduits, with proper serialization for world persistence.

## World Generation Integration
The mod adds new ores (copper, tin, cobalt) with custom world generation features that spawn at appropriate depths and frequencies. The generation system is designed to encourage exploration while providing reasonable resource availability for progression.

## Registration and Data Systems
All mod content uses centralized registration systems (ModItems, ModBlocks, ModEntities, ModCreativeTabs) with proper deferred register implementations. The mod includes comprehensive recipe systems for custom machine types and supports both English and Portuguese localizations.

# External Dependencies

## Core Framework Dependencies
- **NeoForge 21.1.57**: Primary modding framework providing the base API and systems
- **Minecraft 1.21.1**: Target Minecraft version with specific mappings and compatibility requirements
- **Java 21**: Required runtime environment for modern language features and performance

## Build and Development Tools
- **Gradle 8.8**: Build automation and dependency management system
- **NeoForm**: Minecraft deobfuscation and mapping system for development environment
- **Mixins**: Code injection framework for advanced mod compatibility and feature implementation

## Optional Integration Targets
The mod is designed with extensibility in mind to potentially integrate with popular automation and magic mods in the ecosystem, though it functions as a standalone experience. The energy system uses standard Forge Energy to ensure compatibility with other tech mods, while the magic system is designed to be self-contained but extensible.

## Asset and Localization Systems
The mod includes comprehensive texture and model assets, with support for multiple languages through Minecraft's built-in localization system. All text is externalized to language files for easy translation and maintenance.