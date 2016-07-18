package info.zthings.agt;

import javax.annotation.Nullable;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * 
 * 
 * @author HoldYourWaffle
 */
public class ItemAutomaticArrowGun extends Item {

	public ItemAutomaticArrowGun() {
		this.maxStackSize = 1;
        this.setMaxDamage(1024);
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.setUnlocalizedName("automaticarrowgun");
        this.setRegistryName("automaticarrowgun");
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		fire(stack, player.getEntityWorld(), player);
		super.onUsingTick(stack, player, count);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		boolean flag = this.findAmmo(playerIn) != null;
		
		ActionResult<ItemStack> ret = ForgeEventFactory.onArrowNock(itemStackIn, worldIn, playerIn, hand, flag);
		if (ret != null)
			return ret;

		if (!playerIn.capabilities.isCreativeMode && !flag) {
			return !flag ? new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn) : new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
		} else {
			playerIn.setActiveHand(hand);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		}
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based
	 * on material.
	 */
	@Override
	public int getItemEnchantability() {
		return 1;
	}
	
	public void fire(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		EntityPlayer entityplayer = (EntityPlayer) entityLiving;
		boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
		ItemStack itemstack = this.findAmmo(entityplayer);

		int i = 100;
		i = ForgeEventFactory.onArrowLoose(stack, worldIn, (EntityPlayer) entityLiving, i, itemstack != null || flag);
		if (i < 0)
			return;

		if (itemstack != null || flag) {
			if (itemstack == null) {
				itemstack = new ItemStack(Items.ARROW);
			}

			boolean flag1 = entityplayer.capabilities.isCreativeMode || (itemstack.getItem() instanceof ItemArrow ? ((ItemArrow) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer) : false);

			if (!worldIn.isRemote) {
				ItemArrow itemarrow = ((ItemArrow) (itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Items.ARROW));
				EntityArrow entityarrow = itemarrow.createArrow(worldIn, itemstack, entityplayer);
				entityarrow.setAim(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, 3F, 5.0F);
				entityplayer.renderYawOffset += .1F;

				int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

				if (j > 0) {
					entityarrow.setDamage(entityarrow.getDamage() + j * 0.5D + 0.5D);
				}

				int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

				if (k > 0) {
					entityarrow.setKnockbackStrength(k);
				}

				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
					entityarrow.setFire(100);
				}

				stack.damageItem(1, entityplayer);

				if (flag1) {
					entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
				}

				worldIn.spawnEntityInWorld(entityarrow);
			}

			worldIn.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + .5F);

			if (!flag1) {
				--itemstack.stackSize;

				if (itemstack.stackSize == 0) {
					entityplayer.inventory.deleteStack(itemstack);
				}
			}

			entityplayer.addStat(StatList.getObjectUseStats(this));
		}
	}
	
	private ItemStack findAmmo(EntityPlayer player) {
		if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND))) return player.getHeldItem(EnumHand.OFF_HAND);
		else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) return player.getHeldItem(EnumHand.MAIN_HAND);
		else for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
			ItemStack itemstack = player.inventory.getStackInSlot(i);

			if (this.isArrow(itemstack)) return itemstack;
		}
		return null;
	}
	protected boolean isArrow(@Nullable ItemStack stack){return stack != null && stack.getItem() instanceof ItemArrow;}
}
