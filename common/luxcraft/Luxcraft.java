package luxcraft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import luxcraft.block.ModBlocks;
import luxcraft.gui.GuiHandler;
import luxcraft.lib.Reference;
import luxcraft.tileentity.TileLightFurnace;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)

@NetworkMod(clientSideRequired=true, serverSideRequired=false)

public class Luxcraft {
    
    @Instance(Reference.MOD_ID)
    public static Luxcraft instance;
    
    private GuiHandler guiHandler = new GuiHandler();
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        
        ModBlocks.init();
        
        NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
      
        GameRegistry.registerTileEntity(TileLightFurnace.class, "lightfurnace");
        
    }
    
    @Init
    public void load(FMLInitializationEvent event) {
        
        
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
        
    }

}
