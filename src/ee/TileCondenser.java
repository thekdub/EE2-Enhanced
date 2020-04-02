package ee;

import buildcraft.api.ISpecialInventory;
import buildcraft.api.Orientations;
import ee.core.GuiIds;
import ee.item.ItemLootBall;
import forge.ISidedInventory;
import net.minecraft.server.*;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TileCondenser extends TileEE implements ISpecialInventory, ISidedInventory, IEEPowerNet {
  private ItemStack[] items = new ItemStack[92];
  public int scaledEnergy = 0;
  public float lidAngle;
  public float prevLidAngle;
  public int numUsingPlayers;
  private int ticksSinceSync;
  private int eternalDensity;
  private boolean condenseOn;
  private boolean initialized;
  public int displayEnergy = 0;
  public int currentItemProgress = 0;
  private boolean attractionOn;

  public TileCondenser() {
  }

  private boolean isChest(TileEntity tileEntity) {
    return tileEntity instanceof TileEntityChest || tileEntity instanceof TileAlchChest;
  }

  public void onBlockRemoval() {
    for (int itemSlot = 0; itemSlot < this.getSize(); ++itemSlot) {
      ItemStack itemStack = this.getItem(itemSlot);
      if (itemStack != null) {
        float fx = this.world.random.nextFloat() * 0.8F + 0.1F;
        float fy = this.world.random.nextFloat() * 0.8F + 0.1F;
        float fz = this.world.random.nextFloat() * 0.8F + 0.1F;
        while (itemStack.count > 0) {
          int rand = this.world.random.nextInt(21) + 10;
          if (rand > itemStack.count) {
            rand = itemStack.count;
          }
          itemStack.count -= rand;
          EntityItem entityItem = new EntityItem(this.world, (double) ((float) this.x + fx), (double) ((float) this.y + fy), (double) ((float) this.z + fz), new ItemStack(itemStack.id, rand, itemStack.getData()));
          if (entityItem != null) {
            float f = 0.05F;
            entityItem.motX = (double) ((float) this.world.random.nextGaussian() * f);
            entityItem.motY = (double) ((float) this.world.random.nextGaussian() * f + 0.2F);
            entityItem.motZ = (double) ((float) this.world.random.nextGaussian() * f);
            if (entityItem.itemStack.getItem() instanceof ItemKleinStar) {
              ((ItemKleinStar) entityItem.itemStack.getItem()).setKleinPoints(entityItem.itemStack, ((ItemKleinStar) itemStack.getItem()).getKleinPoints(itemStack));
            }
            this.world.addEntity(entityItem);
          }
        }
      }
    }
  }

  public static boolean putInChest(TileEntity tileEntity, ItemStack itemStack) {
    if (itemStack != null && itemStack.id != 0) {
      if (tileEntity == null) {
        return false;
      }
      else {
        int itemSlot;
        ItemStack chestItemStack;
        if (tileEntity instanceof TileEntityChest) {
          for (itemSlot = 0; itemSlot < ((TileEntityChest) tileEntity).getSize(); ++itemSlot) {
            chestItemStack = ((TileEntityChest) tileEntity).getItem(itemSlot);
            if (chestItemStack != null && chestItemStack.doMaterialsMatch(itemStack) && chestItemStack.count + itemStack.count <= chestItemStack.getMaxStackSize()) {
              chestItemStack.count += itemStack.count;
              return true;
            }
          }
          for (itemSlot = 0; itemSlot < ((TileEntityChest) tileEntity).getSize(); ++itemSlot) {
            if (((TileEntityChest) tileEntity).getItem(itemSlot) == null) {
              ((TileEntityChest) tileEntity).setItem(itemSlot, itemStack);
              return true;
            }
          }
        }
        else if (tileEntity instanceof TileAlchChest) {
          for (itemSlot = 0; itemSlot < ((TileAlchChest) tileEntity).getSize(); ++itemSlot) {
            chestItemStack = ((TileAlchChest) tileEntity).getItem(itemSlot);
            if (chestItemStack != null && chestItemStack.doMaterialsMatch(itemStack) && chestItemStack.count + itemStack.count <= chestItemStack.getMaxStackSize() && chestItemStack.getData() == itemStack.getData()) {
              chestItemStack.count += itemStack.count;
              return true;
            }
          }
          for (itemSlot = 0; itemSlot < ((TileAlchChest) tileEntity).getSize(); ++itemSlot) {
            if (((TileAlchChest) tileEntity).getItem(itemSlot) == null) {
              ((TileAlchChest) tileEntity).setItem(itemSlot, itemStack);
              return true;
            }
          }
        }
        return false;
      }
    }
    else {
      return true;
    }
  }

  public boolean tryDropInChest(ItemStack itemStack) {
    TileEntity tileEntity = null;
    if (this.isChest(this.world.getTileEntity(this.x, this.y + 1, this.z))) {
      tileEntity = this.world.getTileEntity(this.x, this.y + 1, this.z);
      return putInChest(tileEntity, itemStack);
    }
    else if (this.isChest(this.world.getTileEntity(this.x, this.y - 1, this.z))) {
      tileEntity = this.world.getTileEntity(this.x, this.y - 1, this.z);
      return putInChest(tileEntity, itemStack);
    }
    else if (this.isChest(this.world.getTileEntity(this.x + 1, this.y, this.z))) {
      tileEntity = this.world.getTileEntity(this.x + 1, this.y, this.z);
      return putInChest(tileEntity, itemStack);
    }
    else if (this.isChest(this.world.getTileEntity(this.x - 1, this.y, this.z))) {
      tileEntity = this.world.getTileEntity(this.x - 1, this.y, this.z);
      return putInChest(tileEntity, itemStack);
    }
    else if (this.isChest(this.world.getTileEntity(this.x, this.y, this.z + 1))) {
      tileEntity = this.world.getTileEntity(this.x, this.y, this.z + 1);
      return putInChest(tileEntity, itemStack);
    }
    else if (this.isChest(this.world.getTileEntity(this.x, this.y, this.z - 1))) {
      tileEntity = this.world.getTileEntity(this.x, this.y, this.z - 1);
      return putInChest(tileEntity, itemStack);
    }
    else {
      return false;
    }
  }

  public void doCondense(ItemStack itemStack) {
    if (this.eternalDensity != -1) {
      int emc = 0;
      int itemSlot;
      for (itemSlot = 1; itemSlot < this.items.length; ++itemSlot) {
        if (this.items[itemSlot] != null && this.isValidMaterial(this.items[itemSlot]) && EEMaps.getEMC(this.items[itemSlot]) > emc) {
          emc = EEMaps.getEMC(this.items[itemSlot]);
        }
      }
      for (itemSlot = 1; itemSlot < this.items.length; ++itemSlot) {
        if (this.items[itemSlot] != null && this.isValidMaterial(this.items[itemSlot]) && EEMaps.getEMC(this.items[itemSlot]) < emc) {
          emc = EEMaps.getEMC(this.items[itemSlot]);
        }
      }
      if (emc < EEMaps.getEMC(EEItem.redMatter.id) && !this.AnalyzeTier(this.items[this.eternalDensity], EEMaps.getEMC(EEItem.redMatter.id)) && emc < EEMaps.getEMC(EEItem.darkMatter.id) && !this.AnalyzeTier(this.items[this.eternalDensity], EEMaps.getEMC(EEItem.darkMatter.id)) && emc < EEMaps.getEMC(Item.DIAMOND.id) && !this.AnalyzeTier(this.items[this.eternalDensity], EEMaps.getEMC(Item.DIAMOND.id)) && emc < EEMaps.getEMC(Item.GOLD_INGOT.id) && !this.AnalyzeTier(this.items[this.eternalDensity], EEMaps.getEMC(Item.GOLD_INGOT.id)) && emc < EEMaps.getEMC(Item.IRON_INGOT.id) && this.AnalyzeTier(this.items[this.eternalDensity], EEMaps.getEMC(Item.IRON_INGOT.id))) {
      }
    }
  }

  private boolean AnalyzeTier(ItemStack itemStack, int emc) {
    if (itemStack == null) {
      return false;
    }
    else {
      int itemEMC = 0;
      int itemSlot;
      for (itemSlot = 0; itemSlot < this.items.length; ++itemSlot) {
        if (this.items[itemSlot] != null && this.isValidMaterial(this.items[itemSlot]) && EEMaps.getEMC(this.items[itemSlot]) < emc) {
          itemEMC += EEMaps.getEMC(this.items[itemSlot]) * this.items[itemSlot].count;
        }
      }
      if (itemEMC + this.emc(itemStack) < emc) {
        return false;
      }
      else {
        itemSlot = 0;
        while (itemEMC + this.emc(itemStack) >= emc && itemSlot < 10) {
          ++itemSlot;
          this.ConsumeMaterialBelowTier(itemStack, emc);
        }
        if (this.emc(itemStack) >= emc && this.roomFor(this.getProduct(emc))) {
          this.PushStack(this.getProduct(emc));
          this.takeEMC(itemStack, emc);
        }
        return true;
      }
    }
  }

  private boolean roomFor(ItemStack itemStack) {
    if (itemStack == null) {
      return false;
    }
    else {
      for (int itemSlot = 1; itemSlot < this.items.length; ++itemSlot) {
        if (this.items[itemSlot] == null) {
          return true;
        }
        if (this.items[itemSlot].doMaterialsMatch(itemStack) && this.items[itemSlot].count <= itemStack.getMaxStackSize() - itemStack.count) {
          return true;
        }
      }
      return false;
    }
  }

  private ItemStack getProduct(int emc) {
    return emc == EEMaps.getEMC(Item.IRON_INGOT.id) ? new ItemStack(Item.IRON_INGOT, 1) : (emc == EEMaps.getEMC(Item.GOLD_INGOT.id) ? new ItemStack(Item.GOLD_INGOT, 1) : (emc == EEMaps.getEMC(Item.DIAMOND.id) ? new ItemStack(Item.DIAMOND, 1) : (emc == EEMaps.getEMC(EEItem.darkMatter.id) ? new ItemStack(EEItem.darkMatter, 1) : (emc == EEMaps.getEMC(EEItem.redMatter.id) ? new ItemStack(EEItem.redMatter, 1) : null))));
  }

  private void ConsumeMaterialBelowTier(ItemStack itemStack, int emc) {
    for (int itemSlot = 1; itemSlot < this.items.length; ++itemSlot) {
      if (this.items[itemSlot] != null && this.isValidMaterial(this.items[itemSlot]) && EEMaps.getEMC(this.items[itemSlot]) < emc) {
        this.addEMC(itemStack, EEMaps.getEMC(this.items[itemSlot]));
        --this.items[itemSlot].count;
        if (this.items[itemSlot].count == 0) {
          this.items[itemSlot] = null;
        }
        return;
      }
    }

  }

  private boolean isValidMaterial(ItemStack itemStack) {
    if (itemStack == null) {
      return false;
    }
    else if (EEMaps.getEMC(itemStack) == 0) {
      return false;
    }
    else if (itemStack.getItem() instanceof ItemKleinStar) {
      return false;
    }
    else {
      int var2 = itemStack.id;
      return var2 == EEItem.redMatter.id ? false : var2 >= Block.byId.length || !(Block.byId[var2] instanceof BlockContainer) || !Block.byId[var2].hasTileEntity(itemStack.getData());
    }
  }

  private int emc(ItemStack itemStack) {
    return !(itemStack.getItem() instanceof ItemEternalDensity) && !(itemStack.getItem() instanceof ItemVoidRing) ? 0 : (itemStack.getItem() instanceof ItemEternalDensity ? ((ItemEternalDensity) itemStack.getItem()).getInteger(itemStack, "emc") : ((ItemVoidRing) itemStack.getItem()).getInteger(itemStack, "emc"));
  }

  private void takeEMC(ItemStack itemStack, int emc) {
    if (itemStack.getItem() instanceof ItemEternalDensity || itemStack.getItem() instanceof ItemVoidRing) {
      if (itemStack.getItem() instanceof ItemEternalDensity) {
        ((ItemEternalDensity) itemStack.getItem()).setInteger(itemStack, "emc", this.emc(itemStack) - emc);
      }
      else {
        ((ItemVoidRing) itemStack.getItem()).setInteger(itemStack, "emc", this.emc(itemStack) - emc);
      }
    }

  }

  private void addEMC(ItemStack itemStack, int emc) {
    if (itemStack.getItem() instanceof ItemEternalDensity || itemStack.getItem() instanceof ItemVoidRing) {
      if (itemStack.getItem() instanceof ItemEternalDensity) {
        ((ItemEternalDensity) itemStack.getItem()).setInteger(itemStack, "emc", this.emc(itemStack) + emc);
      }
      else {
        ((ItemVoidRing) itemStack.getItem()).setInteger(itemStack, "emc", this.emc(itemStack) + emc);
      }
    }

  }

  public ItemStack target() {
    return this.items[0];
  }

  public int getTargetValue(ItemStack ItemStack) {
    return ItemStack == null ? 0 : (EEMaps.getEMC(ItemStack.id, ItemStack.getData()) == 0 ? (ItemStack.d() ? EEMaps.getEMC(ItemStack.id) * (int) (((float) ItemStack.i() - (float) ItemStack.getData()) / (float) ItemStack.i()) : EEMaps.getEMC(ItemStack.id)) : EEMaps.getEMC(ItemStack.id, ItemStack.getData()));
  }

  public boolean canCondense() {
    return this.target() == null ? false : (this.getTargetValue(this.target()) == 0 ? false : !this.isInventoryFull() || this.roomFor(this.target()));
  }

  public boolean isInventoryFull() {
    for (int itemStack = 0; itemStack < this.items.length; ++itemStack) {
      if (this.items[itemStack] == null) {
        return false;
      }
    }

    return true;
  }

  public boolean receiveEnergy(int emc, byte var1, boolean var2) { //2019-04-12 - Meaning of var1 and var2 are unknown.
    if (this.canCondense() && this.scaledEnergy + emc <= 800000000) {
      if (var2) {
        this.scaledEnergy += emc;
      }
      return true;
    }
    else {
      return false;
    }
  }

  public boolean sendEnergy(int emc, byte var1, boolean var2) { //2019-04-12 - Meaning of var1 and var2 are unknown.
    return false;
  }

  public boolean passEnergy(int emc, byte var1, boolean var2) { //2019-04-12 - Meaning of var1 and var2 are unknown.
    return false;
  }

  public void sendAllPackets(int var1) { //2019-04-12 - Meaning of var1 is unknown.
  }

  public int relayBonus() {
    return 0;
  }

  public int getSize() {
    return this.items.length;
  }

  public int getMaxStackSize() {
    return 64;
  }

  public boolean addItem(ItemStack itemStack, boolean var2, Orientations orientation) {
    switch (orientation.ordinal()) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
        if (itemStack != null) {
          for (int itemSlot = 1; itemSlot < this.items.length; ++itemSlot) {
            if (this.items[itemSlot] == null) {
              if (var2) {
                for (this.items[itemSlot] = itemStack.cloneItemStack(); itemStack.count > 0; --itemStack.count) {
                }
              }
              return true;
            }
            if (this.items[itemSlot].doMaterialsMatch(itemStack) && this.items[itemSlot].count < this.items[itemSlot].getMaxStackSize()) {
              if (!var2) {
                return true;
              }
              while (this.items[itemSlot].count < this.items[itemSlot].getMaxStackSize() && itemStack.count > 0) {
                ++this.items[itemSlot].count;
                --itemStack.count;
              }
              if (itemStack.count == 0) {
                return true;
              }
            }
          }
        }
    }
    return false;
  }

  public ItemStack extractItem(boolean var1, Orientations orientation) {
    switch (orientation.ordinal()) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
        for (int var3 = 1; var3 < this.items.length; ++var3) {
          if (this.items[var3] != null && (this.target() == null || this.items[var3].doMaterialsMatch(this.target()))) {
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
    }

    return null;
  }

  public String getName() {
    return "Condenser";
  }

  public void a(NBTTagCompound nbtTagCompound) {
    super.a(nbtTagCompound);
    NBTTagList nbtTagList = nbtTagCompound.getList("Items");
    this.items = new ItemStack[this.getSize()];
    for (int nbtSlot = 0; nbtSlot < nbtTagList.size(); ++nbtSlot) {
      NBTTagCompound nbtTagCompound2 = (NBTTagCompound) nbtTagList.get(nbtSlot);
      byte slot = nbtTagCompound2.getByte("Slot");
      if (slot >= 0 && slot < this.items.length) {
        this.items[slot] = ItemStack.a(nbtTagCompound2);
      }
    }
    this.scaledEnergy = nbtTagCompound.getInt("scaledEnergy");
    this.eternalDensity = nbtTagCompound.getShort("eternalDensity");
  }

  public void b(NBTTagCompound nbtTagCompound) {
    super.b(nbtTagCompound);
    nbtTagCompound.setInt("scaledEnergy", this.scaledEnergy);
    nbtTagCompound.setShort("eternalDensity", (short) this.eternalDensity);
    NBTTagList nbtTagList = new NBTTagList();
    for (int itemSlot = 0; itemSlot < this.items.length; ++itemSlot) {
      if (this.items[itemSlot] != null) {
        NBTTagCompound nbtTagCompound2 = new NBTTagCompound();
        nbtTagCompound2.setByte("Slot", (byte) itemSlot);
        this.items[itemSlot].save(nbtTagCompound2);
        nbtTagList.add(nbtTagCompound2);
      }
    }
    nbtTagCompound.set("Items", nbtTagList);
  }

  public ItemStack getItem(int itemSlot) {
    return this.items[itemSlot];
  }

  public ItemStack splitStack(int itemSlot, int stackSize) {
    if (this.items[itemSlot] != null) {
      ItemStack itemStack;
      if (this.items[itemSlot].count <= stackSize) {
        itemStack = this.items[itemSlot];
        this.items[itemSlot] = null;
        return itemStack;
      }
      else {
        itemStack = this.items[itemSlot].a(stackSize);
        if (this.items[itemSlot].count == 0) {
          this.items[itemSlot] = null;
        }
        return itemStack;
      }
    }
    else {
      return null;
    }
  }

  public void setItem(int itemSlot, ItemStack itemStack) {
    this.items[itemSlot] = itemStack;
    if (itemStack != null && itemStack.count > this.getMaxStackSize()) {
      itemStack.count = this.getMaxStackSize();
    }

  }

  public void update() {
    super.update();
    boolean condense = false;
    boolean attract = false;
    for (int itemSlot = 0; itemSlot < this.getSize(); ++itemSlot) {
      if (this.items[itemSlot] != null) {
        if (this.items[itemSlot].getItem() instanceof ItemVoidRing) { // TODO FIX
          this.eternalDensity = itemSlot;
          if ((this.items[itemSlot].getData() & 1) == 0) {
            this.items[itemSlot].setData(this.items[itemSlot].getData() + 1);
            ((ItemEECharged) this.items[itemSlot].getItem()).setBoolean(this.items[itemSlot], "active", true);
          }
          condense = true;
          attract = true;
        }
        if (this.items[itemSlot].getItem().id == EEItem.eternalDensity.id) {
          this.eternalDensity = itemSlot;
          if ((this.items[itemSlot].getData() & 1) == 0) {
            this.items[itemSlot].setData(this.items[itemSlot].getData() + 1);
            ((ItemEECharged) this.items[itemSlot].getItem()).setBoolean(this.items[itemSlot], "active", true);
          }
          condense = true;
        }
        if (this.items[itemSlot].getItem() instanceof ItemAttractionRing) { // TODO FIX
          if ((this.items[itemSlot].getData() & 1) == 0) {
            this.items[itemSlot].setData(this.items[itemSlot].getData() + 1);
            ((ItemEECharged) this.items[itemSlot].getItem()).setBoolean(this.items[itemSlot], "active", true);
          }
          attract = true;
        }
      }
    }
    if (condense != this.condenseOn) {
      this.condenseOn = condense;
    }
    if (attract != this.attractionOn) {
      this.attractionOn = attract;
    }
  }

  public int getCondenserProgressScaled(int var1) { //2019-04-12 - Unsure of meaning of var1.
    return this.getTargetValue(this.target()) == 0 ? 0 : (this.scaledEnergy / 80 > this.getTargetValue(this.target()) ? var1 : this.scaledEnergy / 80 * var1 / this.getTargetValue(this.target()));
  }

  public boolean isValidTarget() {
    return EEMaps.getEMC(this.items[0].id, this.items[0].getData()) != 0 ? true : EEMaps.getEMC(this.items[0].id) == 0;
  }

  public void q_() {
    this.currentItemProgress = this.getCondenserProgressScaled(102);
    this.displayEnergy = this.latentEnergy();
    if (!this.initialized) {
      this.initialized = true;
      this.update();
    }
    if (++this.ticksSinceSync % 20 * 4 == 0) {
      this.world.playNote(this.x, this.y, this.z, 1, this.numUsingPlayers);
    }
    this.prevLidAngle = this.lidAngle;
    float lidf1 = 0.1F;
    double z;
    if (this.numUsingPlayers > 0 && this.lidAngle == 0.0F) { //Open
      double x = (double) this.x + 0.5D;
      z = (double) this.z + 0.5D;
      this.world.makeSound(x, (double) this.y + 0.5D, z, "random.chestopen", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }
    if (this.numUsingPlayers == 0 && this.lidAngle > 0.0F || this.numUsingPlayers > 0 && this.lidAngle < 1.0F) { //Close
      float var8 = this.lidAngle;
      if (this.numUsingPlayers > 0) {
        this.lidAngle += lidf1;
      }
      else {
        this.lidAngle -= lidf1;
      }
      if (this.lidAngle > 1.0F) {
        this.lidAngle = 1.0F;
      }
      float lidf2 = 0.5F;
      if (this.lidAngle < lidf2 && var8 >= lidf2) {
        z = (double) this.x + 0.5D;
        double z2 = (double) this.z + 0.5D;
        this.world.makeSound(z, (double) this.y + 0.5D, z2, "random.chestclosed", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
      }
      if (this.lidAngle < 0.0F) {
        this.lidAngle = 0.0F;
      }
    }
    if (this.canCondense()) {
      while (this.scaledEnergy >= this.getTargetValue(this.target()) * 80 && this.roomFor(new ItemStack(this.target().id, 1, this.target().getData()))) {
        this.scaledEnergy -= this.getTargetValue(this.target()) * 80;
        this.PushStack(new ItemStack(this.target().id, 1, this.target().getData()));
      }
      for (int itemSlot = 1; itemSlot < this.items.length; ++itemSlot) {
        if (this.items[itemSlot] != null && EEMaps.getEMC(this.items[itemSlot]) != 0 && !this.items[itemSlot].doMaterialsMatch(this.target()) && !(this.items[itemSlot].getItem() instanceof ItemKleinStar) && this.scaledEnergy + EEMaps.getEMC(this.items[itemSlot]) * 80 <= 800000000) {
          this.scaledEnergy += EEMaps.getEMC(this.items[itemSlot]) * 80;
          --this.items[itemSlot].count;
          if (this.items[itemSlot].count == 0) {
            this.items[itemSlot] = null;
          }
          break;
        }
      }
    }
    if (this.condenseOn && this.eternalDensity >= 0) {
      this.doCondense(this.items[this.eternalDensity]);
    }
    if (this.attractionOn) {
      this.doAttraction();
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

  public boolean PushStack(EntityItem item) {
    if (item == null) {
      return false;
    }
    else if (item.itemStack == null) {
      item.die();
      return false;
    }
    else if (item.itemStack.count < 1) {
      item.die();
      return false;
    }
    else {
      for (int itemSlot = 1; itemSlot < this.items.length; ++itemSlot) {
        if (this.items[itemSlot] == null) {
          this.items[itemSlot] = item.itemStack.cloneItemStack();
          for (this.items[itemSlot].count = 0; item.itemStack.count > 0 && this.items[itemSlot].count < this.items[itemSlot].getMaxStackSize(); --item.itemStack.count) {
            ++this.items[itemSlot].count;
          }
          item.die();
          return true;
        }
        if (this.items[itemSlot].doMaterialsMatch(item.itemStack) && this.items[itemSlot].count <= item.itemStack.getMaxStackSize() - item.itemStack.count) {
          while (item.itemStack.count > 0 && this.items[itemSlot].count < this.items[itemSlot].getMaxStackSize()) {
            ++this.items[itemSlot].count;
            --item.itemStack.count;
          }
          item.die();
          return true;
        }
      }
      return false;
    }
  }

  public boolean PushStack(ItemStack pushedItem) {
    if (pushedItem == null) {
      return false;
    }
    else {
      for (int itemSlot = 1; itemSlot < this.items.length; ++itemSlot) {
        if (this.items[itemSlot] == null) {
          this.items[itemSlot] = pushedItem.cloneItemStack();
          pushedItem = null;
          return true;
        }
        if (this.items[itemSlot].doMaterialsMatch(pushedItem) && this.items[itemSlot].count <= pushedItem.getMaxStackSize() - pushedItem.count) {
          ItemStack itemStack = this.items[itemSlot];
          itemStack.count += pushedItem.count;
          pushedItem = null;
          return true;
        }
      }
      return false;
    }
  }

  private void PushDenseStacks(EntityLootBall lootBall) {
    for (int itemSlot = 1; itemSlot < lootBall.items.length; ++itemSlot) {
      if (lootBall.items[itemSlot] != null && this.PushStack(lootBall.items[itemSlot])) {
        lootBall.items[itemSlot] = null;
      }
    }

  }

  private void GrabItems(Entity items) {
    if (items != null && items instanceof EntityItem) {
      ItemStack item = ((EntityItem) items).itemStack;
      if (item == null) {
        items.die();
        return;
      }
      if (item.getItem() instanceof ItemLootBall) {
        ItemLootBall itemLootBall = (ItemLootBall) item.getItem();
        ItemStack[] lootBallDroplist = itemLootBall.getDroplist(item);
        ItemStack[] lootBallDropList2 = lootBallDroplist;
        int lootBallDropListSize = lootBallDroplist.length;

        for (int itemSlot = 0; itemSlot < lootBallDropListSize; ++itemSlot) {
          ItemStack itemStack = lootBallDropList2[itemSlot];
          this.PushStack(itemStack);
        }
        items.die();
      }
      else {
        this.PushStack(item);
        items.die();
      }
    }
    else if (items != null && items instanceof EntityLootBall) {
      if (((EntityLootBall) items).items == null) {
        items.die();
      }
      //ItemStack[] itemStacks = ((EntityLootBall)items).items; //2019-04-12 - Unused code.
      this.PushDenseStacks((EntityLootBall) items);
      if (((EntityLootBall) items).isEmpty()) {
        items.die();
      }
    }
  }

  private void PullItems(Entity item) {
    if (item instanceof EntityItem || item instanceof EntityLootBall) {
      if (item instanceof EntityLootBall) {
        ((EntityLootBall) item).setBeingPulled(true);
      }
      double x = (double) this.x + 0.5D - item.locX;
      double y = (double) this.y + 0.5D - item.locY;
      double z = (double) this.z + 0.5D - item.locZ;
      double distance = x * x + y * y + z * z;
      distance *= distance;
      if (distance <= Math.pow(6.0D, 4.0D)) {
        double attractX = x * 0.019999999552965164D / distance * Math.pow(6.0D, 3.0D);
        double attractY = y * 0.019999999552965164D / distance * Math.pow(6.0D, 3.0D);
        double attracyZ = z * 0.019999999552965164D / distance * Math.pow(6.0D, 3.0D);
        if (attractX > 0.1D) {
          attractX = 0.1D;
        }
        else if (attractX < -0.1D) {
          attractX = -0.1D;
        }
        if (attractY > 0.1D) {
          attractY = 0.1D;
        }
        else if (attractY < -0.1D) {
          attractY = -0.1D;
        }
        if (attracyZ > 0.1D) {
          attracyZ = 0.1D;
        }
        else if (attracyZ < -0.1D) {
          attracyZ = -0.1D;
        }
        item.motX += attractX * 1.2D;
        item.motY += attractY * 1.2D;
        item.motZ += attracyZ * 1.2D;
      }
    }
  }

  public int latentEnergy() {
    return this.scaledEnergy / 80;
  }

  public void b(int var1, int numUsingPlayers) { //2019-04-12 - Meaning of var1 is unknown.
    if (var1 == 1) {
      this.numUsingPlayers = numUsingPlayers;
    }
  }

  public void f() { //Open
    ++this.numUsingPlayers;
    this.world.playNote(this.x, this.y, this.z, 1, this.numUsingPlayers);
  }

  public void g() { //Close
    --this.numUsingPlayers;
    this.world.playNote(this.x, this.y, this.z, 1, this.numUsingPlayers);
  }

  public boolean a(EntityHuman var1) {
    return this.world.getTileEntity(this.x, this.y, this.z) != this ? false : var1.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
  }

  public int getStartInventorySide(int var1) { //2019-04-12 - Meaning of var1 is unknown.
    return 1;
  }

  public int getSizeInventorySide(int var1) { //2019-04-12 - Meaning of var1 is unknown.
    return this.items.length - 1;
  }

  public boolean onBlockActivated(EntityHuman player) {
    if (!this.world.isStatic) {
      player.openGui(mod_EE.getInstance(), GuiIds.CONDENSER, this.world, this.x, this.y, this.z);
    }

    return true;
  }

  public int getTextureForSide(int side) {
    if (side != 1 && side != 0) {
      byte var2 = this.direction;
      return side != var2 ? EEBase.condenserSide : EEBase.condenserFront;
    }
    else {
      return EEBase.condenserTop;
    }
  }

  public int getInventoryTexture(int side) {
    return side != 1 && side != 0 ? (side == 3 ? EEBase.condenserFront : EEBase.condenserSide) : EEBase.condenserTop;
  }

  public int getLightValue() {
    return 10;
  }

  public void onNeighborBlockChange(int var1) { //2019-04-12 - Meaning of var1 is unknown.
  }

  public void randomDisplayTick(Random rand) { //2019-04-12 - Meaning of var1 is unknown.
  }

  public ItemStack splitWithoutUpdate(int var1) { //2019-04-12 - Meaning of var1 is unknown.
    return null;
  }

  public ItemStack[] getContents() {
    return this.items;
  }

  public void setMaxStackSize(int size) {
  }
}