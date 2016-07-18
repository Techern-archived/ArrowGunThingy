package info.zthings.agt;

import info.zthings.agt.proxy.ClientProxy;
import info.zthings.agt.proxy.CommonProxy;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = AgtMod.MODID, version = AgtMod.VERSION)
public class AgtMod {
    public static final String MODID = "agt";
    public static final String VERSION = "1.0";
    
    /**
     * A {@link SidedProxy} of either {@link CommonProxy} or {@link ClientProxy}
     *
     * @since 0.0.1
     */
    @SidedProxy(clientSide = "info.zthings.agt.proxy.ClientProxy",
                serverSide = "info.zthings.agt.proxy.CommonProxy")
    public static CommonProxy PROXY;
    
    public static final Item BAG = new ItemBasicArrowGun();
    public static final Item AAG = new ItemAutomaticArrowGun();
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	MinecraftForge.EVENT_BUS.register(new AgtEventHandler());
    	
        GameRegistry.register(BAG);
        PROXY.registerItemModelMesher(BAG, 0, "basicarrowgun", "inventory");
        GameRegistry.register(AAG);
        PROXY.registerItemModelMesher(AAG, 0, "automaticarrowgun", "inventory");
    }
}
