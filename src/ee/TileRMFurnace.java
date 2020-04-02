//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ee;

import buildcraft.api.ISpecialInventory;
import buildcraft.api.Orientations;
import ee.core.GuiIds;
import forge.ISidedInventory;
import net.minecraft.server.*;

import java.util.Random;

public class TileRMFurnace extends TileEE implements ISpecialInventory, ISidedInventory, IEEPowerNet {
  private ItemStack[] items = new ItemStack[27];
  public int furnaceBurnTime = 0;
  public int currentItemBurnTime = 0;
  public int furnaceCookTime = 0;
  public int nextinstack;
  public int nextoutstack;
  private float woftFactor = 1.0F;

  public TileRMFurnace() {
  }

  private boolean isChest(TileEntity var1) {
    return var1 instanceof TileEntityChest || var1 instanceof TileAlchChest;
  }

  public void onBlockRemoval() {
    for (int var1 = 0; var1 < this.getSize(); ++var1) {
      ItemStack var2 = this.getItem(var1);
      if (var2 != null) {
        float var3 = this.world.random.nextFloat() * 0.8F + 0.1F;
        float var4 = this.world.random.nextFloat() * 0.8F + 0.1F;
        float var5 = this.world.random.nextFloat() * 0.8F + 0.1F;
        while (var2.count > 0) {
          int var6 = this.world.random.nextInt(21) + 10;
          if (var6 > var2.count) {
            var6 = var2.count;
          }
          var2.count -= var6;
          EntityItem var7 = new EntityItem(this.world, (double) ((float) this.x + var3), (double) ((float) this.y + var4), (double) ((float) this.z + var5), new ItemStack(var2.id, var6, var2.getData()));
          if (var7 != null) {
            float var8 = 0.05F;
            var7.motX = (double) ((float) this.world.random.nextGaussian() * var8);
            var7.motY = (double) ((float) this.world.random.nextGaussian() * var8 + 0.2F);
            var7.motZ = (double) ((float) this.world.random.nextGaussian() * var8);
            if (var7.itemStack.getItem() instanceof ItemKleinStar) {
              ((ItemKleinStar) var7.itemStack.getItem()).setKleinPoints(var7.itemStack, ((ItemKleinStar) var2.getItem()).getKleinPoints(var2));
            }
            this.world.addEntity(var7);
          }
        }
      }
    }

  }

  public int getSize() {
    return this.items.length;
  }

  public int getMaxStackSize() {
    return 64;
  }

  public ItemStack getItem(int var1) {
    return this.items[var1];
  }

  public ItemStack splitStack(int slot, int count) {
    if (this.items[slot] != null) {
      ItemStack var3;
      if (this.items[slot].count <= count) {
        var3 = this.items[slot];
        this.items[slot] = null;
        return var3;
      }
      else {
        var3 = this.items[slot].a(count);
        if (this.items[slot].count == 0) {
          this.items[slot] = null;
        }
        return var3;
      }
    }
    else {
      return null;
    }
  }

  public void setItem(int var1, ItemStack var2) {
    this.items[var1] = var2;
    if (var2 != null && var2.count > this.getMaxStackSize()) {
      var2.count = this.getMaxStackSize();
    }
  }

  public boolean addItem(ItemStack itemStack, boolean var2, Orientations orientations) {
    switch (orientations.ordinal()) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
        if (itemStack != null) {
          if (this.getItemBurnTime(itemStack, true) <= 0 || itemStack.id == Block.LOG.id) {
            if (FurnaceRecipes.getInstance().getSmeltingResult(itemStack) != null) {
              for (int i = 1; i <= 13; ++i) {
                if (this.items[i] == null) { // Empty Space
                  if (var2) {
                    for (this.items[i] = itemStack.cloneItemStack(); itemStack.count > 0; --itemStack.count) {
                    }
                  }
                  return true;
                }
                if (this.items[i].doMaterialsMatch(itemStack) && this.items[i].count < this.items[i].getMaxStackSize()) { // Same Stack
                  if (!var2) {
                    return true;
                  }
                  while (this.items[i].count < this.items[i].getMaxStackSize() && itemStack.count > 0) {
                    ++this.items[i].count;
                    --itemStack.count;
                  }
                  if (itemStack.count == 0) {
                    return true;
                  }
                }
              }
            }
          }
          else { // No smelting result
            if (this.items[0] == null) {
              if (var2) {
                for (this.items[0] = itemStack.cloneItemStack(); itemStack.count > 0; --itemStack.count) {
                }
              }
              return true;
            }
            if (this.items[0].doMaterialsMatch(itemStack) && this.items[0].count < this.items[0].getMaxStackSize()) {
              if (var2) {
                while (this.items[0].count < this.items[0].getMaxStackSize() && itemStack.count > 0) {
                  ++this.items[0].count;
                  --itemStack.count;
                }
              }
              return true;
            }
          }
        }
    }
    return false;
  }

  public ItemStack extractItem(boolean var1, Orientations orientations) {
    switch (orientations.ordinal()) {
      case 2:
        if (this.items[0] == null) {
          return null;
        }
        else if (this.items[0].getItem() instanceof ItemKleinStar) {
          ItemStack var5 = this.items[0].cloneItemStack();
          if (var1) {
            this.items[0] = null;
          }
          return var5;
        }
      case 1:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
        for (int var3 = 10; var3 < this.items.length; ++var3) {
          if (this.items[var3] != null) {
            ItemStack var4 = this.items[var3].cloneItemStack();
            var4.count = 1;
            if (var1) {
              --this.items[var3].count;
              if (this.items[var3].count < 1) {
                this.items[var3] = null;
              }
            }
            return var4;
          }
        }
      default:
        return null;
    }
  }

  public String getName() {
    return "RM Furnace";
  }

  public void a(NBTTagCompound var1) {
    super.a(var1);
    NBTTagList var2 = var1.getList("Items");
    this.items = new ItemStack[this.getSize()];
    for (int var3 = 0; var3 < var2.size(); ++var3) {
      NBTTagCompound var4 = (NBTTagCompound) var2.get(var3);
      byte var5 = var4.getByte("Slot");
      if (var5 >= 0 && var5 < this.items.length) {
        this.items[var5] = ItemStack.a(var4);
      }
    }
    this.woftFactor = var1.getFloat("TimeFactor");
    this.furnaceBurnTime = var1.getInt("BurnTime");
    this.furnaceCookTime = var1.getShort("CookTime");
    this.currentItemBurnTime = this.getItemBurnTime(this.items[1], false);
  }

  public void b(NBTTagCompound var1) {
    super.b(var1);
    var1.setInt("BurnTime", this.furnaceBurnTime);
    var1.setShort("CookTime", (short) this.furnaceCookTime);
    var1.setFloat("TimeFactor", this.woftFactor);
    NBTTagList var2 = new NBTTagList();
    for (int var3 = 0; var3 < this.items.length; ++var3) {
      if (this.items[var3] != null) {
        NBTTagCompound var4 = new NBTTagCompound();
        var4.setByte("Slot", (byte) var3);
        this.items[var3].save(var4);
        var2.add(var4);
      }
    }
    var1.set("Items", var2);
  }

  public int getCookProgressScaled(int var1) {
    return this.world != null && !EEProxy.isClient(this.world) ? (this.furnaceCookTime + (this.isBurning() && this.canSmelt() ? 1 : 0)) * var1 / 3 : 0;
  }

  public int getBurnTimeRemainingScaled(int var1) {
    if (this.currentItemBurnTime == 0) {
      this.currentItemBurnTime = 10;
    }
    return this.furnaceBurnTime * var1 / this.currentItemBurnTime;
  }

  public boolean isBurning() {
    return this.furnaceBurnTime > 0;
  }

  public void q_() {
    if (!this.clientFail()) {
      this.woftFactor = EEBase.getPedestalFactor(this.world) * EEBase.getPlayerWatchFactor();
      boolean var1 = this.furnaceBurnTime > 0;
      boolean var2 = false;
      boolean var3 = false;
      if (this.furnaceBurnTime > 0) {
        this.furnaceBurnTime = (int) ((float) this.furnaceBurnTime - (this.getWOFTReciprocal(this.woftFactor) >= 1.0F ? this.getWOFTReciprocal(this.woftFactor) : 1.0F));
        if (this.furnaceBurnTime <= 0) {
          this.furnaceBurnTime = 0;
          var3 = true;
        }
      }
      if (!this.world.isStatic) {
        if (this.furnaceBurnTime <= 0 && this.canSmelt()) {
          this.currentItemBurnTime = this.furnaceBurnTime = this.getItemBurnTime(this.items[0], false) / 48;
          if (this.furnaceBurnTime > 0) {
            var2 = true;
            var3 = true;
            if (this.items[0] != null && !EEBase.isKleinStar(this.items[0].id)) {
              if (this.items[0].getItem().k()) {
                this.items[0] = new ItemStack(this.items[0].getItem().j());
              }
              else {
                --this.items[0].count;
              }
              if (this.items[0].count == 0) {
                this.items[0] = null;
              }
            }
          }
        }
        if (this.isBurning() && this.canSmelt()) {
          for (this.furnaceCookTime = (int) ((float) this.furnaceCookTime + (this.getWOFTReciprocal(this.woftFactor) >= 1.0F ? this.getWOFTReciprocal(this.woftFactor) : 1.0F)); this.furnaceCookTime >= 3 && this.canSmelt(); var3 = true) {
            this.furnaceCookTime -= 3;
            this.smeltItem();
            var2 = true;
          }
        }
        else {
          for (int var4 = 15; var4 < 27; ++var4) {
            if (this.items[var4] != null && this.items[var4].count >= this.items[var4].getMaxStackSize() && this.tryDropInChest(this.items[var4].cloneItemStack())) { // 2020-04-01 Replaced new itemStack with cloneItemStack to solve data value loss bug
              //if (this.items[var4] != null && this.items[var4].count >= this.items[var4].getMaxStackSize() && this.tryDropInChest(new ItemStack(this.items[var4].getItem(), this.items[var4].count))) { // 2020-04-01 Old Code
              this.items[var4] = null;
            }
          }
          this.furnaceCookTime = 0;
          this.furnaceBurnTime = 0;
        }
      }
      if (var2) {
        this.update();
      }
      if (var3) {
        this.world.notify(this.x, this.y, this.z);
      }
    }

  }

  private boolean canSmelt() {
    if (this.items[1] == null) {
      for (int var1 = 2; var1 <= 13; ++var1) {
        if (this.items[var1] != null) {
          this.items[1] = this.items[var1].cloneItemStack();
          this.items[var1] = null;
          break;
        }
      }
      if (this.items[1] == null) {
        return false;
      }
    }
    ItemStack var3 = FurnaceRecipes.getInstance().getSmeltingResult(this.items[1]);
    if (var3 == null) {
      return false;
    }
    else if (this.items[14] == null) {
      return true;
    }
    else {
      int var2;
      if (!this.items[14].doMaterialsMatch(var3)) {
        if (this.tryDropInChest(this.items[14].cloneItemStack())) {
          this.items[14] = null;
          return true;
        }
        for (var2 = 15; var2 <= 26; ++var2) {
          if (this.items[var2] == null) {
            this.items[var2] = this.items[14].cloneItemStack();
            this.items[14] = null;
            return true;
          }
          if (this.items[var2].doMaterialsMatch(this.items[14])) {
            while (this.items[14] != null && this.items[var2].count < 64) {
              --this.items[14].count;
              ++this.items[var2].count;
              if (this.items[14].count == 0) {
                this.items[14] = null;
                return true;
              }
            }
          }
        }
      }
      if (this.items[14].count < this.getMaxStackSize() && this.items[14].count < this.items[14].getMaxStackSize()) {
        return true;
      }
      else {
        for (var2 = 15; var2 < 27; ++var2) {
          if (this.items[var2] != null && this.items[var2].count >= this.items[var2].getMaxStackSize() && this.tryDropInChest(this.items[var2].cloneItemStack())) {
            this.items[var2] = null;
          }
        }
        if (this.items[14] == null) {
          return true;
        }
        else {
          for (var2 = 15; var2 <= 26; ++var2) {
            if (this.items[var2] == null) {
              this.items[var2] = this.items[14].cloneItemStack();
              this.items[14] = null;
              return true;
            }
            if (this.items[var2].doMaterialsMatch(this.items[14])) {
              while (this.items[14] != null && this.items[var2].count < 64) {
                --this.items[14].count;
                ++this.items[var2].count;
                if (this.items[14].count == 0) {
                  this.items[14] = null;
                  return true;
                }
              }
            }
          }
          return this.items[14].count < var3.getMaxStackSize();
        }
      }
    }
  }

  public void smeltItem() {
    if (this.canSmelt()) {
      ItemStack var1 = FurnaceRecipes.getInstance().getSmeltingResult(this.items[1]);
      boolean var2 = false;
      if (this.items[14] == null) {
        this.items[14] = var1.cloneItemStack();
        if (EEMaps.isOreBlock(this.items[1].id)) {
          ++this.items[14].count;
        }
      }
      else if (this.items[14].id == var1.id) {
        ItemStack var10000 = this.items[14];
        var10000.count += var1.count;
        if (EEMaps.isOreBlock(this.items[1].id)) {
          if (this.items[14].count < var1.getMaxStackSize()) {
            ++this.items[14].count;
          }
          else {
            var2 = true;
          }
        }
      }
      if (this.items[14].count == var1.getMaxStackSize()) {
        if (this.tryDropInChest(this.items[14])) {
          this.items[14] = null;
          if (var2) {
            this.items[14] = var1.cloneItemStack();
          }
        }
        else {
          for (int var3 = 15; var3 <= 26; ++var3) {
            if (this.items[var3] == null) {
              this.items[var3] = this.items[14].cloneItemStack();
              this.items[14] = null;
              if (var2) {
                this.items[14] = var1.cloneItemStack();
              }
              break;
            }
          }
        }
      }
      if (this.items[1].getItem().k()) {
        this.items[1] = new ItemStack(this.items[1].getItem().j());
      }
      else {
        --this.items[1].count;
      }
      if (this.items[1].count < 1) {
        this.items[1] = null;
      }
      this.world.notify(this.x, this.y, this.z);
    }
  }

  public int getItemBurnTime(ItemStack var1, boolean var2) {
    if (var1 == null) {
      return 0;
    }
    else {
      int var3 = var1.getItem().id;
      if (EEBase.isKleinStar(var3) && EEBase.takeKleinStarPoints(var1, var2 ? 0 : 32, this.world)) {
        return 1600;
      }
      else {
        int var4 = var1.getData();
        return var3 < 256 && Block.byId[var3].material == Material.WOOD ? 300 : (var3 == Item.STICK.id ? 100 : (var3 == Item.COAL.id ? 6400 : (var3 == Item.LAVA_BUCKET.id ? 3200 : (var3 == Block.SAPLING.id ? 100 : (var3 == EEItem.alchemicalCoal.id ? 25600 : (var3 == EEItem.mobiusFuel.id ? 102400 : (var3 == EEItem.aeternalisFuel.id ? 409600 : ModLoader.addAllFuel(var3, var4))))))));
      }
    }
  }

  public void f() {
  }

  public void g() {
  }

  public boolean a(EntityHuman var1) {
    return this.world.getTileEntity(this.x, this.y, this.z) != this ? false : var1.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
  }

  public int getStartInventorySide(int var1) {
    return var1 == 1 ? 0 : 1;
  }

  public int getSizeInventorySide(int var1) {
    return var1 == 1 ? 1 : 26;
  }

  public boolean onBlockActivated(EntityHuman var1) {
    if (!this.world.isStatic) {
      var1.openGui(mod_EE.getInstance(), GuiIds.RM_FURNACE, this.world, this.x, this.y, this.z);
    }

    return true;
  }

  public int getTextureForSide(int var1) {
    byte var2 = this.direction;
    return var1 == var2 ? EEBase.rmFurnaceFront : EEBase.rmBlockSide;
  }

  public int getInventoryTexture(int var1) {
    return var1 == 3 ? EEBase.rmFurnaceFront : EEBase.rmBlockSide;
  }

  public int getLightValue() {
    return this.isBurning() ? 15 : 0;
  }

  public void randomDisplayTick(Random var1) {
    if (this.isBurning()) {
      byte var2 = this.direction;
      float var3 = (float) this.x + 0.5F;
      float var4 = (float) this.y + 0.0F + var1.nextFloat() * 6.0F / 16.0F;
      float var5 = (float) this.z + 0.5F;
      float var6 = 0.52F;
      float var7 = var1.nextFloat() * 0.6F - 0.3F;
      if (var2 == 4) {
        this.world.a("smoke", (double) (var3 - var6), (double) var4, (double) (var5 + var7), 0.0D, 0.0D, 0.0D);
        this.world.a("flame", (double) (var3 - var6), (double) var4, (double) (var5 + var7), 0.0D, 0.0D, 0.0D);
      }
      else if (var2 == 5) {
        this.world.a("smoke", (double) (var3 + var6), (double) var4, (double) (var5 + var7), 0.0D, 0.0D, 0.0D);
        this.world.a("flame", (double) (var3 + var6), (double) var4, (double) (var5 + var7), 0.0D, 0.0D, 0.0D);
      }
      else if (var2 == 2) {
        this.world.a("smoke", (double) (var3 + var7), (double) var4, (double) (var5 - var6), 0.0D, 0.0D, 0.0D);
        this.world.a("flame", (double) (var3 + var7), (double) var4, (double) (var5 - var6), 0.0D, 0.0D, 0.0D);
      }
      else if (var2 == 3) {
        this.world.a("smoke", (double) (var3 + var7), (double) var4, (double) (var5 + var6), 0.0D, 0.0D, 0.0D);
        this.world.a("flame", (double) (var3 + var7), (double) var4, (double) (var5 + var6), 0.0D, 0.0D, 0.0D);
      }
      for (int var8 = 0; var8 < 4; ++var8) {
        double var9 = (double) ((float) this.x + var1.nextFloat());
        double var11 = (double) ((float) this.y + var1.nextFloat());
        double var13 = (double) ((float) this.z + var1.nextFloat());
        double var15 = 0.0D;
        double var17 = 0.0D;
        double var19 = 0.0D;
        int var21 = var1.nextInt(2) * 2 - 1;
        var15 = ((double) var1.nextFloat() - 0.5D) * 0.5D;
        var17 = ((double) var1.nextFloat() - 0.5D) * 0.5D;
        var19 = ((double) var1.nextFloat() - 0.5D) * 0.5D;
        if ((this.world.getTypeId(this.x - 1, this.y, this.z) != EEBlock.eeStone.id || this.world.getData(this.x - 1, this.y, this.z) != 3) && (this.world.getTypeId(this.x + 1, this.y, this.z) != EEBlock.eeStone.id || this.world.getData(this.x + 1, this.y, this.z) != 3)) {
          var9 = (double) this.x + 0.5D + 0.25D * (double) var21;
          var15 = (double) (var1.nextFloat() * 2.0F * (float) var21);
        }
        else {
          var13 = (double) this.z + 0.5D + 0.25D * (double) var21;
          var19 = (double) (var1.nextFloat() * 2.0F * (float) var21);
        }
        this.world.a("portal", var9, var11, var13, var15, var17, var19);
      }
    }

  }

  public boolean receiveEnergy(int var1, byte var2, boolean var3) {
    if (this.canSmelt()) {
      if (var3) {
        this.furnaceBurnTime += var1;
      }
      return true;
    }
    else {
      return false;
    }
  }

  public boolean sendEnergy(int var1, byte var2, boolean var3) {
    return false;
  }

  public boolean passEnergy(int var1, byte var2, boolean var3) {
    return false;
  }

  public void sendAllPackets(int var1) {
  }

  public int relayBonus() {
    return 0;
  }

  public ItemStack splitWithoutUpdate(int var1) {
    return null;
  }

  public ItemStack[] getContents() {
    return this.items;
  }

  public void setMaxStackSize(int size) {
  }
}
