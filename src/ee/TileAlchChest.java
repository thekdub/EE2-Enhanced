//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ee;

import buildcraft.api.ISpecialInventory;
import buildcraft.api.Orientations;
import ee.core.GuiIds;
import ee.item.ItemLootBall;
import forge.ISidedInventory;
import net.minecraft.server.*;

import java.util.Iterator;
import java.util.List;

public class TileAlchChest extends TileEE implements ISpecialInventory, ISidedInventory {
  private ItemStack[] items = new ItemStack[113];
  private int repairTimer = 0;
  private int eternalDensity;
  private boolean repairOn;
  private boolean condenseOn;
  private boolean interdictionOn;
  public boolean timeWarp;
  public float lidAngle;
  public float prevLidAngle;
  public int numUsingPlayers;
  private int ticksSinceSync;
  private boolean initialized;
  private boolean attractionOn;

  public TileAlchChest() {
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
          int var4 = 0;
          for (; var4 < this.items.length; ++var4) {
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
    }

    return false;
  }

  public ItemStack extractItem(boolean var1, Orientations orientations) {
    switch (orientations.ordinal()) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
        for (int var3 = 0; var3 < this.items.length; ++var3) {
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

  public int getSize() {
    return 104;
  }

  public ItemStack getItem(int var1) {
    return this.items[var1];
  }

  public ItemStack splitStack(int var1, int var2) {
    if (this.items[var1] != null) {
      ItemStack var3;
      if (this.items[var1].count <= var2) {
        var3 = this.items[var1];
        this.items[var1] = null;
        this.update();
        return var3;
      }
      else {
        var3 = this.items[var1].a(var2);
        if (this.items[var1].count == 0) {
          this.items[var1] = null;
        }
        this.update();
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
    this.update();
  }

  public String getName() {
    return "Chest";
  }

  public void a(NBTTagCompound var1) {
    super.a(var1);
    NBTTagList var2 = var1.getList("Items");
    this.items = new ItemStack[this.getSize()];
    for (int var3 = 0; var3 < var2.size(); ++var3) {
      NBTTagCompound var4 = (NBTTagCompound) var2.get(var3);
      int var5 = var4.getByte("Slot") & 255;
      if (var5 >= 0 && var5 < this.items.length) {
        this.items[var5] = ItemStack.a(var4);
      }
    }
    this.condenseOn = var1.getBoolean("condenseOn");
    this.repairOn = var1.getBoolean("repairOn");
    this.eternalDensity = var1.getShort("eternalDensity");
    this.timeWarp = var1.getBoolean("timeWarp");
    this.interdictionOn = var1.getBoolean("interdictionOn");
  }

  public void b(NBTTagCompound var1) {
    super.b(var1);
    var1.setBoolean("timeWarp", this.timeWarp);
    var1.setBoolean("condenseOn", this.condenseOn);
    var1.setBoolean("repairOn", this.repairOn);
    var1.setShort("eternalDensity", (short) this.eternalDensity);
    var1.setBoolean("interdictionOn", this.interdictionOn);
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

  public int getMaxStackSize() {
    return 64;
  }

  public void update() {
    super.update();
    if (this.world != null && !EEProxy.isClient(this.world)) {
      boolean var1 = false;
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      for (int var6 = 0; var6 < this.getSize(); ++var6) {
        if (this.items[var6] != null) {
          if (this.items[var6].getItem().id == EEItem.watchOfTime.id) {
            var4 = true;
          }
          if (this.items[var6].getItem().id == EEItem.repairCharm.id) {
            var1 = true;
          }
          if (this.items[var6].getItem() instanceof ItemVoidRing) {
            this.eternalDensity = var6;
            if ((this.items[var6].getData() & 1) == 0) {
              this.items[var6].setData(this.items[var6].getData() + 1);
              ((ItemEECharged) this.items[var6].getItem()).setBoolean(this.items[var6], "active", true);
            }
            var2 = true;
            var5 = true;
          }
          if (this.items[var6].getItem().id == EEItem.eternalDensity.id) {
            this.eternalDensity = var6;
            if ((this.items[var6].getData() & 1) == 0) {
              this.items[var6].setData(this.items[var6].getData() + 1);
              ((ItemEECharged) this.items[var6].getItem()).setBoolean(this.items[var6], "active", true);
            }
            var2 = true;
          }
          if (this.items[var6].getItem() instanceof ItemAttractionRing) {
            if ((this.items[var6].getData() & 1) == 0) {
              this.items[var6].setData(this.items[var6].getData() + 1);
              ((ItemEECharged) this.items[var6].getItem()).setBoolean(this.items[var6], "active", true);
            }
            var5 = true;
          }
          if (this.items[var6].getItem().id == EEBlock.eeTorch.id && this.items[var6].getData() == 0) {
            var3 = true;
          }
        }
      }
      if (var4 != this.timeWarp) {
        this.timeWarp = var4;
      }
      if (var1 != this.repairOn) {
        this.repairOn = var1;
      }
      if (var5 != this.attractionOn) {
        this.attractionOn = var5;
      }
      if (var2 != this.condenseOn) {
        this.condenseOn = var2;
      }
      else if (!var2) {
        this.eternalDensity = -1;
      }
      if (var3 != this.interdictionOn) {
        this.world.notify(this.x, this.y, this.z);
        this.interdictionOn = var3;
      }
    }

  }

  public void doRepair() {
    if (this.repairTimer >= 20) {
      ItemStack var1 = null;
      boolean var2 = false;
      for (int var3 = 0; var3 < this.getSize(); ++var3) {
        var2 = false;
        var1 = this.items[var3];
        if (var1 != null) {
          for (int var4 = 0; var4 < EEMaps.chargedItems.size(); ++var4) {
            if ((Integer) EEMaps.chargedItems.get(var4) == var1.id) {
              var2 = true;
              break;
            }
          }
          if (!var2 && var1.getData() >= 1 && var1.d()) {
            var1.setData(var1.getData() - 1);
          }
        }
      }
      this.repairTimer = 0;
    }
    ++this.repairTimer;
  }

  public void doCondense(ItemStack var1) {
    if (this.eternalDensity != -1) {
      int var2 = 0;
      int var3;
      for (var3 = 0; var3 < this.items.length; ++var3) {
        if (this.items[var3] != null && this.isValidMaterial(this.items[var3]) && EEMaps.getEMC(this.items[var3]) > var2) {
          var2 = EEMaps.getEMC(this.items[var3]);
        }
      }
      for (var3 = 0; var3 < this.items.length; ++var3) {
        if (this.items[var3] != null && this.isValidMaterial(this.items[var3]) && EEMaps.getEMC(this.items[var3]) < var2) {
          var2 = EEMaps.getEMC(this.items[var3]);
        }
      }
      if (var2 < EEMaps.getEMC(EEItem.redMatter.id) && !this.AnalyzeTier(this.items[this.eternalDensity], EEMaps.getEMC(EEItem.redMatter.id)) && var2 < EEMaps.getEMC(EEItem.darkMatter.id) && !this.AnalyzeTier(this.items[this.eternalDensity], EEMaps.getEMC(EEItem.darkMatter.id)) && var2 < EEMaps.getEMC(Item.DIAMOND.id) && !this.AnalyzeTier(this.items[this.eternalDensity], EEMaps.getEMC(Item.DIAMOND.id)) && var2 < EEMaps.getEMC(Item.GOLD_INGOT.id) && !this.AnalyzeTier(this.items[this.eternalDensity], EEMaps.getEMC(Item.GOLD_INGOT.id)) && var2 < EEMaps.getEMC(Item.IRON_INGOT.id) && this.AnalyzeTier(this.items[this.eternalDensity], EEMaps.getEMC(Item.IRON_INGOT.id))) {
      }
    }
  }

  private boolean AnalyzeTier(ItemStack var1, int var2) {
    if (var1 == null) {
      return false;
    }
    else {
      int var3 = 0;
      int var4;
      for (var4 = 0; var4 < this.items.length; ++var4) {
        if (this.items[var4] != null && this.isValidMaterial(this.items[var4]) && EEMaps.getEMC(this.items[var4]) < var2) {
          var3 += EEMaps.getEMC(this.items[var4]) * this.items[var4].count;
        }
      }
      if (var3 + this.emc(var1) < var2) {
        return false;
      }
      else {
        var4 = 0;
        while (var3 + this.emc(var1) >= var2 && var4 < 10) {
          ++var4;
          this.ConsumeMaterialBelowTier(var1, var2);
        }
        if (this.emc(var1) >= var2 && this.roomFor(this.getProduct(var2))) {
          this.PushStack(this.getProduct(var2));
          this.takeEMC(var1, var2);
        }
        return true;
      }
    }
  }

  private boolean roomFor(ItemStack var1) {
    if (var1 == null) {
      return false;
    }
    else {
      for (int var2 = 0; var2 < this.items.length; ++var2) {
        if (this.items[var2] == null) {
          return true;
        }
        if (this.items[var2].doMaterialsMatch(var1) && this.items[var2].count <= var1.getMaxStackSize() - var1.count) {
          return true;
        }
      }
      return false;
    }
  }

  private ItemStack getProduct(int var1) {
    return var1 == EEMaps.getEMC(Item.IRON_INGOT.id) ? new ItemStack(Item.IRON_INGOT, 1) : (var1 == EEMaps.getEMC(Item.GOLD_INGOT.id) ? new ItemStack(Item.GOLD_INGOT, 1) : (var1 == EEMaps.getEMC(Item.DIAMOND.id) ? new ItemStack(Item.DIAMOND, 1) : (var1 == EEMaps.getEMC(EEItem.darkMatter.id) ? new ItemStack(EEItem.darkMatter, 1) : (var1 == EEMaps.getEMC(EEItem.redMatter.id) ? new ItemStack(EEItem.redMatter, 1) : null))));
  }

  private void ConsumeMaterialBelowTier(ItemStack var1, int var2) {
    for (int var3 = 0; var3 < this.items.length; ++var3) {
      if (this.items[var3] != null && this.isValidMaterial(this.items[var3]) && EEMaps.getEMC(this.items[var3]) < var2) {
        this.addEMC(var1, EEMaps.getEMC(this.items[var3]));
        --this.items[var3].count;
        if (this.items[var3].count == 0) {
          this.items[var3] = null;
        }
        return;
      }
    }

  }

  private boolean isValidMaterial(ItemStack var1) {
    if (var1 == null) {
      return false;
    }
    else if (EEMaps.getEMC(var1) == 0) {
      return false;
    }
    else if (var1.getItem() instanceof ItemKleinStar) {
      return false;
    }
    else {
      int var2 = var1.id;
      return var2 != EEItem.redMatter.id && (var2 >= Block.byId.length || !(Block.byId[var2] instanceof BlockContainer) || !Block.byId[var2].hasTileEntity(var1.getData()));
    }
  }

  private int emc(ItemStack var1) {
    return !(var1.getItem() instanceof ItemEternalDensity) && !(var1.getItem() instanceof ItemVoidRing) ? 0 : (var1.getItem() instanceof ItemEternalDensity ? ((ItemEternalDensity) var1.getItem()).getInteger(var1, "emc") : ((ItemVoidRing) var1.getItem()).getInteger(var1, "emc"));
  }

  private void takeEMC(ItemStack var1, int var2) {
    if (var1.getItem() instanceof ItemEternalDensity || var1.getItem() instanceof ItemVoidRing) {
      if (var1.getItem() instanceof ItemEternalDensity) {
        ((ItemEternalDensity) var1.getItem()).setInteger(var1, "emc", this.emc(var1) - var2);
      }
      else {
        ((ItemVoidRing) var1.getItem()).setInteger(var1, "emc", this.emc(var1) - var2);
      }
    }
  }

  private void addEMC(ItemStack var1, int var2) {
    if (var1.getItem() instanceof ItemEternalDensity || var1.getItem() instanceof ItemVoidRing) {
      if (var1.getItem() instanceof ItemEternalDensity) {
        ((ItemEternalDensity) var1.getItem()).setInteger(var1, "emc", this.emc(var1) + var2);
      }
      else {
        ((ItemVoidRing) var1.getItem()).setInteger(var1, "emc", this.emc(var1) + var2);
      }
    }
  }

  public void q_() {
    if (++this.ticksSinceSync % 20 * 4 == 0) {
      this.world.playNote(this.x, this.y, this.z, 1, this.numUsingPlayers);
    }
    this.prevLidAngle = this.lidAngle;
    float var1 = 0.1F;
    double var2;
    if (this.numUsingPlayers > 0 && this.lidAngle == 0.0F) {
      double var4 = (double) this.x + 0.5D;
      var2 = (double) this.z + 0.5D;
      this.world.makeSound(var4, (double) this.y + 0.5D, var2, "random.chestopen", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }
    if (this.numUsingPlayers == 0 && this.lidAngle > 0.0F || this.numUsingPlayers > 0 && this.lidAngle < 1.0F) {
      float var8 = this.lidAngle;
      if (this.numUsingPlayers > 0) {
        this.lidAngle += var1;
      }
      else {
        this.lidAngle -= var1;
      }
      if (this.lidAngle > 1.0F) {
        this.lidAngle = 1.0F;
      }
      float var5 = 0.5F;
      if (this.lidAngle < var5 && var8 >= var5) {
        var2 = (double) this.x + 0.5D;
        double var6 = (double) this.z + 0.5D;
        this.world.makeSound(var2, (double) this.y + 0.5D, var6, "random.chestclosed", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
      }
      if (this.lidAngle < 0.0F) {
        this.lidAngle = 0.0F;
      }
    }
    if (this.repairOn) {
      this.doRepair();
    }
    if (this.attractionOn) {
      this.doAttraction();
    }
    if (this.condenseOn && this.eternalDensity >= 0) {
      this.doCondense(this.items[this.eternalDensity]);
    }
  }

  private void doAttraction() { // 2020-04-01 - Dead Entity marking added. Patches attraction ring duplication.
    for (int i = 0; i < 2; i++) { //2019-04-12 - for-loop implemented to reduce redundant code. Operation is repeated twice.
      List nearbyLootBalls = this.world.a(EntityLootBall.class, AxisAlignedBB.b((double) (this.x - 10), (double) (this.y - 10), (double) (this.z - 10), (double) (this.x + 10), (double) (this.y + 10), (double) (this.z + 10)));
      Iterator lootBallIterator = nearbyLootBalls.iterator();
      while (lootBallIterator.hasNext()) {
        Entity lootBall = (Entity) lootBallIterator.next();
        this.PullItems(lootBall);
      }
    }
    //List nearbyLootBalls2 = this.world.a(EntityLootBall.class, AxisAlignedBB.b((double) (this.x - 10), (double) (this.y - 10), (double) (this.z - 10), (double) (this.x + 10), (double) (this.y + 10), (double) (this.z + 10)));
    //			Iterator lootBallIterator2 = nearbyLootBalls2.iterator();
    //			while (lootBallIterator2.hasNext()) {
    //				Entity lootBall2 = (Entity) lootBallIterator2.next();
    //				this.PullItems(lootBall2);
    //			}
    List nearbyItems = this.world.a(EntityItem.class, AxisAlignedBB.b((double) (this.x - 10), (double) (this.y - 10), (double) (this.z - 10), (double) (this.x + 10), (double) (this.y + 10), (double) (this.z + 10)));
    Iterator nearbyItemIterator = nearbyItems.iterator();
    while (nearbyItemIterator.hasNext()) {
      Entity item = (Entity) nearbyItemIterator.next();
      this.PullItems(item);
    }
    List grabLootBalls = this.world.a(EntityLootBall.class, AxisAlignedBB.b((double) this.x - 0.5D, (double) this.y - 0.5D, (double) this.z - 0.5D, (double) this.x + 1.25D, (double) this.y + 1.25D, (double) this.z + 1.25D));
    Iterator grabLootBallsIterator = grabLootBalls.iterator();
    while (grabLootBallsIterator.hasNext()) {
      Entity lootBall = (Entity) grabLootBallsIterator.next();
      if (!lootBall.dead) { // Checks if the lootBall has been marked dead by another condenser.
        this.GrabItems(lootBall);
        lootBall.dead = true; // Marks the lootBall dead so other condensers won't grab it
      }
    }
    List grabItems = this.world.a(EntityItem.class, AxisAlignedBB.b((double) this.x - 0.5D, (double) this.y - 0.5D, (double) this.z - 0.5D, (double) this.x + 1.25D, (double) this.y + 1.25D, (double) this.z + 1.25D));
    Iterator grabItemIterator = grabItems.iterator();
    while (grabItemIterator.hasNext()) {
      Entity item = (Entity) grabItemIterator.next();
      if (!item.dead) { // Checks if the item has been marked dead by another condenser.
        this.GrabItems(item);
        item.dead = true; // Marks the item dead so other condensers won't grab it
      }
    }
  }

  public boolean PushStack(EntityItem var1) {
    if (var1 == null) {
      return false;
    }
    else if (var1.itemStack == null) {
      var1.die();
      return false;
    }
    else if (var1.itemStack.count < 1) {
      var1.die();
      return false;
    }
    else {
      for (int var2 = 0; var2 < this.items.length; ++var2) {
        if (this.items[var2] == null) {
          this.items[var2] = var1.itemStack.cloneItemStack();
          for (this.items[var2].count = 0; var1.itemStack.count > 0 && this.items[var2].count < this.items[var2].getMaxStackSize(); --var1.itemStack.count) {
            ++this.items[var2].count;
          }
          var1.die();
          return true;
        }
        if (this.items[var2].doMaterialsMatch(var1.itemStack) && this.items[var2].count <= var1.itemStack.getMaxStackSize() - var1.itemStack.count) {
          while (var1.itemStack.count > 0 && this.items[var2].count < this.items[var2].getMaxStackSize()) {
            ++this.items[var2].count;
            --var1.itemStack.count;
          }
          var1.die();
          return true;
        }
      }
      return false;
    }
  }

  public boolean PushStack(ItemStack var1) {
    if (var1 == null) {
      return false;
    }
    else {
      for (int var2 = 0; var2 < this.items.length; ++var2) {
        if (this.items[var2] == null) {
          this.items[var2] = var1.cloneItemStack();
          var1 = null;
          return true;
        }
        if (this.items[var2].doMaterialsMatch(var1) && this.items[var2].count <= var1.getMaxStackSize() - var1.count) {
          ItemStack var10000 = this.items[var2];
          var10000.count += var1.count;
          var1 = null;
          return true;
        }
      }
      return false;
    }
  }

  private void PushDenseStacks(EntityLootBall var1) {
    for (int var2 = 0; var2 < var1.items.length; ++var2) {
      if (var1.items[var2] != null && this.PushStack(var1.items[var2])) {
        var1.items[var2] = null;
      }
    }
  }

  private void GrabItems(Entity var1) {
    if (var1 != null && var1 instanceof EntityItem) {
      ItemStack var9 = ((EntityItem) var1).itemStack;
      if (var9 == null) {
        var1.die();
        return;
      }
      if (var9.getItem() instanceof ItemLootBall) {
        ItemLootBall var3 = (ItemLootBall) var9.getItem();
        ItemStack[] var4 = var3.getDroplist(var9);
        ItemStack[] var5 = var4;
        int var6 = var4.length;
        for (int var7 = 0; var7 < var6; ++var7) {
          ItemStack var8 = var5[var7];
          this.PushStack(var8);
        }
        var1.die();
      }
      else {
        this.PushStack(var9);
        var1.die();
      }
    }
    else if (var1 != null && var1 instanceof EntityLootBall) {
      if (((EntityLootBall) var1).items == null) {
        var1.die();
      }
      ItemStack[] var2 = ((EntityLootBall) var1).items;
      this.PushDenseStacks((EntityLootBall) var1);
      if (((EntityLootBall) var1).isEmpty()) {
        var1.die();
      }
    }
  }

  private void PullItems(Entity var1) {
    if (var1 instanceof EntityItem || var1 instanceof EntityLootBall) {
      if (var1 instanceof EntityLootBall) {
        ((EntityLootBall) var1).setBeingPulled(true);
      }
      double var3 = (double) this.x + 0.5D - var1.locX;
      double var5 = (double) this.y + 0.5D - var1.locY;
      double var7 = (double) this.z + 0.5D - var1.locZ;
      double var9 = var3 * var3 + var5 * var5 + var7 * var7;
      var9 *= var9;
      if (var9 <= Math.pow(6.0D, 4.0D)) {
        double var11 = var3 * 0.019999999552965164D / var9 * Math.pow(6.0D, 3.0D);
        double var13 = var5 * 0.019999999552965164D / var9 * Math.pow(6.0D, 3.0D);
        double var15 = var7 * 0.019999999552965164D / var9 * Math.pow(6.0D, 3.0D);
        if (var11 > 0.1D) {
          var11 = 0.1D;
        }
        else if (var11 < -0.1D) {
          var11 = -0.1D;
        }
        if (var13 > 0.1D) {
          var13 = 0.1D;
        }
        else if (var13 < -0.1D) {
          var13 = -0.1D;
        }
        if (var15 > 0.1D) {
          var15 = 0.1D;
        }
        else if (var15 < -0.1D) {
          var15 = -0.1D;
        }
        var1.motX += var11 * 1.2D;
        var1.motY += var13 * 1.2D;
        var1.motZ += var15 * 1.2D;
      }
    }
  }

  public boolean isInterdicting() {
    return this.interdictionOn;
  }

  private void PushEntities(Entity var1, int var2, int var3, int var4) {
    if (!(var1 instanceof EntityHuman) && !(var1 instanceof EntityItem)) {
      double var6 = (double) ((float) var2) - var1.locX;
      double var8 = (double) ((float) var3) - var1.locY;
      double var10 = (double) ((float) var4) - var1.locZ;
      double var12 = var6 * var6 + var8 * var8 + var10 * var10;
      var12 *= var12;
      if (var12 <= Math.pow(6.0D, 4.0D)) {
        double var14 = -(var6 * 0.019999999552965164D / var12) * Math.pow(6.0D, 3.0D);
        double var16 = -(var8 * 0.019999999552965164D / var12) * Math.pow(6.0D, 3.0D);
        double var18 = -(var10 * 0.019999999552965164D / var12) * Math.pow(6.0D, 3.0D);
        if (var14 > 0.0D) {
          var14 = 0.22D;
        }
        else if (var14 < 0.0D) {
          var14 = -0.22D;
        }
        if (var16 > 0.2D) {
          var16 = 0.12000000000000001D;
        }
        else if (var16 < -0.1D) {
          var16 = 0.12000000000000001D;
        }
        if (var18 > 0.0D) {
          var18 = 0.22D;
        }
        else if (var18 < 0.0D) {
          var18 = -0.22D;
        }
        var1.motX += var14;
        var1.motY += var16;
        var1.motZ += var18;
      }
    }
  }

  public void b(int var1, int var2) {
    if (var1 == 1) {
      this.numUsingPlayers = var2;
    }
  }

  public void f() {
    ++this.numUsingPlayers;
    this.world.playNote(this.x, this.y, this.z, 1, this.numUsingPlayers);
  }

  public void g() {
    --this.numUsingPlayers;
    this.world.playNote(this.x, this.y, this.z, 1, this.numUsingPlayers);
  }

  public boolean a(EntityHuman var1) {
    return this.world.getTileEntity(this.x, this.y, this.z) == this && var1.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
  }

  public int getStartInventorySide(int var1) {
    return 0;
  }

  public int getSizeInventorySide(int var1) {
    return this.getSize();
  }

  public boolean onBlockActivated(EntityHuman var1) {
    if (!this.world.isStatic) {
      var1.openGui(mod_EE.getInstance(), GuiIds.ALCH_CHEST, this.world, this.x, this.y, this.z);
    }

    return true;
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

  public int getTextureForSide(int var1) {
    if (var1 != 1 && var1 != 0) {
      byte var2 = this.direction;
      return var1 != var2 ? EEBase.alchChestSide : EEBase.alchChestFront;
    }
    else {
      return EEBase.alchChestTop;
    }
  }

  public int getInventoryTexture(int var1) {
    return var1 == 1 ? EEBase.alchChestTop : (var1 == 3 ? EEBase.alchChestFront : EEBase.alchChestSide);
  }

  public int getLightValue() {
    return this.isInterdicting() ? 15 : 0;
  }

  public void onNeighborBlockChange(int var1) {
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
