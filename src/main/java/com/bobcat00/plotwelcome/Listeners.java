// PlotWelcome - Displays welcome message when entering a plot world
// Copyright 2023 Bobcat00
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package com.bobcat00.plotwelcome;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;

public final class Listeners implements Listener
{
    private PlotWelcome plugin;
    
    // Only output messages in this world
    private final String plotworld = "plotworld";
    
    private PlotAPI psAPI;
    
    public Listeners(PlotWelcome plugin)
    {
        this.plugin = plugin;
        this.psAPI = new PlotAPI();
    }
    
    // -------------------------------------------------------------------------
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        String world = player.getWorld().getName();
        
        if (world.equals(plotworld))
        {
            // Delay by 300 msec (6 ticks) to make sure the player sees the message
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    if (player.isOnline())
                    {
                        outputWelcomeMsg(player);
                    }
                }
            }, 6L); // time delay (ticks)
        }
    }
    
    // -------------------------------------------------------------------------
    
    @EventHandler
    public void onChangedWorld(PlayerChangedWorldEvent event)
    {
        Player player = event.getPlayer();
        String world = player.getWorld().getName();
        
        if (world.equals(plotworld))
        {
            outputWelcomeMsg(player);
        }
    }
    
    // -------------------------------------------------------------------------
    
    // Output a welcome message to the player informing him how many plots he
    // has. If he has no plots, tell him how to claim one.
    
    private void outputWelcomeMsg(Player player)
    {
        PlotPlayer<?> plotPlayer = psAPI.wrapPlayer(player.getUniqueId());
        
        if (plotPlayer != null)
        {
            int numPlots = plotPlayer.getPlotCount();
            int maxPlots = plotPlayer.getAllowedPlots();
            
            // If the player has at least one plot
            if (numPlots > 0)
            {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have " + numPlots + " out of " + maxPlots + " allowed plot" + (maxPlots == 1 ? "." : "s.")));
            }
            
            // If the player can claim more plots
            if (numPlots < maxPlots)
            {
                if (numPlots == 0)
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWelcome to PlotWorld. To claim a plot, use &r/plot auto"));
                }
                else
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTo claim another plot, use &r/plot auto"));
                }
            }
            
            // Reminder for everybody
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTo add someone to a plot, use &r/plot trust <player>"));
        }
    }
    
}
