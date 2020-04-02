//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ee;

import net.minecraft.server.*;

import java.util.Iterator;
import java.util.List;

public class ItemVoidRing extends ItemEECharged {
  private int ticksLastSpent;

  public ItemVoidRing(int var1) {
    super(var1, 0);
  }

  public int getIconFromDamage(int var1) {
    return var1 < 2 ? this.textureId + var1 : this.textureId;
  }

  private void PullItems(Entity var1, EntityHuman var2) {
    double var16;
    double var4;
    double var6;
    double var8;
    double var10;
    double var12;
    double var14;
    if (var1.getClass().equals(EntityItem.class)) {
      EntityItem var3 = (EntityItem) var1;
      var4 = (double) ((float) var2.locX + 0.5F) - var3.locX;
      var6 = (double) ((float) var2.locY + 0.5F) - var3.locY;
      var8 = (double) ((float) var2.locZ + 0.5F) - var3.locZ;
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
    else if (var1.getClass().equals(EntityLootBall.class)) {
      EntityLootBall var18 = (EntityLootBall) var1;
      var4 = (double) ((float) var2.locX + 0.5F) - var18.locX;
      var6 = (double) ((float) var2.locY + 0.5F) - var18.locY;
      var8 = (double) ((float) var2.locZ + 0.5F) - var18.locZ;
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

  public void doRelease(ItemStack var1, World var2, EntityHuman var3) {
    this.doTeleport(var1, var2, var3);
  }

  private void doTeleport(ItemStack itemStack, World world, EntityHuman entityHuman) {
//    float var4 = 1.0F;
//    float var5 = entityHuman.lastPitch + (entityHuman.pitch - entityHuman.lastPitch) * var4;
//    float var6 = entityHuman.lastYaw + (entityHuman.yaw - entityHuman.lastYaw) * var4;
//    double var7 = entityHuman.lastX + (entityHuman.locX - entityHuman.lastX) * (double)var4;
//    double var9 = entityHuman.lastY + (entityHuman.locY - entityHuman.lastY) * (double)var4 + 1.62D - (double)entityHuman.height;
//    double var11 = entityHuman.lastZ + (entityHuman.locZ - entityHuman.lastZ) * (double)var4;
//    Vec3D var13 = Vec3D.create(var7, var9, var11);
//    float var14 = MathHelper.cos(-var6 * 0.01745329F - 3.1415927F);
//    float var15 = MathHelper.sin(-var6 * 0.01745329F - 3.1415927F);
//    float var16 = -MathHelper.cos(-var5 * 0.01745329F);
//    float var17 = MathHelper.sin(-var5 * 0.01745329F);
//    float var18 = var15 * var16;
//    float var20 = var14 * var16;
//    double var21 = 150.0D;
//    Vec3D var23 = var13.add((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
//    MovingObjectPosition var24 = world.rayTrace(var13, var23, true);
//    if (var24 != null && var24.type == EnumMovingObjectType.TILE) {
//      int var25 = var24.b;
//      int var26 = var24.c;
//      int var27 = var24.d;
//      int var28 = var24.face;
//      if (var28 == 0) {
//        var26 -= 2;
//      }
//      if (var28 == 1) {
//        ++var26;
//      }
//      if (var28 == 2) {
//        --var27;
//      }
//      if (var28 == 3) {
//        ++var27;
//      }
//      if (var28 == 4) {
//        --var25;
//      }
//      if (var28 == 5) {
//        ++var25;
//      }
//      for(int var29 = 0; var29 < 32; ++var29) {
//        entityHuman.world.a("portal", (double)var25, (double)var26 + entityHuman.world.random.nextDouble() * 2.0D, (double)var27, entityHuman.world.random.nextGaussian(), 0.0D, entityHuman.world.random.nextGaussian());
//      }
    //if (entityHuman.world.getTypeId(var25, var26, var27) == 0 && entityHuman.world.getTypeId(var25, var26 + 1, var27) == 0) {
    entityHuman.enderTeleportTo(entityHuman.locX, entityHuman.locY, entityHuman.locZ); // 2020-04-02 Teleports the user to their current location.
    //entityHuman.enderTeleportTo((double)var25, (double)var26, (double)var27); // 2020-04-02 Disabled teleporting with the ring.
    //}
    entityHuman.fallDistance = 0.0F;
    //}
    entityHuman.getBukkitEntity().getServer().getPlayer(entityHuman.getBukkitEntity().getName())
          .sendMessage(" Teleporting with the Void Ring is not allowed!");
  }

  public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
    if (EEProxy.isClient(var2)) {
      return var1;
    }
    else {
      this.doDisintegrate(var1, var2, var3);
      return var1;
    }
  }

  private void doDisintegrate(ItemStack var1, World var2, EntityHuman var3) {
    float var4 = 1.0F;
    float var5 = var3.lastPitch + (var3.pitch - var3.lastPitch) * var4;
    float var6 = var3.lastYaw + (var3.yaw - var3.lastYaw) * var4;
    double var7 = var3.lastX + (var3.locX - var3.lastX) * (double) var4;
    double var9 = var3.lastY + (var3.locY - var3.lastY) * (double) var4 + 1.62D - (double) var3.height;
    double var11 = var3.lastZ + (var3.locZ - var3.lastZ) * (double) var4;
    Vec3D var13 = Vec3D.create(var7, var9, var11);
    float var14 = MathHelper.cos(-var6 * 0.01745329F - 3.1415927F);
    float var15 = MathHelper.sin(-var6 * 0.01745329F - 3.1415927F);
    float var16 = -MathHelper.cos(-var5 * 0.01745329F);
    float var17 = MathHelper.sin(-var5 * 0.01745329F);
    float var18 = var15 * var16;
    float var20 = var14 * var16;
    double var21 = 5.0D;
    Vec3D var23 = var13.add((double) var18 * var21, (double) var17 * var21, (double) var20 * var21);
    MovingObjectPosition var24 = var2.rayTrace(var13, var23, true);
    if (var24 != null && var24.type == EnumMovingObjectType.TILE) {
      int var25 = var24.b;
      int var26 = var24.c;
      int var27 = var24.d;
      if (var2.getMaterial(var25, var26, var27) == Material.WATER) {
        var2.setTypeId(var25, var26, var27, 0);
      }
      else if (var2.getMaterial(var25, var26 + 1, var27) == Material.WATER) {
        var2.setTypeId(var25, var26 + 1, var27, 0);
      }
      else if (var2.getMaterial(var25, var26, var27) == Material.LAVA) {
        var2.setTypeId(var25, var26, var27, 0);
      }
      else if (var2.getMaterial(var25, var26 + 1, var27) == Material.LAVA) {
        var2.setTypeId(var25, var26 + 1, var27, 0);
      }
    }

  }

  public void doPassive(ItemStack var1, World var2, EntityHuman var3) {
    if (this.isActivated(var1)) {
      this.doAttraction(var1, var2, var3);
      this.doCondense(var1, var2, var3);
    }

    if (!this.isActivated(var1.getData())) {
      this.dumpContents(var1, var3);
    }

  }

  public boolean roomFor(ItemStack var1, EntityHuman var2) {
    if (var1 == null) {
      return false;
    }
    else {
      for (int var3 = 0; var3 < var2.inventory.items.length; ++var3) {
        if (var2.inventory.items[var3] == null) {
          return true;
        }

        if (var2.inventory.items[var3].doMaterialsMatch(var1) && var2.inventory.items[var3].count <= var1.getMaxStackSize() - var1.count) {
          return true;
        }
      }

      return false;
    }
  }

  public void PushStack(ItemStack var1, EntityHuman var2) {
    if (var1 != null) {
      for (int var3 = 0; var3 < var2.inventory.items.length; ++var3) {
        if (var2.inventory.items[var3] == null) {
          var2.inventory.items[var3] = var1.cloneItemStack();
          var1 = null;
          return;
        }

        if (var2.inventory.items[var3].doMaterialsMatch(var1) && var2.inventory.items[var3].count <= var1.getMaxStackSize() - var1.count) {
          ItemStack var10000 = var2.inventory.items[var3];
          var10000.count += var1.count;
          var1 = null;
          return;
        }

        if (var2.inventory.items[var3].doMaterialsMatch(var1)) {
          while (var2.inventory.items[var3].count < var2.inventory.items[var3].getMaxStackSize() && var1 != null) {
            ++var2.inventory.items[var3].count;
            --var1.count;
            if (var1.count <= 0) {
              var1 = null;
              return;
            }
          }
        }
      }
    }

  }

  private void dumpContents(ItemStack var1, EntityHuman var2) {
    while (this.emc(var1) >= EEMaps.getEMC(EEItem.redMatter.id) && this.roomFor(new ItemStack(EEItem.redMatter, 1), var2)) {
      this.takeEMC(var1, EEMaps.getEMC(EEItem.redMatter.id));
      this.PushStack(new ItemStack(EEItem.redMatter, 1), var2);
    }

    while (this.emc(var1) >= EEMaps.getEMC(EEItem.darkMatter.id) && this.roomFor(new ItemStack(EEItem.darkMatter, 1), var2)) {
      this.takeEMC(var1, EEMaps.getEMC(EEItem.darkMatter.id));
      this.PushStack(new ItemStack(EEItem.darkMatter, 1), var2);
    }

    while (this.emc(var1) >= EEMaps.getEMC(Item.DIAMOND.id) && this.roomFor(new ItemStack(Item.DIAMOND, 1), var2)) {
      this.takeEMC(var1, EEMaps.getEMC(Item.DIAMOND.id));
      this.PushStack(new ItemStack(Item.DIAMOND, 1), var2);
    }

    while (this.emc(var1) >= EEMaps.getEMC(Item.GOLD_INGOT.id) && this.roomFor(new ItemStack(Item.GOLD_INGOT, 1), var2)) {
      this.takeEMC(var1, EEMaps.getEMC(Item.GOLD_INGOT.id));
      this.PushStack(new ItemStack(Item.GOLD_INGOT, 1), var2);
    }

    while (this.emc(var1) >= EEMaps.getEMC(Item.IRON_INGOT.id) && this.roomFor(new ItemStack(Item.IRON_INGOT, 1), var2)) {
      this.takeEMC(var1, EEMaps.getEMC(Item.IRON_INGOT.id));
      this.PushStack(new ItemStack(Item.IRON_INGOT, 1), var2);
    }

    while (this.emc(var1) >= EEMaps.getEMC(Block.COBBLESTONE.id) && this.roomFor(new ItemStack(Block.COBBLESTONE, 1), var2)) {
      this.takeEMC(var1, EEMaps.getEMC(Block.COBBLESTONE.id));
      this.PushStack(new ItemStack(Block.COBBLESTONE, 1), var2);
    }

  }

  public ItemStack target(ItemStack var1) {
    return this.getInteger(var1, "targetID") != 0 ? (this.getInteger(var1, "targetMeta") != 0 ? new ItemStack(this.getInteger(var1, "targetID"), 1, this.getInteger(var1, "targetMeta")) : new ItemStack(this.getInteger(var1, "targetID"), 1, 0)) : null;
  }

  public ItemStack product(ItemStack var1) {
    if (this.target(var1) != null) {
      int var2 = EEMaps.getEMC(this.target(var1));
      if (var2 < EEMaps.getEMC(Item.IRON_INGOT.id)) {
        return new ItemStack(Item.IRON_INGOT, 1);
      }

      if (var2 < EEMaps.getEMC(Item.GOLD_INGOT.id)) {
        return new ItemStack(Item.GOLD_INGOT, 1);
      }

      if (var2 < EEMaps.getEMC(Item.DIAMOND.id)) {
        return new ItemStack(Item.DIAMOND, 1);
      }

      if (var2 < EEMaps.getEMC(EEItem.darkMatter.id)) {
        return new ItemStack(EEItem.darkMatter, 1);
      }

      if (var2 < EEMaps.getEMC(EEItem.redMatter.id)) {
        return new ItemStack(EEItem.redMatter, 1);
      }
    }

    return null;
  }

  public void doCondense(ItemStack var1, World var2, EntityHuman var3) {
    if (!EEProxy.isClient(var2)) {
      if (this.product(var1) != null && this.emc(var1) >= EEMaps.getEMC(this.product(var1)) && this.roomFor(this.product(var1), var3)) {
        this.PushStack(this.product(var1), var3);
        this.takeEMC(var1, EEMaps.getEMC(this.product(var1)));
      }

      int var4 = 0;
      ItemStack[] var5 = var3.inventory.items;
      int var6 = var5.length;

      int var7;
      ItemStack var8;
      for (var7 = 0; var7 < var6; ++var7) {
        var8 = var5[var7];
        if (var8 != null && EEMaps.getEMC(var8) != 0 && this.isValidMaterial(var8, var3) && EEMaps.getEMC(var8) > var4) {
          var4 = EEMaps.getEMC(var8);
        }
      }

      var5 = var3.inventory.items;
      var6 = var5.length;

      for (var7 = 0; var7 < var6; ++var7) {
        var8 = var5[var7];
        if (var8 != null && EEMaps.getEMC(var8) != 0 && this.isValidMaterial(var8, var3) && EEMaps.getEMC(var8) <= var4) {
          var4 = EEMaps.getEMC(var8);
          this.setInteger(var1, "targetID", var8.id);
          this.setInteger(var1, "targetMeta", var8.getData());
        }
      }

      if (this.target(var1) != null && this.ConsumeMaterial(this.target(var1), var3)) {
        this.addEMC(var1, EEMaps.getEMC(this.target(var1)));
      }
    }

  }

  private boolean isLastCobbleStack(EntityHuman var1) {
    int var2 = 0;

    for (int var3 = 0; var3 < var1.inventory.items.length; ++var3) {
      if (var1.inventory.items[var3] != null && var1.inventory.items[var3].id == Block.COBBLESTONE.id) {
        var2 += var1.inventory.items[var3].count;
      }
    }

    if (var2 <= 64) {
      return true;
    }
    else {
      return false;
    }
  }

  private boolean isValidMaterial(ItemStack var1, EntityHuman var2) {
    if (EEMaps.getEMC(var1) == 0) {
      return false;
    }
    else if (var1.id == Block.COBBLESTONE.id && this.isLastCobbleStack(var2)) {
      return false;
    }
    else {
      int var3 = var1.id;
      if (var3 >= Block.byId.length) {
        if (var3 != Item.IRON_INGOT.id && var3 != Item.GOLD_INGOT.id && var3 != Item.DIAMOND.id && var3 != EEItem.darkMatter.id) {
          return false;
        }

        if (var3 == EEItem.redMatter.id) {
          return false;
        }
      }

      return EEMaps.isFuel(var1) ? false : (var1.id != Block.LOG.id && var1.id != Block.WOOD.id ? (var3 < Block.byId.length && Block.byId[var3] instanceof BlockContainer && Block.byId[var3].hasTileEntity(var1.getData()) ? false : EEMaps.isValidEDItem(var1)) : false);
    }
  }

  private int emc(ItemStack var1) {
    return this.getInteger(var1, "emc");
  }

  private void takeEMC(ItemStack var1, int var2) {
    this.setInteger(var1, "emc", this.emc(var1) - var2);
  }

  private void addEMC(ItemStack var1, int var2) {
    this.setInteger(var1, "emc", this.emc(var1) + var2);
  }

  public boolean ConsumeMaterial(ItemStack var1, EntityHuman var2) {
    return EEBase.Consume(var1, var2, false);
  }

  public void doActive(ItemStack var1, World var2, EntityHuman var3) {
  }

  private void doAttraction(ItemStack var1, World var2, EntityHuman var3) {
    if (!EEProxy.isClient(var2)) {
      List var4 = var2.a(EntityItem.class, AxisAlignedBB.b(var3.locX - 10.0D, var3.locY - 10.0D, var3.locZ - 10.0D, var3.locX + 10.0D, var3.locY + 10.0D, var3.locZ + 10.0D));
      Iterator var6 = var4.iterator();

      while (var6.hasNext()) {
        Entity var5 = (Entity) var6.next();
        this.PullItems(var5, var3);
      }

      List var11 = var2.a(EntityLootBall.class, AxisAlignedBB.b(var3.locX - 10.0D, var3.locY - 10.0D, var3.locZ - 10.0D, var3.locX + 10.0D, var3.locY + 10.0D, var3.locZ + 10.0D));
      Iterator var8 = var11.iterator();

      while (var8.hasNext()) {
        Entity var7 = (Entity) var8.next();
        this.PullItems(var7, var3);
      }

      List var12 = var3.world.a(EntityExperienceOrb.class, AxisAlignedBB.b(var3.locX - 10.0D, var3.locY - 10.0D, var3.locZ - 10.0D, var3.locX + 10.0D, var3.locY + 10.0D, var3.locZ + 10.0D));
      Iterator var10 = var12.iterator();

      while (var10.hasNext()) {
        Entity var9 = (Entity) var10.next();
        this.PullItems(var9, var3);
      }
    }

  }

  public void doToggle(ItemStack var1, World var2, EntityHuman var3) {
    if (this.isActivated(var1)) {
      var1.setData(0);
      var1.tag.setBoolean("active", false);
      var2.makeSound(var3, "break", 0.8F, 1.0F / (c.nextFloat() * 0.4F + 0.8F));
    }
    else {
      var1.setData(1);
      var1.tag.setBoolean("active", true);
      var2.makeSound(var3, "heal", 0.8F, 1.0F / (c.nextFloat() * 0.4F + 0.8F));
    }

  }

  public boolean canActivate() {
    return true;
  }

  public void doChargeTick(ItemStack var1, World var2, EntityHuman var3) {
  }

  public void doUncharge(ItemStack var1, World var2, EntityHuman var3) {
  }
}
