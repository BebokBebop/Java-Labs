/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.lab.model;

import pl.polsl.lab.exceptions.MyException;

/**
 * Generates a berger table based on the number of teams
 * @author Hubert Przegendza
 * @version 1.0
 */
public class BergerTableGenerator {

    /**
     * generates a full berger table for a given number of teams
     * @param teamCount number of teams to generate a berger table for
     * @return string containing a full berger table 
     * @throws MyException in case an incorrect number is passed as the parameter
     */
    public String generateBergerTable(int teamCount) throws MyException {
        
        //check 
        if(teamCount < 2 || teamCount > 100)
            throw new MyException();
        /**
         * stores information about teamCounts parity - odd numbers require different handling 
         */
        int parity = teamCount % 2;
        //for odd numbers, berger table is generated as for team count one number greater (every round one team has a break - plays against a phantom team)
        if (parity != 0) {
            teamCount++;
        }
        /**
         * table used in berger table generation - stores teams' numbers
         */
        int[] teams = new int[teamCount];
        for (int i = 0; i < teamCount; i++) {
            teams[i] = i + 1; //team number
        }

        /**
         * string to be filled with a full berger table
         */
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
     * moves all team numbers one place lower except the last one (teams[5] → teams[4], teams[6] → teams[5]..., teams[0] → teams[teamsCount-2])
     * @param teams table containing team numbers
     * @param size size of the table
     */
    private void shiftTeams(int[] teams, int size) {
        /**
         * temporarily stores the first number in the teams table
         */
        int temp = teams[0];
        for (int i = 0; i < size - 1; i++) {
            teams[i] = teams[i + 1];
        }
        teams[size - 2] = temp;
    }
}
