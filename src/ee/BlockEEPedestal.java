package ee;

import ee.core.GuiIds;
import forge.ITextureProvider;
import net.minecraft.server.*;

import java.util.ArrayList;
import java.util.Random;

public class BlockEEPedestal extends BlockContainer implements ITextureProvider {
  public boolean isActive;
  private Random furnaceRand;

  public BlockEEPedestal(int hardness) {
    super(hardness, Material.ORE);
    this.c(EEBlock.eeStone.getHardness(5));
    this.a(0.2F, 0.15F, 0.2F, 0.8F, 0.7F, 0.8F);
    this.textureId = EEBase.dmBlockSide;
  }

  public String getTextureFile() {
    return "/eqex/eqexterra.png";
  }

  public int c() {
    return mod_EE.pedestalModelID;
  }

  public void setItemName(int id, String name) {
    Item item = Item.byId[this.id];
    ((ItemBlockEEPedestal) item).setMetaName(id, "tile." + name);
  }

  public void addCreativeItems(ArrayList items) {
    items.add(EEBlock.pedestal);
  }

  public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
    return this.isBurning(iBlockAccess, x, y, z) ? 15 : lightEmission[this.id];
  }

  public boolean isBurning(IBlockAccess iBlockAccess, int x, int y, int z) {
    TileEntity tileEntity = iBlockAccess.getTileEntity(x, y, z);
    return tileEntity instanceof TilePedestal && ((TilePedestal) tileEntity).isInterdicting();
  }

  public boolean a() {
    return false;
  }

  public boolean b() {
    return false;
  }

  public TileEntity a_() {
    return new TilePedestal();
  }

  public void postPlace(World world, int x, int y, int z, EntityLiving entityliving) {
    EntityHuman player = null;
    if (entityliving instanceof EntityHuman) {
      player = (EntityHuman) entityliving;
    }

    int yaw = MathHelper.floor((double) (entityliving.yaw * 4.0F / 360.0F) + 0.5D) & 3;
    int data = world.getData(x, y, z) & 12;
    if (yaw == 0) {
      data |= 2;
    }

    if (yaw == 1) {
      data |= 1;
    }

    if (yaw == 2) {
      data |= 3;
    }

    if (yaw == 3) {
      data |= 0;
    }

    world.setData(x, y, z, data);
    if (player != null) {
      TilePedestal tilePedestal = (TilePedestal) EEProxy.getTileEntity(world, x, y, z, TilePedestal.class);
      tilePedestal.setPlayer(player);
    }

  }

  public void a(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, ArrayList arraylist) {
    int data = world.getData(x, y, z) & 3;
    if (data >= 0 && data <= 3) {
      this.a(0.2F, 0.0F, 0.2F, 0.8F, 0.15F, 0.8F);
      super.a(world, x, y, z, axisAlignedBB, arraylist);
      this.a(0.4F, 0.15F, 0.4F, 0.6F, 0.65F, 0.6F);
      super.a(world, x, y, z, axisAlignedBB, arraylist);
      this.a(0.3F, 0.65F, 0.3F, 0.7F, 0.7F, 0.7F);
      super.a(world, x, y, z, axisAlignedBB, arraylist);
    }

    this.a(0.2F, 0.0F, 0.2F, 0.8F, 0.7F, 0.8F);
  }

  public void doPhysics(World world, int x, int y, int z, int unused) {
    TilePedestal tilePedestal = (TilePedestal) world.getTileEntity(x, y, z);
    if (tilePedestal != null && world.isBlockIndirectlyPowered(x, y, z) && tilePedestal.activationCooldown <= 0) {
      tilePedestal.activate();
    }

  }

  public boolean interact(World world, int x, int y, int z, EntityHuman player) {
    if (EEProxy.isClient(world)) {
      return true;
    }
    else {
      TilePedestal tilePedestal = (TilePedestal) world.getTileEntity(x, y, z);
      if (tilePedestal != null) {
        if (player.isSneaking()) {
          player.openGui(mod_EE.getInstance(), GuiIds.PEDESTAL, world, x, y, z);
        }
        else {
          tilePedestal.activate(player);
        }
      }

      return true;
    }
  }

  protected int getDropData(int data) {
    return data & 12;
  }

  public void remove(World world, int x, int y, int z) {
    IInventory inventory = (IInventory) world.getTileEntity(x, y, z);

    for (int itemCount = 0; itemCount < inventory.getSize(); ++itemCount) {
      ItemStack item = inventory.getItem(itemCount);
      if (item != null) {
        float fx = world.random.nextFloat() * 0.8F + 0.1F;
        float fy = world.random.nextFloat() * 0.8F + 0.1F;
        float fz = world.random.nextFloat() * 0.8F + 0.1F;

        while (item.count > 0) {
          int random = world.random.nextInt(21) + 10;
          if (random > item.count) {
            random = item.count;
          }

          item.count -= random;
          EntityItem droppedItem = new EntityItem(world, (double) ((float) x + fx), (double) ((float) y + fy), (double) ((float) z + fz), new ItemStack(item.id, random, item.getData()));
          float f = 0.05F;
          droppedItem.motX = (double) ((float) world.random.nextGaussian() * f);
          droppedItem.motY = (double) ((float) world.random.nextGaussian() * f + 0.2F);
          droppedItem.motZ = (double) ((float) world.random.nextGaussian() * f);
          world.addEntity(droppedItem);
        }
      }
    }

  }
}