package fr.apex.spells;

import com.google.common.collect.Lists;
import fr.apex.main.References;
import fr.apex.utils.Random;
import fr.apex.main.Vector3;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class AmperiumSpell {

    private final int generations;
    private final boolean branch;
    private final World world;
    private final Player player;

    private List<LightningSegment> segments = Lists.newArrayList();
    private List<CraftPlayer> playersCatch = Lists.newArrayList(); // variable des joueurs attrapé par le sort

    public AmperiumSpell(Vector3 from, Vector3 to, int generations, boolean branch, Player player) { // constructeur sort amperium
        this.segments.add(new LightningSegment(from, to, 1));
        this.generations = generations;
        this.branch = branch;
        this.world = player.getWorld();
        this.player = player;
        calculateSpell();
    }

    private void calculateSpell() { // décompose le segment  principal pour créer de l'électricité
        for(int i = 0; i < generations; i++) { // nombre de découpe du segment
            List<LightningSegment> temp = Lists.newArrayList();
            for(LightningSegment segment : segments) { // boucle de découpe des segments
                Vector3 midPoint = segment.from.getMidpoint(segment.to); //millieu d'un segment
                midPoint = midPoint.addX(Random.randD(-0.75, 0.75)).addY(Random.randD(-0.75, 0.75)).addZ(Random.randD(-0.75, 0.75)); // rotation random dans l'espace

                if(branch && Random.randD(1) > 0.6) { // 40% de chance pour créer une branche
                    Vector3 splitEnd = midPoint.clone().subtract(segment.from).rotate(Random.randD(-0.75, 0.75), midPoint).add(midPoint);
                    temp.add(new LightningSegment(midPoint, splitEnd, 1)); // création de la branche
                }

                temp.add(new LightningSegment(segment.from, midPoint, 1)); // première partie du segment découpé
                temp.add(new LightningSegment(midPoint, segment.to, 1)); // deuxième partie du segment découpé
            }
            segments = temp;
        }
    }

    public AmperiumSpell renderSpell() { // render du sort amperium
        for (LightningSegment s : segments) { //render segment par segment
            s.render(player, s.from, s.to, s.lineWidth);
        }
        return null;
    }

    public AmperiumSpell spell() { // effet du sort sur tous les joueurs de la liste playersCatch
        if(!playersCatch.isEmpty()) {
            for(CraftPlayer p : playersCatch) {
                Player player = p;
                player.damage(6); // damage de 3 coeurs
                world.spawnParticle(Particle.FLASH, player.getLocation(), 10); // particule blanche d'éblouissement
                player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0F, 8.0F); // son de la foudre
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1)); // effet de blindness rapide
                player.setVelocity(this.player.getLocation().getDirection().multiply(2)); // propulsion vers la direction du joueur
            }
        } else {
            player.sendMessage(References.PREFIX + "§cEchec...");
        }
        return null;
    }

    private class LightningSegment {

        private final Vector3 from;
        private final Vector3 to;
        private final int lineWidth;

        LightningSegment(Vector3 from, Vector3 to, int lineWith) { // constructeur du segment
            this.from = from;
            this.to = to;
            this.lineWidth = lineWith;
        }

        void render(Player player, Vector3 from, Vector3 to, int lineWidth) { // render de l'électricité particule par particule
            for(float i = 0; i < 1; i=i+0.1f) { // boucle d'espace entre chaque particule
                Vector3 d = to.clone().subtract(from).multiply(i);
                Vector3 loc = from.clone().add(d); // vecteur de la position de la particule

                Location location = new Location(player.getWorld(), loc.getX(), loc.getY(), loc.getZ()); // position de la particule

                Collection<Entity> near = player.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5); // detection d'un joueur à 0.5 block de la particule
                for(Entity e : near) {
                    if(e instanceof CraftPlayer && !playersCatch.contains(((CraftPlayer) e).getPlayer()) && e != player) {
                        playersCatch.add((CraftPlayer) ((CraftPlayer) e).getPlayer()); // ajoute le joueur proche de la particule dans la liste
                    }
                }

                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 204, 255), 1); // option de la particule
                player.getWorld().spawnParticle(Particle.REDSTONE, location, lineWidth, dustOptions); // affiche la particule
            }
        }
    }
}
