package me.hasenzahn1.glass_fabric_port.blocks;

import me.hasenzahn1.glass_fabric_port.GLASSMod;
import me.hasenzahn1.glass_fabric_port.blocks.custom.GlassExtension;
import me.hasenzahn1.glass_fabric_port.blocks.custom.GlassProjector;
import me.hasenzahn1.glass_fabric_port.blocks.custom.GlassTerminal;
import me.hasenzahn1.glass_fabric_port.blocks.custom.GlassWirelessExtension;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block GLASS_PROJECTOR = registerBlock("glass_projector",
            new GlassProjector(FabricBlockSettings.of(Material.GLASS).nonOpaque().strength(4f).requiresTool()));
    public static final Block GLASS_TERMINAL = registerBlock("glass_terminal",
            new GlassTerminal(FabricBlockSettings.of(Material.GLASS).nonOpaque().strength(4f).requiresTool()));
    public static final Block GLASS_EXTENSION = registerBlock("glass_extension",
            new GlassExtension(FabricBlockSettings.of(Material.GLASS).nonOpaque().strength(4f).requiresTool()));
    public static final Block GLASS_WIRELESS_EXTENSION = registerBlock("glass_wireless_extension",
            new GlassWirelessExtension(FabricBlockSettings.of(Material.GLASS).nonOpaque().strength(4f).requiresTool()));


    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registry.BLOCK, new Identifier(GLASSMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registry.ITEM, new Identifier(GLASSMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(ItemGroup.REDSTONE)));
    }

    public static void registerModBlocks() {
        System.out.println("Registering ModBlocks for " + GLASSMod.MOD_ID);
    }

}
