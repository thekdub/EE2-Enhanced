//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ee;

import ee.core.GuiIds;
import net.minecraft.server.*;

public class ItemAlchemyBag extends ItemEECharged {
  public static MinecraftServer mc = ModLoader.getMinecraftServerInstance();
  public static final String prefix = "bag";
  public static final String prefix_ = "bag_";

  public ItemAlchemyBag(int var1) {
    super(var1, 0);
    this.maxStackSize = 1;
    this.a(true);
  }

  public int getIconFromDamage(int var1) {
    return var1 >= 0 && var1 < 16 ? this.textureId + var1 : this.textureId;
  }

  public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
    if (ModLoader.getMinecraftServerInstance() != null) {
      var3.openGui(mod_EE.getInstance(), GuiIds.ALCH_BAG, var2, var1.getData(), (int) var3.locY, (int) var3.locZ);
    }

    return var1;
  }

  private AlchemyBagData getBagData(ItemStack var1, World var2) {
    String var3 = "bag_global" + var1.getData();
    AlchemyBagData var4 = (AlchemyBagData) var2.a(AlchemyBagData.class, var3);
    if (var4 == null) {
      var4 = new AlchemyBagData(var3);
      var4.a();
      var2.a(var3, var4);
    }

    return var4;
  }

  public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, int var4, int var5, int var6, int var7) {
    if (ModLoader.getMinecraftServerInstance() != null) {
      var2.openGui(mod_EE.getInstance(), GuiIds.ALCH_BAG, var3, var1.getData(), (int) var2.locY, (int) var2.locZ);
    }

    return true;
  }

  public void doChargeTick(ItemStack var1, World var2, EntityHuman var3) {
  }

  public void doToggle(ItemStack var1, World var2, EntityHuman var3) {
  }

  public void doUncharge(ItemStack var1, World var2, EntityHuman var3) {
  }

  public void doAlternate(ItemStack var1, World var2, EntityHuman var3) {
    if (ModLoader.getMinecraftServerInstance() != null) {
      var3.openGui(mod_EE.getInstance(), GuiIds.ALCH_BAG, var2, var1.getData(), (int) var3.locY, (int) var3.locZ);
    }

  }

  public void a(ItemStack var1, World var2, Entity var3, int var4, boolean var5) {
    if (!EEProxy.isClient(var2) && var3 instanceof EntityHuman) {
      EntityHuman var6 = (EntityHuman) var3;
      String var7 = var6.name;
      String var8 = "bag_" + var7 + var1.getData();
      AlchemyBagData var9 = (AlchemyBagData) var2.a(AlchemyBagData.class, var8);
      if (var9 == null) {
        var9 = new AlchemyBagData(var8);
        var9.a();
        var2.a(var8, var9);
      }

      var9.onUpdate(var2, var6);
    }

  }

  public static AlchemyBagData getBagData(ItemStack var0, EntityHuman var1, World var2) {
    String var3 = var1.name;
    String var4 = "bag_" + var3 + var0.getData();
    AlchemyBagData var5 = (AlchemyBagData) var2.a(AlchemyBagData.class, var4);
    if (var5 == null) {
      var5 = new AlchemyBagData(var4);
      var5.a();
      var2.a(var4, var5);
    }

    return var5;
  }

  public static AlchemyBagData getBagData(int var0, EntityHuman var1, World var2) {
    String var3 = var1.name;
    String var4 = "bag_" + var3 + var0;
    AlchemyBagData var5 = (AlchemyBagData) var2.a(AlchemyBagData.class, var4);
    if (var5 == null) {
      var5 = new AlchemyBagData(var4);
      var5.a();
      var2.a(var4, var5);
    }

    return var5;
  }

  public void d(ItemStack var1, World var2, EntityHuman var3) {
    if (!EEProxy.isClient(var2)) {
      String var4 = var3.name;
      String var5 = "bag_" + var4 + var1.getData();
      AlchemyBagData var6 = (AlchemyBagData) var2.a(AlchemyBagData.class, var5);
      if (var6 == null) {
        var6 = new AlchemyBagData(var5);
        var2.a(var5, var6);
        var6.a();
      }
    }

  }
}
