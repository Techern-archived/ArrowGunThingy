package info.zthings.agt.proxy;

import net.minecraft.item.Item;

public abstract class CommonProxy {

    /**
     * Registers an {@link Item} in the {@link net.minecraft.client.renderer.ItemModelMesher}
     *
     * @param item The {@link Item} being registered
     * @param metadata The metadata of said item
     * @param itemName The item's name
     */
    public abstract void registerItemModelMesher(Item item, int metadata);

}
