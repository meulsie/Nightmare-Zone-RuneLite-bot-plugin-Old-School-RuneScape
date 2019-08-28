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

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.time.Instant;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;

/**
 *
 * @author Patryk Sitko
 */
public class VirtualMouse{

    private volatile ArrayList<Entry<String,Runnable>> mouseActions;
    private volatile boolean performingMouseAction = false;
    private volatile boolean stopLOOP = false;
    private final Thread LOOP;
    
   {
        synchronized(VirtualMouse.class){
    mouseActions = new ArrayList(new HashSet());
    LOOP = new Thread( new Runnable() {
        @Override
        public void run() {
            while(!stopLOOP){
                if(mouseActions.size() > 0 && !performingMouseAction){
                    mouseActions.get(0).getValue().run();
                }
            }
        }
    });
    LOOP.setName("VirtualMouseLoop");
    LOOP.setDaemon(true);
    LOOP.start();
        }
    }

    public static enum Button{
    NONE(MouseEvent.NOBUTTON),LEFT(MouseEvent.BUTTON1),MIDDLE(MouseEvent.BUTTON2),RIGHT(MouseEvent.BUTTON3);
    int button;
    Button(int button){
    this.button = button;}
    int modifier(){
    return button;}
    }
    private Point virtualMouseLocation;
    private final Canvas canvas;
    
    public VirtualMouse(Client client) throws AWTException {
        virtualMouseLocation = new Point(0,0);
        this.canvas = client.getCanvas();
}
    public void stop(){
    this.stopLOOP= true;
    }
    public synchronized void clickOn(Widget widget,VirtualMouse.Button button,String uniqueIdentifier){
        this.clickOn(widget.getBounds(), button, uniqueIdentifier);
    }
    private synchronized void clickOn(Rectangle rectangle,VirtualMouse.Button button,String uniqueIdentifier){
    if(!mouseActions.stream().anyMatch(entry ->entry.getKey().equals(uniqueIdentifier))){
        mouseActions.add(new SimpleEntry(uniqueIdentifier,new Runnable() {
            String identifier;
            {
                this.identifier = uniqueIdentifier;
            }
            
            @Override
            public void run() {
                performingMouseAction = true;
                try {
                    if(!rectangle.contains(virtualMouseLocation.getX(),virtualMouseLocation.getY()) || randomise(0, 1) == 1){
                    Thread.sleep(randomise(1,5));
                        mouseMoveTo(rectangle);
                    }
                    Component component = convert(rectangle);
                    Thread.sleep(randomise(4,8));
                    canvas.dispatchEvent(new MouseEvent(component, MouseEvent.MOUSE_PRESSED,Instant.now().toEpochMilli(),button.modifier(), virtualMouseLocation.getX(),virtualMouseLocation.getY(), 1, true));
                    Thread.sleep(randomise(1,3));
                    canvas.dispatchEvent(new MouseEvent(component, MouseEvent.MOUSE_RELEASED,Instant.now().toEpochMilli(),button.modifier(), virtualMouseLocation.getX(),virtualMouseLocation.getY(), 1, true));
                    Thread.sleep(randomise(3,7));
                } catch (InterruptedException ex) {
                    Logger.getLogger(VirtualMouse.class.getName()).log(Level.SEVERE, null, ex);
                }
                mouseActions.stream().filter(mouseAction -> mouseAction.getKey().equals(identifier)).collect(Collectors.toList()).forEach(mouseAction->{
                mouseActions.remove(mouseAction);
                });
                performingMouseAction = false;
            }
        }));}
    }
    public synchronized void clickOn(WidgetItem widgetItem,VirtualMouse.Button button,String uniqueIdentifier){
            this.clickOn(widgetItem.getCanvasBounds(), button, uniqueIdentifier);
    }
    
    protected void mouseMoveTo(Rectangle area){
         determineMouseRouteTo(area).forEach((location) -> {
             try {
                 virtualMouseLocation = location;
                 canvas.dispatchEvent(new MouseEvent(canvas.getComponentAt(location.getX(),location.getY()), MouseEvent.MOUSE_MOVED,Instant.now().toEpochMilli(),VirtualMouse.Button.NONE.modifier(), location.getX(),location.getY(), 0, true));
                 Thread.sleep(randomise(randomise(1,10),randomise(11,21)));
             } catch (InterruptedException | IllegalArgumentException ex) {
                 Logger.getLogger(VirtualMouse.class.getName()).log(Level.SEVERE, null, ex);
             }
         });
        }
    protected Component convert(Rectangle location){
       return canvas.getComponentAt((int)location.getX(),(int)location.getY());
    }
    public static int randomise(int min,int max){
        return max >= min?new Random().nextInt(max - min) + min:new Random().nextInt(min-max) + max;
    }

    private ArrayList<Point> determineMouseRouteTo(Rectangle area) {
         return Curves.quadraticBezierCurve(virtualMouseLocation,determineClickLocation(area));
        }
    private Point determineClickLocation(Rectangle clickBounds){
    final int clickX = randomise(clickBounds.x,clickBounds.x + clickBounds.width);
    final int clickY = randomise(clickBounds.y,clickBounds.y+clickBounds.height);
    return new Point(clickX,clickY);
    }
}
