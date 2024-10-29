import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class BaseballElimination {
    private Map<String, Integer> teamID;
    private List<String> teams;
    private int[] teamWins;
    private int[] teamLosses;
    private int[] teamRemainingGames;
    private int[][] teamGamesAgainstX;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename){
        try (Scanner scanner = new Scanner(new File(filename))){
            int teamCount = Integer.parseInt(scanner.nextLine());
            teamID = new HashMap<>();
            teams = new ArrayList<>();
            teamWins = new int[teamCount];
            teamLosses = new int[teamCount];
            teamRemainingGames = new int[teamCount];
            teamGamesAgainstX = new int[teamCount][teamCount];

            for (int i = 0; i < teamCount; i++){
                String line = scanner.next();
                teamID.put(line, i);
                teams.add((line));
                teamWins[i] = scanner.nextInt();
                teamLosses[i] = scanner.nextInt();
                teamRemainingGames[i] = scanner.nextInt();

                for (int j = 0; j < teamCount; j++){
                    teamGamesAgainstX[i][j] = scanner.nextInt();
                }

            }
        }catch (FileNotFoundException error){
            System.out.println("Error, check code");
        }
    }

    private void validateArgument(String team){
        if (!teams.contains(team)) throw new IllegalArgumentException();
    }

    // number of teams
    public int numberOfTeams(){
        return teams.size();
    }

    // all teams
    public Iterable<String> teams(){
        return teams;
    }

    // number of wins for given team
    public int wins(String team){
        validateArgument(team);
        return teamWins[teamID.get(team)];
    }

    // number of losses for given team
    public int losses(String team){
        validateArgument(team);
        return teamLosses[teamID.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team){
        validateArgument(team);
        return teamRemainingGames[teamID.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2){
        validateArgument(team1);
        validateArgument(team2);
        return teamGamesAgainstX[teamID.get(team1)][teamID.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team){
        validateArgument(team);
        for (String otherTeam: teams){
            if (otherTeam.equals(team)) continue;
            if (wins(otherTeam) > wins(team) + remaining(team)) return true;
        }

        return certificateOfElimination(team) != null;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team){
        validateArgument(team);

        HashSet<String> eliminatingTeams = null;

        //Trivial Elimination
        for (String otherTeam: teams){
            if (otherTeam.equals(team)) continue;

            if (wins(otherTeam) > wins(team) + remaining(team)){
                if (eliminatingTeams == null) eliminatingTeams = new HashSet<>();
                eliminatingTeams.add(otherTeam);
            }
        }
        if (eliminatingTeams != null) return eliminatingTeams;

        //Non-trivial Elimination
        int sourceVertex = 0;
        int totalGames = teams.size() * (teams.size() - 1) / 2;
        int sinkVertex = totalGames + teams.size() + 1;
        FlowNetwork flowNetwork = new FlowNetwork(sinkVertex + 1);

        Map<String, Integer> otherTeamsMap = new HashMap<>();
        Set<String> teamsParticipatedInGames = new HashSet<>();
        Set<String> teamsConnectToTarget = new HashSet<>();

        int gameVertexIndex = totalGames + 1;
        for (String otherTeam: teams()){
            if (otherTeam.equals(team)) continue;
            otherTeamsMap.put(otherTeam, gameVertexIndex);
            gameVertexIndex++;
        }

        gameVertexIndex = 1;
        for (String team1: teams()){
            if (team1.equals(team)) continue;
            for (String team2: teams()){
                if (team2.equals(team) || team2.equals(team1)) continue;
                if (!teamsParticipatedInGames.contains(team1) && !teamsParticipatedInGames.contains(team2)){
                    flowNetwork.addEdge(new FlowEdge(sourceVertex, gameVertexIndex, against(team1, team2)));
                    flowNetwork.addEdge(new FlowEdge(gameVertexIndex, otherTeamsMap.get(team1), Double.POSITIVE_INFINITY));
                    flowNetwork.addEdge(new FlowEdge(gameVertexIndex, otherTeamsMap.get(team2), Double.POSITIVE_INFINITY));
                    gameVertexIndex++;
                }
            }
            teamsParticipatedInGames.add(team1);
            if (!teamsConnectToTarget.contains(team1)){
                flowNetwork.addEdge(new FlowEdge(otherTeamsMap.get(team1), sinkVertex, wins(team) + remaining(team) - wins(team1)));
                teamsConnectToTarget.add(team1);
            }
        }
        FordFulkerson maxFlow = new FordFulkerson(flowNetwork, sourceVertex, sinkVertex);

        for (String otherTeam: otherTeamsMap.keySet()){
            if (maxFlow.inCut(otherTeamsMap.get(otherTeam))){
                if (eliminatingTeams == null) eliminatingTeams = new HashSet<>();
                eliminatingTeams.add(otherTeam);
            }
        }
        return eliminatingTeams;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                System.out.println(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    System.out.println(t + " ");
                }
                System.out.println("}");
            } else {
                System.out.println(team + " is not eliminated");
            }
        }


    }
}
