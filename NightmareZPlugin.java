/*
 * Copyright (c) 2019, Patryk Sitko
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.nightmareZ;


import com.google.inject.Provides;
import java.awt.AWTException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;

/**
 *
 * @author Patryk Sitko
 */
@PluginDescriptor(
	name = "Afk NightmareZone",
	description = "This plugin let's you trully afk nightmareZone",
	tags = {"Afk", "NightmareZone"},
	enabledByDefault = false
)
public class NightmareZPlugin extends Plugin{
    
    private VirtualMouse virtualMouse;
    @Inject
    private volatile Client client;
    private Integer finnishedDrinkingOverload = null;
    private boolean allowedToGruzle;
    
    @Inject
    private NightmareZConfig config;

	@Provides
	NightmareZConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(NightmareZConfig.class);
	}
    
	@Override
	public void startUp()
	{
            NightmarezoneInfo.setClient(client);
            PlayerInfo.setClient(client);
            InventoryInfo.setClient(client);
            Potion.setClient(client);
            Prayers.setClient(client);
        initialiseVirtualMouse();
	}

        @Override
        public void shutDown(){
        virtualMouse.stop();
        }
        
	@Subscribe
	public void onGameTick(GameTick event){
        if(client.getGameState().equals(GameState.LOGGED_IN) && NightmarezoneInfo.isInsideOfNightmarezone()){
            try {
                if(config.useProtectMelee() && !Prayers.PROTECT_MELEE.isActive() && PlayerInfo.getPrayerPoints() > 0){
                Prayers.PROTECT_MELEE.activate(virtualMouse);
                }else{
                keepPlayerAlive();
                gruzle();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(NightmareZPlugin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }

        private void initialiseVirtualMouse(){
        try {
            virtualMouse = new VirtualMouse(client);
        } catch (AWTException ex) {
            Logger.getLogger(NightmareZPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        }

    private void keepPlayerAlive() throws InterruptedException {
        for(Potion potion:Potion.values()){
       // System.out.println("criticalMarginOf("+potion.name() +"): "+potion.criticallyLowDoseMargin);
      //  System.out.println("recommendedToDrink("+potion.name() +"): "+potion.recommendedToDrink());
        if(potion.recommendedToDrink()){
      //      System.out.println("Drinking("+potion.name()+")");
      if(potion.name().equals("OVERLOAD")){
        finnishedDrinkingOverload = PlayerInfo.getBoostedSkillLevel(Skill.HITPOINTS) - 49;
             if(!Potion.ABSORPTION.isPresent()){
                allowedToGruzle = true;
            }
        }
            if(potion.name().equals("ABSORPTION")){
                allowedToGruzle = false;
            }
        potion.drink(virtualMouse);
            }
        }
        if(finnishedDrinkingOverload != null && PlayerInfo.getBoostedSkillLevel(Skill.HITPOINTS) <= finnishedDrinkingOverload){
        finnishedDrinkingOverload = null;
        }
        
      if(Potion.ABSORPTION.overdose()){
      allowedToGruzle = true;
      }
      if(!Potion.ABSORPTION.isPresent() && NightmarezoneInfo.getAbsorptionPoints() < VirtualMouse.randomise(50, 100)){
      allowedToGruzle = false;
      }
      
      if(config.gruzleAnyway()){
      allowedToGruzle = true;
      }
    }

    private void gruzle() {
            if(InventoryInfo.contains(ItemID.DWARVEN_ROCK_CAKE_7510) && allowedToGruzle){
                int HP = PlayerInfo.getBoostedSkillLevel(Skill.HITPOINTS);
            WidgetItem rockCake = InventoryInfo.getItemsWithID(ItemID.DWARVEN_ROCK_CAKE_7510).stream().findFirst().get();
        if(Potion.OVERLOAD.isPresent()){
        if(Potion.OVERLOAD.overdose() && HP > 2){
            if(finnishedDrinkingOverload == null){
                InventoryInfo.showInventory(virtualMouse);
                virtualMouse.clickOn(rockCake, VirtualMouse.Button.LEFT, ItemID.DWARVEN_ROCK_CAKE_7510 + "");
            }
        }
        }else if(Potion.DIVINE_SUPER_COMBAT.isPresent()){
            if(Potion.DIVINE_SUPER_COMBAT.overdose() && HP > 11){
                    InventoryInfo.showInventory(virtualMouse);
                    virtualMouse.clickOn(rockCake, VirtualMouse.Button.LEFT, ItemID.DWARVEN_ROCK_CAKE_7510 + "");
            }
        }else if(HP > 2 && finnishedDrinkingOverload == null){
            InventoryInfo.showInventory(virtualMouse);
            virtualMouse.clickOn(rockCake, VirtualMouse.Button.LEFT, ItemID.DWARVEN_ROCK_CAKE_7510 + "");
        }
        }
    }
}
