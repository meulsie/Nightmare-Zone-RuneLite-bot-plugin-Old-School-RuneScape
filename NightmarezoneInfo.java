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
import net.runelite.api.Varbits;

/**
 *
 * @author Patryk Sitko
 */
public class NightmarezoneInfo {
    private NightmarezoneInfo(){}
    
    @Setter
    private static Client client;
    public static final int NIGHTMAREZONE_REGION = 9033;
    public static final int MAX_ABSORPTION_POINTS = 1000;
    public static final int ONE_ABSORPTION_DOSE_POINTS = 50;
    
    public static Integer getAbsorptionPoints(){
        try{
        return client.getVar(Varbits.NMZ_ABSORPTION);}catch(Error e){
        System.out.println(e.toString());
        return null;
        }
        
    }
    
    public static int getNightmarezonePoints(){
        return client.getVar(Varbits.NMZ_POINTS);
    }
    
    public static boolean isInsideOfNightmarezone(){
    for(int region:client.getMapRegions()){
    if(region == NIGHTMAREZONE_REGION){
    return true;
    }
    }
    return false;
    }
}
