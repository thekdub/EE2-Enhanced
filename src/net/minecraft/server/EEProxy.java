//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.minecraft.server;

import ee.EEBase;
import ee.EEMaps;
import ee.TransTabletData;
import forge.DimensionManager;
import forge.NetworkMod;

import java.util.*;

public class EEProxy {
  public static final int MAXWORLDHEIGHT = 256;
  private static boolean initialized;
  private static MinecraftServer mc;
  private static NetworkMod ee;
  public static World theWorld;
  public static Map<String, EEProxy.TTGroup> ttGroups = new HashMap();
  public static Map<String, EEProxy.TTGroup> playerGroups = new HashMap();

  public EEProxy() {
  }

  public static void Init(MinecraftServer server, NetworkMod instance) {
    if (!initialized) {
      initialized = true;
    }

    ee = instance;
    mc = server;
    theWorld = DimensionManager.getWorld(0);
  }

  public static boolean isClient(World world) {
    return world.isStatic;
  }

  public static boolean isServer() {
    return true;
  }

  public static Object getTileEntity(IBlockAccess var0, int var1, int var2, int var3, Class var4) {
    if (var2 < 0) {
      return null;
    }
    else {
      TileEntity var5 = var0.getTileEntity(var1, var2, var3);
      return !var4.isInstance(var5) ? null : var5;
    }
  }

  public static boolean addTTGroup(String groupName, int masterDimension, Integer... otherDimensions) {
    if (ttGroups.containsKey(groupName)) {
      return false;
    }
    else {
      EEProxy.TTGroup grp = new EEProxy.TTGroup(groupName, masterDimension);
      grp.addDimensions(otherDimensions);
      ttGroups.put(groupName, grp);
      return true;
    }
  }

  public static boolean addPlayerToGroup(String player, String group) {
    EEProxy.TTGroup ttGroup = (EEProxy.TTGroup) ttGroups.get(group);
    if (ttGroup == null) {
      return false;
    }
    else {
      EEProxy.TTGroup pGroup = (EEProxy.TTGroup) playerGroups.get(player);
      if (pGroup != null && pGroup != ttGroup) {
        return false;
      }
      else {
        ttGroup.addPlayer(player);
        return true;
      }
    }
  }

  public static void removePlayerFromGroup(String player) {
    EEProxy.TTGroup grp = (EEProxy.TTGroup) playerGroups.get(player);
    if (grp != null) {
      grp.removePlayer(player);
    }

  }

  public static String getGroupDescription(String gName) {
    EEProxy.TTGroup group = (EEProxy.TTGroup) ttGroups.get(gName);
    return group == null ? "No such group" : String.format("%s : Dimensions (main %d others %s) members %s", group.getGroupName(), group.getMasterDimension(), group.dimensions, group.players);
  }

  public static TransTabletData getTransData(EntityHuman player) {
    String playerName = player.name;
    int dimension = 0;
    if (playerGroups.containsKey(playerName)) {
      EEProxy.TTGroup groupList = (EEProxy.TTGroup) playerGroups.get(playerName);
      if (groupList.containsDimension(player.dimension)) {
        playerName = groupList.getGroupName();
        dimension = groupList.getMasterDimension();
      }
    }
    String tablet = "tablet_" + playerName;
    TransTabletData tabletData = (TransTabletData) DimensionManager.getWorld(dimension).a(TransTabletData.class, tablet);
    if (tabletData == null) {
      tabletData = new TransTabletData(tablet);
      tabletData.a();
      DimensionManager.getWorld(dimension).a(tablet, tabletData);
    }
    return tabletData;
  }

  public static boolean isEntityFireImmune(Entity entity) {
    return entity.fireProof;
  }

  public static int getEntityHealth(EntityLiving entityLiving) {
    return entityLiving.health;
  }

  public static void dealFireDamage(Entity entity, int duration) {
    entity.burn(duration);
  }

  public static int getArmorRating(EntityLiving entityLiving) {
    return entityLiving.lastDamage;
  }

  public static void setArmorRating(EntityLiving entityLiving, int armorRating) {
    entityLiving.lastDamage = armorRating;
  }

  public static FoodMetaData getFoodStats(EntityHuman entityHuman) {
    return entityHuman.foodData;
  }

  public static WorldData getWorldInfo(World world) {
    return world.worldData;
  }

  public static int getMaxStackSize(Item item) {
    return item.maxStackSize;
  }

  public static int blockDamageDropped(Block block, int dropData) {
    return block.getDropData(dropData);
  }

  public static void dropBlockAsItemStack(Block block, EntityLiving entityLiving, int x, int y, int z, ItemStack itemStack) {
    block.a(entityLiving.world, x, y, z, itemStack);
  }

  public static void setPlayerFireImmunity(EntityHuman player, boolean fireImmune) {
    player.fireProof = fireImmune;
  }

  public static void setEMC(ItemStack item, int emc) {
    EEMaps.addEMC(item.id, item.getData(), emc);
  }

  public static void setEMC(int var0, int var1, int var2) {
    EEMaps.addEMC(var0, var1, var2);
  }

  public static void setEMC(int var0, int var1) {
    setEMC(var0, 0, var1);
  }

  public static int getEMC(ItemStack var0) {
    return EEMaps.getEMC(var0);
  }

  public static int getEMC(int id, int var1) {
    ItemStack itemStack = new ItemStack(id, 1, var1);
    return EEMaps.getEMC(itemStack);
  }

  public static int getEMC(int var0) {
    ItemStack itemStack = new ItemStack(var0, 1, 0);
    return EEMaps.getEMC(itemStack);
  }

  public static boolean isFuel(ItemStack itemStack) {
    return isFuel(itemStack.id, itemStack.getData());
  }

  public static boolean isFuel(int var0) {
    return isFuel(var0, 0);
  }

  public static boolean isFuel(int var0, int var1) {
    return EEMaps.isFuel(var0, var1);
  }

  public static void addFuel(ItemStack itemStack) {
    addFuel(itemStack.id, itemStack.getData());
  }

  public static void addFuel(int var0) {
    addFuel(var0, 0);
  }

  public static void addFuel(int var0, int var1) {
    EEMaps.addFuelItem(var0, var1);
  }

  public static void handleControl(NetworkManager networkManager, int var1) {
    NetServerHandler netServerHandler = (NetServerHandler) networkManager.getNetHandler();
    EntityPlayer player = netServerHandler.getPlayerEntity();
    switch (var1) {
      case 0:
        EEBase.doAlternate(player.world, player);
        break;
      case 1:
        EEBase.doCharge(player.world, player);
        break;
      case 2:
        EEBase.doToggle(player.world, player);
        break;
      case 3:
        EEBase.doRelease(player.world, player);
      case 4:
      default:
        break;
      case 5:
        EEBase.doJumpTick(player.world, player);
        break;
      case 6:
        EEBase.doSneakTick(player.world, player);
    }

  }

  public static void handleTEPacket(int var0, int var1, int var2, byte var3, String var4) {
  }

  public static void handlePedestalPacket(int var0, int var1, int var2, int var3, boolean var4) {
  }

  public static class TTGroup {
    private String groupName;
    private int masterDimension;
    private Set<Integer> dimensions;
    private Set<String> players;

    public TTGroup(String name, int masterDimension) {
      this.masterDimension = masterDimension;
      this.groupName = name;
      this.dimensions = new HashSet();
      this.players = new HashSet();
      this.dimensions.add(masterDimension);
    }

    public boolean containsDimension(int dim) {
      return this.dimensions.contains(dim);
    }

    public String getGroupName() {
      return this.groupName;
    }

    public int getMasterDimension() {
      return this.masterDimension;
    }

    public void addPlayer(String player) {
      this.players.add(player);
      EEProxy.playerGroups.put(player, this);
    }

    public void removePlayer(String player) {
      this.players.remove(player);
      EEProxy.playerGroups.remove(player);
    }

    public void addDimensions(Integer... dim) {
      this.dimensions.addAll(Arrays.asList(dim));
    }
  }
}
