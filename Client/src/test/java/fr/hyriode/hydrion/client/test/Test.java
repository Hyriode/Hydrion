package fr.hyriode.hydrion.client.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.hyriode.hydrion.client.HydrionClient;
import fr.hyriode.hydrion.client.module.ResourcesModule;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 05/04/2022 at 18:21
 */
public class Test {

    public static void main(String[] args) {
        final HydrionClient hydrionClient = new HydrionClient("http://localhost:8080", UUID.fromString("92109827-fdcd-4b82-9d74-2d2050f13482"));

        testPlayer(hydrionClient);
        testGames(hydrionClient);
        testFriends(hydrionClient);
        testNetwork(hydrionClient);
    }

    private static void testPlayer(HydrionClient client) {
        final UUID uuid = UUID.fromString("c7e68fae-32af-47ce-afbf-ee57e051a91f");
        final JsonObject player = new JsonObject();

        player.addProperty("uuid", uuid.toString());
        player.addProperty("name", "AstFaster");

        final long before = System.currentTimeMillis();

        client.getPlayerModule().setPlayer(uuid, player.toString()).whenComplete((response, throwable) -> System.out.println("Request took " + (System.currentTimeMillis() - before) + "ms"));
    }

    public static void testGames(HydrionClient client) {
        final JsonObject game = new JsonObject();
        final JsonObject types = new JsonObject();

        types.addProperty("EIGHT_ONE", "Solo");
        types.addProperty("EIGHT_TWO", "Duo");
        types.addProperty("FOUR_THREE", "3v3v3v3");
        types.addProperty("FOUR_FOUR", "4v4v4v4");

        game.addProperty("name", "BedWars");
        game.addProperty("databaseName", "BedWars");
        game.add("types", types);

        final long before = System.currentTimeMillis();
        final ResourcesModule module = client.getResourcesModule();

        module.addGame("BedWars", game.toString()).whenComplete((response, throwable) -> System.out.println("Request took " + (System.currentTimeMillis() - before) + "ms"));
        module.removeGame("BedWars").whenComplete((response, throwable) -> System.out.println("Request took " + (System.currentTimeMillis() - before) + "ms"));
    }

    public static void testFriends(HydrionClient client) {
        final UUID uuid = UUID.fromString("c7e68fae-32af-47ce-afbf-ee57e051a91f");
        final JsonObject friends = new JsonObject();
        final List<UUID> friendsList = new ArrayList<>();

        for (int i = 0; i <= 5; i++) {
            friendsList.add(UUID.randomUUID());
        }

        friends.addProperty("uuid", uuid.toString());
        friends.add("list", new Gson().toJsonTree(friendsList));

        final long before = System.currentTimeMillis();

        client.getFriendsModule().setFriends(uuid, friends.toString()).whenComplete((response, throwable) -> System.out.println("Request took " + (System.currentTimeMillis() - before) + "ms"));
    }

    public static void testNetwork(HydrionClient client) {
        final JsonObject network = new JsonObject();
        final JsonObject maintenance = new JsonObject();

        maintenance.addProperty("active", true);
        maintenance.addProperty("trigger", UUID.randomUUID().toString());
        maintenance.addProperty("reason", "Fixing issues");

        network.addProperty("players", 857);
        network.add("maintenance", maintenance);

        final long before = System.currentTimeMillis();

        client.getNetworkModule().setNetwork(network.toString()).whenComplete((response, throwable) -> System.out.println("Request took " + (System.currentTimeMillis() - before) + "ms"));
    }

}
