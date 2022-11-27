package util;

import org.osbot.rs07.api.Widgets;
import org.osbot.rs07.api.Worlds;
import org.osbot.rs07.api.ui.World;

import java.util.*;
import java.util.stream.Collectors;

public class WorldHop {
    private List<Integer> leaguesWorlds = Arrays.asList(401,402,403,404,405,406,407,408,409,410,411,412,423,438,439,440,441,442,457,458,459,460,461,462,499,500,501,502,503,504,536,537,540,541,542,543,544,549,550,551,557,558,568,569,570,575,576,578,581);
    private Queue<Integer> blackListedWorlds = new PriorityQueue<>();


    public void hopWorlds(Widgets widgets, Worlds worldsContext) {
        widgets.closeOpenInterface();
        worldsContext.isMembersWorld();

        List<World> worlds = worldsContext.getAvailableWorlds(true).stream()
                .filter(w -> !w.isFull() &&
                        !w.isPvpWorld() &&
                        !w.isHighRisk() &&
                        !blackListedWorlds.contains(w.getId()) &&
                        worldsContext.isWorldAllowedForHop(w) &&
                        (worldsContext.isMembersWorld() == w.isMembers()) &&
                        !w.getActivity().contains("Leagues") &&
                        !leaguesWorlds.contains(w.getId())
                )
                .collect(Collectors.toList());
        Collections.shuffle(worlds);
        worldsContext.hop(worlds.get(0).getId());
    }
}
