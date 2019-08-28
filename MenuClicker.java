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

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import net.runelite.api.Client;
import net.runelite.api.FontTypeFace;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;

/**
 *
 * @author Patryk Sitko
 */
public class MenuClicker {
    
    private Client client;
    private final VirtualMouse virtualMouse;
    
    public MenuClicker(VirtualMouse virtualMouse,Client client){
        this.virtualMouse = virtualMouse;
        this.client = client;
    }
    public MenuOptionPicker target(Widget widget){
    return new MenuOptionPicker(this.virtualMouse,widget);
    }
    public static class MenuOptionPicker{

        private final VirtualMouse virtualMouse;
        private final Widget widget;
        
        public MenuOptionPicker(VirtualMouse virtualMouse,Widget widget){
        this.virtualMouse = virtualMouse;
        this.widget = widget;
        }
        
        public void clickOption(int index){
            FontTypeFace actionFont = this.widget.getFont();
            Integer menuWidth = null;
            for(String action:this.widget.getActions()){
                int actionWidth = actionFont.getTextWidth(action);
                menuWidth = menuWidth == null || menuWidth < actionWidth?actionWidth:menuWidth;
            }
            Point mouseLocation = new Point(0,0);
            int potentialPositiveX = mouseLocation.getX() + menuWidth;
            int potentialNegativeX = mouseLocation.getX() - menuWidth;
            client.getCanvas().contains(0, 0)
            int x = mouseLocation.getX() - menuWidth / 2 >= 0?mouseLocation.getX() - menuWidth / 2:
            Rectangle actionLocation = new Rectangle();
        }
    }
}