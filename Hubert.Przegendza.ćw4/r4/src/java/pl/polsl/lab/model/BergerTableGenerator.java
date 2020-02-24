package pl.polsl.lab.model;

import java.util.ArrayList;
import java.util.List;
import static java.util.Collections.*;
import pl.polsl.lab.annotations.RequiresTesting;
import pl.polsl.lab.exceptions.MyException;

/**
 * Generates a berger table based on the number of teams. Stores a list of already used numbers.
 *
 * @author Hubert Przegendza
 * @version 1.12
 */
public class BergerTableGenerator {

    /**
     * List of teamCounts, that have been used
     */
    private List<Integer> historyOfCalculations = new ArrayList<>();
    //private List<List<Integer>> alreadyCalculated = new ArrayList<>();
    /**
     * generates a full berger table for a given number of teams
     *
     * @param teamCount number of teams to generate a berger table for
     * @return 2D list containing a full berger table. Every 2 numbers in a row
     * are teams to have a match. Every row is a round.
     * @throws MyException in case an incorrect number is passed as the
     * parameter
     */
    @RequiresTesting
    public List<List<Integer>> generateBergerTableNew(int teamCount) throws MyException {
        //check parameter
        if (teamCount < 2 || teamCount > 100) {
            throw new MyException();
        }
        
        //saves teamCount in history
        historyOfCalculations.add(teamCount);
        
        // stores information about teamCounts parity - odd numbers require different handling 
        int parity = teamCount % 2;

        //for odd numbers, berger table is generated as for team count one number greater (every round one team has a break - plays against a phantom team)
        if (parity != 0) {
            teamCount++;
        }

        // table used in berger table generation - stores teams' numbers
        Integer[] teams = new Integer[teamCount];
        for (int i = 0; i < teamCount; i++) {
            teams[i] = i + 1; //team number
        }
        //put in a List so that rotate() can be used
        List<Integer> teamsList = new ArrayList<>();
        addAll(teamsList, teams);

        //fill the output list
        List<List<Integer>> output = new ArrayList<List<Integer>>();
        for (int i = 0; i < (teamCount - 1); i++) {
            output.add(new ArrayList<>());
            for (int j = parity; j < teamCount / 2; j++) {
                output.get(i).add(teamsList.get(j));
                output.get(i).add(teamsList.get(teamCount - 1 - j));
            }
            rotate(teamsList, -1);
            swap(teamsList, teamCount - 2, teamCount - 1);
        }
        return output;
    }

    /**
     * getter for historyOfCalculations
     * @return list of all teamCounts that have been used so far
     */
    public List<Integer> getHistory()
    {
        return historyOfCalculations;
    }
    
    /**
     * generates a full berger table for a given number of teams
     *
     * @param teamCount number of teams to generate a berger table for
     * @return string containing a full berger table. Every 2 numbers in a row
     * are teams to have a match. Every row is a round.
     * @throws MyException in case an incorrect number is passed as the
     * parameter
     */
    @Deprecated
    public String generateBergerTable(int teamCount) throws MyException {

        //check 
        if (teamCount < 2 || teamCount > 100) {
            throw new MyException();
        }
        //stores information about teamCounts parity - odd numbers require different handling 
        int parity = teamCount % 2;
        //for odd numbers, berger table is generated as for team count one number greater (every round one team has a break - plays against a phantom team)
        if (parity != 0) {
            teamCount++;
        }
        // table used in berger table generation - stores teams' numbers
        int[] teams = new int[teamCount];
        for (int i = 0; i < teamCount; i++) {
            teams[i] = i + 1; //team number
        }

        // string to be filled with a full berger table
        String bTable = "";
        for (int i = 0; i < (teamCount - 1); i++) {
            for (int j = parity; j < teamCount / 2; j++) {
                bTable += Integer.toString(teams[j]) + '-' + Integer.toString(teams[teamCount - 1 - j]) + '\t';
            }
            shiftTeams(teams, teamCount);
            bTable += '\n';
        }
        return bTable;
    }

    /**
     * moves all team numbers one place lower except the last one (teams[5] →
     * teams[4], teams[6] → teams[5]..., teams[0] → teams[teamsCount-2])
     *
     * @param teams table containing team numbers
     * @param size size of the table
     */
    @Deprecated
    private void shiftTeams(int[] teams, int size) {

        // temporarily stores the first number in the teams table
        int temp = teams[0];
        for (int i = 0; i < size - 1; i++) {
            teams[i] = teams[i + 1];
        }
        teams[size - 2] = temp;
    }
}
