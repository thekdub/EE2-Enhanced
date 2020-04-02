//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ee;

import net.minecraft.server.*;

import java.util.Iterator;
import java.util.List;

public class ItemAttractionRing extends ItemEECharged {
  public ItemAttractionRing(int var1) {
    super(var1, 0);
    this.maxStackSize = 1;
  }

  public int getIconFromDamage(int var1) {
    return !this.isActivated(var1) ? this.textureId : this.textureId + 1;
  }

  private void PullItems(Entity entity, EntityHuman entityHuman) {
    double var16;
    double var4;
    double var6;
    double var8;
    double var10;
    double var12;
    double var14;
    if (entity.getClass().equals(EntityItem.class)) {
      EntityItem var3 = (EntityItem) entity;
      var4 = (double) ((float) entityHuman.locX + 0.5F) - var3.locX;
      var6 = (double) ((float) entityHuman.locY + 0.5F) - var3.locY;
      var8 = (double) ((float) entityHuman.locZ + 0.5F) - var3.locZ;
      var10 = var4 * var4 + var6 * var6 + var8 * var8;
      var10 *= var10;
      if (var10 <= Math.pow(6.0D, 4.0D)) {
        var12 = var4 * 0.019999999552965164D / var10 * Math.pow(6.0D, 3.0D);
        var14 = var6 * 0.019999999552965164D / var10 * Math.pow(6.0D, 3.0D);
        var16 = var8 * 0.019999999552965164D / var10 * Math.pow(6.0D, 3.0D);
        if (var12 > 0.1D) {
          var12 = 0.1D;
        }
        else if (var12 < -0.1D) {
          var12 = -0.1D;
        }

        if (var14 > 0.1D) {
          var14 = 0.1D;
        }
        else if (var14 < -0.1D) {
          var14 = -0.1D;
        }

        if (var16 > 0.1D) {
          var16 = 0.1D;
        }
        else if (var16 < -0.1D) {
          var16 = -0.1D;
        }

        var3.motX += var12 * 1.2D;
        var3.motY += var14 * 1.2D;
        var3.motZ += var16 * 1.2D;
      }
    }
    else if (entity.getClass().equals(EntityLootBall.class)) {
      EntityLootBall var18 = (EntityLootBall) entity;
      var4 = (double) ((float) entityHuman.locX + 0.5F) - var18.locX;
      var6 = (double) ((float) entityHuman.locY + 0.5F) - var18.locY;
      var8 = (double) ((float) entityHuman.locZ + 0.5F) - var18.locZ;
      var10 = var4 * var4 + var6 * var6 + var8 * var8;
      var10 *= var10;
      if (var10 <= Math.pow(6.0D, 4.0D)) {
        var12 = var4 * 0.019999999552965164D / var10 * Math.pow(6.0D, 3.0D);
        var14 = var6 * 0.019999999552965164D / var10 * Math.pow(6.0D, 3.0D);
        var16 = var8 * 0.019999999552965164D / var10 * Math.pow(6.0D, 3.0D);
        if (var12 > 0.1D) {
          var12 = 0.1D;
        }
        else if (var12 < -0.1D) {
          var12 = -0.1D;
        }

        if (var14 > 0.1D) {
          var14 = 0.1D;
        }
        else if (var14 < -0.1D) {
          var14 = -0.1D;
        }

        if (var16 > 0.1D) {
          var16 = 0.1D;
        }
        else if (var16 < -0.1D) {
          var16 = -0.1D;
        }

        var18.motX += var12 * 1.2D;
        var18.motY += var14 * 1.2D;
        var18.motZ += var16 * 1.2D;
      }
    }

  }

  public ItemStack a(ItemStack itemStack, World world, EntityHuman entityHuman) {
    if (EEProxy.isClient(world)) {
      return itemStack;
    }
    else {
      float f = 1.0F;
      float entityPitch = entityHuman.lastPitch + (entityHuman.pitch - entityHuman.lastPitch) * f;
      float entityYaw = entityHuman.lastYaw + (entityHuman.yaw - entityHuman.lastYaw) * f;
      double entityX = entityHuman.lastX + (entityHuman.locX - entityHuman.lastX) * (double) f;
      double entityY = entityHuman.lastY + (entityHuman.locY - entityHuman.lastY) * (double) f + 1.62D - (double) entityHuman.height;
      double entityZ = entityHuman.lastZ + (entityHuman.locZ - entityHuman.lastZ) * (double) f;
      Vec3D vector = Vec3D.create(entityX, entityY, entityZ);
      float var14 = MathHelper.cos(-entityYaw * 0.01745329F - 3.1415927F);
      float var15 = MathHelper.sin(-entityYaw * 0.01745329F - 3.1415927F);
      float var16 = -MathHelper.cos(-entityPitch * 0.01745329F);
      float var17 = MathHelper.sin(-entityPitch * 0.01745329F);
      float var18 = var15 * var16;
      float var20 = var14 * var16;
      double var21 = 5.0D;
      Vec3D vector2 = vector.add((double) var18 * var21, (double) var17 * var21, (double) var20 * var21);
      MovingObjectPosition var24 = world.rayTrace(vector, vector2, true);
      if (var24 == null) {
        return itemStack;
      }
      else {
        if (var24.type == EnumMovingObjectType.TILE) {
          int x = var24.b;
          int y = var24.c;
          int z = var24.d;
          if (world.getMaterial(x, y, z) == Material.WATER) {
            world.setTypeId(x, y, z, 0);
          }
          else if (world.getMaterial(x, y + 1, z) == Material.WATER) {
            world.setTypeId(x, y + 1, z, 0);
          }
          else if (world.getMaterial(x, y, z) == Material.LAVA) {
            world.setTypeId(x, y, z, 0);
          }
          else if (world.getMaterial(x, y + 1, z) == Material.LAVA) {
            world.setTypeId(x, y + 1, z, 0);
          }
        }

        return itemStack;
      }
    }
  }

  public void doActive(ItemStack itemStack, World world, EntityHuman entityHuman) {
    if (!EEProxy.isClient(world)) {
      List var4 = world.a(EntityItem.class, AxisAlignedBB.b(entityHuman.locX - 10.0D, entityHuman.locY - 10.0D, entityHuman.locZ - 10.0D, entityHuman.locX + 10.0D, entityHuman.locY + 10.0D, entityHuman.locZ + 10.0D));
      Iterator var6 = var4.iterator();

      while (var6.hasNext()) {
        Entity var5 = (Entity) var6.next();
        this.PullItems(var5, entityHuman);
      }

      List var11 = world.a(EntityLootBall.class, AxisAlignedBB.b(entityHuman.locX - 10.0D, entityHuman.locY - 10.0D, entityHuman.locZ - 10.0D, entityHuman.locX + 10.0D, entityHuman.locY + 10.0D, entityHuman.locZ + 10.0D));
      Iterator var8 = var11.iterator();

      while (var8.hasNext()) {
        Entity var7 = (Entity) var8.next();
        this.PullItems(var7, entityHuman);
      }

      List var12 = entityHuman.world.a(EntityExperienceOrb.class, AxisAlignedBB.b(entityHuman.locX - 10.0D, entityHuman.locY - 10.0D, entityHuman.locZ - 10.0D, entityHuman.locX + 10.0D, entityHuman.locY + 10.0D, entityHuman.locZ + 10.0D));
      Iterator var10 = var12.iterator();

      while (var10.hasNext()) {
        Entity var9 = (Entity) var10.next();
        this.PullItems(var9, entityHuman);
      }
    }

  }

  public void ConsumeReagent(ItemStack itemStack, EntityHuman entityHuman, boolean var3) {
    EEBase.updatePlayerEffect(itemStack.getItem(), 200, entityHuman);
  }

  public void doToggle(ItemStack itemStack, World world, EntityHuman entityHuman) {
    if (this.isActivated(itemStack.getData())) {
      itemStack.setData(0);
      world.makeSound(entityHuman, "break", 0.8F, 1.0F / (c.nextFloat() * 0.4F + 0.8F));
    }
    else {
      itemStack.setData(1);
      world.makeSound(entityHuman, "heal", 0.8F, 1.0F / (c.nextFloat() * 0.4F + 0.8F));
    }

  }

  public void doChargeTick(ItemStack itemStack, World world, EntityHuman entityHuman) {
  }

  public void doUncharge(ItemStack itemStack, World world, EntityHuman entityHuman) {
  }

  public boolean canActivate() {
    return true;
  }
}
