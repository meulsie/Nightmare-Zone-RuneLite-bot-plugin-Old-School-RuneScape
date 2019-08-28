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
import java.util.Collection;
import lombok.Setter;
import net.runelite.api.Prayer;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;

/**
 *
 * @author Patryk Sitko
 */
public class PlayerInfo {

    private PlayerInfo(){}
    
    @Setter
    private static Client client;
    
    
    /**
     *
     * @return amount of hit points left in percentage;
     */
    public static int getHitPoints(){
    return (int)((float) client.getBoostedSkillLevel(Skill.HITPOINTS) / client.getRealSkillLevel(Skill.HITPOINTS)*100) ;
    }
    
    /**
     *
     * @return amount of prayer points left in percentage;
     */
    public static int getPrayerPoints(){
    return (int)((float) client.getBoostedSkillLevel(Skill.PRAYER) / client.getRealSkillLevel(Skill.PRAYER) * 100);
    }
    
    /**
     *
     * @return amount of special attack points left in percentage;
     */
    public static int getSpecialAttackPoints(){
    return client.getVar(VarPlayer.SPECIAL_ATTACK_PERCENT) /10;
    }
    
    public static int getSkillLevel(Skill skill){
    return client.getRealSkillLevel(skill);
    }
    
    public static int getBoostedSkillLevel(Skill skill){
    return client.getBoostedSkillLevel(skill);
    }
    
    /**
     *
     * @return A collection of active prayers (empty if none are active).
     */
    public static Collection<Prayer> getActivePrayers(){
        Collection<Prayer> activePrayers = new ArrayList<>();
    for(Prayer prayer:Prayer.values()){
    if(client.isPrayerActive(prayer)){
    activePrayers.add(prayer);
    }
    }
    return activePrayers;
    }
    
}
