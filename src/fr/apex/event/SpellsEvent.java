package fr.apex.event;

import fr.apex.main.Vector3;
import fr.apex.spells.AmperiumSpell;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;

public class SpellsEvent implements Listener {

    public static Location getRightSide(Location location, double distance) { // retourne la position d'une distance à droite du joueur
        float angle = location.getYaw() / 60;
        return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if(event.getItem() != null) {
            if(event.getItem().getType().equals(Material.STICK)) {
                Player player = event.getPlayer();

                Location playerLocation = getRightSide(player.getLocation().add(0,0.5,0), 0.5); // position du joueur
                Location playerEyeLocation = player.getTargetBlock((HashSet<Material>) null, 8).getLocation(); // position du bloc ciblé

                Vector3 playerLoc = new Vector3(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ()); // vecteur position du joueur
                Vector3 playerEyeTargetLoc = new Vector3(playerEyeLocation.getX(), playerEyeLocation.getY(), playerEyeLocation.getZ()); // vecteur position du bloc ciblé
                AmperiumSpell lightningSpell = new AmperiumSpell(playerLoc, playerEyeTargetLoc, 3, true, player); // init le sort
                lightningSpell.renderSpell(); // render le sort
                lightningSpell.spell(); // actionne le sort
            }
        }
    }
}
