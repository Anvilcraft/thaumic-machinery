package net.anvilcraft.thaummach.items;

import java.util.List;
import java.util.stream.IntStream;

import dev.tilera.auracore.api.machine.IUpgradable;
import net.anvilcraft.thaummach.TMTab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemUpgrade extends Item {
    public IIcon[] icons;

    public ItemUpgrade() {
        super();
        this.maxStackSize = 16;
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(TMTab.INSTANCE);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        this.icons = IntStream.rangeClosed(0, 6)
                         .mapToObj((i) -> reg.registerIcon("thaummach:upgrade_" + i))
                         .toArray(IIcon[] ::new);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.uncommon;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item.thaummach:upgrade_" + stack.getItemDamage();
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void
    addInformation(ItemStack stack, EntityPlayer alec1, List list, boolean alec2) {
        switch (stack.getItemDamage()) {
            case 0:
                list.add("Improves speed");
                break;
            case 1:
                list.add("Improves efficiency");
                break;
            case 2:
                list.add("Increases aggression and damage");
                break;
            case 3:
                list.add("Unlocks functions that involve taint");
                break;
            case 4:
                list.add("Upgrades?");
                break;
            case 5:
                list.add("Increases capacity");
                break;
            case 6:
                list.add("Increases mystical capacity or knowledge");
        }
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.icons[meta];
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return this.getIconFromDamage(stack.getItemDamage());
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List list) {
        IntStream.rangeClosed(0, 6).forEach((i) -> list.add(new ItemStack(this, 1, i)));
    }

    @Override
    public boolean onItemUseFirst(
        ItemStack ist,
        EntityPlayer player,
        World world,
        int x,
        int y,
        int z,
        int l,
        // useless parameters
        float alec1,
        float alec2,
        float alec3
    ) {
        TileEntity ent = world.getTileEntity(x, y, z);
        if (ent != null && ent instanceof IUpgradable) {
            IUpgradable ue = (IUpgradable) ent;

            for (int a = 0; a < ue.getUpgradeLimit(); ++a) {
                if (ue.getUpgrades()[a] < 0
                    && ue.canAcceptUpgrade((byte) ist.getItemDamage())) {
                    if (!world.isRemote && ue.setUpgrade((byte) ist.getItemDamage())) {
                        world.markBlockForUpdate(x, y, z);
                        ist.stackSize--;
                        world.playSoundEffect(
                            (double) x + 0.5,
                            (double) y + 0.5,
                            (double) z + 0.5,
                            "thaummach:upgrade",
                            0.4F,
                            1.0F
                        );
                        return true;
                    }
                }
            }

            return false;
        }
        return super.onItemUseFirst(ist, player, world, x, y, z, l, alec1, alec2, alec3);
    }

    // TODO: WTF
    //@Override
    //public void useItemOnEntity(ItemStack ist, EntityLiving ent) {
    //    if (ent != null && ent instanceof IUpgradable) {
    //        IUpgradable ue = (IUpgradable) ent;

    //        for (int a = 0; a < ue.getUpgradeLimit(); ++a) {
    //            if (ue.getUpgrades()[a] < 0
    //                && ue.canAcceptUpgrade((byte) ist.getItemDamage())
    //                && ue.setUpgrade((byte) ist.getItemDamage())) {
    //                --ist.stackSize;
    //                ModLoader.getMinecraftInstance().theWorld.playSoundAtEntity(
    //                    ent, "thaumcraft.upgrade", 0.4F, 1.0F
    //                );
    //                return;
    //            }
    //        }
    //    }

    //    super.useItemOnEntity(ist, ent);
    //}
}
