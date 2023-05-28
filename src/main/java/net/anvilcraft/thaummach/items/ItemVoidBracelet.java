package net.anvilcraft.thaummach.items;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.alec.jalec.AlecLogger;
import net.anvilcraft.thaummach.RuneTileData;
import net.anvilcraft.thaummach.TMTab;
import net.anvilcraft.thaummach.tiles.TileSeal;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class ItemVoidBracelet extends Item {
    public IIcon[] icons;

    public ItemVoidBracelet() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(TMTab.INSTANCE);
        this.setUnlocalizedName("thaummach:void_bracelet");
        super.maxStackSize = 1;
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        this.icons = IntStream.rangeClosed(0, 6)
                         .mapToObj((i) -> "thaummach:void_bracelet_" + i)
                         .map(reg::registerIcon)
                         .toArray(IIcon[] ::new);
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.icons[meta];
    }

    public boolean getTalismanDestination(EntityPlayer player, int rune) {
        World worldObj = player.worldObj;
        List<RuneTileData> rd
            = TileSeal.SEALS.stream()
                  .filter((pd) -> pd.dim == worldObj.provider.dimensionId && pd.rune == rune - 1)
                  .collect(Collectors.toList());
        if (rd.size() == 0) {
            return false;
        }
        int q = worldObj.rand.nextInt(rd.size());
        TileEntity te = worldObj.getTileEntity(rd.get(q).x, rd.get(q).y, rd.get(q).z);
        if (te != null && te instanceof TileSeal) {
            worldObj.getChunkProvider().loadChunk(rd.get(q).x >> 4, rd.get(q).z >> 4);
            // TODO: WTF
            //ThaumCraftCore.loadChunk(
            //    worldObj, ((SISpecialTile) rd.get(q)).x, ((SISpecialTile) rd.get(q)).z
            //);
            ((TileSeal) te).delay = 40;
            float tYaw = 0.0F;
            switch (((TileSeal) te).orientation) {
                case 2:
                    tYaw = 180.0F;
                    break;
                case 3:
                    tYaw = 0.0F;
                    break;
                case 4:
                    tYaw = 90.0F;
                    break;
                case 5:
                    tYaw = 270.0F;
            }

            player.motionX = 0.0;
            player.motionZ = 0.0;
            // TODO: sounds
            //worldObj.playSoundEffect(
            //    player.posX,
            //    player.posY,
            //    player.posZ,
            //    "mob.endermen.portal",
            //    1.0F,
            //    1.0F
            //);
            //
            // TODO: FX
            //ThaumCraftCore.poof(
            //    worldObj,
            //    (float) player.posX - 0.5F,
            //    (float) player.posY - 0.5F,
            //    (float) player.posZ - 0.5F
            //);
            int xm = 0;
            int zm = 0;
            int ym = 0;
            switch (((TileSeal) te).orientation) {
                case 0:
                    ym = -1;
                    break;
                case 1:
                    ym = 1;
                    break;
                case 2:
                    zm = -1;
                    break;
                case 3:
                    zm = 1;
                    break;
                case 4:
                    xm = -1;
                    break;
                case 5:
                    xm = 1;
            }

            if (((TileSeal) te).orientation > 1
                && worldObj.isAirBlock(
                    ((TileSeal) te).xCoord + xm,
                    ((TileSeal) te).yCoord + ym - 1,
                    ((TileSeal) te).zCoord + zm
                )) {
                --ym;
            }

            AuraManager.addFluxToClosest(
                worldObj, te.xCoord, te.yCoord, te.zCoord, new AspectList().add(Aspect.ELDRITCH, 25)
            );

            double var10001 = (double) ((TileSeal) te).xCoord + 0.5 + (double) xm;
            double var10002 = (double) ((TileSeal) te).yCoord + 0.5 + (double) ym;
            ((EntityPlayerMP) player)
                .playerNetServerHandler.setPlayerLocation(
                    var10001,
                    var10002,
                    (double) ((TileSeal) te).zCoord + 0.5 + (double) zm,
                    tYaw,
                    player.rotationPitch

                );
            // TODO: sounds
            //worldObj.playSoundEffect(
            //    player.posX,
            //    player.posY,
            //    player.posZ,
            //    "mob.endermen.portal",
            //    1.0F,
            //    1.0F
            //);

            // TODO: fx
            //ThaumCraftCore.poof(
            //    worldObj,
            //    (float) player.posX - 0.5F,
            //    (float) player.posY - 0.5F,
            //    (float) player.posZ - 0.5F
            //);
            return true;
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (!world.isRemote
            && !this.getTalismanDestination(entityplayer, itemstack.getItemDamage())) {
            entityplayer.addChatMessage(new ChatComponentText("No valid destinations found."));
        }

        return itemstack;
    }

    @Override
    public boolean onItemUseFirst(
        ItemStack ist,
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
        TileEntity ent = world.getTileEntity(i, j, k);
        if (!world.isRemote && ent instanceof TileSeal && ((TileSeal) ent).runes[0] == 0
            && ((TileSeal) ent).runes[1] == 1) {
            int rune = ((TileSeal) ent).runes[2] + 1;
            if (rune != ist.getItemDamage()) {
                ist.setItemDamage(rune);
                world.playSoundEffect(
                    (double) ((float) i + 0.5F),
                    (double) ((float) j + 0.5F),
                    (double) ((float) k + 0.5F),
                    "thaumcraft.zap",
                    0.5F,
                    1.0F
                );
                player.addChatMessage(
                    new ChatComponentText("You've linked the bracelet to a new network.")
                );
                return true;
            }
        }

        return super.onItemUseFirst(ist, player, world, i, j, k, l, alec1, alec2, alec3);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.rare;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer arg1, List list, boolean arg3) {
        switch (itemstack.getItemDamage()) {
            case 0:
                list.add("Linked to the default network");
                break;
            case 1:
                list.add("Linked to the Magic network");
                break;
            case 2:
                list.add("Linked to the Air network");
                break;
            case 3:
                list.add("Linked to the Water network");
                break;
            case 4:
                list.add("Linked to the Earth network");
                break;
            case 5:
                list.add("Linked to the Fire network");
                break;
            case 6:
                list.add("Linked to the Dark network");
        }
    }
}
