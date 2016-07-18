package info.zthings.agt;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 
 * 
 * @author HoldYourWaffle
 */
public class AgtEventHandler {
	@SubscribeEvent
	public void use(PlayerInteractEvent.RightClickItem ev) {
		System.out.println("d");
        if(ev.getEntityPlayer().getHeldItem(ev.getHand()).getItem() == AgtMod.AAG) {
        	((ItemAutomaticArrowGun) ev.getEntityPlayer().getHeldItem(ev.getHand()).getItem()).fire(ev.getEntityPlayer().getHeldItem(ev.getHand()), ev.getWorld(), ev.getEntityLiving());
        }
	}
}
