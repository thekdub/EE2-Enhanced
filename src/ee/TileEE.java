package ee;

import ee.network.PacketHandler;
import ee.network.PacketTypeHandler;
import ee.network.TileEntityPacket;
import net.minecraft.server.*;

import java.util.Random;

public class TileEE extends TileEntity {
  public byte direction;
  public String player;

  public TileEE() {
  }

  public void a(NBTTagCompound nbtTagCompound) {
    super.a(nbtTagCompound);
    this.direction = nbtTagCompound.getByte("direction");
    this.player = nbtTagCompound.getString("player");
  }

  public void b(NBTTagCompound nbtTagCompound) {
    super.b(nbtTagCompound);
    nbtTagCompound.setByte("direction", this.direction);
    if (this.player != null && this.player != "") {
      nbtTagCompound.setString("player", this.player);
    }

  }

  public int getKleinLevel(int id) {
    return id == EEItem.kleinStar1.id ? 1 : (id == EEItem.kleinStar2.id ? 2 : (id == EEItem.kleinStar3.id ? 3 : (id == EEItem.kleinStar4.id ? 4 : (id == EEItem.kleinStar5.id ? 5 : (id == EEItem.kleinStar6.id ? 6 : 0)))));
  }

  public float getWOFTReciprocal(float modifier) {
    float reciprocal = 1.0F / modifier;
    return reciprocal * ((float) EEBase.getMachineFactor() / 16.0F);
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
    if (this.world != null && !EEProxy.isClient(this.world)) {
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
    else {
      return false;
    }
  }

  private boolean isChest(TileEntity tileEntity) {
    return tileEntity instanceof TileEntityChest || tileEntity instanceof TileAlchChest;
  }

  public void setDefaultDirection() {
    if (!this.world.isStatic) {
      int negZ = this.world.getTypeId(this.x, this.y, this.z - 1);
      int posZ = this.world.getTypeId(this.x, this.y, this.z + 1);
      int negX = this.world.getTypeId(this.x - 1, this.y, this.z);
      int posX = this.world.getTypeId(this.x + 1, this.y, this.z);
      byte direction = 2;
      if (Block.n[negZ] && !Block.n[posZ]) {
        direction = 3;
      }

      if (Block.n[posZ] && !Block.n[negZ]) {
        direction = 2;
      }

      if (Block.n[negX] && !Block.n[posX]) {
        direction = 5;
      }

      if (Block.n[posX] && !Block.n[negX]) {
        direction = 4;
      }

      this.direction = direction;
    }

  }

  public void onBlockPlacedBy(EntityLiving player) {
    if (player instanceof EntityHuman) {
      this.player = ((EntityHuman) player).name;
    }

    int viewAngle = MathHelper.floor((double) (player.yaw * 4.0F / 360.0F) + 0.5D) & 3;
    if (viewAngle == 0) {
      this.direction = 2;
    }

    if (viewAngle == 1) {
      this.direction = 5;
    }

    if (viewAngle == 2) {
      this.direction = 3;
    }

    if (viewAngle == 3) {
      this.direction = 4;
    }

  }

  public int getTextureForSide(int side) {
    return 0;
  }

  public int getInventoryTexture(int id) {
    return 0;
  }

  public int getLightValue() {
    return 0;
  }

  public boolean onBlockActivated(EntityHuman player) {
    return false;
  }

  public void onNeighborBlockChange(int side) {
  }

  public void onBlockClicked(EntityHuman player) {
  }

  public boolean clientFail() {
    return this.world == null ? true : (this.world.getTileEntity(this.x, this.y, this.z) != this ? true : EEProxy.isClient(this.world));
  }

  public void onBlockAdded() {
  }

  public void onBlockRemoval() {
    for (int itemSlot = 0; itemSlot < this.getSizeInventory(); ++itemSlot) {
      ItemStack itemStack = this.getStackInSlot(itemSlot);
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
            float var8 = 0.05F;
            entityItem.motX = (double) ((float) this.world.random.nextGaussian() * var8);
            entityItem.motY = (double) ((float) this.world.random.nextGaussian() * var8 + 0.2F);
            entityItem.motZ = (double) ((float) this.world.random.nextGaussian() * var8);
            if (entityItem.itemStack.getItem() instanceof ItemKleinStar) {
              ((ItemKleinStar) entityItem.itemStack.getItem()).setKleinPoints(entityItem.itemStack, ((ItemKleinStar) itemStack.getItem()).getKleinPoints(itemStack));
            }
            this.world.addEntity(entityItem);
          }
        }
      }
    }

  }

  private ItemStack getStackInSlot(int itemSlot) {
    return null;
  }

  private int getSizeInventory() {
    return 0;
  }

  public void randomDisplayTick(Random rand) {
  }

  public void onBlockDestroyedByExplosion() {
  }

  public void onBlockDestroyedByPlayer() {
  }

  protected void dropBlockAsItem_do(ItemStack itemStack) {
    if (!this.world.isStatic) {
      float var2 = 0.7F;
      double var3 = (double) (this.world.random.nextFloat() * var2) + (double) (1.0F - var2) * 0.5D;
      double var5 = (double) (this.world.random.nextFloat() * var2) + (double) (1.0F - var2) * 0.5D;
      double var7 = (double) (this.world.random.nextFloat() * var2) + (double) (1.0F - var2) * 0.5D;
      EntityItem entityItem = new EntityItem(this.world, (double) this.x + var3, (double) this.y + var5, (double) this.z + var7, itemStack);
      entityItem.pickupDelay = 10;
      this.world.addEntity(entityItem);
    }

  }

  public void setDirection(byte direction) {
    this.direction = direction;
  }

  public void setPlayerName(String playerName) {
    this.player = playerName;
  }

  public Packet d() {
    TileEntityPacket tileEntityPacket = (TileEntityPacket) PacketHandler.getPacket(PacketTypeHandler.TILE);
    tileEntityPacket.setCoords(this.x, this.y, this.z);
    tileEntityPacket.setOrientation(this.direction);
    tileEntityPacket.setPlayerName(this.player);
    return PacketHandler.getPacketForSending(tileEntityPacket);
  }
}