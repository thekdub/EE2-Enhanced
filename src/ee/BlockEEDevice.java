package ee;

import forge.ITextureProvider;
import net.minecraft.server.*;

import java.util.ArrayList;
import java.util.Random;

public class BlockEEDevice extends BlockContainer implements ITextureProvider {
  private Class[] tileEntityMap = new Class[15];

  protected BlockEEDevice(int hardness) {
    super(hardness, Material.STONE);
    this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
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

  public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
    TileEE tileEE = (TileEE) EEProxy.getTileEntity(iBlockAccess, x, y, z, TileEE.class);
    return tileEE == null ? lightEmission[this.id] : tileEE.getLightValue();
  }

  public void doPhysics(World world, int x, int y, int z, int var5) { //2019-04-12 - var5 property is unknown.
    TileEE var6 = (TileEE) EEProxy.getTileEntity(world, x, y, z, TileEE.class);
    if (var6 == null) {
      world.setTypeId(x, y, z, 0);
    }
    else {
      var6.onNeighborBlockChange(var5);
    }
  }

  public void postPlace(World world, int x, int y, int z, EntityLiving player) {
    TileEE var6 = (TileEE) EEProxy.getTileEntity(world, x, y, z, TileEE.class);
    if (var6 != null) {
      var6.onBlockPlacedBy(player);
    }
  }

  public float getHardness(int var1) { //2019-04-12 - var1 property is unknown.
    switch (var1) {
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
    TileEE var6 = (TileEE) EEProxy.getTileEntity(world, x, y, z, TileEE.class);
    if (var6 != null) {
      var6.randomDisplayTick(random);
    }

  }

  public void addTileEntityMapping(int entityMapSlot, Class aClass) {
    this.tileEntityMap[entityMapSlot] = aClass;
  }

  public int a(int textureID, int blockEntityNumber) {
    return ((TileEE) this.getBlockEntity(blockEntityNumber)).getInventoryTexture(textureID);
  }

  public int a(int textureID) {
    return this.a(textureID, 0);
  }

  public int getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int textureSide) {
    TileEE tileEE = (TileEE) EEProxy.getTileEntity(iBlockAccess, x, y, z, TileEE.class);
    return tileEE == null ? 0 : tileEE.getTextureForSide(textureSide);
  }

  public void remove(World world, int x, int y, int z) {
    TileEE tileEE = (TileEE) EEProxy.getTileEntity(world, x, y, z, TileEE.class);
    if (tileEE != null) {
      tileEE.onBlockRemoval();
      super.remove(world, x, y, z);
    }

  }

  public void onPlace(World world, int x, int y, int z) {
    super.onPlace(world, x, y, z);
    TileEE tileEE = (TileEE) EEProxy.getTileEntity(world, x, y, z, TileEE.class);
    if (tileEE != null) {
      tileEE.setDefaultDirection();
    }

  }

  public boolean interact(World world, int x, int y, int z, EntityHuman player) {
    TileEE tileEE = (TileEE) EEProxy.getTileEntity(world, x, y, z, TileEE.class);
    return tileEE == null ? false : tileEE.onBlockActivated(player);
  }

  public void addCreativeItems(ArrayList items) {
    items.add(EEBlock.transTablet);
  }

  public TileEntity getBlockEntity(int tileEntityID) {
    try {
      return (TileEntity) this.tileEntityMap[tileEntityID].getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      return null;
    }
  }

  public void setItemName(int id, String name) {
    Item item = Item.byId[this.id];
    ((ItemBlockEEDevice) item).setMetaName(id, "tile." + name);
  }
}
