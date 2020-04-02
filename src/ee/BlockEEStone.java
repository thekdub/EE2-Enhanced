package ee;

import forge.ISpecialResistance;
import forge.ITextureProvider;
import net.minecraft.server.*;

import java.util.ArrayList;
import java.util.Random;

public class BlockEEStone extends BlockContainer implements ITextureProvider, ISpecialResistance {
  private Class[] tileEntityMap = new Class[15];

  protected BlockEEStone(int hardness) {
    super(hardness, Material.STONE);
  }

  public String getTextureFile() {
    return "/eqex/eqexterra.png";
  }

  public TileEntity a_() {
    return null;
  }

  public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
    TileEE tileEE = (TileEE) EEProxy.getTileEntity(iBlockAccess, x, y, z, TileEE.class);
    return tileEE == null ? (lightEmission[this.id] > 0 ? lightEmission[this.id] : 0) : tileEE.getLightValue();
  }

  public void doPhysics(World world, int x, int y, int z, int side) {
    TileEE tileEE = (TileEE) EEProxy.getTileEntity(world, x, y, z, TileEE.class);
    if (tileEE == null) {
      world.setTypeId(x, y, z, 0);
    }
    else {
      tileEE.onNeighborBlockChange(side);
    }

  }

  public void postPlace(World world, int x, int y, int z, EntityLiving entityLiving) {
    TileEE tileEE = (TileEE) EEProxy.getTileEntity(world, x, y, z, TileEE.class);
    if (tileEE != null) {
      tileEE.onBlockPlacedBy(entityLiving);
    }

  }

  public float getHardness(int var1) { //2019-04-12 - Unsure of meaning of var1.
    switch (var1) {
      case 0:
        return 1.5F;
      case 1:
        return 1.5F;
      case 2:
        return 1.5F;
      case 3:
        return 3.0F;
      case 4:
        return 4.5F;
      case 5:
        return 5.0F;
      case 6:
        return 5.0F;
      case 7:
        return 5.0F;
      case 8:
        return 1000000.0F;
      case 9:
        return 2000000.0F;
      case 10:
        return 0.0F;
      case 11:
        return 0.0F;
      default:
        return 3.0F;
    }
  }

  public int getDropType(int var1, Random var2, int var3) { //2019-04-12 - Unsure of var1, var2, and var3 meanings.
    return this.id;
  }

  public int quantityDropped(int var1, int var2, Random var3) {
    return var1 != 10 && var1 != 11 ? 1 : 0;
  }

  public int getDropData(int var1) {
    return var1;
  }

  public void wasExploded(World var1, int var2, int var3, int var4) {
    TileEE var5 = (TileEE) EEProxy.getTileEntity(var1, var2, var3, var4, TileEE.class);
    if (var5 != null) {
      var5.onBlockDestroyedByExplosion();
    }

  }

  public void addTileEntityMapping(int var1, Class var2) {
    this.tileEntityMap[var1] = var2;
  }

  public int a(int var1, int var2) {
    return ((TileEE) this.getBlockEntity(var2)).getInventoryTexture(var1);
  }

  public int a(int var1) {
    return this.a(var1, 0);
  }

  public void remove(World var1, int var2, int var3, int var4) {
    TileEE var5 = (TileEE) EEProxy.getTileEntity(var1, var2, var3, var4, TileEE.class);
    if (var5 != null) {
      var5.onBlockRemoval();
      super.remove(var1, var2, var3, var4);
    }

  }

  public void onPlace(World var1, int var2, int var3, int var4) {
    super.onPlace(var1, var2, var3, var4);
    TileEE var5 = (TileEE) EEProxy.getTileEntity(var1, var2, var3, var4, TileEE.class);
    if (var5 != null) {
      var5.setDefaultDirection();
      var5.onBlockAdded();
    }

  }

  public void attack(World var1, int var2, int var3, int var4, EntityHuman var5) {
    TileEE var6 = (TileEE) EEProxy.getTileEntity(var1, var2, var3, var4, TileEE.class);
    if (var6 == null) {
      super.attack(var1, var2, var3, var4, var5);
    }
    else {
      var6.onBlockClicked(var5);
    }

  }

  public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5) {
    if (var5.isSneaking()) {
      return false;
    }
    else {
      TileEE var6 = (TileEE) EEProxy.getTileEntity(var1, var2, var3, var4, TileEE.class);
      return var6 == null ? false : var6.onBlockActivated(var5);
    }
  }

  public void addCreativeItems(ArrayList var1) {
    var1.add(EEBlock.collector);
    var1.add(EEBlock.collector2);
    var1.add(EEBlock.collector3);
    var1.add(EEBlock.dmFurnace);
    var1.add(EEBlock.rmFurnace);
    var1.add(EEBlock.relay);
    var1.add(EEBlock.relay2);
    var1.add(EEBlock.relay3);
    var1.add(EEBlock.dmBlock);
    var1.add(EEBlock.rmBlock);
    var1.add(EEBlock.novaCatalyst);
    var1.add(EEBlock.novaCataclysm);
  }

  public TileEntity getBlockEntity(int var1) {
    try {
      return (TileEntity) this.tileEntityMap[var1].getDeclaredConstructor().newInstance();
    } catch (Exception var3) {
      return null;
    }
  }

  public void setItemName(int var1, String var2) {
    Item var3 = Item.byId[this.id];
    ((ItemBlockEEStone) var3).setMetaName(var1, "tile." + var2);
  }

  public void postBreak(World var1, int var2, int var3, int var4, int var5) {
    if (var5 != 10 && var5 != 11) {
      super.postBreak(var1, var2, var3, var4, var5);
    }
    else {
      if (var1.isStatic) {
        return;
      }

      this.a(var1, var2, var3, var4, new ItemStack(EEBlock.eeStone.id, 1, var5));
    }

  }

  public float getSpecialExplosionResistance(World var1, int var2, int var3, int var4, double var5, double var7, double var9, Entity var11) {
    return this.getHardness(var1.getData(var2, var3, var4));
  }
}