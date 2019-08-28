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

import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.VarClientInt;
import net.runelite.api.widgets.WidgetInfo;

/**
 *
 * @author Patryk Sitko
 */
public enum Prayers{
    PROTECT_MAGIC(Prayer.PROTECT_FROM_MAGIC ,WidgetInfo.PRAYER_PROTECT_FROM_MAGIC),
    PROTECT_RANGED(Prayer.PROTECT_FROM_MISSILES,WidgetInfo.PRAYER_PROTECT_FROM_MISSILES),
    PROTECT_MELEE(Prayer.PROTECT_FROM_MELEE,WidgetInfo.PRAYER_PROTECT_FROM_MELEE);
    
    @Setter
    private static Client client;
    private final Prayer prayer;
    private final WidgetInfo prayerWidget;
    
    Prayers(Prayer prayer,WidgetInfo prayerWidget){
    this.prayer = prayer;
    this.prayerWidget = prayerWidget;
    }
    static boolean isVisible(){
    return client.getVar(VarClientInt.INVENTORY_TAB) == 5;
    }
    static boolean show(VirtualMouse virtualMouse){
        if(!isVisible()){
    virtualMouse.clickOn(client.getWidget(WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB), VirtualMouse.Button.LEFT, WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB+"");
        }
    return isVisible();
    }
    
    boolean isActive(){
    return client.isPrayerActive(prayer);
    }
    static void activateOffenciveBoosts(VirtualMouse virtualMouse){
    int prayerLevel = client.getRealSkillLevel(Skill.PRAYER);
    
    }
    boolean activate(VirtualMouse virtualMouse){
        if(!this.isActive() && Prayers.show(virtualMouse)){
        virtualMouse.clickOn(client.getWidget(this.prayerWidget),VirtualMouse.Button.LEFT,this.prayerWidget.name());
    }
        return this.isActive();
    }
}
