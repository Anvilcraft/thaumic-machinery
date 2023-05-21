package net.anvilcraft.thaummach.items;

import java.util.List;
import java.util.stream.IntStream;

import dev.tilera.auracore.api.HelperLocation;
import dev.tilera.auracore.api.machine.IConnection;
import net.anvilcraft.thaummach.TMTab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemDetector extends Item {
    public IIcon[] icons;

    public ItemDetector() {
        super();
        super.maxStackSize = 1;
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(TMTab.INSTANCE);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.uncommon;
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        this.icons = IntStream.rangeClosed(0, 2)
                         .mapToObj((i) -> "thaummach:detector_" + i)
                         .map(reg::registerIcon)
                         .toArray(IIcon[] ::new);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubItems(Item alec1, CreativeTabs tab, List list) {
        IntStream.rangeClosed(0, 2)
            .mapToObj((i) -> new ItemStack(this, 1, i))
            .forEach(list::add);
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.icons[meta];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item.thaummach:detector_" + stack.getItemDamage();
    }

    @Override
    public boolean onItemUseFirst(
        ItemStack itemstack,
        EntityPlayer entityplayer,
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
        if (ent instanceof IConnection) {
            IConnection tet = (IConnection) ent;
            if (itemstack.getItemDamage() == 0) {
                if (world.isRemote)
                    return false;
                entityplayer.addChatMessage(new ChatComponentText(
                    "Detected " + Math.round(tet.getPureVis()) + " Vis."
                ));
                world.playSoundEffect(
                    (double) x, (double) y, (double) z, "note.harp", 0.8F, 1.0F
                );
                if (tet.getVisSuction((HelperLocation) null) > 0) {
                    entityplayer.addChatMessage(new ChatComponentText(
                        tet.getVisSuction((HelperLocation) null) + " Vis TCB"
                    ));
                }

                return true;
            }

            if (itemstack.getItemDamage() == 1) {
                if (world.isRemote)
                    return false;
                entityplayer.addChatMessage(new ChatComponentText(
                    "Detected " + Math.round(tet.getTaintedVis()) + " Taint."
                ));
                world.playSoundEffect(
                    (double) x, (double) y, (double) z, "note.harp", 0.8F, 1.0F
                );
                if (tet.getTaintSuction((HelperLocation) null) > 0) {
                    entityplayer.addChatMessage(new ChatComponentText(
                        tet.getTaintSuction((HelperLocation) null) + " Taint TCB"
                    ));
                }

                return true;
            }

            if (itemstack.getItemDamage() == 2) {
                if (world.isRemote)
                    return false;
                int cap = Math.round(
                    (float) Math.round(tet.getPureVis() + tet.getTaintedVis())
                    / tet.getMaxVis() * 100.0F
                );
                int capp = Math.round(
                    (float) Math.round(tet.getPureVis()) / tet.getMaxVis() * 100.0F
                );
                int capt = Math.round(
                    (float) Math.round(tet.getTaintedVis()) / tet.getMaxVis() * 100.0F
                );
                entityplayer.addChatMessage(new ChatComponentText(
                    "Detected " + Math.round(tet.getPureVis()) + " Vis (" + capp
                    + "%) and " + Math.round(tet.getTaintedVis()) + " Taint (" + capt
                    + "%)."
                ));
                entityplayer.addChatMessage(
                    new ChatComponentText("The object is at " + cap + "% capacity.")
                );
                world.playSoundEffect(
                    (double) x,
                    (double) y,
                    (double) z,
                    "note.harp",
                    0.8F,
                    1.0F + 1.0F * (float) cap / 100.0F
                );
                if (tet.getTaintSuction((HelperLocation) null) > 0
                    || tet.getVisSuction((HelperLocation) null) > 0) {
                    entityplayer.addChatMessage(new ChatComponentText(
                        "" + tet.getVisSuction((HelperLocation) null) + " Vis TCB, "
                        + tet.getTaintSuction((HelperLocation) null) + " Taint TCB"
                    ));
                }

                return true;
            }
        }

        return super.onItemUseFirst(
            itemstack, entityplayer, world, x, y, z, l, alec1, alec2, alec3
        );
    }
}
