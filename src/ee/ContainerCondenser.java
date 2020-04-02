package ee;

import net.minecraft.server.*;

public class ContainerCondenser extends Container {
  public long currentItemProgress = 0L;
  public int energy = 0;
  public long itemValue = 0L;
  public TileCondenser eCon;

  public ContainerCondenser(PlayerInventory playerInventory, TileCondenser tileCondenser) {
    this.eCon = tileCondenser;
    this.setPlayer(playerInventory.player);
    this.eCon.f();
    this.a(new Slot(tileCondenser, 0, 12, 6));
    int ia;
    int ib;
    for (ia = 0; ia <= 6; ++ia) {
      for (ib = 0; ib <= 12; ++ib) {
        this.a(new Slot(tileCondenser, 1 + ib + ia * 13, 12 + ib * 18, 26 + ia * 18));
      }
    }
    for (ia = 0; ia < 3; ++ia) {
      for (ib = 0; ib < 9; ++ib) {
        this.a(new Slot(playerInventory, ib + ia * 9 + 9, 48 + ib * 18, 154 + ia * 18));
      }
    }
    for (ia = 0; ia < 9; ++ia) {
      this.a(new Slot(playerInventory, ia, 48 + ia * 18, 212));
    }
  }

  public IInventory getInventory() {
    return this.eCon;
  }

  public void a() {
    super.a();
    for (int listenerCount = 0; listenerCount < this.listeners.size(); ++listenerCount) {
      ICrafting iCrafting = (ICrafting) this.listeners.get(listenerCount);
      if (this.currentItemProgress != (long) this.eCon.currentItemProgress) {
        iCrafting.setContainerData(this, 0, this.eCon.currentItemProgress);
      }
      if (this.energy != this.eCon.scaledEnergy) {
        iCrafting.setContainerData(this, 1, this.eCon.scaledEnergy & '\uffff');
      }
      if (this.energy != this.eCon.scaledEnergy) {
        iCrafting.setContainerData(this, 2, this.eCon.scaledEnergy >>> 16);
      }
    }
    this.currentItemProgress = (long) this.eCon.currentItemProgress;
    this.energy = this.eCon.scaledEnergy;
  }

  public boolean b(EntityHuman player) {
    return this.eCon.a(player);
  }

  public ItemStack a(int slot) {
    ItemStack itemStack = null;
    Slot itemSlot = (Slot) this.e.get(slot);
    if (itemSlot != null && itemSlot.c()) {
      ItemStack item = itemSlot.getItem();
      itemStack = item.cloneItemStack();
      if (slot >= 1 && slot <= 91) {
        if (!this.a(item, 92, 127, false)) {
          if (item.count == 0) {
            itemSlot.set((ItemStack) null);
          }
          return null;
        }
      }
      else if (slot >= 92 && slot <= 127 && !this.a(item, 1, 91, false)) {
        if (item.count == 0) {
          itemSlot.set((ItemStack) null);
        }
        return null;
      }
      if (item.count == 0) {
        itemSlot.set((ItemStack) null);
      }
      else {
        itemSlot.d();
      }
      if (item.count == itemStack.count) {
        return null;
      }
      itemSlot.c(item);
    }
    return itemStack;
  }

  public void a(EntityHuman player) {
    super.a(player);
    this.eCon.g();
  }
}