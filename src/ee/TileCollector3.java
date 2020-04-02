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

public class TileCollector3 extends TileEE implements ISpecialInventory, ISidedInventory, IEEPowerNet {
  private ItemStack[] items = new ItemStack[19];
  public int currentSunStatus = 1;
  public int collectorSunTime = 0;
  private int accumulate = 0;
  private float woftFactor = 1.0F;
  public int currentFuelProgress = 0;
  public boolean isUsingPower;
  public int sunTimeScaled = 0;
  public int kleinProgressScaled = 0;
  public int kleinPoints = 0;
  public int tick = 0;

  public TileCollector3() {
  }

  public void onBlockRemoval() {
    for (int i = 0; i < this.getSize(); ++i) {
      ItemStack itemstack = this.getItem(i);
      if (itemstack != null) {
        float f = this.world.random.nextFloat() * 0.8F + 0.1F;
        float f1 = this.world.random.nextFloat() * 0.8F + 0.1F;
        float f2 = this.world.random.nextFloat() * 0.8F + 0.1F;

        while (itemstack.count > 0) {
          int j = this.world.random.nextInt(21) + 10;
          if (j > itemstack.count) {
            j = itemstack.count;
          }

          itemstack.count -= j;
          EntityItem entityitem = new EntityItem(this.world, (double) ((float) this.x + f), (double) ((float) this.y + f1), (double) ((float) this.z + f2), new ItemStack(itemstack.id, j, itemstack.getData()));
          if (entityitem != null) {
            float f3 = 0.05F;
            entityitem.motX = (double) ((float) this.world.random.nextGaussian() * f3);
            entityitem.motY = (double) ((float) this.world.random.nextGaussian() * f3 + 0.2F);
            entityitem.motZ = (double) ((float) this.world.random.nextGaussian() * f3);
            if (entityitem.itemStack.getItem() instanceof ItemKleinStar) {
              ((ItemKleinStar) entityitem.itemStack.getItem()).setKleinPoints(entityitem.itemStack, ((ItemKleinStar) itemstack.getItem()).getKleinPoints(itemstack));
            }

            this.world.addEntity(entityitem);
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

  public ItemStack getItem(int i) {
    return this.items[i];
  }

  public ItemStack splitStack(int i, int j) {
    if (this.items[i] != null) {
      ItemStack itemstack1;
      if (this.items[i].count <= j) {
        itemstack1 = this.items[i];
        this.items[i] = null;
        return itemstack1;
      }
      else {
        itemstack1 = this.items[i].a(j);
        if (this.items[i].count == 0) {
          this.items[i] = null;
        }

        return itemstack1;
      }
    }
    else {
      return null;
    }
  }

  public void setItem(int i, ItemStack itemstack) {
    this.items[i] = itemstack;
    if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
      itemstack.count = this.getMaxStackSize();
    }

  }

  public boolean addItem(ItemStack var1, boolean var2, Orientations orientations) {
    switch (orientations.ordinal()) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
        if (var1 != null) {
          if (EEMaps.isFuel(var1)) {
            int var4 = 0;

            for (; var4 <= this.items.length - 3; ++var4) {
              if (this.items[var4] == null) {
                if (var2) {
                  for (this.items[var4] = var1.cloneItemStack(); var1.count > 0; --var1.count) {
                  }
                }

                return true;
              }

              if (this.items[var4].doMaterialsMatch(var1) && this.items[var4].count < this.items[var4].getMaxStackSize()) {
                if (!var2) {
                  return true;
                }

                while (this.items[var4].count < this.items[var4].getMaxStackSize() && var1.count > 0) {
                  ++this.items[var4].count;
                  --var1.count;
                }

                if (var1.count == 0) {
                  return true;
                }
              }
            }
          }
          else if (EEBase.isKleinStar(var1.id) && this.items[0] == null) {
            if (var2) {
              for (this.items[0] = var1.cloneItemStack(); var1.count > 0; --var1.count) {
              }
            }

            return true;
          }
        }
    }

    return false;
  }

  public ItemStack extractItem(boolean flag, Orientations orientations) {
    switch (orientations.ordinal()) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
        for (int i = 0; i < this.items.length; ++i) {
          if (this.items[i] != null && i != this.items.length - 1) {
            ItemStack itemstack;
            if (i == 0) {
              if (EEBase.isKleinStar(this.items[i].id)) {
                itemstack = this.items[i].cloneItemStack();
                if (flag) {
                  this.items[i] = null;
                }

                return itemstack;
              }
            }
            else if (this.items[i].id == EEItem.aeternalisFuel.id || this.items[this.items.length - 1] != null && this.items[i].doMaterialsMatch(this.items[this.items.length - 1])) {
              itemstack = this.items[i].cloneItemStack();
              itemstack.count = 1;
              if (flag) {
                --this.items[i].count;
                if (this.items[i].count < 1) {
                  this.items[i] = null;
                }
              }

              return itemstack;
            }
          }
        }
    }

    return null;
  }

  public String getName() {
    return "Energy Collector";
  }

  public void a(NBTTagCompound nbttagcompound) {
    super.a(nbttagcompound);
    NBTTagList nbttaglist = nbttagcompound.getList("Items");
    this.items = new ItemStack[this.getSize()];

    for (int i = 0; i < nbttaglist.size(); ++i) {
      NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(i);
      byte byte0 = nbttagcompound1.getByte("Slot");
      if (byte0 >= 0 && byte0 < this.items.length) {
        this.items[byte0] = ItemStack.a(nbttagcompound1);
      }
    }

    this.currentSunStatus = nbttagcompound.getShort("sunStatus");
    this.woftFactor = nbttagcompound.getFloat("timeFactor");
    this.accumulate = nbttagcompound.getInt("accumulate");
    this.collectorSunTime = nbttagcompound.getInt("sunTime");
  }

  public void b(NBTTagCompound nbttagcompound) {
    super.b(nbttagcompound);
    nbttagcompound.setInt("sunTime", this.collectorSunTime);
    nbttagcompound.setFloat("timeFactor", this.woftFactor);
    nbttagcompound.setInt("accumulate", this.accumulate);
    nbttagcompound.setShort("sunStatus", (short) this.currentSunStatus);
    NBTTagList nbttaglist = new NBTTagList();

    for (int i = 0; i < this.items.length; ++i) {
      if (this.items[i] != null) {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setByte("Slot", (byte) i);
        this.items[i].save(nbttagcompound1);
        nbttaglist.add(nbttagcompound1);
      }
    }

    nbttagcompound.set("Items", nbttaglist);
  }

  public int getSunProgressScaled(int i) {
    if (this.canUpgrade()) {
      if (this.getFuelDifference() > 0) {
        return this.collectorSunTime * i / (this.getFuelDifference() * 80) > 24 ? 24 : this.collectorSunTime * i / (this.getFuelDifference() * 80);
      }
      else {
        return this.items[0] != null && EEBase.isKleinStar(this.items[0].id) ? 24 : 0;
      }
    }
    else {
      return 0;
    }
  }

  public boolean canUpgrade() {
    int j;
    if (this.items[0] == null) {
      for (j = this.items.length - 3; j >= 1; --j) {
        if (this.items[j] != null && (this.items[this.items.length - 1] == null || !this.items[j].doMaterialsMatch(this.items[this.items.length - 1])) && EEMaps.isFuel(this.items[j]) && this.items[j].getItem().id != EEItem.aeternalisFuel.id) {
          this.items[0] = this.items[j].cloneItemStack();
          this.items[j] = null;
          break;
        }
      }
    }

    if (this.items[0] == null) {
      if (this.items[this.items.length - 2] == null) {
        return false;
      }

      if (EEMaps.isFuel(this.items[this.items.length - 2]) && this.items[this.items.length - 2].getItem().id != EEItem.aeternalisFuel.id) {
        this.items[0] = this.items[this.items.length - 2].cloneItemStack();
        this.items[this.items.length - 2] = null;
      }
    }

    if (this.items[0] == null) {
      return false;
    }
    else {
      if (EEBase.isKleinStar(this.items[0].id)) {
        if (EEBase.canIncreaseKleinStarPoints(this.items[0], this.world)) {
          return true;
        }

        if (this.items[this.items.length - 2] == null) {
          this.items[this.items.length - 2] = this.items[0].cloneItemStack();
          this.items[0] = null;
          return false;
        }

        for (j = 1; j <= this.items.length - 3; ++j) {
          if (this.items[j] == null) {
            this.items[j] = this.items[this.items.length - 2].cloneItemStack();
            this.items[this.items.length - 2] = this.items[0].cloneItemStack();
            this.items[0] = null;
            return false;
          }
        }
      }

      if (this.items[0].getItem().id != EEItem.aeternalisFuel.id && EEMaps.isFuel(this.items[0])) {
        return true;
      }
      else {
        return this.items[0].getItem().id == EEItem.darkMatter.id;
      }
    }
  }

  public boolean receiveEnergy(int i, byte byte0, boolean flag) {
    if (!this.isUsingPower()) {
      return false;
    }
    else if (flag) {
      this.accumulate += i;
      return true;
    }
    else {
      return true;
    }
  }

  public boolean sendEnergy(int i, byte byte0, boolean flag) {
    TileEntity tileentity = this.world.getTileEntity(this.x + (byte0 != 5 ? (byte0 != 4 ? 0 : 1) : -1), this.y + (byte0 != 1 ? (byte0 != 0 ? 0 : 1) : -1), this.z + (byte0 != 3 ? (byte0 != 2 ? 0 : 1) : -1));
    if (tileentity == null) {
      return false;
    }
    else {
      return tileentity instanceof IEEPowerNet && ((IEEPowerNet) tileentity).receiveEnergy(i + ((IEEPowerNet) tileentity).relayBonus(), byte0, flag);
    }
  }

  public void sendAllPackets(int i) {
    int j = 0;

    for (byte byte0 = 0; byte0 < 6; ++byte0) {
      if (this.sendEnergy(i, byte0, false)) {
        ++j;
      }
    }

    if (j == 0) {
      if (this.collectorSunTime <= 4800000 - i) {
        this.collectorSunTime += i;
      }

    }
    else {
      int k = i / j;
      if (k >= 1) {
        for (byte byte1 = 0; byte1 < 6; ++byte1) {
          this.sendEnergy(k, byte1, true);
        }

      }
    }
  }

  public boolean passEnergy(int i, byte byte0, boolean flag) {
    return false;
  }

  public int relayBonus() {
    return 0;
  }

  public int getRealSunStatus() {
    if (this.world == null) {
      System.out.println("World object is turning a null for collectors..");
      return 0;
    }
    else {
      if (this.world.worldProvider.d) {
        this.currentSunStatus = 16;
      }
      else {
        this.currentSunStatus = this.world.getLightLevel(this.x, this.y + 1, this.z) + 1;
      }

      return this.currentSunStatus;
    }
  }

  public int getSunStatus(int i) {
    return this.getRealSunStatus() * i / 16;
  }

  public void q_() {
    tick++;
    if (!this.clientFail()) {
      if (!this.world.isStatic) {
        if (this.collectorSunTime < 0) {
          this.collectorSunTime = 0;
        }

        if (this.items[0] != null && this.items[0].getItem() instanceof ItemKleinStar) {
          this.kleinProgressScaled = this.getKleinProgressScaled(48);
          this.kleinPoints = this.getKleinPoints(this.items[0]);
        }

        this.sunTimeScaled = this.getSunTimeScaled(48);
        this.currentFuelProgress = this.getSunProgressScaled(24);
        this.currentSunStatus = this.getSunStatus(12);
        this.isUsingPower = this.isUsingPower();

        int j;
        for (j = this.items.length - 3; j >= 2; --j) {
          if (this.items[j] == null && this.items[j - 1] != null) {
            this.items[j] = this.items[j - 1].cloneItemStack();
            this.items[j - 1] = null;
          }
        }

        this.woftFactor = EEBase.getPedestalFactor(this.world) * EEBase.getPlayerWatchFactor();
        if (this.isUsingPower()) {
          this.collectorSunTime += this.getFactoredProduction();
          if (this.accumulate > 0) {
            this.collectorSunTime += this.accumulate;
            this.accumulate = 0;
          }

          if (EEBase.isKleinStar(this.items[0].id)) {
            for (j = this.getFactoredProduction() * EEBase.getKleinLevel(this.items[0].id); j > 0 && this.collectorSunTime >= 80 && EEBase.addKleinStarPoints(this.items[0], 1, this.world); --j) {
              this.collectorSunTime -= 80;
            }
          }
          else {
            while (this.getFuelDifference() > 0 && this.collectorSunTime >= this.getFuelDifference() * 80) {
              this.collectorSunTime -= this.getFuelDifference() * 80;
              this.uptierFuel();
            }
          }
        }
        else {
          if (this.accumulate > 0) {
            this.collectorSunTime += this.accumulate;
            this.accumulate = 0;
          }
          if (tick % 20 == 0) {
            this.sendAllPackets(this.getFactoredProduction() * 20);
          }
        }
      }

    }
  }

  private int getKleinPoints(ItemStack itemstack) {
    if (itemstack == null) {
      return 0;
    }
    else {
      return itemstack.getItem() instanceof ItemKleinStar ? ((ItemKleinStar) itemstack.getItem()).getKleinPoints(itemstack) : 0;
    }
  }

  private int getSunTimeScaled(int i) {
    return this.collectorSunTime * i / 4800000;
  }

  private int getKleinProgressScaled(int i) {
    return this.items[0] != null && this.items[0].getItem() instanceof ItemKleinStar ? ((ItemKleinStar) this.items[0].getItem()).getKleinPoints(this.items[0]) * i / ((ItemKleinStar) this.items[0].getItem()).getMaxPoints(this.items[0]) : 0;
  }

  public int getFactoredProduction() {
    return (int) ((float) this.getProduction() * this.getWOFTReciprocal(this.woftFactor));
  }

  public int getProduction() {
    return this.getRealSunStatus() * 10;
  }

  public boolean isUsingPower() {
    return this.canUpgrade() && this.canCollect();
  }

  private int getFuelDifference() {
    return this.items[0] == null ? 0 : this.getFuelLevel(this.getNextFuel(this.items[0])) - this.getFuelLevel(this.items[0]);
  }

  private int getFuelLevel(ItemStack itemstack) {
    return EEMaps.getEMC(itemstack);
  }

  private ItemStack getNextFuel(ItemStack itemstack) {
    int i = itemstack.id;
    int j = itemstack.getData();
    if (this.items[this.items.length - 1] == null) {
      if (EEMaps.isFuel(itemstack)) {
        if (i == Item.COAL.id && j == 1) {
          return new ItemStack(Item.REDSTONE.id, 1, 0);
        }

        if (i == Item.REDSTONE.id) {
          return new ItemStack(Item.COAL.id, 1, 0);
        }

        if (i == Item.COAL.id) {
          return new ItemStack(Item.SULPHUR.id, 1, 0);
        }

        if (i == Item.SULPHUR.id) {
          return new ItemStack(Item.GLOWSTONE_DUST.id, 1, 0);
        }

        if (i == Item.GLOWSTONE_DUST.id) {
          return new ItemStack(EEItem.alchemicalCoal.id, 1, 0);
        }

        if (i == EEItem.alchemicalCoal.id) {
          return new ItemStack(Item.BLAZE_POWDER.id, 1, 0);
        }

        if (i == Item.BLAZE_POWDER.id) {
          return new ItemStack(Block.GLOWSTONE.id, 1, 0);
        }

        if (i == Block.GLOWSTONE.id) {
          return new ItemStack(EEItem.mobiusFuel.id, 1, 0);
        }

        if (i == EEItem.mobiusFuel.id) {
          return new ItemStack(EEItem.aeternalisFuel.id, 1, 0);
        }
      }

      return null;
    }
    else if (EEMaps.isFuel(this.items[this.items.length - 1])) {
      return EEMaps.getEMC(i, j) < EEMaps.getEMC(this.items[this.items.length - 1].id, this.items[this.items.length - 1].getData()) ? this.items[this.items.length - 1] : null;
    }
    else {
      EntityItem entityitem = new EntityItem(this.world, (double) this.x, (double) this.y, (double) this.z, this.items[this.items.length - 1].cloneItemStack());
      this.items[this.items.length - 1] = null;
      entityitem.pickupDelay = 10;
      this.world.addEntity(entityitem);
      return null;
    }
  }

  private boolean canCollect() {
    if (this.items[0] == null) {
      for (int i = 1; i <= this.items.length - 3; ++i) {
        if (this.items[i] != null && (this.items[this.items.length - 1] == null || this.items[this.items.length - 1] != null && this.items[this.items.length - 1].doMaterialsMatch(this.items[i]))) {
          this.items[0] = this.items[i].cloneItemStack();
          this.items[i] = null;
          break;
        }
      }

      if (this.items[0] == null) {
        return false;
      }
    }

    if (EEBase.isKleinStar(this.items[0].id)) {
      return true;
    }
    else if (this.getNextFuel(this.items[0]) == null) {
      return false;
    }
    else {
      ItemStack itemstack = this.getNextFuel(this.items[0]).cloneItemStack();
      if (this.items[this.items.length - 2] == null) {
        return true;
      }
      else {
        int l;
        if (!this.items[this.items.length - 2].doMaterialsMatch(itemstack)) {
          for (l = 1; l <= this.items.length - 3; ++l) {
            if (this.items[l] == null) {
              this.items[l] = this.items[this.items.length - 2].cloneItemStack();
              this.items[this.items.length - 2] = null;
              return true;
            }

            if (this.items[l].doMaterialsMatch(this.items[this.items.length - 2])) {
              while (this.items[this.items.length - 2] != null && this.items[l].count < 64) {
                --this.items[this.items.length - 2].count;
                ++this.items[l].count;
                if (this.items[this.items.length - 2].count == 0) {
                  this.items[this.items.length - 2] = null;
                  return true;
                }
              }
            }
          }
        }

        if (this.items[this.items.length - 2] != null && !this.items[this.items.length - 2].doMaterialsMatch(itemstack)) {
          return false;
        }
        else if (this.items[this.items.length - 2].count < this.getMaxStackSize() && this.items[this.items.length - 2].count < this.items[this.items.length - 2].getMaxStackSize()) {
          return true;
        }
        else {
          for (l = 1; l <= this.items.length - 2; ++l) {
            if (this.items[l] != null && (this.items[l].getItem().id == EEItem.mobiusFuel.id || this.items[this.items.length - 1] != null && this.items[l].doMaterialsMatch(this.items[this.items.length - 1])) && this.items[l].count >= this.items[l].getMaxStackSize() && this.tryDropInChest(new ItemStack(this.items[l].getItem(), this.items[l].count))) {
              this.items[l] = null;
            }
          }

          if (this.items[this.items.length - 2] == null) {
            return true;
          }
          else {
            for (l = 1; l <= this.items.length - 3; ++l) {
              if (this.items[l] == null) {
                this.items[l] = this.items[this.items.length - 2].cloneItemStack();
                this.items[this.items.length - 2] = null;
                return true;
              }

              if (this.items[l].doMaterialsMatch(this.items[this.items.length - 2])) {
                while (this.items[this.items.length - 2] != null && this.items[l].count < 64) {
                  --this.items[this.items.length - 2].count;
                  ++this.items[l].count;
                  if (this.items[this.items.length - 2].count == 0) {
                    this.items[this.items.length - 2] = null;
                    return true;
                  }
                }
              }
            }

            if (this.items[this.items.length - 2].count < itemstack.getMaxStackSize()) {
              return true;
            }
            else {
              return false;
            }
          }
        }
      }
    }
  }

  public void uptierFuel() {
    if (this.canCollect()) {
      if (this.getNextFuel(this.items[0]) != null) {
        ItemStack itemstack = this.getNextFuel(this.items[0]).cloneItemStack();
        itemstack.count = 1;
        if (this.items[this.items.length - 2] == null) {
          if ((this.items[this.items.length - 1] == null || !itemstack.doMaterialsMatch(this.items[this.items.length - 1])) && itemstack.getItem() != EEItem.aeternalisFuel) {
            this.items[this.items.length - 2] = itemstack.cloneItemStack();
          }
          else if (!this.tryDropInChest(itemstack)) {
            this.items[this.items.length - 2] = itemstack.cloneItemStack();
          }
        }
        else if (this.items[this.items.length - 2].id == itemstack.id) {
          if (this.items[this.items.length - 2].count == itemstack.getMaxStackSize()) {
            if (this.items[this.items.length - 2].getItem().id != EEItem.aeternalisFuel.id && (this.items[this.items.length - 1] == null || !this.items[this.items.length - 2].doMaterialsMatch(this.items[this.items.length - 1]))) {
              for (int i = 1; i <= this.items.length - 3; ++i) {
                if (this.items[i] == null) {
                  this.items[i] = this.items[this.items.length - 2].cloneItemStack();
                  this.items[this.items.length - 2] = null;
                  break;
                }

                if (this.items[i].doMaterialsMatch(this.items[this.items.length - 2])) {
                  while (this.items[i].count < this.items[i].getMaxStackSize() && this.items[this.items.length - 2] != null) {
                    --this.items[this.items.length - 2].count;
                    ++this.items[i].count;
                    if (this.items[this.items.length - 2].count == 0) {
                      this.items[this.items.length - 2] = null;
                    }
                  }
                }
              }
            }
            else if (this.tryDropInChest(this.items[this.items.length - 2].cloneItemStack())) {
              this.items[this.items.length - 2] = null;
            }
          }
          else {
            ItemStack var10000;
            if ((this.items[this.items.length - 1] == null || !itemstack.doMaterialsMatch(this.items[this.items.length - 1])) && itemstack.getItem() != EEItem.aeternalisFuel) {
              var10000 = this.items[this.items.length - 2];
              var10000.count += itemstack.count;
            }
            else if (!this.tryDropInChest(itemstack)) {
              var10000 = this.items[this.items.length - 2];
              var10000.count += itemstack.count;
            }
          }
        }
        else if ((this.items[this.items.length - 1] != null && itemstack.doMaterialsMatch(this.items[this.items.length - 1]) || itemstack.getItem() == EEItem.aeternalisFuel) && this.tryDropInChest(this.items[this.items.length - 2].cloneItemStack())) {
          this.items[this.items.length - 2] = null;
        }

        if (this.items[0].getItem().k()) {
          this.items[0] = new ItemStack(this.items[0].getItem().j());
        }
        else {
          --this.items[0].count;
        }

        if (this.items[0].count <= 0) {
          this.items[0] = null;
        }

      }
    }
  }

  public void f() {
  }

  public void g() {
  }

  public boolean a(EntityHuman entityhuman) {
    if (this.world.getTileEntity(this.x, this.y, this.z) != this) {
      return false;
    }
    else {
      return entityhuman.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
    }
  }

  public int getStartInventorySide(int i) {
    return i != 0 ? 1 : 0;
  }

  public int getSizeInventorySide(int i) {
    return i == 0 ? 1 : this.items.length - 2;
  }

  public boolean onBlockActivated(EntityHuman entityhuman) {
    if (!this.world.isStatic) {
      entityhuman.openGui(mod_EE.getInstance(), GuiIds.COLLECTOR_3, this.world, this.x, this.y, this.z);
    }

    return true;
  }

  public int getTextureForSide(int i) {
    if (i == 1) {
      return EEBase.collector3Top;
    }
    else {
      byte byte0 = this.direction;
      return i != byte0 ? EEBase.collectorSide : EEBase.collectorFront;
    }
  }

  public int getInventoryTexture(int i) {
    if (i == 1) {
      return EEBase.collector3Top;
    }
    else {
      return i == 3 ? EEBase.collectorFront : EEBase.collectorSide;
    }
  }

  public int getLightValue() {
    return 15;
  }

  public void onNeighborBlockChange(int i) {
  }

  public ItemStack splitWithoutUpdate(int i) {
    return null;
  }

  public ItemStack[] getContents() {
    return this.items;
  }

  public void setMaxStackSize(int size) {
  }
}
