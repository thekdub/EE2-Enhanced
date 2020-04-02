package ee;

import ee.item.ItemLootBall;
import net.minecraft.server.*;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AlchemyBagData extends WorldMapBase implements IInventory {
  public boolean voidOn;
  public boolean repairOn;
  public boolean markForUpdate;
  public boolean condenseOn;
  public int repairTimer = 0;
  public int condenseCheckTimer = 0;
  public static final String prefix = "bag";
  public static final String prefix_ = "bag_";
  public ItemStack[] items = new ItemStack[113];
  private int eternalDensity;
  private boolean initialized;
  public static List datas = new LinkedList();

  public AlchemyBagData(String var1) {
    super(var1);
    datas.add(this);
  }

  public void onUpdate(World world, EntityHuman player) {
    if (!this.initialized) {
      this.initialized = true;
      this.update();
    }
    if (this.repairOn) {
      this.doRepair();
    }
    if (this.condenseOn) {
      this.doCondense(this.items[this.eternalDensity]);
    }
    if (this.voidOn) {
      boolean voidOn = false;
      for (int color = 0; color <= 15; ++color) {
        boolean bagMissing = true;
        ItemStack[] itemStacks = player.inventory.items;
        int itemCount = itemStacks.length;
        for (int i = 0; i < itemCount; ++i) {
          ItemStack item = itemStacks[i];
          if (item != null && item.doMaterialsMatch(new ItemStack(EEItem.alchemyBag, 1, color))) {
            bagMissing = false;
          }
        }
        if (!bagMissing) {
          String bagName = "bag_" + player.name + "_" + color; // Added underscore between player.name and color to fix format issue.
          AlchemyBagData bagData = (AlchemyBagData) world.a(AlchemyBagData.class, bagName);
          if (bagData != null) {
            if (voidOn) {
              break;
            }
            if (bagData.voidOn) {
              voidOn = true;
            }
            if (bagData == this && voidOn) {
              this.doAttraction(player);
              break;
            }
          }
        }
      }
    }
    if (this.markForUpdate) {
      this.a();
    }
  }

  public int getSize() {
    return 104;
  }

  public ItemStack getItem(int itemSlot) {
    return this.items[itemSlot];
  }

  public ItemStack splitStack(int itemSlot, int stackSize) {
    if (this.items[itemSlot] != null) {
      ItemStack item;
      if (this.items[itemSlot].count <= stackSize) {
        item = this.items[itemSlot];
        this.items[itemSlot] = null;
        this.update();
        return item;
      }
      else {
        item = this.items[itemSlot].a(stackSize);
        if (this.items[itemSlot].count == 0) {
          this.items[itemSlot] = null;
        }
        this.update();
        return item;
      }
    }
    else {
      return null;
    }
  }

  public void setItem(int itemSlot, ItemStack item) {
    this.items[itemSlot] = item;
    if (item != null && item.count > this.getMaxStackSize()) {
      item.count = this.getMaxStackSize();
    }

    this.update();
  }

  public String getName() {
    return "Bag";
  }

  public int getMaxStackSize() {
    return 64;
  }

  public void update() {
    this.markForUpdate = true;
    boolean repair = false;
    boolean condense = false;
    boolean attract = false;

    for (int itemSlot = 0; itemSlot < this.items.length; ++itemSlot) {
      if (this.items[itemSlot] != null) {
        if (this.items[itemSlot].getItem() == EEItem.repairCharm) {
          repair = true;
        }
        if (this.items[itemSlot].getItem() == EEItem.voidRing) {
          this.eternalDensity = itemSlot;
          if ((this.items[itemSlot].getData() & 1) == 0) {
            this.items[itemSlot].setData(this.items[itemSlot].getData() + 1);
            ((ItemEECharged) this.items[itemSlot].getItem()).setBoolean(this.items[itemSlot], "active", true);
          }
          attract = true;
          condense = true;
        }
        if (this.items[itemSlot].getItem() == EEItem.eternalDensity) {
          this.eternalDensity = itemSlot;
          if ((this.items[itemSlot].getData() & 1) == 0) {
            this.items[itemSlot].setData(this.items[itemSlot].getData() + 1);
            ((ItemEECharged) this.items[itemSlot].getItem()).setBoolean(this.items[itemSlot], "active", true);
          }
          condense = true;
        }
        if (this.items[itemSlot].getItem() == EEItem.attractionRing) {
          attract = true;
          if ((this.items[itemSlot].getData() & 1) == 0) {
            this.items[itemSlot].setData(this.items[itemSlot].getData() + 1);
            ((ItemEECharged) this.items[itemSlot].getItem()).setBoolean(this.items[itemSlot], "active", true);
          }
        }
      }
    }
    if (repair != this.repairOn) {
      this.repairOn = repair;
    }
    if (condense != this.condenseOn) {
      this.condenseOn = condense;
    }
    if (attract != this.voidOn) {
      this.voidOn = attract;
    }
  }

  public void doRepair() {
    if (this.repairTimer >= 20) {
      ItemStack item = null;
      boolean damaged = false;

      for (int i = 0; i < this.getSize(); ++i) {
        damaged = false;
        item = this.items[i];
        if (item != null) {
          for (int a = 0; a < EEMaps.chargedItems.size(); ++a) {
            if ((Integer) EEMaps.chargedItems.get(a) == item.id) {
              damaged = true;
              break;
            }
          }

          if (!damaged && item.getData() >= 1 && item.d()) {
            item.setData(item.getData() - 1);
          }
        }
      }
      this.repairTimer = 0;
    }
    ++this.repairTimer;
    this.markForUpdate = true;
  }

  public void doCondense(ItemStack item) {
    if (this.eternalDensity != -1) {
      int emc = 0;
      for (int itemSlot = 0; itemSlot < this.items.length; ++itemSlot) {
        if (this.items[itemSlot] != null && this.isValidMaterial(this.items[itemSlot]) && EEMaps.getEMC(this.items[itemSlot]) > emc) {
          emc = EEMaps.getEMC(this.items[itemSlot]);
        }
      }

      for (int itemSlot = 0; itemSlot < this.items.length; ++itemSlot) {
        if (this.items[itemSlot] != null && this.isValidMaterial(this.items[itemSlot]) && EEMaps.getEMC(this.items[itemSlot]) < emc) {
          emc = EEMaps.getEMC(this.items[itemSlot]);
        }
      }

      if (emc < EEMaps.getEMC(EEItem.redMatter.id) && !this.AnalyzeTier(this.items[this.eternalDensity],
            EEMaps.getEMC(EEItem.redMatter.id)) && emc < EEMaps.getEMC(EEItem.darkMatter.id) && !this.AnalyzeTier(this.items[this.eternalDensity],
            EEMaps.getEMC(EEItem.darkMatter.id)) && emc < EEMaps.getEMC(Item.DIAMOND.id) && !this.AnalyzeTier(this.items[this.eternalDensity],
            EEMaps.getEMC(Item.DIAMOND.id)) && emc < EEMaps.getEMC(Item.GOLD_INGOT.id) && !this.AnalyzeTier(this.items[this.eternalDensity],
            EEMaps.getEMC(Item.GOLD_INGOT.id)) && emc < EEMaps.getEMC(Item.IRON_INGOT.id) && this.AnalyzeTier(this.items[this.eternalDensity],
            EEMaps.getEMC(Item.IRON_INGOT.id))) {
      }
    }

  }

  private boolean AnalyzeTier(ItemStack item, int itemTierEMC) {
    if (item == null) {
      return false;
    }
    else {
      int emc = 0;
      int itemSlot;
      for (itemSlot = 0; itemSlot < this.items.length; ++itemSlot) {
        if (this.items[itemSlot] != null && this.isValidMaterial(this.items[itemSlot]) && EEMaps.getEMC(this.items[itemSlot]) < itemTierEMC) {
          emc += EEMaps.getEMC(this.items[itemSlot]) * this.items[itemSlot].count;
        }
      }
      if (emc + this.emc(item) < itemTierEMC) {
        return false;
      }
      else {
        itemSlot = 0;
        while (emc + this.emc(item) >= itemTierEMC && itemSlot < 10) {
          ++itemSlot;
          this.ConsumeMaterialBelowTier(item, itemTierEMC);
        }
        if (this.emc(item) >= itemTierEMC && this.roomFor(this.getProduct(itemTierEMC))) {
          this.PushStack(this.getProduct(itemTierEMC));
          this.takeEMC(item, itemTierEMC);
        }

        return true;
      }
    }
  }

  private boolean roomFor(ItemStack item) {
    if (item == null) {
      return false;
    }
    else {
      for (int itemSlot = 0; itemSlot < this.items.length; ++itemSlot) {
        if (this.items[itemSlot] == null) {
          return true;
        }
        if (this.items[itemSlot].doMaterialsMatch(item) && this.items[itemSlot].count <= item.getMaxStackSize() - item.count) {
          return true;
        }
      }
      return false;
    }
  }

  private ItemStack getProduct(int itemTierEMC) {
    return itemTierEMC == EEMaps.getEMC(Item.IRON_INGOT.id) ? new ItemStack(Item.IRON_INGOT, 1) :
          (itemTierEMC == EEMaps.getEMC(Item.GOLD_INGOT.id) ? new ItemStack(Item.GOLD_INGOT, 1) :
                (itemTierEMC == EEMaps.getEMC(Item.DIAMOND.id) ? new ItemStack(Item.DIAMOND, 1) :
                      (itemTierEMC == EEMaps.getEMC(EEItem.darkMatter.id) ? new ItemStack(EEItem.darkMatter, 1) :
                            (itemTierEMC == EEMaps.getEMC(EEItem.redMatter.id) ? new ItemStack(EEItem.redMatter, 1) : null))));
  }

  public boolean PushStack(ItemStack item) {
    if (item == null) {
      return true;
    }
    else {
      for (int itemSlot = 0; itemSlot < this.items.length; ++itemSlot) {
        if (this.items[itemSlot] == null) {
          this.items[itemSlot] = item.cloneItemStack();
          item = null;
          return true;
        }

        if (this.items[itemSlot].doMaterialsMatch(item) && this.items[itemSlot].count <= item.getMaxStackSize() - item.count) {
          ItemStack item2 = this.items[itemSlot];
          item2.count += item.count;
          item = null;
          return true;
        }
      }
      return false;
    }
  }

  private void ConsumeMaterialBelowTier(ItemStack item, int itemTierEMC) {
    for (int var3 = 0; var3 < this.items.length; ++var3) {
      if (this.items[var3] != null && this.isValidMaterial(this.items[var3]) && EEMaps.getEMC(this.items[var3]) < itemTierEMC) {
        this.addEMC(item, EEMaps.getEMC(this.items[var3]));
        --this.items[var3].count;
        if (this.items[var3].count == 0) {
          this.items[var3] = null;
        }
        return;
      }
    }
  }

  private boolean isValidMaterial(ItemStack item) {
    if (item == null) {
      return false;
    }
    else if (EEMaps.getEMC(item) == 0) {
      return false;
    }
    else if (item.getItem() instanceof ItemKleinStar) {
      return false;
    }
    else {
      int itemID = item.id;
      return itemID != EEItem.redMatter.id && (itemID >= Block.byId.length || !(Block.byId[itemID] instanceof BlockContainer) || !Block.byId[itemID].hasTileEntity(item.getData()));
    }
  }

  private int emc(ItemStack item) {
    return !(item.getItem() instanceof ItemEternalDensity) && !(item.getItem() instanceof ItemVoidRing) ? 0 : (item.getItem() instanceof ItemEternalDensity ? ((ItemEternalDensity) item.getItem()).getInteger(item, "emc") : ((ItemVoidRing) item.getItem()).getInteger(item, "emc"));
  }

  private void takeEMC(ItemStack item, int emc) {
    if (item.getItem() instanceof ItemEternalDensity || item.getItem() instanceof ItemVoidRing) {
      if (item.getItem() instanceof ItemEternalDensity) {
        ((ItemEternalDensity) item.getItem()).setInteger(item, "emc", this.emc(item) - emc);
      }
      else {
        ((ItemVoidRing) item.getItem()).setInteger(item, "emc", this.emc(item) - emc);
      }
    }
  }

  private void addEMC(ItemStack item, int emc) {
    if (item.getItem() instanceof ItemEternalDensity || item.getItem() instanceof ItemVoidRing) {
      if (item.getItem() instanceof ItemEternalDensity) {
        ((ItemEternalDensity) item.getItem()).setInteger(item, "emc", this.emc(item) + emc);
      }
      else {
        ((ItemVoidRing) item.getItem()).setInteger(item, "emc", this.emc(item) + emc);
      }
    }
  }

  public void doAttraction(EntityHuman player) { // 2020-04-01 - Dead Entity marking added. Patches attraction ring duplication.
    List nearbyItems = player.world.a(EntityItem.class, AxisAlignedBB.b(EEBase.playerX(player) - 10.0D, EEBase.playerY(player) - 10.0D, EEBase.playerZ(player) - 10.0D, EEBase.playerX(player) + 10.0D, EEBase.playerY(player) + 10.0D, EEBase.playerZ(player) + 10.0D));
    Iterator itemIterator = nearbyItems.iterator();
    while (itemIterator.hasNext()) {
      Entity item = (Entity) itemIterator.next();
      this.PullItems(item, player);
    }
    List nearbyItems2 = player.world.a(EntityItem.class, AxisAlignedBB.b(EEBase.playerX(player) - 0.55D, EEBase.playerY(player) - 0.55D, EEBase.playerZ(player) - 0.55D, EEBase.playerX(player) + 0.55D, EEBase.playerY(player) + 0.55D, EEBase.playerZ(player) + 0.55D));
    Iterator itemIterator2 = nearbyItems2.iterator();
    while (itemIterator2.hasNext()) {
      Entity item = (Entity) itemIterator2.next();
      if (!item.dead) { // Checks if the item has been marked dead by another condenser.
        this.GrabItems(item);
        item.dead = true; // Marks the item dead so other condensers won't grab it
      }
    }
    List nearbyLootBalls = player.world.a(EntityLootBall.class, AxisAlignedBB.b(EEBase.playerX(player) - 10.0D, EEBase.playerY(player) - 10.0D, EEBase.playerZ(player) - 10.0D, EEBase.playerX(player) + 10.0D, EEBase.playerY(player) + 10.0D, EEBase.playerZ(player) + 10.0D));
    Iterator lootBallIterator = nearbyLootBalls.iterator();
    while (lootBallIterator.hasNext()) {
      Entity lootBall = (Entity) lootBallIterator.next();
      this.PullItems(lootBall, player);
    }
    List nearbyLootBalls2 = player.world.a(EntityLootBall.class, AxisAlignedBB.b(EEBase.playerX(player) - 0.55D, EEBase.playerY(player) - 0.55D, EEBase.playerZ(player) - 0.55D, EEBase.playerX(player) + 0.55D, EEBase.playerY(player) + 0.55D, EEBase.playerZ(player) + 0.55D));
    Iterator lootBallIterator2 = nearbyLootBalls2.iterator();
    while (lootBallIterator2.hasNext()) {
      Entity lootBall = (Entity) lootBallIterator2.next();
      if (!lootBall.dead) { // Checks if the lootBall has been marked dead by another condenser.
        this.GrabItems(lootBall);
        lootBall.dead = true; // Marks the lootBall dead so other condensers won't grab it
      }
    }
    List nearbyExperience = player.world.a(EntityExperienceOrb.class, AxisAlignedBB.b(EEBase.playerX(player) - 10.0D, EEBase.playerY(player) - 10.0D, EEBase.playerZ(player) - 10.0D, EEBase.playerX(player) + 10.0D, EEBase.playerY(player) + 10.0D, EEBase.playerZ(player) + 10.0D));
    Iterator experienceIterator = nearbyExperience.iterator();
    while (experienceIterator.hasNext()) {
      Entity experience = (Entity) experienceIterator.next();
      this.PullItems(experience, player);
    }
  }

  private void PullItems(Entity entity, EntityHuman player) {
    if (entity instanceof EntityItem || entity instanceof EntityLootBall) {
      if (entity instanceof EntityLootBall) {
        ((EntityLootBall) entity).setBeingPulled(true);
      }
      double x = EEBase.playerX(player) + 0.5D - entity.locX;
      double y = EEBase.playerY(player) + 0.5D - entity.locY;
      double z = EEBase.playerZ(player) + 0.5D - entity.locZ;
      double distanceSquared = x * x + y * y + z * z;
      distanceSquared *= distanceSquared;
      if (distanceSquared <= Math.pow(6.0D, 4.0D)) {
        double x2 = x * 0.019999999552965164D / distanceSquared * Math.pow(6.0D, 3.0D);
        double y2 = y * 0.019999999552965164D / distanceSquared * Math.pow(6.0D, 3.0D);
        double z2 = z * 0.019999999552965164D / distanceSquared * Math.pow(6.0D, 3.0D);
        if (x2 > 0.1D) {
          x2 = 0.1D;
        }
        else if (x2 < -0.1D) {
          x2 = -0.1D;
        }
        if (y2 > 0.1D) {
          y2 = 0.1D;
        }
        else if (y2 < -0.1D) {
          y2 = -0.1D;
        }
        if (z2 > 0.1D) {
          z2 = 0.1D;
        }
        else if (z2 < -0.1D) {
          z2 = -0.1D;
        }
        entity.motX += x2 * 1.2D;
        entity.motY += y2 * 1.2D;
        entity.motZ += z2 * 1.2D;
      }
    }
  }

  private void GrabItems(Entity entity) {
    if (entity != null && entity instanceof EntityItem) {
      ItemStack item = ((EntityItem) entity).itemStack;
      if (item == null) {
        entity.die();
        return;
      }
      if (item.getItem() instanceof ItemLootBall) {
        ItemLootBall var3 = (ItemLootBall) item.getItem();
        ItemStack[] var4 = var3.getDroplist(item);
        ItemStack[] var5 = var4;
        int var6 = var4.length;
        for (int var7 = 0; var7 < var6; ++var7) {
          ItemStack var8 = var5[var7];
          this.PushStack(var8);
        }
        entity.die();
      }
      else {
        this.PushStack(item);
        entity.die();
      }
    }
    else if (entity != null && entity instanceof EntityLootBall) {
      if (((EntityLootBall) entity).items == null) {
        entity.die();
      }
      //ItemStack[] lootballItems = ((EntityLootBall)entity).items; //2019-04-11 - Commented: Unused
      this.PushDenseStacks((EntityLootBall) entity);
      if (((EntityLootBall) entity).isEmpty()) {
        entity.die();
      }
    }
  }

  private void PushDenseStacks(EntityLootBall lootBall) {
    for (int itemSlot = 0; itemSlot < lootBall.items.length; ++itemSlot) {
      if (lootBall.items[itemSlot] != null && this.PushStack(lootBall.items[itemSlot])) {
        lootBall.items[itemSlot] = null;
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
      for (int itemSlot = 0; itemSlot < this.items.length; ++itemSlot) {
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

  private void PushDenseStacks(EntityLootBall lootBall, EntityHuman player) {
    for (int itemSlot = 0; itemSlot < lootBall.items.length; ++itemSlot) {
      if (lootBall.items[itemSlot] != null) {
        this.PushStack(lootBall.items[itemSlot], player);
        lootBall.items[itemSlot] = null;
      }
    }
  }

  public void PushStack(ItemStack item, EntityHuman player) {
    int itemSlot;
    for (itemSlot = 0; itemSlot < this.getSize(); ++itemSlot) {
      if (item != null) {
        if (this.items[itemSlot] == null) {
          this.items[itemSlot] = item.cloneItemStack();
          item = null;
          this.markForUpdate = true;
          return;
        }
        if (this.items[itemSlot].doMaterialsMatch(item)) {
          while (this.items[itemSlot].count < this.items[itemSlot].getMaxStackSize() && item != null) {
            ++this.items[itemSlot].count;
            --item.count;
            if (item.count == 0) {
              item = null;
              this.markForUpdate = true;
              return;
            }
          }
        }
        else if (itemSlot == this.items.length - 1) {
          EntityItem droppedItem = new EntityItem(player.world, EEBase.playerX(player), EEBase.playerY(player), EEBase.playerZ(player), item); //2019-04-11 - EntityItem droppedItem = new EntityItem(player.world, EEBase.playerX(player), EEBase.playerY(player), EEBase.playerZ(player), droppedItem);
          droppedItem.pickupDelay = 1;
          player.world.addEntity(droppedItem);
          this.markForUpdate = true;
          return;
        }
      }
    }
    if (item != null) {
      for (itemSlot = 0; itemSlot < this.items.length; ++itemSlot) {
        if (this.items[itemSlot] == null) {
          this.items[itemSlot] = item.cloneItemStack();
          item = null;
          this.markForUpdate = true;
          return;
        }
      }
    }
  }

  public boolean a(EntityHuman player) {
    return true;
  }

  public void f() {
  }

  public void g() {
  }

  public void a(NBTTagCompound nbtTagCompound) {
    this.voidOn = nbtTagCompound.getBoolean("voidOn");
    this.repairOn = nbtTagCompound.getBoolean("repairOn");
    this.condenseOn = nbtTagCompound.getBoolean("condenseOn");
    this.eternalDensity = nbtTagCompound.getShort("eternalDensity");
    NBTTagList items = nbtTagCompound.getList("Items");
    this.items = new ItemStack[113];

    for (int itemSlot = 0; itemSlot < items.size(); ++itemSlot) {
      NBTTagCompound tagCompound = (NBTTagCompound) items.get(itemSlot);
      int bagItemSlot = tagCompound.getByte("Slot") & 255;
      if (bagItemSlot >= 0 && bagItemSlot < this.items.length) {
        this.items[bagItemSlot] = ItemStack.a(tagCompound);
      }
    }
  }

  public void b(NBTTagCompound nbtTagCompound) {
    nbtTagCompound.setBoolean("voidOn", this.voidOn);
    nbtTagCompound.setBoolean("repairOn", this.repairOn);
    nbtTagCompound.setBoolean("condenseOn", this.condenseOn);
    nbtTagCompound.setShort("eternalDensity", (short) this.eternalDensity);
    NBTTagList nbtTagList = new NBTTagList();

    for (int itemSlot = 0; itemSlot < this.items.length; ++itemSlot) {
      if (this.items[itemSlot] != null) {
        NBTTagCompound item = new NBTTagCompound();
        item.setByte("Slot", (byte) itemSlot);
        this.items[itemSlot].save(item);
        nbtTagList.add(item);
      }
    }
    nbtTagCompound.set("Items", nbtTagList);
  }

  public ItemStack splitWithoutUpdate(int stackSize) {
    return null;
  }

  public ItemStack[] getContents() {
    return this.items;
  }

  public void onOpen(CraftHumanEntity player) {
  }

  public void onClose(CraftHumanEntity player) {
  }

  public List<HumanEntity> getViewers() {
    return new ArrayList(0);
  }

  public InventoryHolder getOwner() {
    return null;
  }

  public void setMaxStackSize(int size) {
  }
}
