package net.anvilcraft.thaummach.items;

import java.util.List;

import net.anvilcraft.thaummach.TMBlocks;
import net.anvilcraft.thaummach.TMTab;
import net.anvilcraft.thaummach.tiles.TileSeal;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemRunicEssence extends Item {
    private IIcon[] icons = new IIcon[6];

    public ItemRunicEssence() {
        super();
        super.maxStackSize = 16;
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(TMTab.INSTANCE);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        for (int i = 0; i < 6; i++) {
            this.icons[i] = reg.registerIcon("thaummach:runic_essence_" + i);
        }
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List l) {
        for (int i = 0; i < 6; i++) {
            l.add(new ItemStack(this, 1, i));
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
    public String getUnlocalizedName(ItemStack is) {
        return "thaummach:runic_essence." + is.getItemDamage();
    }

    @Override
    public boolean onItemUseFirst(
        ItemStack itemstack,
        EntityPlayer player,
        World world,
        int i,
        int j,
        int k,
        int l,
        // useless parameters
        float alec1,
        float alec2,
        float alec3
    ) {
        Block bi = world.getBlock(i, j, k);
        if (itemstack.stackSize == 0) {
            return false;
        } else {
            if (bi == TMBlocks.seal) {
                TileSeal ts = (TileSeal) world.getTileEntity(i, j, k);
                if (ts != null) {
                    boolean added = false;
                    int addPitch = 0;

                    for (int q = 0; q < 3; ++q) {
                        if (ts.runes[q] == -1) {
                            ts.runes[q] = (byte) itemstack.getItemDamage();
                            added = true;
                            addPitch = ts.runes[q];
                            ts.delay = 60;

                            break;
                        }
                    }

                    if (added) {
                        world.playSoundEffect(
                            (double) ((float) i + 0.5F),
                            (double) ((float) j + 0.5F),
                            (double) ((float) k + 0.5F),
                            "thaumcraft:rune_set",
                            0.5F,
                            1.2F - (float) addPitch * 0.075F
                        );
                        --itemstack.stackSize;
                        return true;
                    }
                }
            }

            return super.onItemUseFirst(
                itemstack, player, world, i, j, k, l, alec1, alec2, alec3
            );
        }
    }
}
