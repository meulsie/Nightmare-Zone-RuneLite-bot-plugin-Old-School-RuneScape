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

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.widgets.WidgetItem;

/**
 *
 * @author Patryk Sitko
 */
public enum Potion {
    OVERLOAD(ItemID.OVERLOAD_1,ItemID.OVERLOAD_2,ItemID.OVERLOAD_3,ItemID.OVERLOAD_4,()->{
    return PlayerInfo.getBoostedSkillLevel(Skill.ATTACK);
    }),
    ABSORPTION(ItemID.ABSORPTION_1,ItemID.ABSORPTION_2,ItemID.ABSORPTION_3,ItemID.ABSORPTION_4,()->{
    return  NightmarezoneInfo.getAbsorptionPoints();
    }),
    PRAYER(ItemID.PRAYER_POTION1,ItemID.PRAYER_POTION2,ItemID.PRAYER_POTION3,ItemID.PRAYER_POTION4,()->{
    return PlayerInfo.getBoostedSkillLevel(Skill.PRAYER);
    }),
    SUPER_RESTORE(ItemID.SUPER_RESTORE1,ItemID.SUPER_RESTORE2,ItemID.SUPER_RESTORE3,ItemID.SUPER_RESTORE4,()->{
    return PlayerInfo.getBoostedSkillLevel(Skill.PRAYER);
    }),
    SUPER_COMBAT(ItemID.SUPER_COMBAT_POTION1,ItemID.SUPER_COMBAT_POTION2,ItemID.SUPER_COMBAT_POTION3,ItemID.SUPER_COMBAT_POTION4,()->{
    return PlayerInfo.getBoostedSkillLevel(Skill.ATTACK);
    }),
    DIVINE_SUPER_COMBAT(ItemID.DIVINE_SUPER_COMBAT_POTION1,ItemID.DIVINE_SUPER_COMBAT_POTION2,ItemID.DIVINE_SUPER_COMBAT_POTION3,ItemID.DIVINE_SUPER_COMBAT_POTION4,()->{
    return PlayerInfo.getBoostedSkillLevel(Skill.ATTACK);
    });
    
    private final int oneDoseID;
    private final int twoDosesID;
    private final int threeDosesID;
    private final int fourDosesID;
    protected volatile boolean reachedCriticallyLowDose;
    protected volatile boolean recoveredFromCriticallyLowDoseState;
    protected volatile Integer criticallyLowDoseMargin;
    private final Margin currentMargin;
    
    Potion(int oneDoseID,int twoDosesID,int threeDosesID,int fourDosesID,Margin margin){
    this.oneDoseID = oneDoseID;
    this.twoDosesID = twoDosesID;
    this.threeDosesID = threeDosesID;
    this.fourDosesID = fourDosesID;
    this.currentMargin = margin;
    }
    
    static void setClient(Client client){
    PlayerInfo.setClient(client);
    InventoryInfo.setClient(client);
    NightmarezoneInfo.setClient(client);
    }
    
    void drink(VirtualMouse virtualMouse) throws InterruptedException{
        WidgetItem absorptionPotion = getPotionWidget();
            if(absorptionPotion != null){
                if(!InventoryInfo.isInventoryVisible()){
                    InventoryInfo.showInventory(virtualMouse);
                }else{
                    virtualMouse.clickOn(absorptionPotion, VirtualMouse.Button.LEFT,this.name());
                }
            }
    }
    
    boolean isPresent(){
       return InventoryInfo.contains(this.oneDoseID) ||
               InventoryInfo.contains(this.twoDosesID) ||
               InventoryInfo.contains(this.threeDosesID) ||
               InventoryInfo.contains(this.fourDosesID);
    }
    
    boolean overdose(){
        switch(this){
            default: return false;
            case ABSORPTION:
               return NightmarezoneInfo.getAbsorptionPoints() + NightmarezoneInfo.ONE_ABSORPTION_DOSE_POINTS > NightmarezoneInfo.MAX_ABSORPTION_POINTS;
            case SUPER_RESTORE:
                return PlayerInfo.getBoostedSkillLevel(Skill.PRAYER) + 8 + Math.round(PlayerInfo.getSkillLevel(Skill.PRAYER) /4) > PlayerInfo.getSkillLevel(Skill.PRAYER);
            case PRAYER:
                return PlayerInfo.getBoostedSkillLevel(Skill.PRAYER) + 7 + Math.round(PlayerInfo.getSkillLevel(Skill.PRAYER) /4) > PlayerInfo.getSkillLevel(Skill.PRAYER);
            case OVERLOAD:
                return PlayerInfo.getBoostedSkillLevel(Skill.ATTACK)>=PlayerInfo.getSkillLevel(Skill.ATTACK)+ ((long)(PlayerInfo.getSkillLevel(Skill.ATTACK) * 0.15 + 5)) || PlayerInfo.getBoostedSkillLevel(Skill.HITPOINTS) < 51;
            case DIVINE_SUPER_COMBAT:
                return PlayerInfo.getBoostedSkillLevel(Skill.ATTACK) >= PlayerInfo.getSkillLevel(Skill.ATTACK)+ ((long)(PlayerInfo.getSkillLevel(Skill.ATTACK) * 0.15 + 5)) || PlayerInfo.getBoostedSkillLevel(Skill.HITPOINTS) < 11; 
            case SUPER_COMBAT:
                return PlayerInfo.getBoostedSkillLevel(Skill.ATTACK) >= PlayerInfo.getSkillLevel(Skill.ATTACK)+ ((long)(PlayerInfo.getSkillLevel(Skill.ATTACK) * 0.15 + 5)); 
        }
    }
    
    boolean recommendedToDrink(){
        return this.isPresent() &&  this.reachedCriticallyLowDose();
    }
    
    private boolean reachedCriticallyLowDose(){
        if(this.criticallyLowDoseMargin == null){
        this.criticallyLowDoseMargin =  this.getCriticallyLowDoseMargin();
        }
        if(reachedCriticallyLowDose){
            criticallyLowDoseMargin = this.getCriticallyLowDoseMargin();
            recoveredFromCriticallyLowDoseState = this.overdose();
           }else if(this.currentMargin.get() <= criticallyLowDoseMargin){
               if(this == OVERLOAD && !(PlayerInfo.getBoostedSkillLevel(Skill.HITPOINTS) > 50)){
                   return reachedCriticallyLowDose;
               }
                reachedCriticallyLowDose = true;
                recoveredFromCriticallyLowDoseState = false;
           }
        if(recoveredFromCriticallyLowDoseState){
            reachedCriticallyLowDose = false;
        }
        // System.out.println("("+this.name()+"):"+criticallyLowDoseMargin);
        return reachedCriticallyLowDose;
    }
    /**
     * 
     * @return null if not applicable, else returns the value.
     * @throws UnknownCriticallyLowDoseMargin if the potion is not present in the case statements of the switch.
     */
    private Integer getCriticallyLowDoseMargin(){
    switch(this){
        default:
            throw new UnknownCriticallyLowDoseMargin(this.name());
        case SUPER_RESTORE:{
            int maxPrayer = PlayerInfo.getSkillLevel(Skill.PRAYER) - (8 + Math.round(PlayerInfo.getSkillLevel(Skill.PRAYER) /4));
        return (int) VirtualMouse.randomise(1,maxPrayer <=0?PlayerInfo.getSkillLevel(Skill.PRAYER):maxPrayer/ (int) VirtualMouse.randomise(1, 2));
        }
        case PRAYER:{
            int maxPrayer = PlayerInfo.getSkillLevel(Skill.PRAYER) - (7 + Math.round(PlayerInfo.getSkillLevel(Skill.PRAYER) /4));
        return (int) VirtualMouse.randomise(1,maxPrayer <=0?PlayerInfo.getSkillLevel(Skill.PRAYER):maxPrayer / (int) VirtualMouse.randomise(1, 2));
        }
        case ABSORPTION:{
        return  (int) VirtualMouse.randomise(NightmarezoneInfo.ONE_ABSORPTION_DOSE_POINTS,(NightmarezoneInfo.MAX_ABSORPTION_POINTS  - NightmarezoneInfo.ONE_ABSORPTION_DOSE_POINTS)/(int)VirtualMouse.randomise(1,4));
        }
        case OVERLOAD:{
        return PlayerInfo.getSkillLevel(Skill.ATTACK);
        }
        case DIVINE_SUPER_COMBAT:
        case SUPER_COMBAT:{
            return PlayerInfo.getSkillLevel(Skill.ATTACK) + (int) VirtualMouse.randomise( 0, (int)Math.round( PlayerInfo.getSkillLevel(Skill.ATTACK) * 0.08)); 
        }
    }
    }
    private WidgetItem getPotionWidget() {
        WidgetItem overload = getWidgetItemFromInventory(this.oneDoseID);
        if(overload == null){
        overload = getWidgetItemFromInventory(this.twoDosesID);
        }
        if(overload == null){
        overload = getWidgetItemFromInventory(this.threeDosesID);
        }
        if(overload == null){
        overload = getWidgetItemFromInventory(this.fourDosesID);
        }
       return overload;
    }

    private WidgetItem getWidgetItemFromInventory( int itemID) {
       if(InventoryInfo.contains(itemID)){
          ArrayList<WidgetItem> overloadWidgets = (ArrayList) InventoryInfo.getItemsWithID(itemID);
          if(overloadWidgets.size() > 0){
              if(overloadWidgets.size()-1 > 0){
                return overloadWidgets.get(new Random().nextInt(overloadWidgets.size() -1));
              }else{
                return overloadWidgets.get(0);
              }
          }
       }
       return null;
    }
}

class UnknownCriticallyLowDoseMargin extends Error{
public UnknownCriticallyLowDoseMargin(String potionName){
super("Error: the critically low dose margin is unknown for the potion called: \"" + potionName + ", please report that.");
}

}
