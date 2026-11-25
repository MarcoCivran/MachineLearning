import java.util.*;

public class lab_ER_1_javaVersion {
    public static final String target = "trytofindme";
    public void main(String[] args) {
        List<String> population = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            population.add(generateRandomString(5, 20));
        }

        eaCustom(population, 0.15, 25, 200);
    }

    public static String generateRandomString(int i, int j) {
        int length = i + (int) (Math.random() * (j - i + 1));
        StringBuilder sb = new StringBuilder(length);
        for (int k = 0; k < length; k++) {
            char randomChar = (char) ('a' + Math.random() * 26);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public String mutation(String individual, double mutationRate) {
        StringBuilder mutated = new StringBuilder("");

        for (int i = 0; i < individual.length(); i++) {
            if (Math.random() < mutationRate) {
                char randomChar = (char) ('a' + Math.random() * 26);
                mutated.append(randomChar);
            } else {
                mutated.append(individual.charAt(i));
            } 
                
            if (Math.random() < mutationRate) {
                char randomChar = (char) ('a' + Math.random() * 26);
                mutated.append(randomChar);
            }
            if (Math.random() < mutationRate) {
                mutated.setLength(mutated.length() - 1);
            }
        }
        

        if (fitness(mutated.toString()) >= fitness(individual)) {
            return individual;
        } else{
            return mutated.toString();
        }
    }

    public int fitness(String individual) {
        return LevenshteinDistance.compute_Levenshtein_distance(target, individual);
    }

    public String tournament_selection(List<String> population, int tournament_size) {
        List<String> tournament = new ArrayList<>();
        for (int i = 0; i < tournament_size; i++) {
            int randomIndex = (int) (Math.random() * population.size());
            tournament.add(population.get(randomIndex));
        }

        String bestIndividual = null;
        int bestFitness = Integer.MAX_VALUE;
        for (String individual : tournament) {
            int currentFitness = fitness(individual);
            if (currentFitness < bestFitness) {
                bestFitness = currentFitness;
                bestIndividual = individual;
            }
        }

        return bestIndividual;
    }

    public String ranked_selection(List<String> population) {
        List<String> sortedPopulation = new ArrayList<>(population);
        sortedPopulation.sort(Comparator.comparingInt(this::fitness));

        int totalRank = (sortedPopulation.size() * (sortedPopulation.size() + 1)) / 2;
        int randomValue = (int) (Math.random() * totalRank);

        int cumulativeRank = 0;
        for (int i = 0; i < sortedPopulation.size(); i++) {
            cumulativeRank += (i + 1);
            if (cumulativeRank >= randomValue) {
                return sortedPopulation.get(i);
            }
        }

        return sortedPopulation.get(sortedPopulation.size() - 1);
    }

    public void eaCustom(List<String> population, double mutationRate, int tournament_size, int generations) {
        List<String> newPopulation = new ArrayList<>();

        for (int gen = 0; gen < generations; gen++) {
            while (newPopulation.size() < population.size()) {
                String parent = tournament_selection(population, tournament_size);
                String offspring = mutation(parent, mutationRate);

                newPopulation.add(offspring);
            }

            population.clear();
            population.addAll(newPopulation);
            newPopulation.clear();

            for (String individual : population) {
                System.out.print(individual + " " + fitness(individual) + " : ");
                break;
            }
            System.out.println();
        }

        
    }


}



//imported class for the Levenshtein distance algorithm

class LevenshteinDistance {

    static int compute_Levenshtein_distance(String str1,
                                              String str2)
    {

        // A 2-D matrix to store previously calculated
        // answers of subproblems in order
        // to obtain the final

        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) 
        {
            for (int j = 0; j <= str2.length(); j++) {

                // If str1 is empty, all characters of
                // str2 are inserted into str1, which is of
                // the only possible method of conversion
                // with minimum operations.
                if (i == 0) {
                    dp[i][j] = j;
                }

                // If str2 is empty, all characters of str1
                // are removed, which is the only possible
                //  method of conversion with minimum
                //  operations.
                else if (j == 0) {
                    dp[i][j] = i;
                }

                else {
                    // find the minimum among three
                    // operations below

                    
                    dp[i][j] = minm_edits(dp[i - 1][j - 1]
                        + NumOfReplacement(str1.charAt(i - 1),str2.charAt(j - 1)), // replace
                        dp[i - 1][j] + 1, // delete
                        dp[i][j - 1] + 1); // insert
                }
            }
        }

        return dp[str1.length()][str2.length()];
    }

    // check for distinct characters
    // in str1 and str2
  
    static int NumOfReplacement(char c1, char c2)
    {
        return c1 == c2 ? 0 : 1;
    }

    // receives the count of different
    // operations performed and returns the
    // minimum value among them.
  
    static int minm_edits(int... nums)
    {

        return Arrays.stream(nums).min().orElse(
            Integer.MAX_VALUE);
    }
}