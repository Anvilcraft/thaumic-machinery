package net.anvilcraft.thaummach.items;

import java.util.List;

import net.anvilcraft.thaummach.TMTab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.IRepairable;

public class ItemFocus extends Item implements IRepairable {
    public int type = 0;

    public ItemFocus(int tp) {
        super();
        super.maxStackSize = 1;
        this.setMaxDamage(tp == 0 ? 2500 : 2000);
        this.setCreativeTab(TMTab.INSTANCE);
        this.setUnlocalizedName("thaummach:focus_" + tp);
        this.canRepair = true;
        this.type = tp;
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        this.itemIcon = reg.registerIcon("thaummach:focus_" + this.type);
    }

    // TODO: WTF
    //public float visRepairCost() {
    //    return 0.05F;
    //}

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }

    @Override
    public boolean hasEffect(ItemStack itemstack) {
        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void
    addInformation(ItemStack itemstack, EntityPlayer alec1, List list, boolean alec2) {
        switch (this.type) {
            case 0:
                list.add("Range: Medium - Focus: Narrow");
                list.add("Slightly improved durability");
                break;
            case 1:
                list.add("Range: Medium - Focus: Medium");
                list.add("Improved speed");
                break;
            case 2:
                list.add("Range: Long - Focus: Medium");
                break;
            case 3:
                list.add("Range: Medium - Focus: Wide");
                break;
            case 4:
                list.add("Range: Medium - Focus: Medium");
                list.add("Low value items are consumed");
                list.add("to improve efficiency");
        }
    }
}
