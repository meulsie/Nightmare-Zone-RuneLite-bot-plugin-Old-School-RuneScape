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
import net.runelite.api.Client;
import net.runelite.api.VarClientInt;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;

/**
 *
 * @author Patryk Sitko
 */
public class InventoryInfo {

    @Setter
    private static Client client;
    
    /**
     *
     * @param itemID The id of the item (using: net.runelite.api.ItemID);
     * @return true if item is inside of the inventory, else returns false;
     */
    public static boolean contains(int itemID){
        return client.getWidget(WidgetInfo.INVENTORY).getWidgetItems().stream().anyMatch((item) -> (item.getId() == itemID));
    }

    /**
     *
     * @param itemID The id of the item (using: net.runelite.api.ItemID);
     * @return a collection of WidgetItems. (The Collection will be empty if no item with the specified id is present).
     */
    public static Collection<WidgetItem> getItemsWithID(int itemID){
    ArrayList<WidgetItem> matchingItems = new ArrayList<>();
    client.getWidget(WidgetInfo.INVENTORY).getWidgetItems().forEach(item->{
    if(item.getId() == itemID){
    matchingItems.add(item);
    }
    });
    return matchingItems;
    }

    /**
     *
     * @param index the index of the desired item (from 0-27);
     * @return requested item
     * @throws InventoryIndexOutOfBoundsException if the index value is not between 0-27. 
     */
    public static WidgetItem getItem(int index){
        if(index < 0 || index > 27){
        throw new InventoryIndexOutOfBoundsException(index);
        }
        return client.getWidget(WidgetInfo.INVENTORY).getWidgetItem(index);
    }
    public static Collection<WidgetItem> getAllItems(){
        return client.getWidget(WidgetInfo.INVENTORY).getWidgetItems();
    }
    
    /**
     *
     * @return true if the current tab that is shown to the player is the inventory tab.
     */
    public static boolean isInventoryVisible(){
        return client.getVar(VarClientInt.INVENTORY_TAB) == 3;
    }
    
    /**
     * Changes the current tab that is shown to the player to the inventory tab.
     * @param virtualMouse the mouse responsible for clicking.
     */
    public static void showInventory(VirtualMouse virtualMouse){
        if(!InventoryInfo.isInventoryVisible()){
       virtualMouse.clickOn(client.getWidget(WidgetInfo.FIXED_VIEWPORT_INVENTORY_TAB), VirtualMouse.Button.LEFT, WidgetInfo.FIXED_VIEWPORT_INVENTORY_TAB+"");
        }
    }

}

class InventoryIndexOutOfBoundsException extends ArrayIndexOutOfBoundsException{

    public InventoryIndexOutOfBoundsException(int requestedItemPosition) {
        super("Inventory item index starts from 0 and ends at 27. Requested item index:" + requestedItemPosition);
    }

}