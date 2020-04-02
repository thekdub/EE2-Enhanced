package ee;

import forge.ITextureProvider;
import net.minecraft.server.*;

import java.util.ArrayList;
import java.util.Random;

public class BlockEEChest extends BlockContainer implements ITextureProvider {
  private Random random = new Random();
  private Class[] tileEntityMap = new Class[15];

  protected BlockEEChest(int hardness) { //2019-04-12 - Unsure of value name
    super(hardness, Material.STONE);
  }

  public String getTextureFile() {
    return "/eqex/eqexterra.png";
  }

  public TileEntity a_() {
    return null;
  }

  public boolean a() {
    return false;
  }

  public boolean b() {
    return false;
  }

  public int c() {
    return mod_EE.chestModelID;
  }

  public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
    TileEE tileEE = (TileEE) EEProxy.getTileEntity(iBlockAccess, x, y, z, TileEE.class);
    return tileEE == null ? lightEmission[this.id] : tileEE.getLightValue();
  }

  public void doPhysics(World world, int x, int y, int z, int var5) {
    TileEE tileEE = (TileEE) EEProxy.getTileEntity(world, x, y, z, TileEE.class);
    if (tileEE == null) {
      world.setTypeId(x, y, z, 0);
    }
    else {
      tileEE.onNeighborBlockChange(var5);
    }

  }

  public void postPlace(World world, int x, int y, int z, EntityLiving player) {
    TileEE tileEE = (TileEE) EEProxy.getTileEntity(world, x, y, z, TileEE.class);
    if (tileEE != null) {
      tileEE.onBlockPlacedBy(player);
    }

  }

  public float getHardness(int hardness) {
    switch (hardness) {
      case 0:
        return 1.5F;
      case 1:
        return 3.5F;
      default:
        return 3.0F;
    }
  }

  public int getDropType(int var1, Random random, int var3) {
    return this.id;
  }

  public int a(Random random) {
    return 1;
  }

  public int getDropData(int dropData) {
    return dropData;
  }

  public void randomDisplayTick(World world, int x, int y, int z, Random random) {
    TileEE tileEE = (TileEE) EEProxy.getTileEntity(world, x, y, z, TileEE.class);
    if (tileEE != null) {
      tileEE.randomDisplayTick(random);
    }

  }

  public void addTileEntityMapping(int entitySlot, Class aClass) {
    this.tileEntityMap[entitySlot] = aClass;
  }

  public int a(int inventoryTexture, int entitySlot) {
    return ((TileEE) this.getBlockEntity(entitySlot)).getInventoryTexture(inventoryTexture);
  }

  public int a(int var1) {
    return this.a(var1, 0);
  }

  public int getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int textureSide) {
    TileEE tileEE = (TileEE) EEProxy.getTileEntity(iBlockAccess, x, y, z, TileEE.class);
    return tileEE == null ? 0 : tileEE.getTextureForSide(textureSide);
  }

  public void remove(World world, int x, int y, int z) {
    int itemSlot;
    ItemStack item;
    float x2;
    float y2;
    EntityItem itemDrop;
    float z2;
    int stackSize;
    float offset;
    if (world.getTileEntity(x, y, z) instanceof TileAlchChest) {
      TileAlchChest var5 = (TileAlchChest) world.getTileEntity(x, y, z);
      if (var5 != null) {
        for (itemSlot = 0; itemSlot < var5.getSize(); ++itemSlot) {
          item = var5.getItem(itemSlot);
          if (item != null) {
            x2 = this.random.nextFloat() * 0.8F + 0.1F;
            y2 = this.random.nextFloat() * 0.8F + 0.1F;
            for (z2 = this.random.nextFloat() * 0.8F + 0.1F; item.count > 0; world.addEntity(itemDrop)) {
              stackSize = this.random.nextInt(21) + 10;
              if (stackSize > item.count) {
                stackSize = item.count;
              }
              item.count -= stackSize;
              itemDrop = new EntityItem(world, (double) ((float) x + x2), (double) ((float) y + y2), (double) ((float) z + z2), new ItemStack(item.id, stackSize, item.getData()));
              offset = 0.05F;
              itemDrop.motX = (double) ((float) this.random.nextGaussian() * offset);
              itemDrop.motY = (double) ((float) this.random.nextGaussian() * offset + 0.2F);
              itemDrop.motZ = (double) ((float) this.random.nextGaussian() * offset);
              if (item.hasTag()) {
                itemDrop.itemStack.setTag((NBTTagCompound) item.getTag().clone());
              }
            }
          }
        }
      }
    }
    else if (world.getTileEntity(x, y, z) instanceof TileCondenser) {
      TileCondenser condenser = (TileCondenser) world.getTileEntity(x, y, z);
      if (condenser != null) {
        for (itemSlot = 0; itemSlot < condenser.getSize(); ++itemSlot) {
          item = condenser.getItem(itemSlot);
          if (item != null) {
            x2 = this.random.nextFloat() * 0.8F + 0.1F;
            y2 = this.random.nextFloat() * 0.8F + 0.1F;
            for (z2 = this.random.nextFloat() * 0.8F + 0.1F; item.count > 0; world.addEntity(itemDrop)) {
              stackSize = this.random.nextInt(21) + 10;
              if (stackSize > item.count) {
                stackSize = item.count;
              }
              item.count -= stackSize;
              itemDrop = new EntityItem(world, (double) ((float) x + x2), (double) ((float) y + y2), (double) ((float) z + z2), new ItemStack(item.id, stackSize, item.getData()));
              offset = 0.05F;
              itemDrop.motX = (double) ((float) this.random.nextGaussian() * offset);
              itemDrop.motY = (double) ((float) this.random.nextGaussian() * offset + 0.2F);
              itemDrop.motZ = (double) ((float) this.random.nextGaussian() * offset);
              if (item.hasTag()) {
                itemDrop.itemStack.setTag((NBTTagCompound) item.getTag().clone());
              }
            }
          }
        }
      }
    }
    super.remove(world, x, y, z);
  }

  public void onPlace(World world, int x, int y, int z) {
    super.onPlace(world, x, y, z);
    TileEE tileEE = (TileEE) EEProxy.getTileEntity(world, x, y, z, TileEE.class);
    if (tileEE != null) {
      tileEE.setDefaultDirection();
    }

  }

  public boolean interact(World world, int x, int y, int z, EntityHuman player) {
    if (player.isSneaking()) {
      return false;
    }
    else {
      TileEE tileEE = (TileEE) EEProxy.getTileEntity(world, x, y, z, TileEE.class);
      return tileEE == null ? false : tileEE.onBlockActivated(player);
    }
  }

  public void addCreativeItems(ArrayList items) {
    items.add(EEBlock.alchChest);
    items.add(EEBlock.condenser);
  }

  public TileEntity getBlockEntity(int entityMapSlot) {
    try {
      return (TileEntity) this.tileEntityMap[entityMapSlot].getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      return null;
    }
  }

  public void setItemName(int i, String name) {
    Item item = Item.byId[this.id];
    ((ItemBlockEEChest) item).setMetaName(i, "tile." + name);
  }
}