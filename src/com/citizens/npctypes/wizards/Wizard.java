package com.citizens.npctypes.wizards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import com.citizens.Permission;
import com.citizens.commands.CommandHandler;
import com.citizens.commands.commands.WizardCommands;
import com.citizens.interfaces.Saveable;
import com.citizens.npctypes.CitizensNPC;
import com.citizens.npctypes.wizards.WizardManager.WizardMode;
import com.citizens.properties.Node;
import com.citizens.properties.SettingsManager;
import com.citizens.properties.SettingsManager.SettingsType;
import com.citizens.properties.properties.WizardProperties;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.InventoryUtils;
import com.citizens.utils.MessageUtils;
import com.citizens.utils.StringUtils;

public class Wizard extends CitizensNPC {
	private String locations = "";
	private int currentLocation = 0;
	private int numberOfLocations = 0;
	private Location currentLoc;
	private WizardMode mode = WizardMode.TELEPORT;
	private int mana = 10;
	private String time = "morning";
	private CreatureType mob = CreatureType.CHICKEN;
	private boolean unlimitedMana = false;
	private String command = "";

	/**
	 * Addds a location to the main location sting.
	 * 
	 * @param location
	 * @param locName
	 */
	public void addLocation(Location location, String locName) {
		String addedLoc = "";
		addedLoc = "(" + locName + "," + location.getWorld().getName() + ","
				+ location.getX() + "," + location.getY() + ","
				+ location.getZ() + "," + location.getYaw() + ","
				+ location.getPitch() + "):";
		locations = locations + addedLoc;
		numberOfLocations = locations.split(":").length;
		if (locations.split(":")[0].isEmpty()) {
			numberOfLocations = 0;
		}
	}

	/**
	 * Returns the main location string.
	 * 
	 * @return
	 */
	public String getLocations() {
		return locations;
	}

	/**
	 * Sets all the locations of the wizard (one big combined string)
	 * 
	 * @param locationsinc
	 */
	public void setLocations(String locationsinc) {
		locations = locationsinc;
		numberOfLocations = locations.split(":").length;
		if (locations.split(":")[0].isEmpty()) {
			numberOfLocations = 0;
		}
	}

	/**
	 * Sets the next location in the list as active.
	 * 
	 * @return
	 */
	public void cycle(HumanNPC npc, WizardMode mode) {
		Wizard wizard = npc.getType("wizard");
		switch (mode) {
		case TELEPORT:
			currentLocation++;
			if (currentLocation >= numberOfLocations) {
				currentLocation = 0;
			}
			break;
		case SPAWN:
			CreatureType type = wizard.getMob();
			CreatureType newType = null;
			switch (type) {
			case PIG:
				newType = CreatureType.WOLF;
				break;
			case WOLF:
				newType = CreatureType.COW;
				break;
			case COW:
				newType = CreatureType.CHICKEN;
				break;
			case CHICKEN:
				newType = CreatureType.SHEEP;
				break;
			case SHEEP:
				newType = CreatureType.SQUID;
				break;
			case SQUID:
				newType = CreatureType.PIG_ZOMBIE;
				break;
			case PIG_ZOMBIE:
				newType = CreatureType.GHAST;
				break;
			case GHAST:
				newType = CreatureType.GIANT;
				break;
			case GIANT:
				newType = CreatureType.CREEPER;
				break;
			case CREEPER:
				newType = CreatureType.ZOMBIE;
				break;
			case ZOMBIE:
				newType = CreatureType.SPIDER;
				break;
			case SPIDER:
				newType = CreatureType.SKELETON;
				break;
			case SKELETON:
				newType = CreatureType.PIG;
				break;
			}
			wizard.setMob(newType);
			break;
		case TIME:
			String time = wizard.getTime();
			String newTime = "";
			if (time.equals("day")) {
				newTime = "afternoon";
			} else if (time.equals("night")) {
				newTime = "morning";
			} else if (time.equals("morning")) {
				newTime = "day";
			} else if (time.equals("afternoon")) {
				newTime = "night";
			}
			wizard.setTime(newTime);
			break;
		}
	}

	/**
	 * Returns the total amount of locations bound to the wizard.
	 * 
	 * @return
	 */
	public int getNumberOfLocations() {
		return numberOfLocations;
	}

	/**
	 * Return the current active teleport location for the wizard.
	 * 
	 * @return
	 */
	public Location getCurrentLocation() {
		String locs[] = locations.split(":")[currentLocation].split(",");
		this.currentLoc = new Location(Bukkit.getServer().getWorld(locs[1]),
				Double.parseDouble(locs[2]), Double.parseDouble(locs[3]),
				Double.parseDouble(locs[4]), Float.parseFloat(locs[5]),
				Float.parseFloat(locs[6].replace(")", "")));
		return this.currentLoc;
	}

	/**
	 * Return the current active teleport location name for the wizard.
	 * 
	 * @return
	 */
	public String getCurrentLocationName() {
		String locs[] = locations.split(":")[currentLocation].split(",");
		return locs[0].replace("(", "");
	}

	/**
	 * Get the mana that a wizard NPC has remaining
	 * 
	 * @return
	 */
	public int getMana() {
		return mana;
	}

	/**
	 * Set the mana of a wizard NPC
	 * 
	 * @param mana
	 */
	public void setMana(int mana) {
		this.mana = mana;
	}

	/**
	 * Get the mode of a wizard
	 * 
	 * @return
	 */
	public WizardMode getMode() {
		return mode;
	}

	/**
	 * Set the mode of a wizard
	 * 
	 * @param mode
	 */
	public void setMode(WizardMode mode) {
		this.mode = mode;
	}

	/**
	 * Get the time setting of a wizard
	 * 
	 * @return
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Set the time setting for a wizard
	 * 
	 * @param time
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * Get the mob that a wizard will spawn
	 * 
	 * @return
	 */
	public CreatureType getMob() {
		return mob;
	}

	/**
	 * Set the mob that a wizard will spawn
	 * 
	 * @param mob
	 */
	public void setMob(CreatureType mob) {
		this.mob = mob;
	}

	/**
	 * Get whether a wizard has unlimited mana
	 * 
	 * @return
	 */
	public boolean hasUnlimitedMana() {
		return unlimitedMana;
	}

	/**
	 * Set whether a wizard has unlimited mana
	 * 
	 * @param unlimitedMana
	 */
	public void setUnlimitedMana(boolean unlimitedMana) {
		this.unlimitedMana = unlimitedMana;
	}

	/**
	 * Get a wizard's current command to execute
	 * 
	 * @return
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Set the command for a wizard to execute
	 * 
	 * @param command
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	public String getType() {
		return "wizard";
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
		if (Permission.generic(player, "citizens.wizard.use.interact")) {
			if (player.getItemInHand().getTypeId() == SettingsManager
					.getInt("WizardInteractItem")) {
				Wizard wizard = npc.getType("wizard");
				String msg = ChatColor.GREEN + "";
				switch (wizard.getMode()) {
				case COMMAND:
					return;
				case TELEPORT:
					if (wizard.getNumberOfLocations() > 0) {
						wizard.cycle(npc, WizardMode.TELEPORT);
						msg += "Location set to "
								+ StringUtils.wrap(wizard
										.getCurrentLocationName());
					} else {
						msg += ChatColor.RED + npc.getStrippedName()
								+ " has no locations.";
					}
					break;
				case SPAWN:
					wizard.cycle(npc, WizardMode.SPAWN);
					msg += "Mob to spawn set to "
							+ StringUtils.wrap(StringUtils.format(wizard
									.getMob()));
					break;
				case TIME:
					wizard.cycle(npc, WizardMode.TIME);
					msg += "Time setting set to "
							+ StringUtils.wrap(wizard.getTime());
					break;
				case WEATHER:
					return;
				default:
					msg = ChatColor.RED + "No valid mode selected.";
					break;
				}
				player.sendMessage(msg);
			}
		} else {
			player.sendMessage(MessageUtils.noPermissionsMessage);
		}
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (Permission.generic(player, "citizens.wizard.use.interact")) {
			Wizard wizard = npc.getType("wizard");
			if (player.getItemInHand().getTypeId() == SettingsManager
					.getInt("WizardInteractItem")) {
				String op = "";
				switch (wizard.getMode()) {
				case COMMAND:
					op = "executecommand";
					break;
				case TELEPORT:
					op = "teleport";
					break;
				case TIME:
					op = "changetime";
					break;
				case SPAWN:
					op = "spawnmob";
					break;
				case WEATHER:
					op = "togglestorm";
					break;
				}
				WizardManager.buy(player, npc, "wizard." + op);
			} else if (player.getItemInHand().getTypeId() == SettingsManager
					.getInt("WizardManaRegenItem")) {
				String msg = StringUtils.wrap(npc.getStrippedName() + "'s");
				int mana = 0;
				if (wizard.getMana() + 10 < SettingsManager
						.getInt("WizardMaxMana")) {
					mana = wizard.getMana() + 10;
					msg += " mana has been increased to "
							+ StringUtils.wrap(mana) + ".";
				} else if (wizard.getMana() + 10 == SettingsManager
						.getInt("WizardMaxMana")) {
					mana = SettingsManager.getInt("WizardMaxMana");
					msg += " mana has been fully replenished.";
				} else {
					msg += " mana cannot be regenerated with that item any further.";
					return;
				}
				InventoryUtils.decreaseItemInHand(player);
				player.sendMessage(msg);
				wizard.setMana(mana);
			}
		} else {
			player.sendMessage(MessageUtils.noPermissionsMessage);
		}
	}

	@Override
	public Saveable getProperties() {
		return new WizardProperties();
	}

	@Override
	public CommandHandler getCommands() {
		return new WizardCommands();
	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node("", SettingsType.ECONOMY, "prices.wizard.teleport",
				100));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.wizard.changetime", 100));
		nodes.add(new Node("", SettingsType.ECONOMY, "prices.wizard.spawnmob",
				100));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.wizard.togglestorm", 100));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.wizard.executecommand", 100));
		nodes.add(new Node("WizardMaxLocations", SettingsType.GENERAL,
				"general.wizards.wizard-max-locations", 10));
		nodes.add(new Node("WizardMaxMana", SettingsType.GENERAL,
				"general.wizards.max-mana", 100));
		nodes.add(new Node("WizardInteractItem", SettingsType.GENERAL,
				"items.wizards.interact-item", 288));
		nodes.add(new Node("WizardManaRegenItem", SettingsType.GENERAL,
				"items.wizards.mana-regen-item", 348));
		nodes.add(new Node("WizardManaRegenRate", SettingsType.GENERAL,
				"ticks.wizards.mana-regen-rate", 6000));
		nodes.add(new Node("RegenWizardMana", SettingsType.GENERAL,
				"general.wizards.regen-mana", true));
		return nodes;
	}
}