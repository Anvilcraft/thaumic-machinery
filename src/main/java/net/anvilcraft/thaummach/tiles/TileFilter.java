package net.anvilcraft.thaummach.tiles;

import dev.tilera.auracore.api.HelperLocation;
import dev.tilera.auracore.api.machine.IConnection;
import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.thaummach.blocks.BlockApparatusFragile;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.client.fx.particles.FXWisp;

public class TileFilter extends TileConduit implements IConnection {
   public short taintedStore;
   public short stack;

   public void updateEntity() {
      if (!super.worldObj.isRemote) {
         if (Math.round(super.prevdisplayPure) != Math.round(super.displayPure) || Math.round(super.prevdisplayTaint) != Math.round(super.displayTaint)) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
            super.prevdisplayPure = super.displayPure;
            super.prevdisplayTaint = super.displayTaint;
         }

         this.calculateSuction();
         if (super.taintSuction < 15) {
            this.setTaintSuction(15);
         }

         if (this.getSuction((HelperLocation)null) > 0) {
            this.equalizeWithNeighbours();
         }

         super.displayTaint = Math.max(super.displayTaint, MathHelper.clamp_float(super.taintedVis, 0.0F, super.maxVis));
         super.displayPure = Math.max(super.displayPure, MathHelper.clamp_float(super.pureVis, 0.0F, super.maxVis));
         if (super.displayTaint + super.displayPure < 0.1F) {
            super.displayTaint = 0.0F;
            super.displayPure = 0.0F;
         }

         if (this.taintedStore < 40 + this.stack * 4 && super.taintedVis >= 0.025F) {
            ++this.taintedStore;
            super.taintedVis -= 0.025F;
            this.stack = 0;

            for(TileEntity te = super.worldObj.getTileEntity(super.xCoord, super.yCoord + 1, super.zCoord); te != null && te instanceof TileFilter && super.yCoord + 1 + this.stack < super.worldObj.getHeight(); te = super.worldObj.getTileEntity(super.xCoord, super.yCoord + 1 + this.stack, super.zCoord)) {
               ++this.stack;
            }

            if (this.taintedStore % 16 == 0) {
               FXWisp ef = new FXWisp(super.worldObj, (double)((float)super.xCoord + 0.5F), (double)((float)super.yCoord + 0.8F + (float)this.stack), (double)((float)super.zCoord + 0.5F), (double)((float)super.xCoord + 0.5F + (super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat())), (double)((float)super.yCoord + 3.0F + (float)this.stack + super.worldObj.rand.nextFloat()), (double)((float)super.zCoord + 0.5F + (super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat())), 0.5F, 5);
               Minecraft.getMinecraft().effectRenderer.addEffect(ef);
            }
         }

         if (this.taintedStore >= 40 + this.stack * 4) {
            int auraX = super.xCoord >> 4;
            int auraZ = super.zCoord >> 4;
            AuraManager.addTaintToClosest(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 1);
            this.taintedStore = 0;
         }

      }
   }

   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      this.taintedStore = nbttagcompound.getShort("taintedStore");
   }

   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      nbttagcompound.setShort("taintedStore", this.taintedStore);
   }

   public boolean getConnectable(ForgeDirection face) { //TODO: BLOCK
      if (super.worldObj.getBlock(super.xCoord, super.yCoord - 1, super.zCoord) == new BlockApparatusFragile() && super.worldObj.getBlockMetadata(super.xCoord, super.yCoord - 1, super.zCoord) == this.getBlockMetadata()) {
         return false;
      } else {
         switch (face) {
            case UP:
            case UNKNOWN:
               return false;
            default:
               return true;
         }
      }
   }
}
