package luxcraft.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;

public class ModBlocks {
    
    public static Block lightFurnace;
    
    public static void init() {
        
        lightFurnace = new BlockLightFurnace(500, Material.rock);
        
        GameRegistry.registerBlock(lightFurnace, "lightFurnace");
        
        MinecraftForge.setBlockHarvestLevel(lightFurnace, "pickaxe", 1);
        
        LanguageRegistry.addName(lightFurnace, "Light Furnace");
        
    }
}
