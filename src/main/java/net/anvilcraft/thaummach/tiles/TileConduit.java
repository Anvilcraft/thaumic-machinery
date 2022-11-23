package net.anvilcraft.thaummach.tiles;

import dev.tilera.auracore.api.HelperLocation;
import dev.tilera.auracore.api.machine.IConnection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public class TileConduit extends TileEntity implements IConnection {
   public float pureVis = 0.0F;
   public float taintedVis = 0.0F;
   public float maxVis = 4.0F;
   float fillAmount = 4.0F;
   public float displayPure;
   public float displayTaint;
   public float prevdisplayPure;
   public float prevdisplayTaint;
   public int visSuction = 0;
   public int taintSuction = 0;

   @Override
   public void updateEntity() {
      if (!super.worldObj.isRemote) {
         if (this.prevdisplayPure != this.displayPure || this.prevdisplayTaint != this.displayTaint) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
            this.prevdisplayPure = this.displayPure;
            this.prevdisplayTaint = this.displayTaint;
         }

         this.calculateSuction();
         if (this.getSuction((HelperLocation)null) > 0) {
            this.equalizeWithNeighbours();
         }

         this.displayTaint = Math.max(this.displayTaint, MathHelper.clamp_float(this.taintedVis, 0.0F, this.maxVis));
         this.displayPure = Math.max(this.displayPure, MathHelper.clamp_float(this.pureVis, 0.0F, this.maxVis));
         if (this.displayTaint + this.displayPure < 0.1F) {
            this.displayTaint = 0.0F;
            this.displayPure = 0.0F;
         }

      }
   }

   protected void calculateSuction() {
      this.setSuction(0);

      for(int dir = 0; dir < 6; ++dir) {
         HelperLocation loc = new HelperLocation(this);
         switch (dir) {
            case 0:
               loc.facing = ForgeDirection.UP;
               break;
            case 1:
               loc.facing = ForgeDirection.DOWN;
               break;
            case 2:
               loc.facing = ForgeDirection.SOUTH;
               break;
            case 3:
               loc.facing = ForgeDirection.NORTH;
               break;
            case 4:
               loc.facing = ForgeDirection.EAST;
               break;
            case 5:
               loc.facing = ForgeDirection.WEST;
         }

         if (this.getConnectable(loc.facing)) {
            TileEntity te = loc.getConnectableTile(super.worldObj);
            if (te != null && te instanceof IConnection) {
               IConnection ic = (IConnection)te;
               if (this.getVisSuction((HelperLocation)null) < ic.getVisSuction(new HelperLocation(this)) - 1) {
                  this.setVisSuction(ic.getVisSuction(new HelperLocation(this)) - 1);
               }

               if (this.getTaintSuction((HelperLocation)null) < ic.getTaintSuction(new HelperLocation(this)) - 1) {
                  this.setTaintSuction(ic.getTaintSuction(new HelperLocation(this)) - 1);
               }
            }
         }
      }

   }

   protected void equalizeWithNeighbours() {
      for(int dir = 0; dir < 6; ++dir) {
         HelperLocation loc = new HelperLocation(this);
         switch (dir) {
            case 0:
               loc.facing = ForgeDirection.UP;
               break;
            case 1:
               loc.facing = ForgeDirection.DOWN;
               break;
            case 2:
               loc.facing = ForgeDirection.SOUTH;
               break;
            case 3:
               loc.facing = ForgeDirection.NORTH;
               break;
            case 4:
               loc.facing = ForgeDirection.EAST;
               break;
            case 5:
               loc.facing = ForgeDirection.WEST;
         }

         if (this.getConnectable(loc.facing)) {
            TileEntity te = loc.getConnectableTile(super.worldObj);
            if (te != null && te instanceof IConnection) {
               IConnection ent = (IConnection)te;
               if (this.pureVis + this.taintedVis < this.maxVis && (this.getVisSuction((HelperLocation)null) > ent.getVisSuction(new HelperLocation(this)) || this.getTaintSuction((HelperLocation)null) > ent.getTaintSuction(new HelperLocation(this)))) {
                  float qq = Math.min((ent.getPureVis() + ent.getTaintedVis()) / 4.0F, this.fillAmount);
                  float[] results = ent.subtractVis(Math.min(qq, this.maxVis - (this.pureVis + this.taintedVis)));
                  if (this.getVisSuction((HelperLocation)null) > ent.getVisSuction(new HelperLocation(this))) {
                     this.pureVis += results[0];
                  } else {
                     ent.setPureVis(results[0] + ent.getPureVis());
                  }

                  if (this.getTaintSuction((HelperLocation)null) > ent.getTaintSuction(new HelperLocation(this))) {
                     this.taintedVis += results[1];
                  } else {
                     ent.setTaintedVis(results[1] + ent.getTaintedVis());
                  }
               }
            }
         }
      }

      this.pureVis = MathHelper.clamp_float(this.pureVis, 0.0F, this.maxVis);
      this.taintedVis = MathHelper.clamp_float(this.taintedVis, 0.0F, this.maxVis);
   }

   @Override
   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      this.pureVis = nbttagcompound.getFloat("pureVis");
      this.taintedVis = nbttagcompound.getFloat("taintedVis");
   }

   @Override
   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      nbttagcompound.setFloat("pureVis", this.pureVis);
      nbttagcompound.setFloat("taintedVis", this.taintedVis);
   }

   @Override
   public boolean getConnectable(ForgeDirection face) {
      return true;
   }

   @Override
   public boolean isVisSource() {
      return false;
   }

   @Override
   public boolean isVisConduit() {
      return true;
   }

   @Override
   public float getPureVis() {
      return this.pureVis;
   }

   @Override
   public void setPureVis(float amount) {
      this.pureVis = amount;
   }

   @Override
   public float getTaintedVis() {
      return this.taintedVis;
   }

   @Override
   public void setTaintedVis(float amount) {
      this.taintedVis = amount;
   }

   @Override
   public float getMaxVis() {
      return this.maxVis;
   }

   @Override
   public float[] subtractVis(float amount) {
      float pureAmount = amount / 2.0F;
      float taintAmount = amount / 2.0F;
      float[] result = new float[]{0.0F, 0.0F};
      if (amount < 0.001F) {
         return result;
      } else {
         if (this.pureVis < pureAmount) {
            pureAmount = this.pureVis;
         }

         if (this.taintedVis < taintAmount) {
            taintAmount = this.taintedVis;
         }

         if (pureAmount < amount / 2.0F && taintAmount == amount / 2.0F) {
            taintAmount = Math.min(amount - pureAmount, this.taintedVis);
         } else if (taintAmount < amount / 2.0F && pureAmount == amount / 2.0F) {
            pureAmount = Math.min(amount - taintAmount, this.pureVis);
         }

         this.pureVis -= pureAmount;
         this.taintedVis -= taintAmount;
         result[0] = pureAmount;
         result[1] = taintAmount;
         return result;
      }
   }

   @Override
   public int getVisSuction(HelperLocation loc) {
      return this.visSuction;
   }

   @Override
   public void setVisSuction(int suction) {
      this.visSuction = suction;
   }

   @Override
   public int getTaintSuction(HelperLocation loc) {
      return this.taintSuction;
   }

   @Override
   public void setTaintSuction(int suction) {
      this.taintSuction = suction;
   }

   @Override
   public void setSuction(int suction) {
      this.visSuction = suction;
      this.taintSuction = suction;
   }

   @Override
   public int getSuction(HelperLocation loc) {
      return Math.max(this.visSuction, this.taintSuction);
   }
}
