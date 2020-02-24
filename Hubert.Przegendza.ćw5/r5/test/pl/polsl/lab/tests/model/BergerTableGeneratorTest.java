package pl.polsl.lab.tests.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.fail;
import pl.polsl.lab.exceptions.MyException;
import pl.polsl.lab.model.BergerTableGenerator;

/**
 * Testing a public method of the Model
 *
 * @author Hubert Przegendza
 * @version 1.2
 */
public class BergerTableGeneratorTest {

    /**
     * tests the 'generateBergerTableNew(int)' method with parameters out of the
     * set range and minimal and maximal parameters
     * Also tests models history (number that have been used so far)
     */
    @Test
    public void testGeneration() {
        BergerTableGenerator BTG = new BergerTableGenerator();
        List<List<Integer>> output = new ArrayList<List<Integer>>();

        //testing exceptions
        try {
            BTG.generateBergerTableNew(0);
            fail("Should have thrown an exception");
        } catch (MyException e) {
        }
        try {
            BTG.generateBergerTableNew(1000);
            fail("Should have thrown an exception");
        } catch (MyException e) {
        }
        try {
            BTG.generateBergerTableNew(-1);
            fail("Should have thrown an exception");
        } catch (MyException e) {
        }

        //testing table for minimal amount of teams (2)
        try {
            output = BTG.generateBergerTableNew(2);
        } catch (MyException e) {
            fail("Shouldn't have thrown an exception");
        }
        if (output == null) {
            fail("Table has not been generated!");
        } else if (output.size() != 1) {
            fail("Incorrect table generated (wrong size)");
        } else {
            if (output.get(0).size() != 2) {
                fail("Incorrect table generated (wrong size)");
            } else if (output.get(0).get(0) != 1) {
                fail("Incorrect table generated (wrong number)");
            } else if (output.get(0).get(1) != 2) {
                fail("Incorrect table generated (wrong number)");
            }
        }

        //gard-coding a correct table for 5 teams for comparison
        List<List<Integer>> test = new ArrayList<List<Integer>>();
        test.add(new ArrayList<>());
        test.get(0).add(2);
        test.get(0).add(5);
        test.get(0).add(3);
        test.get(0).add(4);

        test.add(new ArrayList<>());
        test.get(1).add(3);
        test.get(1).add(1);
        test.get(1).add(4);
        test.get(1).add(5);

        test.add(new ArrayList<>());
        test.get(2).add(4);
        test.get(2).add(2);
        test.get(2).add(5);
        test.get(2).add(1);

        test.add(new ArrayList<>());
        test.get(3).add(5);
        test.get(3).add(3);
        test.get(3).add(1);
        test.get(3).add(2);

        test.add(new ArrayList<>());
        test.get(4).add(1);
        test.get(4).add(4);
        test.get(4).add(2);
        test.get(4).add(3);

        //testing table for 5 teams
        try {
            output = BTG.generateBergerTableNew(5);
        } catch (MyException e) {
            fail("Shouldn't have thrown an exception");
        }

        if (output == null) {
            fail("Table has not been generated!");
        } else if (output.size() != test.size()) {
            fail("Incorrect table generated (wrong size)");
        } else {
            for (int i = 0; i < test.size(); i++) {
                List<Integer> cmp = test.get(i);
                List<Integer> outputCmp = output.get(i);
                if (outputCmp.size() != cmp.size()) {
                    fail("Incorrect table generated (wrong size)");
                }
                for (int j = 0; j < cmp.size(); j++) {
                    if (cmp.get(j) != outputCmp.get(j)) {
                        fail("Incorrect table generated (wrong numbers)");
                    }
                }
            }
        }

        //reading test file - table for 100 teams is too big, to hard-code
        {
            BufferedReader bf = null;
            try {
                bf = new BufferedReader(new FileReader(new File("testFile")));
            } catch (FileNotFoundException ex) {
                fail("Test file not found!");
            }

            test = new ArrayList<List<Integer>>();
            String read = null;
            if (bf == null) {
                fail("Test file not found!");
            }
            try {
                read = bf.readLine();
            } catch (IOException ex) {
                fail("Can't read TestFile (IOException)");
            }

            int i = 0;
            while (read != null) {
                test.add(new ArrayList<>());
                read = read.replaceAll("-", "\t");
                String line[] = read.split("\t");
                for (String l : line) {
                    test.get(i).add(Integer.parseInt(l));
                }
                i++;
                try {
                    read = bf.readLine();
                } catch (IOException ex) {
                    fail("Can't read TestFile (IOException)");
                }
            }
        }

        //testing table for 100 teams
        try {
            output = BTG.generateBergerTableNew(100);
        } catch (MyException e) {
            fail("Shouldn't have thrown an exception");
        }

        if (output == null) {
            fail("Table has not been generated!");
        } else if (output.size() != test.size()) {
            fail("Incorrect table generated (wrong size)");
        } else {
            for (int i = 0; i < test.size(); i++) {
                List<Integer> cmp = test.get(i);
                List<Integer> outputCmp = output.get(i);
                if (outputCmp.size() != cmp.size()) {
                    fail("Incorrect table generated (wrong size)");
                }
                for (int j = 0; j < cmp.size(); j++) {
                    if (cmp.get(j) != outputCmp.get(j)) {
                        fail("Incorrect table generated (wrong numbers)");
                    }
                }
            }
        }
        //to be deleted soon
        List<Integer> hist = BTG.getHistory();
        if (hist.size() != 3) {
            fail("Incorrect history");
        } else {
            if (hist.get(0) != 2 || hist.get(1) != 5 || hist.get(2) != 100) {
                fail("Incorrect history");
            }
        }
    }
}
